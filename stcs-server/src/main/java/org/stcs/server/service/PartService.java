package org.stcs.server.service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import com.mongodb.client.result.DeleteResult;
import com.mongodb.client.result.UpdateResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.stcs.server.common.Pagination;
import org.stcs.server.dao.PartDao;
import org.stcs.server.dto.PartDto;
import org.stcs.server.entity.PartEntity;
import org.stcs.server.mapper.PartMapper;

@Service
@Slf4j
public class PartService extends AbstractService {

    private final PartDao partDao;

    public PartService(PartDao partDao) {
        this.partDao = partDao;
    }

    public PartEntity find(int partId) {
        PartEntity partEntity = new PartEntity();
        PartDto partDto = partDao.find(partId);
        PartMapper.converter.toEntity(partDto, partEntity);
        return partEntity;
    }

    public List<PartEntity> findAll() {
        List<PartEntity> partEntities = new ArrayList<>();
        List<PartDto> partDtoList = partDao.findAll();
        PartMapper.converter.toEntity(partDtoList, partEntities);
        return partEntities;
    }

    public Pagination<PartEntity> find(Pagination pagination, PartEntity partEntity) {
        PartDto partDto = new PartDto();
        PartMapper.converter.toDto(partEntity, partDto);

        Pagination<PartDto> result = partDao.find(pagination, partDto);
        Pagination<PartEntity> newPagination = new Pagination<>();
        PartMapper.converter.toPageResult(result, newPagination);
        return newPagination;
    }

    public long add(PartEntity partEntity) {
        PartDto partDto = new PartDto();
        PartMapper.converter.toDto(partEntity, partDto);
        Collection<PartDto> resp = partDao.insert(partDto);
        return resp.size();
    }

    public long add(List<PartEntity> partEntities) {
        final List<PartDto> partDtos = new ArrayList<>();
        PartMapper.converter.toDto(partEntities, partDtos);
//        for (PartEntity partEntity : partEntities) {
//            // Spring5.3之后，匹配源对象和目标对象中的属性时遵循泛型类型信息，意思是copy属性时，会判断属性的泛型是否一致，如不一致，直接忽略属性的拷贝
//            // BeanUtils.copyProperties(partEntity, partDto);
//            PartDto partDto = new PartDto();
//            PartMapper.MAPPER.toDto(partEntity, partDto);
//            partDtos.add(partDto);
//        }
        Collection<PartDto> result = partDao.insert(partDtos);
        log.info("add result {}", result);
        return result.size();
    }

    public long update(PartEntity partEntity) {
        PartDto partDto = new PartDto();
        PartMapper.converter.toDto(partEntity, partDto);
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
