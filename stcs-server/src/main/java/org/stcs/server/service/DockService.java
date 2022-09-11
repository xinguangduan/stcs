package org.stcs.server.service;

import static org.stcs.server.protocol.STCSProtocolBuilder.checkNotFoundException;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import com.mongodb.client.result.DeleteResult;
import com.mongodb.client.result.UpdateResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.stcs.server.common.Pagination;
import org.stcs.server.dao.DockDao;
import org.stcs.server.dto.DockDto;
import org.stcs.server.entity.DockEntity;
import org.stcs.server.exception.STCSException;
import org.stcs.server.mapper.DockMapper;

@Service
@Slf4j
public class DockService {

    private final DockDao dockDao;

    public DockService(DockDao dockDao) {
        this.dockDao = dockDao;
    }

    public DockEntity find(int dockId) throws STCSException {
        DockDto dockDto = dockDao.find(dockId);
        checkNotFoundException(dockDto);
        DockEntity dockEntity = new DockEntity();
        DockMapper.converter.toEntity(dockDto, dockEntity);
        return dockEntity;
    }

    public List<DockEntity> findAll() {
        List<DockEntity> dockEntities = new ArrayList<>();
        List<DockDto> dockDtos = dockDao.findAll();
        DockMapper.converter.toEntity(dockDtos, dockEntities);
        return dockEntities;
    }

    public Pagination<DockEntity> find(Pagination pagination, DockEntity dockEntity) {
        DockDto dockDto = new DockDto();
        DockMapper.converter.toDto(dockEntity, dockDto);

        Pagination<DockDto> result = dockDao.find(pagination, dockDto);
        Pagination<DockEntity> newPagination = new Pagination<>();
        DockMapper.converter.toPageResult(result, newPagination);
        return newPagination;
    }

    public long add(DockEntity dockEntity) {
        return add(Arrays.asList(dockEntity));
    }

    public long add(List<DockEntity> dockEntities) {
        final List<DockDto> dockDtoList = new ArrayList<>();
        DockMapper.converter.toDto(dockEntities, dockDtoList);
        Collection<DockDto> result = dockDao.insert(dockDtoList);
        log.info("add result {}", result);
        return result.size();
    }

    public long update(DockEntity dockEntity) {
        DockDto dockDto = new DockDto();
        DockMapper.converter.toDto(dockEntity, dockDto);
        UpdateResult result = dockDao.update(dockDto);
        log.info("update result {}, {}", result, dockEntity);
        return result.getModifiedCount();
    }

    public long delete(int dockId) {
        DeleteResult result = dockDao.delete(dockId);
        log.info("delete result {}, {}", result, dockId);
        return result.getDeletedCount();
    }
}
