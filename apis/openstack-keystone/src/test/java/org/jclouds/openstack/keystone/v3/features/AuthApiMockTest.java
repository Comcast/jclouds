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
package org.jclouds.openstack.keystone.v3.features;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNull;

import org.jclouds.openstack.keystone.v3.domain.Token;
import org.jclouds.openstack.keystone.v3.internal.BaseV3KeystoneApiMockTest;
import org.testng.annotations.Test;

import com.squareup.okhttp.mockwebserver.RecordedRequest;

@Test(groups = "unit", testName = "AuthApiMockTest", singleThreaded = true)
public class AuthApiMockTest extends BaseV3KeystoneApiMockTest {

   public void testGetToken() throws InterruptedException {
      enqueueAuthentication(server);
      server.enqueue(jsonResponse("/v3/token.json"));

      Token token = api.getAuthApi().get(authToken);

      assertEquals(token, tokenFromResource("/v3/token.json"));

      assertEquals(server.getRequestCount(), 2);
      assertAuthentication(server);
      RecordedRequest request = assertSent(server, "GET", "/auth/tokens");
      assertEquals(request.getHeader("X-Subject-Token"), authToken);
   }

   public void testGetTokenReturns404() throws InterruptedException {
      enqueueAuthentication(server);
      server.enqueue(response404());

      Token token = api.getAuthApi().get("foo");
      assertNull(token);

      assertEquals(server.getRequestCount(), 2);
      assertAuthentication(server);
      RecordedRequest request = assertSent(server, "GET", "/auth/tokens");
      assertEquals(request.getHeader("X-Subject-Token"), "foo");
   }
   
}
