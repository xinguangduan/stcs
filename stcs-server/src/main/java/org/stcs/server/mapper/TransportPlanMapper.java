package org.stcs.server.mapper;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.factory.Mappers;
import org.stcs.server.common.Pagination;
import org.stcs.server.dto.TransportPlanDto;
import org.stcs.server.entity.TransportPlanEntity;

@Mapper
public interface TransportPlanMapper {

    TransportPlanMapper converter = Mappers.getMapper(TransportPlanMapper.class);

    void toDto(TransportPlanEntity source, @MappingTarget TransportPlanDto target);

    void toDto(List<TransportPlanEntity> source, @MappingTarget List<TransportPlanDto> target);

    void toEntity(TransportPlanDto source, @MappingTarget TransportPlanEntity target);

    void toEntity(List<TransportPlanDto> source, @MappingTarget List<TransportPlanEntity> target);

    void toPageResult(Pagination<TransportPlanDto> source, @MappingTarget Pagination<TransportPlanEntity> target);
}
