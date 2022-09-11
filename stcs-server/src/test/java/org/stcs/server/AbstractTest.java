package org.stcs.server;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.nio.charset.StandardCharsets;

import com.alibaba.fastjson2.JSON;
import lombok.extern.slf4j.Slf4j;
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
import org.testcontainers.containers.MongoDBContainer;
import org.testcontainers.utility.DockerImageName;

@SpringBootTest
@AutoConfigureMockMvc
@Slf4j
public class AbstractTest {
    private static final MongoDBContainer mongoDBContainer = new MongoDBContainer(DockerImageName.parse("mongo"));

    @Autowired
    MockMvc mockMvc;

    @DynamicPropertySource
    static void mongodbProperties(DynamicPropertyRegistry registry) {
        mongoDBContainer.start();
        registry.add("spring.data.mongodb.uri", mongoDBContainer::getReplicaSetUrl);
    }

    MockHttpServletResponse getMockResponseByRestApiGet(String url) throws Exception {
        RequestBuilder requestBuilder = MockMvcRequestBuilders.get(url);
        MockHttpServletResponse response = mockMvc.perform(requestBuilder)
                .andExpect(status().isOk())
                .andReturn().getResponse();
        log.info(response.getCharacterEncoding());
        response.setCharacterEncoding(StandardCharsets.UTF_8.name());
        log.info(response.getContentAsString());

        return response;
    }

    MockHttpServletResponse getMockResponseByRestApiPostWithJson(String url, Object jsonObject) throws Exception {
        RequestBuilder requestBuilder = MockMvcRequestBuilders.post(url)
                .accept(MediaType.APPLICATION_JSON).content(JSON.toJSONBytes(jsonObject))
                .contentType(MediaType.APPLICATION_JSON);
        MockHttpServletResponse response = mockMvc.perform(requestBuilder)
                .andExpect(status().isOk())
                .andReturn().getResponse();
        log.info(response.getContentAsString());

        return response;
    }

    MockHttpServletResponse getMockResponseByRestApiPutWithJson(String url, Object jsonObject) throws Exception {
        RequestBuilder requestBuilder = MockMvcRequestBuilders.put(url)
                .accept(MediaType.APPLICATION_JSON).content(JSON.toJSONBytes(jsonObject))
                .contentType(MediaType.APPLICATION_JSON);
        MockHttpServletResponse response = mockMvc.perform(requestBuilder)
                .andExpect(status().isOk())
                .andReturn().getResponse();
        log.info(response.getContentAsString());

        return response;
    }

    MockHttpServletResponse getMockResponseByRestApiDelete(String url) throws Exception {
        RequestBuilder requestBuilder = MockMvcRequestBuilders.delete(url);
        MockHttpServletResponse response = mockMvc.perform(requestBuilder)
                .andExpect(status().isOk())
                .andReturn().getResponse();
        log.info(response.getContentAsString());

        return response;
    }
}
