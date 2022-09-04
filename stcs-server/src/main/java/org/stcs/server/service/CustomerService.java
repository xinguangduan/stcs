package org.stcs.server.service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import com.mongodb.client.result.DeleteResult;
import com.mongodb.client.result.UpdateResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.stcs.server.dao.CustomerDao;
import org.stcs.server.dto.CustomerDto;
import org.stcs.server.entity.CustomerEntity;

@Service
@Slf4j
public class CustomerService {
    private final CustomerDao customerDao;

    public CustomerService(CustomerDao customerDao) {
        this.customerDao = customerDao;
    }

    public CustomerEntity find(int custId) {
        CustomerEntity customerEntity = CustomerEntity.builder().build();
        CustomerDto customerDto = customerDao.find(custId);
        BeanUtils.copyProperties(customerDto, customerEntity, "_id");
        return customerEntity;
    }

    public List<CustomerEntity> findAll() {
        List<CustomerEntity> customerEntities = new ArrayList<>();
        List<CustomerDto> customerDtos = customerDao.findAll();
        customerDtos.forEach(s->{
            CustomerEntity customerEntity = CustomerEntity.builder().build();
            BeanUtils.copyProperties(s, customerEntity, "_id");
            customerEntities.add(customerEntity);
        });
        return customerEntities;
    }

    public long add(CustomerEntity customerEntity) {
        CustomerDto customerDto = new CustomerDto();
        BeanUtils.copyProperties(customerEntity, customerDto, "_id");
        final Collection<CustomerDto> res = customerDao.insert(customerDto);
        return res.size();
    }

    public long add(List<CustomerEntity> customerEntities) {
        final List<CustomerDto> customerDtoList = new ArrayList<>();
        customerEntities.forEach(s -> {
            CustomerDto customerDTO = new CustomerDto();
            BeanUtils.copyProperties(s, customerDTO);
            customerDtoList.add(customerDTO);
        });

        final Collection<CustomerDto> result = customerDao.insert(customerDtoList);
        log.info("add result {}, {}", result, customerEntities);
        return result.size();
    }

    public long update(CustomerEntity customerEntity) {
        CustomerDto customerDto = new CustomerDto();
        BeanUtils.copyProperties(customerEntity, customerDto);
        UpdateResult result = customerDao.update(customerDto);
        log.info("update result {}, {}", result, customerEntity);
        return result.getModifiedCount();
    }

    public long delete(int custId) {
        DeleteResult result = customerDao.delete(custId);
        log.info("delete result {}, {}", result, custId);
        return result.getDeletedCount();
    }
}
