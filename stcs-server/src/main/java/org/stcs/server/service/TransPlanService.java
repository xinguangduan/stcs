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
import org.stcs.server.dao.TransPlanDao;
import org.stcs.server.dto.TransPlanDto;
import org.stcs.server.entity.TransPlanEntity;
import org.stcs.server.exception.STCSException;
import org.stcs.server.mapper.TransPlanMapper;

@Slf4j
@Service
public class TransPlanService extends AbstractService {
    private final TransPlanDao planDao;

    public TransPlanService(TransPlanDao planDao) {
        this.planDao = planDao;
    }

    public TransPlanEntity find(int planId) throws STCSException {
        TransPlanEntity planEntity = TransPlanEntity.builder().build();
        TransPlanDto planDto = planDao.find(planId);
        checkNotFoundException(planDto);
        TransPlanMapper.converter.toEntity(planDto, planEntity);
        return planEntity;
    }

    public List<TransPlanEntity> findAll() {
        List<TransPlanEntity> planEntities = new ArrayList<>();
        List<TransPlanDto> planDtos = planDao.findAll();
        TransPlanMapper.converter.toEntity(planDtos, planEntities);
        return planEntities;
    }

    public Pagination<TransPlanEntity> find(Pagination pagination, TransPlanEntity partEntity) {
        TransPlanDto planDto = new TransPlanDto();
        TransPlanMapper.converter.toDto(partEntity, planDto);

        Pagination<TransPlanDto> result = planDao.find(pagination, planDto);
        Pagination<TransPlanEntity> newPagination = new Pagination<>();
        TransPlanMapper.converter.toPageResult(result, newPagination);
        return newPagination;
    }

    public long add(TransPlanEntity planEntity) {
        return add(Arrays.asList(planEntity));
    }

    public long add(List<TransPlanEntity> planEntities) {
        final List<TransPlanDto> planDtos = new ArrayList<>();
        TransPlanMapper.converter.toDto(planEntities, planDtos);
        Collection<TransPlanDto> result = planDao.insert(planDtos);
        log.info("add result {}", result);
        return result.size();
    }

    public long update(TransPlanEntity planEntity) {
        TransPlanDto planDto = new TransPlanDto();
        TransPlanMapper.converter.toDto(planEntity, planDto);
        UpdateResult result = planDao.update(planDto);
        log.info("update result {}", result);
        return result.getModifiedCount();
    }

    public long delete(int planId) {
        DeleteResult res = planDao.delete(planId);
        log.info("delete result {}", res);
        return res.getDeletedCount();
    }
}
