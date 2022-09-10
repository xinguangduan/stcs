package org.stcs.server;

import static org.assertj.core.api.Assertions.assertThat;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import org.junit.jupiter.api.*;
import org.skyscreamer.jsonassert.JSONAssert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mock.web.MockHttpServletResponse;
import org.stcs.server.entity.CustomerEntity;
import org.stcs.server.service.CustomerService;

import java.util.ArrayList;
import java.util.List;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class CustomerControllerE2ETest extends AbstractTest {
    private static final String CUSTOMERS_PATH = "/api/v1/customers";

    @Autowired
    private CustomerService customerService;

    @BeforeAll
    void setUp() {
        List<CustomerEntity> customerEntities = new ArrayList<>();
        customerEntities.add(CustomerEntity.builder().custId(101).custName("太阳").build());
        customerEntities.add(CustomerEntity.builder().custId(102).custName("月亮").build());
        customerEntities.add(CustomerEntity.builder().custId(106).custName("Sea").build());
        customerEntities.add(CustomerEntity.builder().custId(109).custName("泰山").build());
        customerEntities.add(CustomerEntity.builder().custId(999).custName("宇宙").build());
        customerService.add(customerEntities);

        customerEntities.clear();
        customerEntities.add(CustomerEntity.builder().custId(119901).custName("待更新").build());
        customerEntities.add(CustomerEntity.builder().custId(119913).custName("待删除").build());
        customerService.add(customerEntities);
    }

    @Test
    void testAddCustomer() throws Exception {
        final List<CustomerEntity> customerEntities = new ArrayList<>();
        customerEntities.add(CustomerEntity.builder().custId(10).custName("张三").build());
        customerEntities.add(CustomerEntity.builder().custId(11).custName("天一").build());

        MockHttpServletResponse response = getMockResponseByRestApiPostWithJson(CUSTOMERS_PATH, customerEntities);
        JSONObject retBodyJson = JSON.parseObject(response.getContentAsString());

        assertThat(retBodyJson.get("code")).isEqualTo("ok");
        assertThat(retBodyJson.get("messageId")).isNotNull();
    }

    @Test
    void testGet() throws Exception {
        String expectedResponse = "[" +
                "{\"custId\": 101,\"custName\": \"太阳\"}," +
                "{\"custId\": 102,\"custName\": \"月亮\"}," +
                "{\"custId\": 106,\"custName\": \"Sea\"}," +
                "{\"custId\": 109,\"custName\": \"泰山\"}," +
                "{\"custId\": 999,\"custName\": \"宇宙\"}" +
                "]";

        MockHttpServletResponse response = getMockResponseByRestApiGet(CUSTOMERS_PATH);
        JSONObject retBodyJson = JSONObject.parseObject(response.getContentAsString());

        assertThat(retBodyJson.getString("code")).isEqualTo("ok");
        assertThat(retBodyJson.getIntValue("total")).isGreaterThanOrEqualTo(5);

        List<Object> resultJson = retBodyJson.getJSONArray("records").stream()
                .filter(s -> List.of(101, 102, 106, 109, 999)
                        .contains(((JSONObject) s).to(CustomerEntity.class).getCustId()))
                .toList();
        JSONAssert.assertEquals(expectedResponse, resultJson.toString(), false);
    }

    @Test
    void testGetOneCustomer() throws Exception {
        String expectedResponse = "{\"custId\": 999,\"custName\": \"宇宙\"}";

        MockHttpServletResponse response = getMockResponseByRestApiGet(CUSTOMERS_PATH + "/999");
        JSONObject retBodyJson = JSONObject.parseObject(response.getContentAsString());

        assertThat(retBodyJson.getString("code")).isEqualTo("ok");
        assertThat(retBodyJson.getIntValue("total")).isEqualTo(1);

        JSONObject resultJson = retBodyJson.getJSONArray("records").getJSONObject(0);
        JSONAssert.assertEquals(expectedResponse, resultJson.toString(), false);
    }

    @Test
    void testUpdateOneCustomer() throws Exception {
        final CustomerEntity customer = CustomerEntity.builder().custId(119901).custName("已更新").build();

        MockHttpServletResponse response = getMockResponseByRestApiPutWithJson(
                CUSTOMERS_PATH + "/" + customer.getCustId(), customer);
        JSONObject retBodyJson = JSON.parseObject(response.getContentAsString());

        assertThat(retBodyJson.get("code")).isEqualTo("ok");
        assertThat(retBodyJson.get("message")).isEqualTo("update success");
        assertThat(retBodyJson.get("messageId")).isNotNull();

        response = getMockResponseByRestApiGet(CUSTOMERS_PATH + "/" + customer.getCustId());
        retBodyJson = JSON.parseObject(response.getContentAsString());

        JSONObject resultJson = retBodyJson.getJSONArray("records").getJSONObject(0);
        JSONAssert.assertEquals(JSONObject.toJSONString(customer), resultJson.toString(), false);
    }

    @Test
    void testDeleteOneCustomer() throws Exception {
        MockHttpServletResponse response = getMockResponseByRestApiDelete(CUSTOMERS_PATH + "/119913");
        JSONObject retBodyJson = JSON.parseObject(response.getContentAsString());

        assertThat(retBodyJson.get("code")).isEqualTo("ok");
        assertThat(retBodyJson.get("message")).isEqualTo("delete success");
        assertThat(retBodyJson.get("messageId")).isNotNull();

        response = getMockResponseByRestApiGet(CUSTOMERS_PATH + "/119913");
        retBodyJson = JSON.parseObject(response.getContentAsString());

        assertThat(retBodyJson.get("code")).isEqualTo("ok");
        assertThat(retBodyJson.get("total")).isEqualTo(0);
        assertThat(retBodyJson.getJSONArray("records")).isEqualTo(JSONArray.of());
    }
}
