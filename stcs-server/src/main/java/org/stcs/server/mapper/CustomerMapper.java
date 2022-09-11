package org.stcs.server.mapper;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.factory.Mappers;
import org.stcs.server.common.Pagination;
import org.stcs.server.dto.CustomerDto;
import org.stcs.server.entity.CustomerEntity;

@Mapper
public interface CustomerMapper {

    CustomerMapper converter = Mappers.getMapper(CustomerMapper.class);

    void toDto(CustomerEntity source, @MappingTarget CustomerDto target);

    void toDto(List<CustomerEntity> source, @MappingTarget List<CustomerDto> target);

    void toEntity(CustomerDto source, @MappingTarget CustomerEntity target);

    void toEntity(List<CustomerDto> source, @MappingTarget List<CustomerEntity> target);

    void toPageResult(Pagination<CustomerDto> source, @MappingTarget Pagination<CustomerEntity> target);
}
