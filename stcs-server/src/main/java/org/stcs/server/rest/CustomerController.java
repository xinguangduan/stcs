package org.stcs.server.rest;

import static org.stcs.server.protocol.STCSProtocolBuilder.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.stcs.server.annotation.LatencyTime;
import org.stcs.server.common.Pagination;
import org.stcs.server.constant.ResultType;
import org.stcs.server.entity.CustomerEntity;
import org.stcs.server.exception.STCSException;
import org.stcs.server.service.CustomerService;

@RestController
@Slf4j
@RequestMapping(value = "/api/v1/customers")
public class CustomerController extends AbstractController {

    private final CustomerService customerService;

    @Autowired
    public CustomerController(CustomerService custInfoService) {
        this.customerService = custInfoService;
    }

    @LatencyTime
    @GetMapping
    public ResponseEntity<?>  find() {
        try{
            final List<CustomerEntity> customerEntities = customerService.findAll();
            return ResponseEntity.ok().body(buildResponseCollections(customerEntities));
        }catch (STCSException e){
            return ResponseEntity.badRequest().body(buildResponseCollections(new ArrayList<>()));
        }catch (Exception e){
            return ResponseEntity.badRequest().body(buildResponseCollections(new ArrayList<>()));
        }
    }

    @LatencyTime
    @GetMapping("/{custId}")
    public ResponseEntity<?> findOne(@PathVariable int custId) throws STCSException {
        final CustomerEntity customerEntity = customerService.find(custId);
        return ResponseEntity.ok().body(buildResponseCollections(Arrays.asList(customerEntity)));
    }

    @LatencyTime
    @GetMapping(value = "/{pageNum}/{pageSize}")
    public ResponseEntity find(@PathVariable int pageNum, @PathVariable int pageSize, @RequestBody(required = false) CustomerEntity customerEntity) {
        Pagination page = Pagination.builder().pageNum(pageNum).pageSize(pageSize).build();
        final Pagination<CustomerEntity> partEntities = customerService.find(page, customerEntity);
        return ResponseEntity.ok().body(buildResponsePagination(partEntities));
    }

    @LatencyTime
    @PostMapping
    public ResponseEntity add(@RequestBody String req) {
        final List<CustomerEntity> customerEntities = JSON.parseArray(req, CustomerEntity.class);
        long result = customerService.add(customerEntities);
        if (result > 0) {
            return ResponseEntity.ok().body(buildSuccess());
        }
        return ResponseEntity.ok(buildFailure(ResultType.CUSTOMER_ADD_FAILURE));
    }

    @LatencyTime
    @PutMapping("/{custId}")
    public ResponseEntity update(@RequestBody JSONObject req, @PathVariable int custId) throws STCSException {
        final CustomerEntity customerEntity = customerService.find(custId);
        final CustomerEntity newCustomerEntity = JSON.to(CustomerEntity.class, req);
        long result = customerService.update(newCustomerEntity);
        if (result > 0) {
            return ResponseEntity.ok(buildSuccess(ResultType.UPDATE_SUCCESS));
        }
        return ResponseEntity.ok(buildFailure(ResultType.CUSTOMER_UPDATE_FAILURE));
    }

    @LatencyTime
    @DeleteMapping("/{custId}")
    public ResponseEntity delete(@PathVariable int custId) {
        long result = customerService.delete(custId);
        if (result > 0) {
            return ResponseEntity.ok(buildSuccess(ResultType.DELETE_SUCCESS));
        }
        return ResponseEntity.ok(buildFailure(ResultType.CUSTOMER_DELETE_FAILURE));
    }
}
