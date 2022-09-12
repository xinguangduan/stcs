package org.stcs.server.dao;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import com.mongodb.client.result.DeleteResult;
import com.mongodb.client.result.UpdateResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;
import org.stcs.server.common.Pagination;
import org.stcs.server.dto.TransPlanDto;

@Slf4j
@Service
public class TransPlanDao extends AbstractDao<TransPlanDto> {
    private final MongoTemplate mongoTemplate;

    @Autowired
    public TransPlanDao(MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
        super.setMongoTemplate(mongoTemplate);
    }

    private static Query buildUniqueQuery(int planId) {
        return new Query().addCriteria(new Criteria("planId").is(planId));
    }

    private static Query buildQuery(TransPlanDto planDto) {
        Criteria criteria = new Criteria();
        if (planDto.getPlanId() > 0) {
            criteria.and("planId").is(planDto.getPlanId());
        }
        if (planDto.getCustId() > 0) {
            criteria.and("custId").is(planDto.getCustId());
        }
        if (planDto.getOrderId() > 0) {
            criteria.and("orderId").is(planDto.getOrderId());
        }
        if (planDto.getOrderId() > 0) {
            criteria.and("dockId").is(planDto.getDockId());
        }
        return new Query().addCriteria(criteria);
    }

    public TransPlanDto find(int planId) {
        Query query = buildUniqueQuery(planId);
        TransPlanDto res = mongoTemplate.findOne(query, TransPlanDto.class);
        return res;
    }

    public List<TransPlanDto> findAll() {
        return mongoTemplate.findAll(TransPlanDto.class);
    }

    public List<TransPlanDto> find(TransPlanDto planDto) {
        Query query = buildQuery(planDto);
        return mongoTemplate.find(query, TransPlanDto.class);
    }

    public Pagination<TransPlanDto> find(Pagination pagination, TransPlanDto planDto) {
        Query query = buildQuery(planDto);
        return pagination(TransPlanDto.class, pagination.getPageSize(), pagination.getPageNum(), query);
    }

    public Collection<TransPlanDto> insert(TransPlanDto transPlanDto) {
        return insert(Arrays.asList(transPlanDto));
    }

    public Collection<TransPlanDto> insert(List<TransPlanDto> transPlanDtos) {
        return mongoTemplate.insertAll(transPlanDtos);
    }

    public UpdateResult update(TransPlanDto planDto) {
        Query query = buildUniqueQuery(planDto.getPlanId());
        Update update = new Update();
        update.set("custId", planDto.getCustId());
        update.set("dockId", planDto.getDockId());
        update.set("orderId", planDto.getOrderId());
        return mongoTemplate.updateMulti(query, update, TransPlanDto.class);
    }

    public DeleteResult delete(int planId) {
        Query query = buildUniqueQuery(planId);
        return mongoTemplate.remove(query, TransPlanDto.class);
    }

}
