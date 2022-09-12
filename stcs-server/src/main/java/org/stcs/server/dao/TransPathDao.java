package org.stcs.server.dao;

import static org.apache.commons.lang3.StringUtils.isNotBlank;

import java.util.ArrayList;
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
import org.stcs.server.dto.TransPathDto;

@Slf4j
@Service
public class TransPathDao extends AbstractDao<TransPathDto> {
    private final MongoTemplate mongoTemplate;

    @Autowired
    public TransPathDao(MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
        super.setMongoTemplate(mongoTemplate);
    }


    public TransPathDto find(int pathId) {
        Query query = buildUniqueQuery(pathId);
        TransPathDto res = mongoTemplate.findOne(query, TransPathDto.class);
        return res;
    }

    public List<TransPathDto> findAll() {
        return mongoTemplate.findAll(TransPathDto.class);
    }

    public List<TransPathDto> find(TransPathDto pathDto) {
        Query query = buildQuery(pathDto);
        return mongoTemplate.find(query, TransPathDto.class);
    }

    public Pagination<TransPathDto> find(Pagination pagination, TransPathDto pathDto) {
        Query query = buildQuery(pathDto);
        return pagination(TransPathDto.class, pagination.getPageSize(), pagination.getPageNum(), query);
    }

    public Collection<TransPathDto> insert(TransPathDto transPathDto) {
        return insert(Arrays.asList(transPathDto));
    }

    public Collection<TransPathDto> insert(List<TransPathDto> transPathDtos) {
        return mongoTemplate.insertAll(transPathDtos);
    }

    public UpdateResult update(TransPathDto transPathDto) {
        Query query = buildUniqueQuery(transPathDto.getPartId());
        Update update = new Update();
        update.set("pathId", transPathDto.getPathId());
        update.set("currentNode", transPathDto.getCurrentNode());
        return mongoTemplate.updateMulti(query, update, TransPathDto.class);
    }

    public DeleteResult delete(int pathId) {
        Query query = buildUniqueQuery(pathId);
        return mongoTemplate.remove(query, TransPathDto.class);
    }

    private static Query buildUniqueQuery(int planId) {
        return new Query().addCriteria(new Criteria("pathId").is(planId));
    }

    private static Query buildQuery(TransPathDto pathDto) {
        List<Criteria> criteriaList = new ArrayList<>();
        if (isNotBlank(pathDto.getPathDef())) {
            Criteria c1 = Criteria.where("pathDef").regex(".*?" + pathDto.getPathDef() + ".*?");
            criteriaList.add(c1);
        }
        if (isNotBlank(pathDto.getCurrentNode())) {
            Criteria c2 = Criteria.where("currentNode").regex(".*?" + pathDto.getCurrentNode() + ".*?");
            criteriaList.add(c2);
        }
        Criteria[] arr = new Criteria[criteriaList.size()];
        Criteria criteria = new Criteria().andOperator(criteriaList.toArray(arr));
        Query query = new Query(criteria);
//        if (startDate != null && endDate != null) {
//            query.addCriteria(Criteria.where("createDate").gte(startDate).lte(endDate));
//        }
        if (criteriaList.size() == 0) {
            query = new Query();
        }
        return query;
    }
}
