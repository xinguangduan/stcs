package org.stcs.server.mapper;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.factory.Mappers;
import org.stcs.server.common.Pagination;
import org.stcs.server.dto.TransPlanDto;
import org.stcs.server.entity.TransPlanEntity;

@Mapper
public interface TransPlanMapper {

    TransPlanMapper converter = Mappers.getMapper(TransPlanMapper.class);

    void toDto(TransPlanEntity source, @MappingTarget TransPlanDto target);

    void toDto(List<TransPlanEntity> source, @MappingTarget List<TransPlanDto> target);

    void toEntity(TransPlanDto source, @MappingTarget TransPlanEntity target);

    void toEntity(List<TransPlanDto> source, @MappingTarget List<TransPlanEntity> target);

    void toPageResult(Pagination<TransPlanDto> source, @MappingTarget Pagination<TransPlanEntity> target);
}
