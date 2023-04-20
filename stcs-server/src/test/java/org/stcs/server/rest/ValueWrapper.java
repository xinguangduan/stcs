package org.stcs.server.rest;

import java.util.List;
import java.util.stream.Collectors;

import org.stcs.server.dto.CustomerDto;
import org.stcs.server.entity.CustomerEntity;

public class ValueWrapper {
    public static void main(String[] args) {

    }

    public static CustomerDto convert(CustomerEntity entity) {
        CustomerDto customerDto = new CustomerDto();
        customerDto.setCustId(entity.getCustId());
        customerDto.setCustName(entity.getCustName());
        return customerDto;
    }

    public static CustomerEntity convert(CustomerDto entity) {
        CustomerEntity customerEntity = CustomerEntity.builder()
                .custId(entity.getCustId())
                .custName(entity.getCustName())
                .build();
        return customerEntity;
    }

    public List<CustomerEntity> convert(List<CustomerDto> customerDtoList) {
        return customerDtoList.stream().map((ValueWrapper::convert)).collect(Collectors.toList());
    }

    public List<CustomerDto> convertList(List<CustomerEntity> customerEntities) {
        return customerEntities.stream().map((ValueWrapper::convert)).collect(Collectors.toList());
    }
}
