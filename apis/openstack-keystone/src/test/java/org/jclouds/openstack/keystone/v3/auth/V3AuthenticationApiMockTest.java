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
package org.jclouds.openstack.keystone.v3.auth;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNull;
import static org.testng.Assert.assertTrue;

import org.jclouds.openstack.keystone.auth.domain.AuthInfo;
import org.jclouds.openstack.keystone.auth.domain.PasswordCredentials;
import org.jclouds.openstack.keystone.auth.domain.TenantAndCredentials;
import org.jclouds.openstack.keystone.auth.domain.TokenCredentials;
import org.jclouds.openstack.keystone.v3.domain.Token;
import org.jclouds.openstack.keystone.v3.internal.BaseV3KeystoneApiMockTest;
import org.testng.annotations.Test;

import com.google.common.reflect.TypeToken;

import java.util.Map;

@Test(groups = "unit", testName = "TokenApiMockTest", singleThreaded = true)
public class V3AuthenticationApiMockTest extends BaseV3KeystoneApiMockTest {

   public void testAuthenticatePassword() throws InterruptedException {
      server.enqueue(jsonResponse("/v3/token.json"));

      AuthInfo authInfo = authenticationApi.authenticatePassword(TenantAndCredentials.<PasswordCredentials>builder().credentials(PasswordCredentials.builder().username("user").password("pwd").build()).build());

      assertTrue(authInfo instanceof Token);
      assertEquals(authInfo, tokenFromResource("/v3/token.json"));

      assertEquals(server.getRequestCount(), 1);
      assertSent(server, "POST", "/auth/tokens");
   }

   public void testAuthenticateToken() throws InterruptedException {
      server.enqueue(jsonResponse("/v3/token.json"));

      AuthInfo authInfo = authenticationApi.authenticateToken(TenantAndCredentials.<TokenCredentials>builder().credentials(TokenCredentials.builder().id("token").build()).build());

      assertTrue(authInfo instanceof Token);
      assertEquals(authInfo, tokenFromResource("/v3/token.json"));

      assertEquals(server.getRequestCount(), 1);
      assertSent(server, "POST", "/auth/tokens");
   }

}
