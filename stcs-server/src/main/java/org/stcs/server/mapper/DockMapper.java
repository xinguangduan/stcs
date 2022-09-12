package org.stcs.server.mapper;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.factory.Mappers;
import org.stcs.server.common.Pagination;
import org.stcs.server.dto.DockDto;
import org.stcs.server.entity.DockEntity;

@Mapper
public interface DockMapper {

    DockMapper converter = Mappers.getMapper(DockMapper.class);

    void toDto(DockEntity source, @MappingTarget DockDto target);

    void toDto(List<DockEntity> source, @MappingTarget List<DockDto> target);

    void toEntity(DockDto source, @MappingTarget DockEntity target);

    void toEntity(List<DockDto> source, @MappingTarget List<DockEntity> target);

    void toPageResult(Pagination<DockDto> source, @MappingTarget Pagination<DockEntity> target);
}