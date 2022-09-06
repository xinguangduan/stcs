package org.stcs.server.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

import java.util.Collections;
import java.util.List;

import org.junit.FixMethodOrder;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.runners.MethodSorters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
import org.springframework.test.web.servlet.MockMvc;
import org.stcs.server.entity.MaterialSpecEntity;
import org.stcs.server.entity.PartEntity;
import org.stcs.server.service.PartService;

@ExtendWith(SpringExtension.class)
@WebMvcTest(PartController.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class PartControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private PartService mockPartService;

    @Test
    @Order(1)
    void testFind() throws Exception {
        // Setup
        // Configure PartService.findAll(...).
        final List<PartEntity> partEntities = List.of(
                new PartEntity(0, "partDesc", new MaterialSpecEntity(0, "materialDesc", "materialSpec"), 0));
        when(mockPartService.findAll()).thenReturn(partEntities);

        // Run the test
        final MockHttpServletResponse response = mockMvc.perform(get("/api/v1/parts")
                        .accept(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        // Verify the results
        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.getContentAsString()).isEqualTo("expectedResponse");
    }

    @Test
    @Order(2)
    void testFind_PartServiceReturnsNoItems() throws Exception {
        // Setup
        when(mockPartService.findAll()).thenReturn(Collections.emptyList());

        // Run the test
        final MockHttpServletResponse response = mockMvc.perform(get("/api/v1/parts")
                        .accept(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        // Verify the results
        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.getContentAsString()).isEqualTo("expectedResponse");
    }

    @Test
    @Order(3)
    void testFindOne() throws Exception {
        // Setup
        // Configure PartService.find(...).
        final PartEntity partEntity = new PartEntity(0, "partDesc",
                new MaterialSpecEntity(0, "materialDesc", "materialSpec"), 0);
        when(mockPartService.find(0)).thenReturn(partEntity);

        // Run the test
        final MockHttpServletResponse response = mockMvc.perform(get("/api/v1/parts/{partId}", 0)
                        .accept(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        // Verify the results
        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.getContentAsString()).isEqualTo("expectedResponse");
    }

    @Test
    void testAdd() throws Exception {
        // Setup
        when(mockPartService.add(
                List.of(new PartEntity(0, "partDesc", new MaterialSpecEntity(0, "materialDesc", "materialSpec"),
                        0)))).thenReturn(0L);

        // Run the test
        final MockHttpServletResponse response = mockMvc.perform(post("/api/v1/parts")
                        .content("content").contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        // Verify the results
        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.getContentAsString()).isEqualTo("expectedResponse");
    }

    @Test
    void testUpdate() throws Exception {
        // Setup
        // Configure PartService.find(...).
        final PartEntity partEntity = new PartEntity(0, "partDesc",
                new MaterialSpecEntity(0, "materialDesc", "materialSpec"), 0);
        when(mockPartService.find(0)).thenReturn(partEntity);

        when(mockPartService.update(
                new PartEntity(0, "partDesc", new MaterialSpecEntity(0, "materialDesc", "materialSpec"),
                        0))).thenReturn(0L);

        // Run the test
        final MockHttpServletResponse response = mockMvc.perform(put("/api/v1/parts/{partId}", 0)
                        .content("content").contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        // Verify the results
        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.getContentAsString()).isEqualTo("expectedResponse");
    }

    @Test
    void testUpdate_PartServiceFindReturnsNull() throws Exception {
        // Setup
        when(mockPartService.find(0)).thenReturn(null);

        // Run the test
        final MockHttpServletResponse response = mockMvc.perform(put("/api/v1/parts/{partId}", 0)
                        .content("content").contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        // Verify the results
        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.getContentAsString()).isEqualTo("expectedResponse");
    }

    @Test
    void testDelete() throws Exception {
        // Setup
        when(mockPartService.delete(0)).thenReturn(0L);

        // Run the test
        final MockHttpServletResponse response = mockMvc.perform(delete("/api/v1/parts/{partId}", 0)
                        .accept(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        // Verify the results
        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.getContentAsString()).isEqualTo("expectedResponse");
    }
}
