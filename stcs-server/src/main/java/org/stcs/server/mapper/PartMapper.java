package org.stcs.server.mapper;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.factory.Mappers;
import org.stcs.server.common.Pagination;
import org.stcs.server.dto.PartDto;
import org.stcs.server.entity.PartEntity;


@Mapper
public interface PartMapper {

    PartMapper converter = Mappers.getMapper(PartMapper.class);

    void toDto(PartEntity source, @MappingTarget PartDto target);

    void toDto(List<PartEntity> source, @MappingTarget List<PartDto> target);

    void toEntity(PartDto source, @MappingTarget PartEntity target);

    void toCloneEntity(PartEntity source, @MappingTarget PartEntity target);

    void toEntity(List<PartDto> source, @MappingTarget List<PartEntity> target);

    //    @Mapping(target = "records", source = "records")
    void toPageResult(Pagination<PartDto> source, @MappingTarget Pagination<PartEntity> target);
}