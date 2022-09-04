package org.stcs.server;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.alibaba.fastjson2.JSONObject;
import org.junit.jupiter.api.Test;
import org.skyscreamer.jsonassert.JSONAssert;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.stcs.server.service.OrderService;
import org.testcontainers.containers.MongoDBContainer;
import org.testcontainers.utility.DockerImageName;

//@DataMongoTest
@SpringBootTest
@AutoConfigureMockMvc
//@WebMvcTest(controllers = OrderInfoController.class)
public class OrderControllerE2ETest {
    private static final Logger logger = LoggerFactory.getLogger(OrderControllerE2ETest.class);

    private static final MongoDBContainer mongoDBContainer = new MongoDBContainer(DockerImageName.parse("mongo"));
    private static final String ORDERS_PATH = "/stcs/api/v1/orders";
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private OrderService orderService;

    @DynamicPropertySource
    static void mongodbProperties(DynamicPropertyRegistry registry) {
        mongoDBContainer.start();
        registry.add("spring.data.mongodb.uri", mongoDBContainer::getReplicaSetUrl);
    }

    @Test
    void testAddOrder() throws Exception {

        String newOrderInfoStr = "{\"orderId\": 401,\"orderDesc\": \"new order test #1\",\"custId\": 101,\"partId\": 301}";

        RequestBuilder requestBuilder = MockMvcRequestBuilders.post(ORDERS_PATH)
                .accept(MediaType.APPLICATION_JSON).content(newOrderInfoStr)
                .contentType(MediaType.APPLICATION_JSON);
        MockHttpServletResponse response = mockMvc.perform(requestBuilder)
                .andExpect(status().isOk())
                .andReturn().getResponse();
        logger.info(response.getContentAsString());

        JSONObject retBodyJson = JSONObject.parseObject(response.getContentAsString());
        assertThat(retBodyJson.get("message")).isEqualTo("ok");
        assertThat(retBodyJson.get("messageId")).isNotNull();

    }

    @Test
    void testGetOrders() throws Exception {

        String expectedResponse = "{\"orderId\": 401,\"orderDesc\": \"new order test #1\",\"custId\": 101,\"partId\": 301}";

        RequestBuilder requestBuilder = MockMvcRequestBuilders.get("/stcs/api/v1/orders");
        MockHttpServletResponse response = mockMvc.perform(requestBuilder)
                .andExpect(status().isOk())
                .andReturn().getResponse();
        logger.info(response.getContentAsString());
        JSONObject retBodyJson = JSONObject.parseObject(response.getContentAsString());

        assertThat(retBodyJson.getString("code")).isEqualTo("ok");
        assertThat(retBodyJson.getIntValue("total")).isEqualTo(1);

        JSONObject resultJson = retBodyJson.getJSONArray("records").getJSONObject(0);
        JSONAssert.assertEquals(resultJson.toString(), expectedResponse, false);

    }
}
