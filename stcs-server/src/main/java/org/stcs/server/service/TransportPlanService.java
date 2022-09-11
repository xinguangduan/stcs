package org.stcs.server.service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import com.mongodb.client.result.DeleteResult;
import com.mongodb.client.result.UpdateResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.stcs.server.common.Pagination;
import org.stcs.server.dao.TransportPlanDao;
import org.stcs.server.dto.TransportPlanDto;
import org.stcs.server.entity.TransportPlanEntity;
import org.stcs.server.exception.STCSException;
import org.stcs.server.mapper.TransportPlanMapper;

@Slf4j
@Service
public class TransportPlanService extends AbstractService {
    private final TransportPlanDao planDao;

    public TransportPlanService(TransportPlanDao planDao) {
        this.planDao = planDao;
    }

    public TransportPlanEntity find(int planId) throws STCSException {
        TransportPlanEntity planEntity = TransportPlanEntity.builder().build();
        TransportPlanDto planDto = planDao.find(planId);
        checkNotFoundException(planDto);
        BeanUtils.copyProperties(planDto, planEntity, "_id");
        return planEntity;
    }

    public List<TransportPlanEntity> findAll() {
        List<TransportPlanEntity> planEntities = new ArrayList<>();
        List<TransportPlanDto> planDtos = planDao.findAll();
        planDtos.forEach(s -> {
            TransportPlanEntity planEntity = TransportPlanEntity.builder().build();
            BeanUtils.copyProperties(s, planEntity, "_id");
            planEntities.add(planEntity);
        });
        return planEntities;
    }

    public Pagination<TransportPlanEntity> find(Pagination pagination, TransportPlanEntity partEntity) {
        TransportPlanDto planDto = new TransportPlanDto();
        TransportPlanMapper.converter.toDto(partEntity, planDto);

        Pagination<TransportPlanDto> result = planDao.find(pagination, planDto);
        Pagination<TransportPlanEntity> newPagination = new Pagination<>();
        TransportPlanMapper.converter.toPageResult(result, newPagination);
        return newPagination;
    }

    public long add(TransportPlanEntity planEntity) {
        TransportPlanDto planDto = new TransportPlanDto();
        BeanUtils.copyProperties(planEntity, planDto, "_id");
        Collection<TransportPlanDto> resp = planDao.insert(planDto);
        return resp.size();
    }

    public long add(List<TransportPlanEntity> planEntities) {
        final List<TransportPlanDto> planDtos = new ArrayList<>();
        for (TransportPlanEntity planEntity : planEntities) {
            TransportPlanDto planDto = new TransportPlanDto();
            BeanUtils.copyProperties(planEntity, planDto);
            planDtos.add(planDto);
        }
        Collection<TransportPlanDto> resp = planDao.insert(planDtos);
        log.info("add result {}", resp);
        return resp.size();
    }

    public long update(TransportPlanEntity planEntity) {
        TransportPlanDto planDto = new TransportPlanDto();
        BeanUtils.copyProperties(planEntity, planDto);
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
