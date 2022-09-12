package org.stcs.server.mapper;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.factory.Mappers;
import org.stcs.server.common.Pagination;
import org.stcs.server.dto.NodeGraphDto;
import org.stcs.server.entity.NodeGraphEntity;

@Mapper
public interface NodeGraphMapper {

    NodeGraphMapper converter = Mappers.getMapper(NodeGraphMapper.class);

    void toDto(NodeGraphEntity source, @MappingTarget NodeGraphDto target);

    void toDto(List<NodeGraphEntity> source, @MappingTarget List<NodeGraphDto> target);

    void toEntity(NodeGraphDto source, @MappingTarget NodeGraphEntity target);

    void toEntity(List<NodeGraphDto> source, @MappingTarget List<NodeGraphEntity> target);

    void toPageResult(Pagination<NodeGraphDto> source, @MappingTarget Pagination<NodeGraphEntity> target);
}
