/**
 * Alipay.com Inc.
 * Copyright (c) 2004-2023 All Rights Reserved.
 */
package com.alipay.sofa.runtime.test;

import com.alipay.sofa.runtime.api.aware.ClientFactoryAware;
import com.alipay.sofa.runtime.api.aware.ExtensionClientAware;
import com.alipay.sofa.runtime.api.client.ClientFactory;
import com.alipay.sofa.runtime.api.client.ExtensionClient;
import com.alipay.sofa.runtime.filter.JvmFilter;
import com.alipay.sofa.runtime.filter.JvmFilterContext;
import com.alipay.sofa.runtime.filter.JvmFilterHolder;
import com.alipay.sofa.runtime.spi.component.SofaRuntimeContext;
import com.alipay.sofa.runtime.spi.component.SofaRuntimeManager;
import com.alipay.sofa.runtime.spi.spring.RuntimeShutdownAware;
import com.alipay.sofa.runtime.spi.spring.SofaRuntimeContextAware;
import com.alipay.sofa.runtime.spring.SofaRuntimeAwareProcessor;
import com.alipay.sofa.runtime.test.configuration.RuntimeConfiguration;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.ImportResource;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * @author yuanxuan
 * @version : SofaRuntimeAwareProcessorTest.java, v 0.1 2023年03月28日 16:23 yuanxuan Exp $
 */
@RunWith(SpringRunner.class)
@SpringBootTest
@TestPropertySource(properties = "spring.application.name=SofaRuntimeAwareProcessorTest")
public class SofaRuntimeAwareProcessorTest {

    @Autowired
    private SofaRuntimeContext sofaRuntimeContext;

    @Test
    public void testSofaRuntimeAware() {
        SofaRuntimeAwareProcessor sofaRuntimeAwareProcessor = new SofaRuntimeAwareProcessor(
            sofaRuntimeContext.getSofaRuntimeManager());
        SofaRuntimeManager sofaRuntimeManager = sofaRuntimeContext.getSofaRuntimeManager();
        SofaRuntimeAwareBean sofaRuntimeAwareBean = new SofaRuntimeAwareBean();
        sofaRuntimeAwareProcessor.postProcessBeforeInitialization(sofaRuntimeAwareBean, "testBean");

        Assert.assertEquals(sofaRuntimeAwareBean.getSofaRuntimeContext(),
            sofaRuntimeManager.getSofaRuntimeContext());
        Assert.assertEquals(sofaRuntimeAwareBean.getClientFactory(),
            sofaRuntimeContext.getClientFactory());
        Assert.assertNotNull(sofaRuntimeAwareBean.getExtensionClient());
        Assert.assertTrue(JvmFilterHolder.getJvmFilters().contains(sofaRuntimeAwareBean));

        Assert.assertFalse(sofaRuntimeAwareBean.isInvoked());
        sofaRuntimeManager.shutdown();
        Assert.assertTrue(sofaRuntimeAwareBean.isInvoked());
    }

    static class SofaRuntimeAwareBean implements SofaRuntimeContextAware, ClientFactoryAware,
                                     ExtensionClientAware, JvmFilter, RuntimeShutdownAware {

        private ClientFactory      clientFactory;

        private ExtensionClient    extensionClient;

        private SofaRuntimeContext sofaRuntimeContext;

        private boolean            invoked;

        @Override
        public void setClientFactory(ClientFactory clientFactory) {
            this.clientFactory = clientFactory;
        }

        @Override
        public void setExtensionClient(ExtensionClient extensionClient) {
            this.extensionClient = extensionClient;
        }

        @Override
        public boolean before(JvmFilterContext context) {
            return false;
        }

        @Override
        public boolean after(JvmFilterContext context) {
            return false;
        }

        @Override
        public void shutdown() {
            invoked = true;
        }

        @Override
        public void setSofaRuntimeContext(SofaRuntimeContext sofaRuntimeContext) {
            this.sofaRuntimeContext = sofaRuntimeContext;
        }

        @Override
        public int getOrder() {
            return 0;
        }

        public ClientFactory getClientFactory() {
            return clientFactory;
        }

        public ExtensionClient getExtensionClient() {
            return extensionClient;
        }

        public SofaRuntimeContext getSofaRuntimeContext() {
            return sofaRuntimeContext;
        }

        public boolean isInvoked() {
            return invoked;
        }
    }

    @Configuration(proxyBeanMethods = false)
    @Import(RuntimeConfiguration.class)
    static class SofaRuntimeAwareProcessorTestConfiguration {

    }
}
