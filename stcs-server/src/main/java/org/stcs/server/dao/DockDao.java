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
import org.stcs.server.dto.DockDto;

@Slf4j
@Service
public class DockDao {
    private final MongoTemplate mongoTemplate;

    @Autowired
    public DockDao(MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
    }


    public DockDto find(int dockId) {
        final Query query = buildUserUniqueQuery(dockId);
        final long begin = System.currentTimeMillis();
        DockDto res = mongoTemplate.findOne(query, DockDto.class);
        if (log.isDebugEnabled()) {
            log.debug("dockId:{}, find result:{}, cost(ms):{}", dockId, res, (System.currentTimeMillis() - begin));
        }
        return res;
    }

    public List<DockDto> findAll() {
        final long begin = System.currentTimeMillis();
        List<DockDto> res = mongoTemplate.findAll(DockDto.class);
        if (log.isDebugEnabled()) {
            log.debug("find all dock info, find result:{}, cost(ms):{}", res.size(), (System.currentTimeMillis() - begin));
        }
        return res;
    }

    public Collection<DockDto> insert(DockDto partDto) {
        return insert(Arrays.asList(partDto));
    }

    public Collection<DockDto> insert(List<DockDto> partList) {
        return mongoTemplate.insertAll(partList);
    }

    public UpdateResult update(DockDto partDto) {
        Query query = buildUserUniqueQuery(partDto.getDockId());
        Update update = new Update();
        update.set("dockDesc", partDto.getDockDesc());
        return mongoTemplate.findAndModify(query, update, UpdateResult.class);
    }

    public DeleteResult delete(int dockId) {
        Query query = buildUserUniqueQuery(dockId);
        return mongoTemplate.findAndRemove(query, DeleteResult.class);
    }

    private static Query buildUserUniqueQuery(int dockId) {
        return new Query().addCriteria(new Criteria("dockId").is(dockId));
    }
}
