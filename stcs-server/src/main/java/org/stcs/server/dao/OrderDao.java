package org.stcs.server.dao;


import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import com.mongodb.client.result.DeleteResult;
import com.mongodb.client.result.UpdateResult;
import lombok.extern.slf4j.Slf4j;
import org.bson.BsonValue;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;
import org.stcs.server.dto.OrderDto;

@Slf4j
@Service
public class OrderDao {
    private final MongoTemplate mongoTemplate;

    @Autowired
    public OrderDao(MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
    }


    public OrderDto find(int orderId) {
        Query query = buildUserUniqueQuery(orderId);
        final long begin = System.currentTimeMillis();
        OrderDto res = mongoTemplate.findOne(query, OrderDto.class);
        if (log.isDebugEnabled()) {
            log.debug("orderId:{},find result:{},cost(ms):{}", orderId, res, (System.currentTimeMillis() - begin));
        }
        return res;
    }

    public List<OrderDto> findAll() {
        final long begin = System.currentTimeMillis();
        List<OrderDto> res = mongoTemplate.findAll(OrderDto.class);
        if (log.isDebugEnabled()) {
            log.debug("find all order info,find result:{},cost(ms):{}", res.size(), (System.currentTimeMillis() - begin));
        }
        return res;
    }

    public Collection<OrderDto> insert(OrderDto req) {
        Collection<OrderDto> insertResult = insert(Arrays.asList(req));
        return insertResult;
    }

    public Collection<OrderDto> insert(List<OrderDto> orderDtoList) {
        Collection<OrderDto> insertResult = mongoTemplate.insertAll(orderDtoList);
        return insertResult;
    }

    public UpdateResult update(OrderDto orderDto) {
        Query query = buildUserUniqueQuery(orderDto.getOrderId());
        Update update = new Update();
        update.set("orderDesc", orderDto.getOrderDesc());
        OrderDto dto = mongoTemplate.findAndModify(query, update, OrderDto.class);
        return new UpdateResult() {
            @Override
            public boolean wasAcknowledged() {
                return false;
            }

            @Override
            public long getMatchedCount() {
                return 0;
            }

            @Override
            public long getModifiedCount() {
                return 0;
            }

            @Override
            public BsonValue getUpsertedId() {
                return null;
            }
        };
    }

    public DeleteResult delete(int orderId) {
        Query query = buildUserUniqueQuery(orderId);
        OrderDto dto = mongoTemplate.findAndRemove(query, OrderDto.class);
        return new DeleteResult() {
            @Override
            public boolean wasAcknowledged() {
                return false;
            }

            @Override
            public long getDeletedCount() {
                return 0;
            }
        };
    }

    private static Query buildUserUniqueQuery(int orderId) {
        return new Query().addCriteria(new Criteria("orderId").is(orderId));
    }
}

