package com.pronoia.camel;

import java.util.Dictionary;
import java.util.Map;

import com.pronoia.karaf.service.TestService;
import com.pronoia.karaf.service.impl.TestServiceOne;

import org.apache.camel.test.blueprint.CamelBlueprintTestSupport;

import org.apache.camel.util.KeyValueHolder;
import org.junit.Test;

public class ConsumerOneRouteTest extends CamelBlueprintTestSupport {
	
    @Override
    protected String getBlueprintDescriptor() {
        return "/OSGI-INF/blueprint/consumer-one-blueprint.xml";
    }

    @Override
    protected void addServicesOnStartup(Map<String, KeyValueHolder<Object, Dictionary>> services) {
        services.put( TestService.class.getName(), asService( new TestServiceOne(), null, null));
    }

    @Test
    public void testRoute() throws Exception {
        getMockEndpoint("mock://result").expectedMinimumMessageCount(1);

        assertMockEndpointsSatisfied();
    }

}
