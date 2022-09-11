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
import org.springframework.util.StringUtils;
import org.stcs.server.annotation.TakeTime;
import org.stcs.server.common.Pagination;
import org.stcs.server.dto.PartDto;

@Slf4j
@Service
public class PartDao extends AbstractDao<PartDto> {
    private final MongoTemplate mongoTemplate;

    @Autowired
    public PartDao(MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
        super.setMongoTemplate(mongoTemplate);
    }

    private static Query buildQuery(PartDto part) {
        if (part.getPartId() == 0) {
            return new Query();
        }
        if (StringUtils.hasText(part.getPartDesc())) {
            return new Query().addCriteria(new Criteria("partId").regex(".*?" + part.getPartDesc() + ".*?"));
        }
        return new Query();
    }

    private static Query buildUniqueQuery(int partId) {
        return new Query().addCriteria(new Criteria("partId").is(partId));
    }

    @TakeTime
    public PartDto find(int partId) {
        Query query = buildUniqueQuery(partId);
        return mongoTemplate.findOne(query, PartDto.class);
    }

    public Pagination<PartDto> find(Pagination pagination, PartDto partDto) {
        Query query = buildQuery(partDto);
        return pagination(PartDto.class, pagination.getPageSize(), pagination.getPageNum(), query);
    }

    public List<PartDto> findAll() {
        return mongoTemplate.findAll(PartDto.class);
    }

    public Collection<PartDto> insert(PartDto partDto) {
        return insert(Arrays.asList(partDto));
    }

    public Collection<PartDto> insert(List<PartDto> partList) {
        Collection<PartDto> insertResult = mongoTemplate.insertAll(partList);
        return insertResult;
    }

    public UpdateResult update(PartDto partDto) {
        Query query = buildUniqueQuery(partDto.getPartId());
        Update update = new Update();
        update.set("partDesc", partDto.getPartDesc());
        return mongoTemplate.updateMulti(query, update, PartDto.class);
    }

    public DeleteResult delete(int partId) {
        Query query = buildUniqueQuery(partId);
        return mongoTemplate.remove(query, PartDto.class);
    }
}
