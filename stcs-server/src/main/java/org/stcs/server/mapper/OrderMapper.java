package org.stcs.server.mapper;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.factory.Mappers;
import org.stcs.server.common.Pagination;
import org.stcs.server.dto.OrderDto;
import org.stcs.server.entity.OrderEntity;

@Mapper
public interface OrderMapper {

    OrderMapper converter = Mappers.getMapper(OrderMapper.class);

    void toDto(OrderEntity source, @MappingTarget OrderDto target);

    void toDto(List<OrderEntity> source, @MappingTarget List<OrderDto> target);

    void toEntity(OrderDto source, @MappingTarget OrderEntity target);

    void toEntity(List<OrderDto> source, @MappingTarget List<OrderEntity> target);

    void toPageResult(Pagination<OrderDto> source, @MappingTarget Pagination<OrderEntity> target);
}
