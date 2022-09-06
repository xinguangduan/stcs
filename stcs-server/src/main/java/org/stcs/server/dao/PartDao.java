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
import org.stcs.server.dto.PartDto;

@Slf4j
@Service
public class PartDao {
    private final MongoTemplate mongoTemplate;

    @Autowired
    public PartDao(MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
    }

    public PartDto find(int partId) {
        Query query = buildUniqueQuery(partId);
        final long begin = System.currentTimeMillis();
        PartDto res = mongoTemplate.findOne(query, PartDto.class);
        if (log.isDebugEnabled()) {
            log.debug("partId:{}, find result:{}, cost(ms):{}", partId, res, (System.currentTimeMillis() - begin));
        }
        return res;
    }

    public List<PartDto> findAll() {
        final long begin = System.currentTimeMillis();
        List<PartDto> res = mongoTemplate.findAll(PartDto.class);
        if (log.isDebugEnabled()) {
            log.debug("find all part info, find result:{},cost(ms):{}", res.size(), (System.currentTimeMillis() - begin));
        }
        return res;
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

    private static Query buildUniqueQuery(int partId) {
        return new Query().addCriteria(new Criteria("partId").is(partId));
    }
}
