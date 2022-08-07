package org.stcs.server.dao;


import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import com.mongodb.client.result.UpdateResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;
import org.stcs.server.dto.OrderInfoDTO;

@Slf4j
@Service
public class OrderInfoDAO {
    private final MongoTemplate mongoTemplate;

    @Autowired
    public OrderInfoDAO(MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
    }


    public OrderInfoDTO find(int orderId) {
        Query query = new Query().addCriteria(new Criteria("orderId").is(orderId));
        final long begin = System.currentTimeMillis();
        OrderInfoDTO res = mongoTemplate.findOne(query, OrderInfoDTO.class);
        if (log.isDebugEnabled()) {
            log.debug("orderId:{},find result:{},cost(ms):{}", orderId, res, (System.currentTimeMillis() - begin));
        }
        return res;
    }

    public List<OrderInfoDTO> findByKeys(int custId) {
        final long begin = System.currentTimeMillis();
        Query query = new Query().addCriteria(new Criteria("custId").is(custId));
        List<OrderInfoDTO> res = mongoTemplate.find(query, OrderInfoDTO.class);
        if (log.isDebugEnabled()) {
            log.debug("find order info,find result:{},cost(ms):{}", res.size(), (System.currentTimeMillis() - begin));
        }
        return res;
    }

    public List<OrderInfoDTO> findAll() {
        final long begin = System.currentTimeMillis();
        List<OrderInfoDTO> res = mongoTemplate.findAll(OrderInfoDTO.class);
        if (log.isDebugEnabled()) {
            log.debug("find all order info,find result:{},cost(ms):{}", res.size(), (System.currentTimeMillis() - begin));
        }
        return res;
    }

    public Collection<OrderInfoDTO> insert(OrderInfoDTO req) {
        final List<OrderInfoDTO> orderInfoDTOS = new ArrayList<>();
        orderInfoDTOS.add(req);
        Collection<OrderInfoDTO> insertResult = mongoTemplate.insertAll(orderInfoDTOS);
        return insertResult;
    }

    public Collection<OrderInfoDTO> insert(List<OrderInfoDTO> orderInfoDTOList) {
        Collection<OrderInfoDTO> insertResult = mongoTemplate.insertAll(orderInfoDTOList);
        return insertResult;
    }

    public OrderInfoDTO update(OrderInfoDTO orderInfoDTO) {
        Query query = buildUserUniqueQuery(orderInfoDTO.getOrderId());
        Update update = new Update();
        update.inc("", 1);
//        update.set("", );
        return mongoTemplate.findAndModify(query, update, OrderInfoDTO.class);
    }


    public UpdateResult update(String userId, String custId) {
        Query query = new Query().addCriteria(new Criteria("userId").is(userId));
        Update update = new Update();

        return mongoTemplate.updateMulti(query, update, OrderInfoDTO.class);
    }

    private static Query buildUserUniqueQuery(int orderId) {
        return new Query().addCriteria(new Criteria("orderId").is(orderId));
    }
}

