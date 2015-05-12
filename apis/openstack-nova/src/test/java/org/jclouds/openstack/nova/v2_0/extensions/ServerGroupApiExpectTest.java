/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.jclouds.openstack.nova.v2_0.extensions;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Iterables;
import org.jclouds.http.HttpResponse;
import org.jclouds.openstack.nova.v2_0.NovaApi;
import org.jclouds.openstack.nova.v2_0.domain.ServerGroup;
import org.jclouds.openstack.nova.v2_0.internal.BaseNovaApiExpectTest;
import org.jclouds.rest.ResourceNotFoundException;
import org.testng.annotations.Test;

import java.net.URI;
import java.util.Set;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertNull;
import static org.testng.Assert.assertTrue;


/**
 * Tests ServerGroupApi guice wiring and parsing
 */
@Test(groups = "unit", testName = "ServerGroupApiExpectTest")
public class ServerGroupApiExpectTest extends BaseNovaApiExpectTest {
    private static final String TEST_SERVER_GROUP_NAME = "antiaffinity";
    private static final String TEST_SERVER_GROUP_ID = "f612aca1-abad-4d21-81ee-0413f7583dc7";
    private static final String TEST_SERVER_GROUP_POLICY = "anti-affinity";

   public void testListServerGroups() {
      URI endpoint = URI.create("https://az-1.region-a.geo-1.compute.hpcloudsvc.com/v2/3456/os-server-groups");
      ServerGroupApi api = requestsSendResponses(
            keystoneAuthWithUsernameAndPasswordAndTenantName,
            responseWithKeystoneAccess, extensionsOfNovaRequest, extensionsOfNovaResponse,
            authenticatedGET().endpoint(endpoint).build(),
            HttpResponse.builder().statusCode(200).payload(payloadFromResource("/servergroup_list.json")).build()
      ).getServerGroupApi("az-1.region-a.geo-1").get();

      Set<? extends ServerGroup> serverGroups = api.list().toSet();
      assertEquals(serverGroups, ImmutableSet.of(testServerGroup()));
   }

   public void testListServerGroupsFail() {
      URI endpoint = URI.create("https://az-1.region-a.geo-1.compute.hpcloudsvc.com/v2/3456/os-server-groups");
      ServerGroupApi api = requestsSendResponses(
            keystoneAuthWithUsernameAndPasswordAndTenantName,
            responseWithKeystoneAccess, extensionsOfNovaRequest, extensionsOfNovaResponse,
            authenticatedGET().endpoint(endpoint).build(),
            HttpResponse.builder().statusCode(404).build()
      ).getServerGroupApi("az-1.region-a.geo-1").get();

      Set<? extends ServerGroup> servergroups = api.list().toSet();
      assertTrue(servergroups.isEmpty());
   }

   public void testCreateServerGroup() {
      URI endpoint = URI.create("https://az-1.region-a.geo-1.compute.hpcloudsvc.com/v2/3456/os-server-groups");
       NovaApi novaApi = requestsSendResponses(
               keystoneAuthWithUsernameAndPasswordAndTenantName,
               responseWithKeystoneAccess, extensionsOfNovaRequest, extensionsOfNovaResponse,
               authenticatedGET().endpoint(endpoint)
                       .method("POST")
                       .payload(payloadFromResource("/servergroup_create.json"))
                       .build(),
               HttpResponse.builder().statusCode(200).payload(payloadFromResource("/servergroup_created.json")).build()
       );
       ServerGroupApi api = novaApi.getServerGroupApi("az-1.region-a.geo-1").get();

      ServerGroup servergroup = api.create(TEST_SERVER_GROUP_NAME, ImmutableList.of(TEST_SERVER_GROUP_POLICY));
      assertEquals(servergroup, testServerGroup());
   }

