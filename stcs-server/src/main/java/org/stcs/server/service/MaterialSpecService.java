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
import org.stcs.server.dao.MaterialSpecDao;
import org.stcs.server.dto.MaterialSpecDto;
import org.stcs.server.entity.MaterialSpecEntity;
import org.stcs.server.exception.STCSException;
import org.stcs.server.mapper.MaterialSpecMapper;

@Service
@Slf4j
public class MaterialSpecService {

    private final MaterialSpecDao materialSpecDao;

    public MaterialSpecService(MaterialSpecDao materialSpecDao) {
        this.materialSpecDao = materialSpecDao;
    }

    public MaterialSpecEntity find(int materialId) throws STCSException {
        MaterialSpecDto materialSpecDto = materialSpecDao.find(materialId);
        checkNotFoundException(materialSpecDto);
        MaterialSpecEntity materialSpecEntity = new MaterialSpecEntity();
        MaterialSpecMapper.converter.toEntity(materialSpecDto, materialSpecEntity);
        return materialSpecEntity;
    }

    public List<MaterialSpecEntity> findAll() {
        List<MaterialSpecEntity> materialSpecEntities = new ArrayList<>();
        List<MaterialSpecDto> materialSpecDtos = materialSpecDao.findAll();
        MaterialSpecMapper.converter.toEntity(materialSpecDtos, materialSpecEntities);
        return materialSpecEntities;
    }

    public Pagination<MaterialSpecEntity> find(Pagination pagination, MaterialSpecEntity materialSpecEntity) {
        MaterialSpecDto materialSpecDto = new MaterialSpecDto();
        MaterialSpecMapper.converter.toDto(materialSpecEntity, materialSpecDto);

        Pagination<MaterialSpecDto> result = materialSpecDao.find(pagination, materialSpecDto);
        Pagination<MaterialSpecEntity> newPagination = new Pagination<>();
        MaterialSpecMapper.converter.toPageResult(result, newPagination);
        return newPagination;
    }

    public long add(MaterialSpecEntity materialSpecEntity) {
        return add(Arrays.asList(materialSpecEntity));
    }

    public long add(List<MaterialSpecEntity> materialSpecEntities) {
        final List<MaterialSpecDto> materialSpecDtos = new ArrayList<>();
        MaterialSpecMapper.converter.toDto(materialSpecEntities, materialSpecDtos);
        Collection<MaterialSpecDto> resp = materialSpecDao.insert(materialSpecDtos);
        log.info("add result {}, {}", resp, materialSpecEntities);
        return resp.size();
    }

    public long update(MaterialSpecEntity materialSpecEntity) {
        MaterialSpecDto materialSpecDto = new MaterialSpecDto();
        MaterialSpecMapper.converter.toDto(materialSpecEntity, materialSpecDto);
        UpdateResult result = materialSpecDao.update(materialSpecDto);
        log.info("update result {}, {}", result, materialSpecEntity);
        return result.getModifiedCount();
    }

    public long delete(int materialId) throws STCSException {
        final DeleteResult result = materialSpecDao.delete(materialId);
        checkNotFoundException(result);
        log.info("delete result {}, materialId:{}", result, materialId);
        return result.getDeletedCount();
    }
}
