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
import org.stcs.server.dto.MaterialSpecDto;

@Slf4j
@Service
public class MaterialSpecDao extends AbstractDao<MaterialSpecDto> {

    private final MongoTemplate mongoTemplate;

    @Autowired
    public MaterialSpecDao(MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
        super.setMongoTemplate(mongoTemplate);
    }

    private static Query buildQuery(MaterialSpecDto materialSpecDto) {
        if (materialSpecDto.getMaterialId() == 0) {
            return new Query();
        }
        return new Query().addCriteria(new Criteria("materialDesc").regex(".*?" + materialSpecDto.getMaterialDesc() + ".*?"));
    }

    private static Query buildUniqueQuery(int materialId) {
        return new Query().addCriteria(new Criteria("materialId").is(materialId));
    }

    public MaterialSpecDto find(int materialId) {
        Query query = buildUniqueQuery(materialId);
        return mongoTemplate.findOne(query, MaterialSpecDto.class);
    }

    public List<MaterialSpecDto> findAll() {
        return mongoTemplate.findAll(MaterialSpecDto.class);
    }

    public Pagination<MaterialSpecDto> find(Pagination pagination, MaterialSpecDto materialSpecDto) {
        Query query = buildQuery(materialSpecDto);
        return pagination(MaterialSpecDto.class, pagination.getPageSize(), pagination.getPageNum(), query);
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
}

