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

import com.google.common.collect.Iterables;
import com.google.inject.Injector;
import com.google.inject.Module;
import org.jclouds.http.HttpRequest;
import org.jclouds.openstack.keystone.auth.filters.AuthenticateRequest;
import org.jclouds.openstack.keystone.v3.KeystoneApi;
import org.jclouds.openstack.keystone.v3.internal.BaseV3KeystoneApiLiveTest;
import org.testng.annotations.Test;

import java.util.Properties;

@Test(groups = "live", testName = "TokenApiLiveTest")
public class TokenApiLiveTest extends BaseV3KeystoneApiLiveTest {

   protected String token;

   @Override
   protected KeystoneApi create(Properties props, Iterable<Module> modules) {
      Injector injector = newBuilder().modules(modules).overrides(props).buildInjector();
      grabToken(injector.getInstance(AuthenticateRequest.class));
      return injector.getInstance(KeystoneApi.class);
   }

   // Get the token currently in use
   private void grabToken(AuthenticateRequest ar) {
      HttpRequest test = ar.filter(HttpRequest.builder().method("GET").endpoint(endpoint).build());
      token = Iterables.getOnlyElement(test.getHeaders().get("X-Auth-Token"));
   }

   public void testGetToken() {
      System.out.println(api().get(token));
   }

   private TokenApi api() {
      return api.getTokenApi();
   }
}
