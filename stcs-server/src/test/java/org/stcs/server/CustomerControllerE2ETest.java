package org.stcs.server;

import static org.assertj.core.api.Assertions.assertThat;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.skyscreamer.jsonassert.JSONAssert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mock.web.MockHttpServletResponse;
import org.stcs.server.entity.CustomerEntity;
import org.stcs.server.service.CustomerService;

import java.util.ArrayList;
import java.util.List;

@TestMethodOrder(MethodOrderer.MethodName.class)
public class CustomerControllerE2ETest extends AbstractTest {
    private static final String CUSTOMERS_PATH = "/api/v1/customers";

    @Autowired
    private CustomerService customerService;

    @Test
    void testAddCustomer() throws Exception {
        final List<CustomerEntity> customerEntities = new ArrayList<>();
        CustomerEntity customer = CustomerEntity.builder()
                .custId(10)
                .custName("张三")
                .build();
        customerEntities.add(customer);

        MockHttpServletResponse response = getMockResponseByRestApiPostWithJson(CUSTOMERS_PATH, customerEntities);
        JSONObject retBodyJson = JSON.parseObject(response.getContentAsString());

        assertThat(retBodyJson.get("code")).isEqualTo("ok");
        assertThat(retBodyJson.get("messageId")).isNotNull();
    }

    @Test
    void testGet() throws Exception {
        String expectedResponse = "{\"custId\": 10,\"custName\": \"张三\"}";

        MockHttpServletResponse response = getMockResponseByRestApiGet(CUSTOMERS_PATH);
        JSONObject retBodyJson = JSONObject.parseObject(response.getContentAsString());

        assertThat(retBodyJson.getString("code")).isEqualTo("ok");
        assertThat(retBodyJson.getIntValue("total")).isEqualTo(1);

        JSONObject resultJson = retBodyJson.getJSONArray("records").getJSONObject(0);
        JSONAssert.assertEquals(expectedResponse, resultJson.toString(), false);
    }
}
