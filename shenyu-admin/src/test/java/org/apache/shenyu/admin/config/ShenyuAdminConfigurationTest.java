/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.apache.shenyu.admin.config;

import org.apache.shenyu.admin.service.converter.GrpcSelectorHandleConverter;
import org.apache.shenyu.admin.service.converter.SelectorHandleConverter;
import org.apache.shenyu.admin.service.converter.SelectorHandleConverterFactor;
import org.apache.shenyu.common.enums.PluginEnum;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;

/**
 * Test cases for {@link ShenyuAdminConfiguration}.
 */
@RunWith(MockitoJUnitRunner.Silent.class)
public class ShenyuAdminConfigurationTest {

    @InjectMocks
    private ShenyuAdminConfiguration shenyuAdminConfiguration;

    @Test
    public void testSelectorHandleConverterFactor() {
        List<SelectorHandleConverter> converterList = new ArrayList<>();
        GrpcSelectorHandleConverter grpc = new GrpcSelectorHandleConverter();
        converterList.add(grpc);
        SelectorHandleConverterFactor factor = shenyuAdminConfiguration.selectorHandleConverterFactor(converterList);
        assertEquals(grpc, factor.newInstance(PluginEnum.GRPC.getName()));
    }
}
