package org.stcs.server.rest;

import static org.stcs.server.constant.GlobalConstant.*;
import static org.stcs.server.protocol.STCSProtocolBuilder.*;

import java.util.Arrays;
import java.util.List;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.stcs.server.annotation.LatencyTime;
import org.stcs.server.common.Pagination;
import org.stcs.server.entity.PartEntity;
import org.stcs.server.mapper.PartMapper;
import org.stcs.server.service.PartService;

@RestController
@Slf4j
@SecurityRequirement(name = "stcs")
@RequestMapping("/api/v1/parts")
public class PartController extends AbstractController {

    private final PartService partService;

    @Autowired
    public PartController(PartService partService) {
        this.partService = partService;
    }


    @Operation(summary = "Get all Parts")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Found all Parts",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = PartEntity.class))}),
            @ApiResponse(responseCode = "404", description = "Part not found",
                    content = @Content),
            @ApiResponse(responseCode = "500", description = "Server Internal Error",
                    content = @Content)
    })
    @LatencyTime
    @GetMapping
    public ResponseEntity find() {
        final List<PartEntity> partEntities = partService.findAll();
        return ResponseEntity.ok().body(buildResponseCollections(partEntities));
    }

    @Operation(summary = "Get a Part by its id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Found the Part",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = PartEntity.class))}),
            @ApiResponse(responseCode = "400", description = "Invalid id supplied",
                    content = @Content),
            @ApiResponse(responseCode = "404", description = "Part not found",
                    content = @Content),
            @ApiResponse(responseCode = "500", description = "Server Internal Error")})
    @LatencyTime
    @GetMapping(value = "/{partId}")
    public ResponseEntity findOne(@PathVariable int partId) {
        final PartEntity partEntity = partService.find(partId);
        return ResponseEntity.ok().body(buildResponseCollections(Arrays.asList(partEntity)));
    }

    @Operation(summary = "Get a Part by pagination")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Found the pagination parts",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = PartEntity.class))}),
            @ApiResponse(responseCode = "400", description = "Invalid parameters supplied",
                    content = @Content),
            @ApiResponse(responseCode = "404", description = "Parts not found",
                    content = @Content),
            @ApiResponse(responseCode = "500", description = "Server Internal Error")})
    @LatencyTime
    @GetMapping(value = "/{pageNum}/{pageSize}")
    public ResponseEntity find(@PathVariable int pageNum, @PathVariable int pageSize, @RequestBody(required = false) PartEntity part) {
        Pagination page = Pagination.builder().pageNum(pageNum).pageSize(pageSize).build();
        final Pagination<PartEntity> partEntities = partService.find(page, part);
        return ResponseEntity.ok().body(buildResponsePagination(partEntities));
    }

    @Operation(summary = "Add parts, support batch or single")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Add the parts",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = PartEntity.class))}),
            @ApiResponse(responseCode = "400", description = "Invalid parts supplied",
                    content = @Content),
            @ApiResponse(responseCode = "500", description = "Server Internal Error")})
    @LatencyTime
    @PostMapping
    public ResponseEntity add(@RequestBody String req) {
        final List<PartEntity> partEntities = JSON.parseArray(req, PartEntity.class);
        long result = partService.add(partEntities);
        JSONObject res = buildSuccess(result + " entities were added successfully");
        if (result <= 0) {
            res = buildFailure(ERROR_1001);
            return ResponseEntity.internalServerError().body(res);
        }
        return ResponseEntity.ok(res);
    }

    @Operation(summary = "Update part, only support single")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Update the parts",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = PartEntity.class))}),
            @ApiResponse(responseCode = "400", description = "Invalid parts supplied",
                    content = @Content),
            @ApiResponse(responseCode = "500", description = "Server Internal Error")})
    @LatencyTime
    @PutMapping(value = "/{partId}")
    public ResponseEntity update(@RequestBody String req, @PathVariable int partId) {
        final PartEntity partEntity = partService.find(partId);
        if (partEntity == null) {
            return ResponseEntity.ok().body(buildFailure(ERROR_1005, "order not found"));
        }
        final PartEntity newPartEntity = JSON.to(PartEntity.class, req);
        PartMapper.converter.toCloneEntity(newPartEntity, partEntity);
        long result = partService.update(partEntity);
        JSONObject resp = buildSuccess(result + " entities were updated successfully");
        if (result <= 0) {
            resp = buildFailure(ERROR_1003, "update failure");
        }
        return ResponseEntity.ok().body(resp);
    }

    @Operation(summary = "Delete part, only support single")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Delete the parts",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = PartEntity.class))}),
            @ApiResponse(responseCode = "400", description = "Invalid parts supplied",
                    content = @Content),
            @ApiResponse(responseCode = "500", description = "Server Internal Error")})
    @LatencyTime
    @DeleteMapping(value = "/{partId}")
    public ResponseEntity delete(@PathVariable int partId) {
        long result = partService.delete(partId);
        JSONObject resp = buildSuccess(result + " entities were deleted successfully");
        if (result <= 0) {
            resp = buildFailure(ERROR_1002, "delete failure");
        }
        return ResponseEntity.ok().body(resp);
    }

}
