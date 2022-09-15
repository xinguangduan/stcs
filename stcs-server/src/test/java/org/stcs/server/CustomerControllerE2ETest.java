package org.stcs.server;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import com.mongodb.client.result.DeleteResult;
import com.mongodb.client.result.UpdateResult;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.mockito.Mockito;
import org.skyscreamer.jsonassert.JSONAssert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.mock.web.MockHttpServletResponse;
import org.stcs.server.constant.ResultType;
import org.stcs.server.dto.CustomerDto;
import org.stcs.server.entity.CustomerEntity;
import org.stcs.server.service.CustomerService;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class CustomerControllerE2ETest extends AbstractTest {
    private static final String CUSTOMERS_PATH = "/api/v1/customers";

    @Autowired
    private CustomerService customerService;

    @SpyBean
    MongoTemplate spyMongoTemplate;

    @MockBean
    UpdateResult mockUpdateResult;

    @MockBean
    DeleteResult mockDeleteResult;

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
    void testAddDuplicatedKeyCustomer() throws Exception {
        final List<CustomerEntity> customerEntities = List.of(
            CustomerEntity.builder().custId(106).custName("江河").build()
        );

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
    void testGetAllWithPage() throws Exception {
        MockHttpServletResponse response = getMockResponseByRestApiGet(CUSTOMERS_PATH + "/1/4");
        JSONObject retBodyJson = JSONObject.parseObject(response.getContentAsString());

        assertThat(retBodyJson.getString("code")).isEqualTo("ok");
        assertThat(retBodyJson.getIntValue("pageNum")).isEqualTo(1);
        assertThat(retBodyJson.getIntValue("pageSize")).isEqualTo(4);
        assertThat(retBodyJson.getIntValue("pages")).isGreaterThanOrEqualTo(2);
        assertThat(retBodyJson.getIntValue("total")).isGreaterThanOrEqualTo(5);

        response = getMockResponseByRestApiGet(CUSTOMERS_PATH + "/2/4");
        retBodyJson = JSONObject.parseObject(response.getContentAsString());

        assertThat(retBodyJson.getString("code")).isEqualTo("ok");
        assertThat(retBodyJson.getIntValue("pageNum")).isEqualTo(2);
        assertThat(retBodyJson.getIntValue("pageSize")).isEqualTo(4);
        assertThat(retBodyJson.getIntValue("pages")).isGreaterThanOrEqualTo(2);
        assertThat(retBodyJson.getIntValue("total")).isGreaterThanOrEqualTo(5);

        response = getMockResponseByRestApiGet(CUSTOMERS_PATH + "/3/4");
        retBodyJson = JSONObject.parseObject(response.getContentAsString());

        assertThat(retBodyJson.getString("code")).isEqualTo("ok");
        assertThat(retBodyJson.getIntValue("pageNum")).isGreaterThanOrEqualTo(1);
        assertThat(retBodyJson.getIntValue("pageSize")).isEqualTo(4);
        assertThat(retBodyJson.getIntValue("pages")).isGreaterThanOrEqualTo(2);
        assertThat(retBodyJson.getIntValue("total")).isGreaterThanOrEqualTo(5);
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

        assertThat(retBodyJson.get("code")).isEqualTo(ResultType.UPDATE_SUCCESS.getCode());
        assertThat(retBodyJson.get("message")).isEqualTo(ResultType.UPDATE_SUCCESS.getInfo());
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

        assertThat(retBodyJson.get("code")).isEqualTo(ResultType.DELETE_SUCCESS.getCode());
        assertThat(retBodyJson.get("message")).isEqualTo(ResultType.DELETE_SUCCESS.getInfo());
        assertThat(retBodyJson.get("messageId")).isNotNull();

        response = getMockResponseByRestApiGet(CUSTOMERS_PATH + "/119913");
        retBodyJson = JSON.parseObject(response.getContentAsString());

        assertThat(retBodyJson.get("code")).isEqualTo(ResultType.RECORD_NOT_FOUND.getCode());
        assertThat(retBodyJson.get("reason")).isEqualTo(ResultType.RECORD_NOT_FOUND.getInfo());
        assertThat(retBodyJson.get("desc")).isEqualTo(ResultType.RECORD_NOT_FOUND.getDescription());
    }

    @Test
    void testAddCustomerFailWithSpyMongoTemplate() throws Exception {
        final List<CustomerEntity> customerEntities = List.of(
                CustomerEntity.builder().custId(17).custName("新增失败").build());

        when(spyMongoTemplate.insertAll(Mockito.anyList())).thenReturn(List.of());

        MockHttpServletResponse response = getMockResponseByRestApiPostWithJson(CUSTOMERS_PATH, customerEntities);
        JSONObject retBodyJson = JSON.parseObject(response.getContentAsString());

        assertThat(retBodyJson.get("code")).isEqualTo(ResultType.CUSTOMER_ADD_FAILURE.getCode());
        assertThat(retBodyJson.get("reason")).isEqualTo(ResultType.CUSTOMER_ADD_FAILURE.getInfo());
        assertThat(retBodyJson.get("desc")).isNotNull();
    }

    @Test
    void testAddCustomerFailWithNonCustomerObject() throws Exception {
        MockHttpServletResponse response = getMockResponseByRestApiPostWithJson(CUSTOMERS_PATH, List.of("Non Customer Object"));
        JSONObject retBodyJson = JSON.parseObject(response.getContentAsString());

        assertThat(retBodyJson.get("code")).isEqualTo("JSONException");
        assertThat(retBodyJson.get("reason")).isEqualTo("JSONException");
        assertThat(retBodyJson.get("desc")).isEqualTo("expect ':', but ], offset 2, character \", line 1, column 3, fastjson-version 2.0.12 [\"Non Customer Object\"]");
    }

    @Test
    void testUpdateCustomerFailWithSpyMongoTemplate() throws Exception {
        final CustomerEntity customerEntity = CustomerEntity.builder().custId(119901).custName("修改失败").build();

        // when().thenReturn() 调用检查更严格，尝试多种方法来模拟第三个实参 CustomerDto.class 均编译/运行失败，故改为 doReturn 方式
        doReturn(mockUpdateResult).when(spyMongoTemplate).updateMulti(Mockito.any(), Mockito.any(), Mockito.eq(CustomerDto.class));
        when(mockUpdateResult.getMatchedCount()).thenReturn(0L);

        MockHttpServletResponse response = getMockResponseByRestApiPutWithJson(CUSTOMERS_PATH + "/119901", customerEntity);
        JSONObject retBodyJson = JSON.parseObject(response.getContentAsString());

        assertThat(retBodyJson.get("code")).isEqualTo(ResultType.CUSTOMER_UPDATE_FAILURE.getCode());
        assertThat(retBodyJson.get("reason")).isEqualTo(ResultType.CUSTOMER_UPDATE_FAILURE.getInfo());
        assertThat(retBodyJson.get("desc")).isNotNull();
    }

    @Test
    void testUpdateCustomerFailWithNonExistRecord() throws Exception {
        final CustomerEntity customerEntity = CustomerEntity.builder().custId(17).custName("修改失败").build();

        MockHttpServletResponse response = getMockResponseByRestApiPutWithJson(CUSTOMERS_PATH + "/17", customerEntity);
        JSONObject retBodyJson = JSON.parseObject(response.getContentAsString());

        assertThat(retBodyJson.get("code")).isEqualTo(ResultType.RECORD_NOT_FOUND.getCode());
        assertThat(retBodyJson.get("reason")).isEqualTo(ResultType.RECORD_NOT_FOUND.getInfo());
        assertThat(retBodyJson.get("desc")).isEqualTo(ResultType.RECORD_NOT_FOUND.getDescription());
    }

    @Test
    void testUpdateCustomerFailWithNonCustomerObject() throws Exception {
        MockHttpServletResponse response = getMockResponseByRestApiPutWithJson(CUSTOMERS_PATH +"/17","Non Customer Object");
        JSONObject retBodyJson = JSON.parseObject(response.getContentAsString());

        assertThat(retBodyJson.get("code")).isEqualTo("HttpMessageNotReadableException");
        assertThat(retBodyJson.get("reason")).isEqualTo("HttpMessageNotReadableException");
        assertThat((String)retBodyJson.get("desc")).startsWith("JSON parse error: Cannot deserialize value of");
    }

    @Test
    void testDeleteCustomerFailWithSpyMongoTemplate() throws Exception {
        doReturn(mockDeleteResult).when(spyMongoTemplate).remove(Mockito.any(), Mockito.eq(CustomerDto.class));
        when(mockDeleteResult.getDeletedCount()).thenReturn(0L);

        MockHttpServletResponse response = getMockResponseByRestApiDelete(CUSTOMERS_PATH + "/109");
        JSONObject retBodyJson = JSON.parseObject(response.getContentAsString());

        assertThat(retBodyJson.get("code")).isEqualTo(ResultType.CUSTOMER_DELETE_FAILURE.getCode());
        assertThat(retBodyJson.get("reason")).isEqualTo(ResultType.CUSTOMER_DELETE_FAILURE.getInfo());
        assertThat(retBodyJson.get("desc")).isNotNull();
    }

    @Test
    void testDeleteCustomerFailWithNonExistRecord() throws Exception {
        MockHttpServletResponse response = getMockResponseByRestApiDelete(CUSTOMERS_PATH + "/2999");
        JSONObject retBodyJson = JSON.parseObject(response.getContentAsString());

        assertThat(retBodyJson.get("code")).isEqualTo(ResultType.CUSTOMER_DELETE_FAILURE.getCode());
        assertThat(retBodyJson.get("reason")).isEqualTo(ResultType.CUSTOMER_DELETE_FAILURE.getInfo());
        assertThat(retBodyJson.get("desc")).isNotNull();
    }
}
