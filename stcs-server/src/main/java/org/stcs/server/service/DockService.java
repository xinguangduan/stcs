package org.stcs.server.service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import com.mongodb.client.result.DeleteResult;
import com.mongodb.client.result.UpdateResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.stcs.server.dao.DockDao;
import org.stcs.server.dto.DockDto;
import org.stcs.server.entity.DockEntity;

@Service
@Slf4j
public class DockService {

    private final DockDao dockDao;

    public DockService(DockDao dockDao) {
        this.dockDao = dockDao;
    }

    public DockEntity find(int dockId) {
        DockEntity dockEntity = DockEntity.builder().build();
        DockDto dockDto = dockDao.find(dockId);
        BeanUtils.copyProperties(dockDto, dockEntity, "_id");
        return dockEntity;
    }

    public List<DockEntity> findAll() {
        List<DockEntity> dockEntities = new ArrayList<>();
        List<DockDto> docks = dockDao.findAll();
        docks.forEach(s -> {
            DockEntity dockEntity = DockEntity.builder().build();
            BeanUtils.copyProperties(s, dockEntity, "_id");
            dockEntities.add(dockEntity);
        });
        return dockEntities;
    }

    public long add(DockEntity dockEntity) {
        DockDto dockDto = new DockDto();
        BeanUtils.copyProperties(dockEntity, dockDto, "_id");
        Collection<DockDto> resp = dockDao.insert(dockDto);
        return resp.size();
    }

    public long add(List<DockEntity> dockEntities) {
        final List<DockDto> dockDtos = new ArrayList<>();
        dockEntities.forEach(s -> {
            DockDto dockDto = new DockDto();
            BeanUtils.copyProperties(s, dockDto);
            dockDtos.add(dockDto);
        });

        Collection<DockDto> resp = dockDao.insert(dockDtos);
        log.info("add result {}", resp);
        return resp.size();
    }

    public long update(DockEntity dockEntity) {
        DockDto dockDto = new DockDto();
        BeanUtils.copyProperties(dockEntity, dockDto);
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
