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
import org.stcs.server.dto.DockDto;

@Slf4j
@Service
public class DockDao extends AbstractDao<DockDto> {
    private final MongoTemplate mongoTemplate;

    @Autowired
    public DockDao(MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
        super.setMongoTemplate(mongoTemplate);
    }

    public DockDto find(int dockId) {
        final Query query = buildUniqueQuery(dockId);
        return mongoTemplate.findOne(query, DockDto.class);
    }

    public List<DockDto> findAll() {
        return mongoTemplate.findAll(DockDto.class);
    }

    public Pagination<DockDto> find(Pagination pagination, DockDto dockDto) {
        Query query = buildQuery(dockDto);
        return pagination(DockDto.class, pagination.getPageSize(), pagination.getPageNum(), query);
    }

    public Collection<DockDto> insert(DockDto dockDto) {
        return insert(Arrays.asList(dockDto));
    }

    public Collection<DockDto> insert(List<DockDto> dockDtoList) {
        return mongoTemplate.insertAll(dockDtoList);
    }

    public UpdateResult update(DockDto dockDto) {
        Query query = buildUniqueQuery(dockDto.getDockId());
        Update update = new Update();
        update.set("dockDesc", dockDto.getDockDesc());
        return mongoTemplate.updateMulti(query, update, DockDto.class);
    }

    public DeleteResult delete(int dockId) {
        Query query = buildUniqueQuery(dockId);
        return mongoTemplate.remove(query, DockDto.class);
    }

    private Query buildQuery(DockDto dockDto) {
        if (dockDto.getDockId() == 0) {
            return new Query();
        }
        return new Query().addCriteria(new Criteria("dockDesc").regex((".*?" + dockDto.getDockDesc() + ".*?")));
    }

    private Query buildUniqueQuery(int dockId) {
        return new Query().addCriteria(new Criteria("dockId").is(dockId));
    }
}
