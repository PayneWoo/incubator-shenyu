/*
 *   Licensed to the Apache Software Foundation (ASF) under one or more
 *   contributor license agreements.  See the NOTICE file distributed with
 *   this work for additional information regarding copyright ownership.
 *   The ASF licenses this file to You under the Apache License, Version 2.0
 *   (the "License"); you may not use this file except in compliance with
 *   the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *   Unless required by applicable law or agreed to in writing, software
 *   distributed under the License is distributed on an "AS IS" BASIS,
 *   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *   See the License for the specific language governing permissions and
 *   limitations under the License.
 *
 */

package org.apache.shenyu.springboot.starter.client.sofa;

import org.apache.shenyu.client.sofa.SofaServiceBeanPostProcessor;
import org.junit.Test;
import org.springframework.boot.autoconfigure.AutoConfigurations;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.test.context.runner.ApplicationContextRunner;
import org.springframework.context.annotation.Configuration;

import static org.junit.Assert.assertNotNull;

/**
 * Test case for {@link ShenyuSofaClientConfiguration}.
 */
@Configuration
@EnableConfigurationProperties
public class ShenyuSofaClientConfigurationTest {

    @Test
    public void testSofaServiceBeanPostProcessor() {
        new ApplicationContextRunner()
            .withConfiguration(AutoConfigurations.of(ShenyuSofaClientConfiguration.class))
            .withBean(ShenyuSofaClientConfigurationTest.class)
            .withPropertyValues(
                "debug=true",
                "shenyu.register.registerType=http",
                "shenyu.register.serverLists=http://localhost:9095",
                "shenyu.client.sofa.props[contextPath]=/sofa",
                "shenyu.client.sofa.props[appName]=sofa",
                "shenyu.client.sofa.props[host]=127.0.0.1",
                "shenyu.client.sofa.props[port]=8888"
            )
            .run(context -> {
                SofaServiceBeanPostProcessor processor = context.getBean("sofaServiceBeanPostProcessor", SofaServiceBeanPostProcessor.class);
                        assertNotNull(processor);
                    }
            );
    }
}
