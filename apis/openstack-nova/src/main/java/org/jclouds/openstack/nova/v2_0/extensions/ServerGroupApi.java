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

import com.google.common.annotations.Beta;
import com.google.common.collect.FluentIterable;
import org.jclouds.Fallbacks.EmptyFluentIterableOnNotFoundOr404;
import org.jclouds.Fallbacks.FalseOnNotFoundOr404;
import org.jclouds.Fallbacks.NullOnNotFoundOr404;
import org.jclouds.javax.annotation.Nullable;
import org.jclouds.openstack.keystone.v2_0.filters.AuthenticateRequest;
import org.jclouds.openstack.nova.v2_0.domain.ServerGroup;
import org.jclouds.openstack.v2_0.ServiceType;
import org.jclouds.openstack.v2_0.services.Extension;
import org.jclouds.rest.annotations.Fallback;
import org.jclouds.rest.annotations.PayloadParam;
import org.jclouds.rest.annotations.RequestFilters;
import org.jclouds.rest.annotations.SelectJson;
import org.jclouds.rest.annotations.WrapWith;

import javax.inject.Named;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.util.List;

/**
 * Provides access to the OpenStack Compute (Nova) server Group extension API.
 */
@Beta
@Extension(of = ServiceType.COMPUTE, namespace = ExtensionNamespaces.SERVER_GROUPS)
@RequestFilters(AuthenticateRequest.class)
@Consumes(MediaType.APPLICATION_JSON)
public interface ServerGroupApi {
   /**
    * List all server Groups.
    *
    * @return all server Groups
    */
   @Named("ServerGroup:list")
   @GET
   @Path("/os-server-groups")
   @SelectJson("server_groups")
   @Fallback(EmptyFluentIterableOnNotFoundOr404.class)
   FluentIterable<ServerGroup> list();

   /**
    * Get a specific server Group
    *
    * @return a specific server Group
    */
   @Named("ServerGroup:get")
   @GET
   @Path("/os-server-groups/{id}")
   @SelectJson("server_group")
   @Fallback(NullOnNotFoundOr404.class)
   @Nullable
   ServerGroup get(@PathParam("id") String id);

   /**
    * Create a server Group
    *
    * @return a new server Group
    */
   @Named("ServerGroup:create")
   @POST
   @Path("/os-server-groups")
   @SelectJson("server_group")
   @Produces(MediaType.APPLICATION_JSON)
   @WrapWith("server_group")
   ServerGroup create(@PayloadParam("name") String name, @PayloadParam("policies") List<String> policies);

   /**
    * Delete a server Group.
    *
    * @return true on success and false if not found
    */
   @Named("ServerGroup:delete")
   @DELETE
   @Path("/os-server-groups/{id}")
   @Fallback(FalseOnNotFoundOr404.class)
   boolean delete(@PathParam("id") String id);
}
