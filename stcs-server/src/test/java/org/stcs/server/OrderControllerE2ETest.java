package org.stcs.server;

import static org.assertj.core.api.Assertions.assertThat;

import com.alibaba.fastjson2.JSONObject;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.skyscreamer.jsonassert.JSONAssert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mock.web.MockHttpServletResponse;
import org.stcs.server.entity.CustomerEntity;
import org.stcs.server.entity.MaterialSpecEntity;
import org.stcs.server.entity.OrderEntity;
import org.stcs.server.entity.PartEntity;
import org.stcs.server.service.OrderService;

import java.util.ArrayList;
import java.util.List;

@TestMethodOrder(MethodOrderer.MethodName.class)
public class OrderControllerE2ETest extends AbstractTest {
    private static final String ORDERS_PATH = "/api/v1/orders";

    @Autowired
    private OrderService orderService;

    @Test
    void testAddOrder() throws Exception {
        List<PartEntity> partEntities = new ArrayList<>();
        PartEntity part = PartEntity.builder()
                .partId(301).partDesc("part 001")
                .materialSpec(MaterialSpecEntity.builder()
                        .materialId(302).materialDesc("material 001").materialSpec("3x5")
                        .build())
                .partNum(8)
                .build();
        partEntities.add(part);
        OrderEntity order = OrderEntity.builder()
                .orderId(401).orderDesc("new order test #1")
                .customer(CustomerEntity.builder()
                        .custId(101).custName("customer 001")
                        .build())
                .parts(partEntities)
                .build();

        MockHttpServletResponse response = getMockResponseByRestApiPostWithJson(ORDERS_PATH, order);
        JSONObject retBodyJson = JSONObject.parseObject(response.getContentAsString());

        assertThat(retBodyJson.get("message")).isEqualTo("add success");
        assertThat(retBodyJson.get("messageId")).isNotNull();
    }

    @Test
    void testGetOrders() throws Exception {
        String expectedResponse = "{\"orderId\": 401," +
                "\"orderDesc\": \"new order test #1\"," +
                "\"customer\":" +
                "{\"custId\": 101,\"custName\": \"customer 001\"}," +
                "\"parts\":" +
                "[" +
                "{\"partId\": 301,\"partDesc\": \"part 001\"," +
                "\"materialSpec\":{\"materialId\":302,\"materialDesc\":\"material 001\",\"materialSpec\":\"3x5\"}," +
                "\"partNum\":8}]}";

        MockHttpServletResponse response = getMockResponseByRestApiGet(ORDERS_PATH);
        JSONObject retBodyJson = JSONObject.parseObject(response.getContentAsString());

        assertThat(retBodyJson.getString("code")).isEqualTo("ok");
        assertThat(retBodyJson.getIntValue("total")).isEqualTo(1);

        JSONObject resultJson = retBodyJson.getJSONArray("records").getJSONObject(0);
        JSONAssert.assertEquals(expectedResponse, resultJson.toString(), false);
    }
}
