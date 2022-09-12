package org.stcs.server.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import com.mongodb.client.result.DeleteResult;
import com.mongodb.client.result.UpdateResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.stcs.server.dao.NodeGraphDao;
import org.stcs.server.dto.NodeGraphDto;
import org.stcs.server.entity.NodeGraphEntity;
import org.stcs.server.exception.STCSException;
import org.stcs.server.mapper.NodeGraphMapper;


@Slf4j
@Service
public class NodeGraphService extends AbstractService {
    private final NodeGraphDao nodeGraphDao;

    public NodeGraphService(NodeGraphDao nodeGraphDao) {
        this.nodeGraphDao = nodeGraphDao;
    }

    public NodeGraphEntity find(String nodeFrom, String nodeTo) throws STCSException {
        NodeGraphEntity pathEntity = NodeGraphEntity.builder().build();
        NodeGraphDto planDto = nodeGraphDao.find(nodeFrom, nodeTo);
        checkNotFoundException(planDto);
        NodeGraphMapper.converter.toEntity(planDto, pathEntity);
        return pathEntity;
    }

    public List<NodeGraphEntity> findAll() {
        List<NodeGraphEntity> nodeGraphEntities = new ArrayList<>();
        List<NodeGraphDto> nodeGraphDtos = nodeGraphDao.findAll();
        NodeGraphMapper.converter.toEntity(nodeGraphDtos, nodeGraphEntities);
        return nodeGraphEntities;
    }

    public long add(NodeGraphEntity pathEntity) {
        return add(Arrays.asList(pathEntity));
    }

    public long add(List<NodeGraphEntity> nodeGraphEntities) {
        final List<NodeGraphDto> nodeGraphDtos = new ArrayList<>();
        NodeGraphMapper.converter.toDto(nodeGraphEntities, nodeGraphDtos);
        Collection<NodeGraphDto> resp = nodeGraphDao.insert(nodeGraphDtos);
        log.info("add result {}", resp);
        return resp.size();
    }

    public long update(NodeGraphEntity nodeGraphEntity) {
        NodeGraphDto pathDto = new NodeGraphDto();
        NodeGraphMapper.converter.toDto(nodeGraphEntity, pathDto);
        UpdateResult result = nodeGraphDao.update(pathDto);
        log.info("update result {}", result);
        return result.getModifiedCount();
    }

    public long delete(String nodeFrom, String nodeTo) {
        DeleteResult res = nodeGraphDao.delete(nodeFrom, nodeTo);
        log.info("delete result {}", res);
        return res.getDeletedCount();
    }

    public long delete() {
        DeleteResult res = nodeGraphDao.delete();
        log.info("delete result {}", res);
        return res.getDeletedCount();
    }
}
