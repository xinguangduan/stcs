package org.stcs.server.mapper;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.factory.Mappers;
import org.stcs.server.common.Pagination;
import org.stcs.server.dto.MaterialSpecDto;
import org.stcs.server.entity.MaterialSpecEntity;


@Mapper
public interface MaterialSpecMapper {

    MaterialSpecMapper converter = Mappers.getMapper(MaterialSpecMapper.class);

    void toDto(MaterialSpecEntity source, @MappingTarget MaterialSpecDto target);

    void toDto(List<MaterialSpecEntity> source, @MappingTarget List<MaterialSpecDto> target);

    void toEntity(MaterialSpecDto source, @MappingTarget MaterialSpecEntity target);

    void toEntity(List<MaterialSpecDto> source, @MappingTarget List<MaterialSpecEntity> target);

    void toPageResult(Pagination<MaterialSpecDto> source, @MappingTarget Pagination<MaterialSpecEntity> target);
}