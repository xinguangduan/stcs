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
import org.stcs.server.dto.NodeGraphDto;
import org.stcs.server.dto.TransPathDto;

@Slf4j
@Service
public class NodeGraphDao extends AbstractDao<NodeGraphDto> {
    private final MongoTemplate mongoTemplate;

    @Autowired
    public NodeGraphDao(MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
        super.setMongoTemplate(mongoTemplate);
    }


    public NodeGraphDto find(String nodeFrom, String nodeTo) {
        Query query = buildUniqueQuery(nodeFrom, nodeTo);
        NodeGraphDto res = mongoTemplate.findOne(query, NodeGraphDto.class);
        return res;
    }

    public List<NodeGraphDto> findAll() {
        return mongoTemplate.findAll(NodeGraphDto.class);
    }


    public Collection<NodeGraphDto> insert(NodeGraphDto nodeGraphDto) {
        return insert(Arrays.asList(nodeGraphDto));
    }

    public Collection<NodeGraphDto> insert(List<NodeGraphDto> nodeGraphDtos) {
        return mongoTemplate.insertAll(nodeGraphDtos);
    }

    public UpdateResult update(NodeGraphDto nodeGraphDto) {
        Query query = buildUniqueQuery(nodeGraphDto.getNodeFrom(), nodeGraphDto.getNodeTo());
        Update update = new Update();
        update.set("nodeFrom", nodeGraphDto.getNodeFrom());
        update.set("nodeTo", nodeGraphDto.getNodeTo());
        update.set("distance", nodeGraphDto.getDistance());
        return mongoTemplate.updateMulti(query, update, TransPathDto.class);
    }

    public DeleteResult delete(String nodeFrom, String nodeTo) {
        Query query = buildUniqueQuery(nodeFrom, nodeTo);
        return mongoTemplate.remove(query, NodeGraphDto.class);
    }

    public DeleteResult delete() {
        Query query = new Query();
        return mongoTemplate.remove(query, NodeGraphDto.class);
    }

    private static Query buildUniqueQuery(String nodeFrom, String nodeTo) {
        return new Query().addCriteria(new Criteria("nodeFrom").is(nodeFrom)
                .andOperator(new Criteria("nodeTo").is(nodeTo)));
    }
}
