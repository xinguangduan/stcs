package org.stcs.server.mapper;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.factory.Mappers;
import org.stcs.server.common.Pagination;
import org.stcs.server.dto.TransPathDto;
import org.stcs.server.entity.TransPathEntity;

@Mapper
public interface TransPathMapper {

    TransPathMapper converter = Mappers.getMapper(TransPathMapper.class);

    void toDto(TransPathEntity source, @MappingTarget TransPathDto target);

    void toDto(List<TransPathEntity> source, @MappingTarget List<TransPathDto> target);

    void toEntity(TransPathDto source, @MappingTarget TransPathEntity target);

    void toEntity(List<TransPathDto> source, @MappingTarget List<TransPathEntity> target);

    void toPageResult(Pagination<TransPathDto> source, @MappingTarget Pagination<TransPathEntity> target);
}
