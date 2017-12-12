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
package org.jclouds.openstack.keystone.v3.config;

import java.net.URI;

import org.jclouds.openstack.keystone.v2_0.config.NamespaceAliases;
import org.jclouds.openstack.keystone.v3.KeystoneApi;
import org.jclouds.rest.ConfiguresHttpApi;
import org.jclouds.rest.config.HttpApiModule;

import com.google.inject.Binder;
import com.google.inject.multibindings.MapBinder;

/**
 * Configures the Keystone API.
 */
@ConfiguresHttpApi
public class KeystoneHttpApiModule extends HttpApiModule<KeystoneApi> {

   public KeystoneHttpApiModule() {
   }

   // Allow providers to cleanly contribute their own aliases
   public static MapBinder<URI, URI> namespaceAliasBinder(Binder binder) {
      return MapBinder.newMapBinder(binder, URI.class, URI.class, NamespaceAliases.class).permitDuplicates();
   }

   @Override
   protected void configure() {
      super.configure();
      namespaceAliasBinder(binder());
   }

}
