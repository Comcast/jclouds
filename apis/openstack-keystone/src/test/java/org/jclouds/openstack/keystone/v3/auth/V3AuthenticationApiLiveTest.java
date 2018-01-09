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

import static org.testng.Assert.assertNotNull;

import org.jclouds.openstack.keystone.auth.AuthenticationApi;
import org.jclouds.openstack.keystone.auth.domain.PasswordCredentials;
import org.jclouds.openstack.keystone.auth.domain.TenantAndCredentials;
import org.jclouds.openstack.keystone.auth.domain.TokenCredentials;
import org.jclouds.openstack.keystone.auth.filters.AuthenticateRequest;
import org.jclouds.openstack.keystone.v3.KeystoneApi;
import org.jclouds.openstack.keystone.v3.internal.BaseV3KeystoneApiLiveTest;
import org.jclouds.rest.ApiContext;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.google.common.base.Splitter;
import com.google.common.collect.Iterables;
import com.google.common.reflect.TypeToken;

@Test(groups = "live", testName = "V3AuthenticationApiLiveTest")
public class V3AuthenticationApiLiveTest extends BaseV3KeystoneApiLiveTest {

   private String tenant;
   private String user;
   private ApiContext<KeystoneApi> context;

   @BeforeClass
   public void parseCredentials() {
      tenant = Iterables.get(Splitter.on(":").split(identity), 0);
      user = Iterables.get(Splitter.on(":").split(identity), 1);
      context = newBuilder().modules(setupModules()).overrides(setupProperties())
            .build(new TypeToken<ApiContext<KeystoneApi>>() {
               private static final long serialVersionUID = 1L;
            });

      grabToken(context.utils().injector().getInstance(AuthenticateRequest.class));
   }

   public void testAuthenticatePassword() {
      assertNotNull(api().authenticatePassword(TenantAndCredentials.<PasswordCredentials> builder().tenantName(tenant)
            .credentials(PasswordCredentials.builder().username(user).password(credential).build()).build()));
   }

   public void testAuthenticateToken() {
      assertNotNull(api().authenticateToken(TenantAndCredentials.<TokenCredentials> builder().tenantName(tenant)
            .credentials(TokenCredentials.builder().id(token).build()).build()));
   }

   protected AuthenticationApi api() {
      return context.utils().injector().getInstance(AuthenticationApi.class);
   }

}
