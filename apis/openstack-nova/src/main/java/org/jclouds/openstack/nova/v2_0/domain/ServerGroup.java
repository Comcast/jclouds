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
package org.jclouds.openstack.nova.v2_0.domain;

import com.google.common.base.Objects;
import com.google.common.base.Objects.ToStringHelper;
import com.google.common.collect.ImmutableList;

import java.beans.ConstructorProperties;
import java.util.List;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Defines a security group
*/
public class ServerGroup {

   public static Builder<?> builder() {
      return new ConcreteBuilder();
   }

   public Builder<?> toBuilder() {
      return new ConcreteBuilder().fromSecurityGroup(this);
   }

   public abstract static class Builder<T extends Builder<T>>  {
      protected abstract T self();

      protected String id;
      protected List<String> policies = ImmutableList.of();

      /**
       * @see org.jclouds.openstack.nova.v2_0.domain.ServerGroup#getId()
       */
      public T id(String id) {
         this.id = id;
         return self();
      }

       /**
       * @see org.jclouds.openstack.nova.v2_0.domain.ServerGroup#getPolicies()
       */
      public T policies(List<String> policies) {
         this.policies = ImmutableList.copyOf(checkNotNull(policies, "policies"));
         return self();
      }

      public T policies(String... in) {
         return policies(ImmutableList.copyOf(in));
      }

      public ServerGroup build() {
         return new ServerGroup(id, policies);
      }

      public T fromSecurityGroup(ServerGroup in) {
         return this
                  .id(in.getId())
                  .policies(in.getPolicies());
      }
   }

   private static class ConcreteBuilder extends Builder<ConcreteBuilder> {
      @Override
      protected ConcreteBuilder self() {
         return this;
      }
   }

    private final String id;
   private final List<String> policies;

   @ConstructorProperties({
      "id", "policies"
   })
   protected ServerGroup(String id, List<String> policies) {
      this.id = checkNotNull(id, "id");
       this.policies = checkNotNull(policies, "policies").isEmpty() ? null : ImmutableList.copyOf(policies);
   }

   public String getId() {
      return this.id;
   }

   public List<String> getPolicies() {
      return this.policies;
   }

   @Override
   public int hashCode() {
      return Objects.hashCode(id, policies);
   }

   @Override
   public boolean equals(Object obj) {
      if (this == obj) return true;
      if (obj == null || getClass() != obj.getClass()) return false;
      ServerGroup that = ServerGroup.class.cast(obj);
      return Objects.equal(this.id, that.id)
               && Objects.equal(this.policies, that.policies);
   }

   protected ToStringHelper string() {
      return Objects.toStringHelper(this)
            .add("id", id).add("policies", policies);
   }

   @Override
   public String toString() {
      return string().toString();
   }

}
