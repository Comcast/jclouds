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
package org.jclouds.openstack.keystone.v3.internal;

import java.util.Properties;

import com.google.common.collect.Iterables;
import org.jclouds.apis.BaseApiLiveTest;
import org.jclouds.http.HttpRequest;
import org.jclouds.openstack.keystone.auth.filters.AuthenticateRequest;
import org.jclouds.openstack.keystone.config.KeystoneProperties;
import org.jclouds.openstack.keystone.v3.KeystoneApi;

import static org.jclouds.openstack.keystone.config.KeystoneProperties.SERVICE_TYPE;

public class BaseV3KeystoneApiLiveTest extends BaseApiLiveTest<KeystoneApi> {

   protected String token;

   public BaseV3KeystoneApiLiveTest() {
      provider = "openstack-keystone-3";
   }

   @Override
   protected Properties setupProperties() {
      Properties props = super.setupProperties();
      setIfTestSystemPropertyPresent(props, KeystoneProperties.CREDENTIAL_TYPE);
      props.setProperty(SERVICE_TYPE, "identityv3");
      return props;
   }

   // Get the token currently in use
   protected void grabToken(AuthenticateRequest ar) {
      HttpRequest test = ar.filter(HttpRequest.builder().method("GET").endpoint(endpoint).build());
      token = Iterables.getOnlyElement(test.getHeaders().get("X-Auth-Token"));
   }

}
