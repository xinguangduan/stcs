package org.stcs.server.service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import com.mongodb.client.result.DeleteResult;
import com.mongodb.client.result.UpdateResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.stcs.server.dao.PartDao;
import org.stcs.server.dto.PartDto;
import org.stcs.server.entity.PartEntity;

@Service
@Slf4j
public class PartService {

    private final PartDao partDao;

    public PartService(PartDao partDao) {
        this.partDao = partDao;
    }

    public PartEntity find(int partId) {
        PartEntity partEntity = PartEntity.builder().build();
        PartDto partDto = partDao.find(partId);
        BeanUtils.copyProperties(partDto, partEntity, "_id");
        return partEntity;
    }

    public List<PartEntity> findAll() {
        List<PartEntity> partEntities = new ArrayList<>();
        List<PartDto> partDtos = partDao.findAll();
        partDtos.forEach(s -> {
            PartEntity materialSpecEntity = PartEntity.builder().build();
            BeanUtils.copyProperties(s, materialSpecEntity, "_id");
            partEntities.add(materialSpecEntity);
        });
        return partEntities;
    }

    public long add(PartEntity partEntity) {
        PartDto partDto = new PartDto();
        BeanUtils.copyProperties(partEntity, partDto, "_id");
        Collection<PartDto> resp = partDao.insert(partDto);
        return resp.size();
    }

    public long add(List<PartEntity> partEntities) {
        final List<PartDto> partDtos = new ArrayList<>();
        for (PartEntity partEntity : partEntities) {
            PartDto partDto = new PartDto();
            BeanUtils.copyProperties(partEntity, partDto);
            partDtos.add(partDto);
        }
        Collection<PartDto> resp = partDao.insert(partDtos);
        log.info("add result {}", resp);
        return resp.size();
    }

    public long update(PartEntity partEntity) {
        PartDto partDto = new PartDto();
        BeanUtils.copyProperties(partEntity, partDto);
        UpdateResult result = partDao.update(partDto);
        log.info("update result {}", result);
        return result.getModifiedCount();
    }

    public long delete(int partId) {
        DeleteResult res = partDao.delete(partId);
        log.info("delete result {}", res);
        return res.getDeletedCount();
    }
}
