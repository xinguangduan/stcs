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
import org.stcs.server.dto.TransportPlanDto;

@Slf4j
@Service
public class TransportPlanDao extends AbstractDao<TransportPlanDto> {
    private final MongoTemplate mongoTemplate;

    @Autowired
    public TransportPlanDao(MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
        super.setMongoTemplate(mongoTemplate);
    }

    private static Query buildUniqueQuery(int planId) {
        return new Query().addCriteria(new Criteria("planId").is(planId));
    }

    private static Query buildQuery(TransportPlanDto planDto) {
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

    public TransportPlanDto find(int planId) {
        Query query = buildUniqueQuery(planId);
        TransportPlanDto res = mongoTemplate.findOne(query, TransportPlanDto.class);
        return res;
    }

    public List<TransportPlanDto> findAll() {
        return mongoTemplate.findAll(TransportPlanDto.class);
    }

    public List<TransportPlanDto> find(TransportPlanDto planDto) {
        Query query = buildQuery(planDto);
        return mongoTemplate.find(query, TransportPlanDto.class);
    }

    public Pagination<TransportPlanDto> find(Pagination pagination, TransportPlanDto planDto) {
        Query query = buildQuery(planDto);
        return pagination(TransportPlanDto.class, pagination.getPageSize(), pagination.getPageNum(), query);
    }

    public Collection<TransportPlanDto> insert(TransportPlanDto transportPlanDto) {
        return insert(Arrays.asList(transportPlanDto));
    }

    public Collection<TransportPlanDto> insert(List<TransportPlanDto> transportPlanDtos) {
        return mongoTemplate.insertAll(transportPlanDtos);
    }

    public UpdateResult update(TransportPlanDto partDto) {
        Query query = buildUniqueQuery(partDto.getPlanId());
        Update update = new Update();
        update.set("custId", partDto.getCustId());
        update.set("dockId", partDto.getDockId());
        update.set("orderId", partDto.getOrderId());
        return mongoTemplate.updateMulti(query, update, TransportPlanDto.class);
    }

    public DeleteResult delete(int partId) {
        Query query = buildUniqueQuery(partId);
        return mongoTemplate.remove(query, TransportPlanDto.class);
    }
}
