package org.stcs.server.service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import com.mongodb.client.result.DeleteResult;
import com.mongodb.client.result.UpdateResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.stcs.server.dao.MaterialSpecDao;
import org.stcs.server.dto.MaterialSpecDto;
import org.stcs.server.entity.MaterialSpecEntity;

@Service
@Slf4j
public class MaterialSpecService {

    private final MaterialSpecDao materialSpecDao;

    public MaterialSpecService(MaterialSpecDao materialSpecDao) {
        this.materialSpecDao = materialSpecDao;
    }

    public MaterialSpecEntity find(int materialId) {
        MaterialSpecEntity materialSpecEntity = MaterialSpecEntity.builder().build();
        MaterialSpecDto materialSpecDto = materialSpecDao.find(materialId);
        BeanUtils.copyProperties(materialSpecDto, materialSpecEntity, "_id");
        return materialSpecEntity;
    }

    public List<MaterialSpecEntity> findAll() {
        List<MaterialSpecEntity> materialSpecEntities = new ArrayList<>();
        List<MaterialSpecDto> materialSpecDtos = materialSpecDao.findAll();
        for (MaterialSpecDto materialSpecDto : materialSpecDtos) {
            MaterialSpecEntity materialSpecEntity = MaterialSpecEntity.builder().build();
            BeanUtils.copyProperties(materialSpecDto, materialSpecEntity, "_id");
            materialSpecEntities.add(materialSpecEntity);
        }
        return materialSpecEntities;
    }

    public long add(MaterialSpecEntity materialSpecEntity) {
        MaterialSpecDto materialSpecDto = new MaterialSpecDto();
        BeanUtils.copyProperties(materialSpecEntity, materialSpecDto, "_id");
        Collection<MaterialSpecDto> resp = materialSpecDao.insert(materialSpecDto);
        return resp.size();
    }

    public long add(List<MaterialSpecEntity> materialSpecEntities) {
        final List<MaterialSpecDto> materialSpecDtos = new ArrayList<>();
        materialSpecEntities.forEach(s -> {
            MaterialSpecDto materialSpecDto = new MaterialSpecDto();
            BeanUtils.copyProperties(s, materialSpecDto);
            materialSpecDtos.add(materialSpecDto);
        });
        Collection<MaterialSpecDto> resp = materialSpecDao.insert(materialSpecDtos);
        log.info("add result {}", resp);
        return resp.size();
    }

    public long update(MaterialSpecEntity materialSpecEntity) {
        MaterialSpecDto materialSpecDto = new MaterialSpecDto();
        BeanUtils.copyProperties(materialSpecEntity, materialSpecDto);
        UpdateResult result = materialSpecDao.update(materialSpecDto);
        log.info("update result {}, {}", result, materialSpecEntity);
        return result.getModifiedCount();
    }

    public long delete(int materialId) {
        final DeleteResult result = materialSpecDao.delete(materialId);
        log.info("delete result {}, materialId:{}", result, materialId);
        return result.getDeletedCount();
    }
}
