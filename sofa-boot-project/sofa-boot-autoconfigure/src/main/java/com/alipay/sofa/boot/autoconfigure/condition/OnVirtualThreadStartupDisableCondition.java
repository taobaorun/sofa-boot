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
package com.alipay.sofa.boot.autoconfigure.condition;

import org.springframework.boot.autoconfigure.condition.AnyNestedCondition;
import org.springframework.boot.autoconfigure.condition.ConditionalOnJava;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.system.JavaVersion;

/**
 * Condition for not startup SOFABoot on virtual thread.
 *
 * @author huzijie
 * @version StartupOnVirtualThreadDisableCondition.java, v 0.1 2023年12月05日 4:51 PM huzijie Exp $
 */
public class OnVirtualThreadStartupDisableCondition extends AnyNestedCondition {
    public OnVirtualThreadStartupDisableCondition() {
        super(ConfigurationPhase.REGISTER_BEAN);
    }

    @ConditionalOnJava(value = JavaVersion.TWENTY_ONE, range = ConditionalOnJava.Range.OLDER_THAN)
    static class JdkVersionAvailable {

    }

    @ConditionalOnProperty(value = "sofa.boot.startup.threads.virtual.enabled", havingValue = "false", matchIfMissing = true)
    static class IslePropertyAvailable {

    }
}
