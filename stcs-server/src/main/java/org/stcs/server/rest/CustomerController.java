package org.stcs.server.rest;

import static org.stcs.server.protocol.STCSProtocolBuilder.*;

import java.util.List;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.stcs.server.constant.ResultType;
import org.stcs.server.entity.CustomerEntity;
import org.stcs.server.service.CustomerService;

@RestController
@Slf4j
@RequestMapping(value = "/api/v1/customers")
public class CustomerController extends AbstractRestController {

    private final CustomerService customerService;

    @Autowired
    public CustomerController(CustomerService custInfoService) {
        this.customerService = custInfoService;
    }

    @GetMapping
    public ResponseEntity find() {
        final List<CustomerEntity> customerEntities = customerService.findAll();
        log.info("find result {}", customerEntities);
        return ResponseEntity.ok().body(buildResponseCollections(customerEntities));
    }

    @GetMapping("/{custId}")
    public ResponseEntity findOne(@PathVariable int custId) {
        final CustomerEntity customerEntity = customerService.find(custId);
        log.info("find result {}", customerEntity);
        if (customerEntity == null)
            return ResponseEntity.ok().body(buildFailure(ResultType.CUSTOMER_NOT_FOUND));
        return ResponseEntity.ok().body(buildResponseCollections(List.of(customerEntity)));
    }

    @PostMapping
    public ResponseEntity add(@RequestBody String req) {
        final List<CustomerEntity> customerEntities = JSON.parseArray(req, CustomerEntity.class);
        long result = customerService.add(customerEntities);
        if (result > 0) {
            return ResponseEntity.ok().body(buildSuccess());
        }
        return ResponseEntity.ok(buildFailure(ResultType.CUSTOMER_ADD_FAILURE));
    }

    @PutMapping("/{custId}")
    public ResponseEntity update(@RequestBody JSONObject req, @PathVariable int custId) {
        final CustomerEntity customerEntity = customerService.find(custId);
        if (customerEntity == null) {
            return ResponseEntity.ok().body(buildFailure(ResultType.CUSTOMER_NOT_FOUND));
        }
        final CustomerEntity newCustomerEntity = JSON.to(CustomerEntity.class, req);
        long result = customerService.update(newCustomerEntity);
        if (result > 0) {
            return ResponseEntity.ok(buildSuccess(ResultType.UPDATE_SUCCESS));
        }
        return ResponseEntity.ok(buildFailure(ResultType.CUSTOMER_UPDATE_FAILURE));
    }

    @DeleteMapping("/{custId}")
    public ResponseEntity delete(@PathVariable int custId) {
        long result = customerService.delete(custId);
        if (result > 0) {
            return ResponseEntity.ok(buildSuccess(ResultType.DELETE_SUCCESS));
        }
        return ResponseEntity.ok(buildFailure(ResultType.CUSTOMER_DELETE_FAILURE));
    }
}
