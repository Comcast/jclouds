/**
 * Licensed to jclouds, Inc. (jclouds) under one or more
 * contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  jclouds licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package org.jclouds.glesys.features;

import org.testng.annotations.BeforeGroups;
import org.testng.annotations.Test;

import java.util.Set;

import static org.testng.Assert.assertTrue;

/**
 * Tests behavior of {@code IpClient}
 *
 * @author Adrian Cole
 */
@Test(groups = "live", testName = "IpClientLiveTest")
public class IpClientLiveTest extends BaseGleSYSClientLiveTest {

   @BeforeGroups(groups = { "live" })
   public void setupClient() {
      super.setupClient();
      client = context.getApi().getIpClient();
   }

   private IpClient client;

   @Test
   public void testListFree() throws Exception {
      Set<String> freeIps = client.listFree("4", "Falkenberg", "OpenVZ");
      assertTrue(freeIps.size() >= 0);
   }
}
