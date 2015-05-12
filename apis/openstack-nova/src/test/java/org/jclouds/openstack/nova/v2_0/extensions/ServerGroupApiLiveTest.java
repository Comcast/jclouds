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

import com.google.common.base.Objects;
import com.google.common.base.Optional;
import com.google.common.base.Predicate;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Iterables;
import org.jclouds.openstack.nova.v2_0.domain.ServerGroup;
import org.jclouds.openstack.nova.v2_0.internal.BaseNovaApiLiveTest;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.util.Set;

import static org.jclouds.util.Predicates2.retry;
import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertTrue;

/**
 * Tests behavior of ServerGroupApi
 */
@Test(groups = "live", testName = "ServerGroupApiLiveTest", singleThreaded = true)
public class ServerGroupApiLiveTest extends BaseNovaApiLiveTest {

    private static final String TEST_SERVER_GROUP_NAME = "test_server_group";
    private static final String TEST_SERVER_GROUP_POLICY = "anti-affinity";

    private Optional<? extends ServerGroupApi> serverGroupApiOptional;
   private String region;

   private ServerGroup testServerGroup;

   @BeforeClass(groups = {"integration", "live"})
   @Override
   public void setup() {
      super.setup();
      region = Iterables.getLast(api.getConfiguredRegions(), "nova");
      serverGroupApiOptional = api.getServerGroupApi(region);

   }

   @AfterClass(groups = { "integration", "live" })
   @Override
   protected void tearDown() {
      if (serverGroupApiOptional.isPresent()) {
         if (testServerGroup != null) {
            final String serverGroupId = testServerGroup.getId();
            assertTrue(serverGroupApiOptional.get().delete(serverGroupId));
            assertTrue(retry(new Predicate<ServerGroupApi>() {
               public boolean apply(ServerGroupApi serverGroupApi) {
                  return serverGroupApiOptional.get().get(serverGroupId) == null;
               }
            }, 180 * 1000L).apply(serverGroupApiOptional.get()));
         }
      }
      super.tearDown();
   }

   @Test
    public void testCreateServerGroup() {
      if (serverGroupApiOptional.isPresent()) {
         testServerGroup = serverGroupApiOptional.get().create(
               TEST_SERVER_GROUP_NAME, ImmutableList.of(TEST_SERVER_GROUP_POLICY));
         assertTrue(retry(new Predicate<ServerGroupApi>() {
            public boolean apply(ServerGroupApi serverGroupApi) {
               return serverGroupApiOptional.get().get(testServerGroup.getId()).equals(testServerGroup);
            }
         }, 180 * 1000L).apply(serverGroupApiOptional.get()));
      }
   }

   @Test(dependsOnMethods = "testCreateServerGroup")
   public void testListServerGroups() {
      if (serverGroupApiOptional.isPresent()) {
         Set<? extends ServerGroup> serverGroups = serverGroupApiOptional.get().list().toSet();
         assertNotNull(serverGroups);
         boolean foundIt = false;
         for (ServerGroup serverGroup : serverGroups) {
            ServerGroup details = serverGroupApiOptional.get().get(serverGroup.getId());
            assertNotNull(details);
            if (Objects.equal(details.getId(), testServerGroup.getId())) {
               foundIt = true;
            }
         }
         assertTrue(foundIt, "Failed to find the serverGroup we created in list() response");
      }
   }
}
