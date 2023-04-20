package org.stcs.server.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import com.mongodb.client.result.DeleteResult;
import com.mongodb.client.result.UpdateResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.stcs.server.common.Pagination;
import org.stcs.server.dao.CustomerDao;
import org.stcs.server.dto.CustomerDto;
import org.stcs.server.entity.CustomerEntity;
import org.stcs.server.exception.STCSException;
import org.stcs.server.exception.STCSExceptionEntity;
import org.stcs.server.mapper.CustomerMapper;

@Service
@Slf4j
public class CustomerService extends AbstractService {
    private final CustomerDao customerDao;

    public CustomerService(CustomerDao customerDao) {
        this.customerDao = customerDao;
    }

    public CustomerEntity find(int custId) throws STCSException {
        CustomerDto customerDto = customerDao.find(custId);
        checkNotFoundException(customerDto);
        CustomerEntity customerEntity = CustomerEntity.builder().build();
        CustomerMapper.converter.toEntity(customerDto, customerEntity);
        return customerEntity;
    }

    public List<CustomerEntity> findAll() throws STCSException {
        List<CustomerEntity> customerEntities = new ArrayList<>();
        List<CustomerDto> customerDtos = customerDao.findAll();
        CustomerMapper.converter.toEntity(customerDtos, customerEntities);
        if(customerDtos==null){
            throw new STCSException(STCSExceptionEntity.builder().build());
        }
        return customerEntities;
    }

    public Pagination<CustomerEntity> find(Pagination pagination, CustomerEntity customerEntity) {
        CustomerDto customerDto = new CustomerDto();
        CustomerMapper.converter.toDto(customerEntity, customerDto);

        Pagination<CustomerDto> result = customerDao.find(pagination, customerDto);
        Pagination<CustomerEntity> newPagination = new Pagination<>();
        CustomerMapper.converter.toPageResult(result, newPagination);
        return newPagination;
    }

    public long add(CustomerEntity customerEntity) {
        return add(Arrays.asList(customerEntity));
    }

    public long add(List<CustomerEntity> customerEntities) {
        final List<CustomerDto> customerDtoList = new ArrayList<>();
        CustomerMapper.converter.toDto(customerEntities, customerDtoList);
        final Collection<CustomerDto> result = customerDao.insert(customerDtoList);
        log.info("add result {}, {}", result, customerEntities);
        return result.size();
    }

    public long update(CustomerEntity customerEntity) {
        CustomerDto customerDto = new CustomerDto();
        CustomerMapper.converter.toDto(customerEntity, customerDto);
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
