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
package com.alipay.sofa.runtime.service.helper;

import com.alipay.sofa.runtime.api.client.param.BindingParam;
import com.alipay.sofa.runtime.spi.binding.Binding;
import com.alipay.sofa.runtime.spi.binding.Contract;
import com.alipay.sofa.runtime.spi.component.SofaRuntimeContext;
import org.springframework.context.ApplicationContext;

import java.lang.reflect.Constructor;
import java.util.Iterator;
import java.util.Set;

/**
 *
 * @author yuanxuan
 * @version : BindingHelper.java, v 0.1 2020年11月24日 10:49 yuanxuan Exp $
 */
public class BindingHelper {

    public static void addBoltBinding(Contract contract, SofaRuntimeContext sofaRuntimeContext) {
        try {
            boolean boltExists = false;
            Set<Binding> bindings = contract.getBindings();
            Iterator<Binding> it = bindings.iterator();
            while (it.hasNext()) {
                Binding b = it.next();
                if (b.getBindingType().getType().equals("bolt")
                    || b.getBindingType().getType().equals("tr")) {
                    boltExists = true;
                    break;
                }
            }
            if (!boltExists) {
                Class bindingClz = Class
                    .forName("com.alipay.sofa.rpc.boot.runtime.binding.BoltBinding");
                Class bindingParamClz = Class
                    .forName("com.alipay.sofa.rpc.boot.runtime.param.RpcBindingParam");

                Class boltParamClz = Class
                    .forName("com.alipay.sofa.rpc.boot.runtime.param.BoltBindingParam");
                BindingParam param = (BindingParam) boltParamClz.getConstructor().newInstance();
                Constructor constructor = bindingClz.getConstructor(bindingParamClz,
                    ApplicationContext.class, boolean.class);
                Binding boltBiding = (Binding) constructor.newInstance(param, sofaRuntimeContext
                    .getSofaRuntimeManager().getRootApplicationContext(), true);
                contract.addBinding(boltBiding);
            }
        } catch (Throwable e) {
            //TODO
            e.printStackTrace();
        }
    }
}