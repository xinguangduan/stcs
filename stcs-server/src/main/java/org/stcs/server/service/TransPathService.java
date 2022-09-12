package org.stcs.server.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import com.mongodb.client.result.DeleteResult;
import com.mongodb.client.result.UpdateResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.stcs.server.common.Pagination;
import org.stcs.server.dao.TransPathDao;
import org.stcs.server.dto.TransPathDto;
import org.stcs.server.entity.TransPathEntity;
import org.stcs.server.exception.STCSException;
import org.stcs.server.mapper.TransPathMapper;


@Slf4j
@Service
public class TransPathService extends AbstractService {
    private final TransPathDao pathDao;

    public TransPathService(TransPathDao pathDao) {
        this.pathDao = pathDao;
    }

    public TransPathEntity find(int pathId) throws STCSException {
        TransPathEntity pathEntity = TransPathEntity.builder().build();
        TransPathDto planDto = pathDao.find(pathId);
        checkNotFoundException(planDto);
        TransPathMapper.converter.toEntity(planDto, pathEntity);
        return pathEntity;
    }

    public List<TransPathEntity> findAll() {
        List<TransPathEntity> pathEntities = new ArrayList<>();
        List<TransPathDto> pathDtos = pathDao.findAll();
        TransPathMapper.converter.toEntity(pathDtos, pathEntities);
        return pathEntities;
    }

    public Pagination<TransPathEntity> find(Pagination pagination, TransPathEntity pathEntity) {
        TransPathDto planDto = new TransPathDto();
        TransPathMapper.converter.toDto(pathEntity, planDto);
        Pagination<TransPathDto> result = pathDao.find(pagination, planDto);
        Pagination<TransPathEntity> newPagination = new Pagination<>();
        TransPathMapper.converter.toPageResult(result, newPagination);
        return newPagination;
    }

    public long add(TransPathEntity pathEntity) {
        return add(Arrays.asList(pathEntity));
    }

    public long add(List<TransPathEntity> pathEntities) {
        final List<TransPathDto> pathDaos = new ArrayList<>();
        TransPathMapper.converter.toDto(pathEntities, pathDaos);
        Collection<TransPathDto> resp = pathDao.insert(pathDaos);
        log.info("add result {}", resp);
        return resp.size();
    }

    public long update(TransPathEntity pathEntity) {
        TransPathDto pathDto = new TransPathDto();
        TransPathMapper.converter.toDto(pathEntity, pathDto);
        UpdateResult result = pathDao.update(pathDto);
        log.info("update result {}", result);
        return result.getModifiedCount();
    }

    public long delete(int planId) {
        DeleteResult res = pathDao.delete(planId);
        log.info("delete result {}", res);
        return res.getDeletedCount();
    }
}
