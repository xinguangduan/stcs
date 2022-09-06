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
import org.stcs.server.dto.MaterialSpecDto;

@Slf4j
@Service
public class MaterialSpecDao {

    private final MongoTemplate mongoTemplate;

    @Autowired
    public MaterialSpecDao(MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
    }


    public MaterialSpecDto find(int materialId) {
        Query query = buildUniqueQuery(materialId);
        final long begin = System.currentTimeMillis();
        MaterialSpecDto res = mongoTemplate.findOne(query, MaterialSpecDto.class);
        if (log.isDebugEnabled()) {
            log.debug("find material spec info, materialId:{}, find result:{}, cost(ms):{}", materialId, res, (System.currentTimeMillis() - begin));
        }
        return res;
    }

    public List<MaterialSpecDto> findAll() {
        final long begin = System.currentTimeMillis();
        List<MaterialSpecDto> res = mongoTemplate.findAll(MaterialSpecDto.class);
        if (log.isDebugEnabled()) {
            log.debug("find all material spec info, find result:{}, cost(ms):{}", res.size(), (System.currentTimeMillis() - begin));
        }
        return res;
    }

    public Collection<MaterialSpecDto> insert(MaterialSpecDto materialSpecDto) {
        return insert(Arrays.asList(materialSpecDto));
    }

    public Collection<MaterialSpecDto> insert(List<MaterialSpecDto> materialSpecDtos) {
        final long begin = System.currentTimeMillis();
        Collection<MaterialSpecDto> insertResult = mongoTemplate.insertAll(materialSpecDtos);
        if (log.isDebugEnabled()) {
            log.debug("insert material spec info, result:{}, cost(ms):{}", insertResult.size(), (System.currentTimeMillis() - begin));
        }
        return insertResult;
    }

    public UpdateResult update(MaterialSpecDto materialSpecDto) {
        Query query = buildUniqueQuery(materialSpecDto.getMaterialId());
        Update update = new Update();
        update.set("materialSpec", materialSpecDto.getMaterialSpec());
        update.set("materialDesc", materialSpecDto.getMaterialDesc());
        return mongoTemplate.updateMulti(query, update, MaterialSpecDto.class);
    }

    public DeleteResult delete(int materialId) {
        Query query = buildUniqueQuery(materialId);
        return mongoTemplate.remove(query, MaterialSpecDto.class);
    }

    private static Query buildUniqueQuery(int materialId) {
        return new Query().addCriteria(new Criteria("materialId").is(materialId));
    }
}

