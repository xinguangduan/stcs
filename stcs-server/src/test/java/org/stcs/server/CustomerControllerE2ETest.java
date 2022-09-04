package org.stcs.server;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.stcs.server.entity.CustomerEntity;
import org.stcs.server.service.CustomerService;

//@DataMongoTest
@SpringBootTest
@AutoConfigureMockMvc
//@WebMvcTest(controllers = OrderInfoController.class)
@Slf4j
public class CustomerControllerE2ETest extends AbstractTest {

    private static final String CUSTOMERS_PATH = "/stcs/api/v1/customers";
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private CustomerService customerService;


    @Test
    void testAddCustomer() throws Exception {

        final CustomerEntity customer = CustomerEntity.builder()
                .custId(10)
                .custName("张三")
                .build();

        RequestBuilder requestBuilder = MockMvcRequestBuilders.post(CUSTOMERS_PATH)
                .accept(MediaType.APPLICATION_JSON).content(JSON.toJSONBytes(customer))
                .contentType(MediaType.APPLICATION_JSON);
        MockHttpServletResponse response = mockMvc.perform(requestBuilder)
                .andExpect(status().isOk())
                .andReturn().getResponse();
        log.info(response.getContentAsString());

        JSONObject retBodyJson = JSON.parseObject(response.getContentAsString());

        assertThat(retBodyJson.get("message")).isEqualTo("ok");
        assertThat(retBodyJson.get("messageId")).isNotNull();

    }

    @Test
    void testGet() throws Exception {

        RequestBuilder requestBuilder = MockMvcRequestBuilders.get(CUSTOMERS_PATH);
        MockHttpServletResponse response = mockMvc.perform(requestBuilder)
                .andExpect(status().isOk())
                .andReturn().getResponse();
        log.info(response.getContentAsString());
        JSONObject retBodyJson = JSONObject.parseObject(response.getContentAsString());

        assertThat(retBodyJson.getString("code")).isEqualTo("ok");
        assertThat(retBodyJson.getIntValue("total")).isEqualTo(1);

        JSONObject resultJson = retBodyJson.getJSONArray("records").getJSONObject(0);

        int recorders = resultJson.getIntValue("records");
//        JSONAssert.assertEquals(resultJson.toString(), expectedResponse, false);
        assertThat(resultJson.getIntValue("code")).isEqualTo("ok");
    }
}
