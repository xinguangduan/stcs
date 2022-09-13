package org.stcs.server;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.skyscreamer.jsonassert.JSONAssert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mock.web.MockHttpServletResponse;
import org.stcs.server.entity.NodeGraphEntity;
import org.stcs.server.entity.TransPathEntity;
import org.stcs.server.entity.TransPlanEntity;
import org.stcs.server.service.NodeGraphService;
import org.stcs.server.service.TransPlanService;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class TransPathControllerE2ETest extends AbstractTest {
    private static final String TRANSPLAN_PATH = "/api/v1/transpaths";

    @Autowired
    private NodeGraphService nodeGraphService;
    @Autowired
    private TransPlanService transPlanService;

    @BeforeAll
    void setUp() {
        List<NodeGraphEntity> nodeGraphEntities = List.of(
                NodeGraphEntity.builder().nodeFrom("A1").nodeTo("B2").distance(3).build(),
                NodeGraphEntity.builder().nodeFrom("B2").nodeTo("C3").distance(13).build(),
                NodeGraphEntity.builder().nodeFrom("C3").nodeTo("D4").distance(31).build()
        );
        nodeGraphService.add(nodeGraphEntities);

        List<TransPlanEntity> transPlanEntities = List.of(
                TransPlanEntity.builder().planId(60061).custId(1001).orderId(4004).dockId(5005).build(),
                TransPlanEntity.builder().planId(60062).custId(1001).orderId(4005).dockId(5006).build(),
                TransPlanEntity.builder().planId(60063).custId(1001).dockId(5007).build(),
                TransPlanEntity.builder().planId(60064).custId(1002).dockId(5008).build()
        );
        transPlanService.add(transPlanEntities);
    }

    @Test
    void testDefinePathAndStepToEnd() throws Exception {
        List<TransPathEntity> transPathEntities = List.of(
                TransPathEntity.builder()
                        .pathId(8001).partId(3003).orderId(4004).pathDef("A1,B2,C3,D4").currentNode("A1").build()
        );

        MockHttpServletResponse response = getMockResponseByRestApiPostWithJson(TRANSPLAN_PATH, transPathEntities);
        JSONObject retBodyJson = JSON.parseObject(response.getContentAsString());

        assertThat(retBodyJson.get("code")).isEqualTo("ok");
        assertThat(retBodyJson.get("message")).isEqualTo("add success");
        assertThat(retBodyJson.get("messageId")).isNotNull();

        TransPathEntity transPathEntity = TransPathEntity.builder().pathId(8001).partId(3003).currentNode("B2").build();

        response = getMockResponseByRestApiPatchWithJson(TRANSPLAN_PATH + "/8001", transPathEntity);
        retBodyJson = JSON.parseObject(response.getContentAsString());

        assertThat(retBodyJson.get("code")).isEqualTo("ok");
        assertThat(retBodyJson.get("message")).isEqualTo("transport ok");
        assertThat(retBodyJson.get("messageId")).isNotNull();

        String expectedResponse = "{\"transPath\":{\"currentNode\":\"B2\",\"nextNode\":\"C3\",\"distance\":13,\"end\":false}}}\n";
        JSONObject retDataJson = retBodyJson.getJSONObject("data");
        JSONAssert.assertEquals(expectedResponse, retDataJson.toJSONString(), false);

        transPathEntity = TransPathEntity.builder().pathId(8001).partId(3003).currentNode("C3").build();

        response = getMockResponseByRestApiPatchWithJson(TRANSPLAN_PATH + "/8001", transPathEntity);
        retBodyJson = JSON.parseObject(response.getContentAsString());

        assertThat(retBodyJson.get("code")).isEqualTo("ok");
        assertThat(retBodyJson.get("message")).isEqualTo("transport ok");
        assertThat(retBodyJson.get("messageId")).isNotNull();

        expectedResponse = "{\"transPath\":{\"currentNode\":\"C3\",\"nextNode\":\"D4\",\"distance\":31,\"end\":false}}}\n";
        retDataJson = retBodyJson.getJSONObject("data");
        JSONAssert.assertEquals(expectedResponse, retDataJson.toJSONString(), false);

        transPathEntity = TransPathEntity.builder().pathId(8001).partId(3003).currentNode("D4").build();

        response = getMockResponseByRestApiPatchWithJson(TRANSPLAN_PATH + "/8001", transPathEntity);
        retBodyJson = JSON.parseObject(response.getContentAsString());

        assertThat(retBodyJson.get("code")).isEqualTo("ok");
        assertThat(retBodyJson.get("message")).isEqualTo("transport ok");
        assertThat(retBodyJson.get("messageId")).isNotNull();

        expectedResponse = "{\"transPath\":{\"currentNode\":\"D4\",\"nextNode\":\"\",\"distance\":0,\"end\":true}}}\n";
        retDataJson = retBodyJson.getJSONObject("data");
        JSONAssert.assertEquals(expectedResponse, retDataJson.toJSONString(), false);
    }
}
