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
import org.stcs.server.dto.OrderDto;

@Slf4j
@Service
public class OrderDao extends AbstractDao<OrderDto> {
    private final MongoTemplate mongoTemplate;

    @Autowired
    public OrderDao(MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
        super.setMongoTemplate(mongoTemplate);
    }

    private static Query buildQuery(OrderDto orderDto) {
        if (orderDto.getOrderId() == 0) {
            return new Query();
        }
        return new Query().addCriteria(new Criteria("orderDesc").regex(".*?" + orderDto.getOrderDesc() + ".*?"));
    }

    private static Query buildUniqueQuery(int orderId) {
        return new Query().addCriteria(new Criteria("orderId").is(orderId));
    }

    public OrderDto find(int orderId) {
        Query query = buildUniqueQuery(orderId);
        return mongoTemplate.findOne(query, OrderDto.class);
    }

    public List<OrderDto> findAll() {
        return mongoTemplate.findAll(OrderDto.class);
    }

    public Pagination<OrderDto> find(Pagination pagination, OrderDto orderDto) {
        Query query = buildQuery(orderDto);
        return pagination(OrderDto.class, pagination.getPageSize(), pagination.getPageNum(), query);
    }

    public Collection<OrderDto> insert(OrderDto req) {
        Collection<OrderDto> insertResult = insert(Arrays.asList(req));
        return insertResult;
    }

    public Collection<OrderDto> insert(List<OrderDto> orderDtoList) {
        return mongoTemplate.insertAll(orderDtoList);
    }

    public UpdateResult update(OrderDto orderDto) {
        Query query = buildUniqueQuery(orderDto.getOrderId());
        Update update = new Update();
        update.set("orderDesc", orderDto.getOrderDesc());
        return mongoTemplate.updateMulti(query, update, OrderDto.class);
    }

    public DeleteResult delete(int orderId) {
        Query query = buildUniqueQuery(orderId);
        return mongoTemplate.remove(query, OrderDto.class);
    }
}