   @Test(expectedExceptions = ResourceNotFoundException.class)
   public void testCreateServerGroupFail() {
       URI endpoint = URI.create("https://az-1.region-a.geo-1.compute.hpcloudsvc.com/v2/3456/os-server-groups");
       ServerGroupApi api = requestsSendResponses(
               keystoneAuthWithUsernameAndPasswordAndTenantName,
               responseWithKeystoneAccess, extensionsOfNovaRequest, extensionsOfNovaResponse,
               authenticatedGET().endpoint(endpoint)
                       .method("POST")
                       .payload(payloadFromResource("/servergroup_create.json")).build(),
               HttpResponse.builder().statusCode(404).payload(payloadFromResource("/servergroup_created.json")).build()
       ).getServerGroupApi("az-1.region-a.geo-1").get();

       api.create(TEST_SERVER_GROUP_NAME, ImmutableList.of(TEST_SERVER_GROUP_POLICY));
   }

   public void testGetServerGroup() {
      URI endpoint = URI.create("https://az-1.region-a.geo-1.compute.hpcloudsvc.com/v2/3456/os-server-groups/" + TEST_SERVER_GROUP_ID);
      ServerGroupApi api = requestsSendResponses(
            keystoneAuthWithUsernameAndPasswordAndTenantName,
            responseWithKeystoneAccess, extensionsOfNovaRequest, extensionsOfNovaResponse,
            authenticatedGET().endpoint(endpoint).build(),
            HttpResponse.builder().statusCode(200).payload(payloadFromResource("/servergroup_created.json")).build()
      ).getServerGroupApi("az-1.region-a.geo-1").get();

      ServerGroup serverGroup = api.get(TEST_SERVER_GROUP_ID);
      assertEquals(serverGroup, testServerGroup());

      // double-check equals()
      assertEquals(serverGroup.getId(), TEST_SERVER_GROUP_ID);
      assertEquals(Iterables.getOnlyElement(serverGroup.getPolicies()), TEST_SERVER_GROUP_POLICY);
   }

   public void testGetServerGroupFail() {
      URI endpoint = URI.create("https://az-1.region-a.geo-1.compute.hpcloudsvc.com/v2/3456/os-server-groups/" + TEST_SERVER_GROUP_ID);
      ServerGroupApi api = requestsSendResponses(
            keystoneAuthWithUsernameAndPasswordAndTenantName,
            responseWithKeystoneAccess, extensionsOfNovaRequest, extensionsOfNovaResponse,
            authenticatedGET().endpoint(endpoint).build(),
            HttpResponse.builder().statusCode(404).build()
      ).getServerGroupApi("az-1.region-a.geo-1").get();

      assertNull(api.get(TEST_SERVER_GROUP_ID));
   }

   public void testDeleteServerGroup() {
      URI endpoint = URI.create("https://az-1.region-a.geo-1.compute.hpcloudsvc.com/v2/3456/os-server-groups/" + TEST_SERVER_GROUP_ID);
       ServerGroupApi api = requestsSendResponses(
            keystoneAuthWithUsernameAndPasswordAndTenantName,
            responseWithKeystoneAccess, extensionsOfNovaRequest, extensionsOfNovaResponse,
            authenticatedGET().endpoint(endpoint).method("DELETE").build(),
            HttpResponse.builder().statusCode(200).build()
      ).getServerGroupApi("az-1.region-a.geo-1").get();

      assertTrue(api.delete(TEST_SERVER_GROUP_ID));
   }

   public void testDeleteServerGroupFail() {
      URI endpoint = URI.create("https://az-1.region-a.geo-1.compute.hpcloudsvc.com/v2/3456/os-server-groups/" + TEST_SERVER_GROUP_ID);
       ServerGroupApi api = requestsSendResponses(
            keystoneAuthWithUsernameAndPasswordAndTenantName,
            responseWithKeystoneAccess, extensionsOfNovaRequest, extensionsOfNovaResponse,
            authenticatedGET().endpoint(endpoint).method("DELETE").build(),
            HttpResponse.builder().statusCode(404).build()
      ).getServerGroupApi("az-1.region-a.geo-1").get();

      assertFalse(api.delete(TEST_SERVER_GROUP_ID));
   }

   protected ServerGroup testServerGroup() {
      return ServerGroup.builder().id(TEST_SERVER_GROUP_ID).policies(TEST_SERVER_GROUP_POLICY).build();
   }

}
