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
import org.stcs.server.dto.CustomerDto;

@Slf4j
@Service
public class CustomerDao {
    private final MongoTemplate mongoTemplate;

    @Autowired
    public CustomerDao(MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
    }


    public CustomerDto find(int custId) {
        Query query = buildUserUniqueQuery(custId);
        final long begin = System.currentTimeMillis();
        CustomerDto res = mongoTemplate.findOne(query, CustomerDto.class);
        if (log.isDebugEnabled()) {
            log.debug("custId:{}, find result:{}, cost(ms):{}", custId, res, (System.currentTimeMillis() - begin));
        }
        return res;
    }

    public List<CustomerDto> findAll() {
        final long begin = System.currentTimeMillis();
        List<CustomerDto> res = mongoTemplate.findAll(CustomerDto.class);
        if (log.isDebugEnabled()) {
            log.debug("find all cust info, find result:{}, cost(ms):{}", res.size(), (System.currentTimeMillis() - begin));
        }
        return res;
    }

    public Collection<CustomerDto> insert(CustomerDto req) {
        Collection<CustomerDto> insertResult = insert(Arrays.asList(req));
        return insertResult;
    }

    public Collection<CustomerDto> insert(List<CustomerDto> customerDtoList) {
        Collection<CustomerDto> insertResult = mongoTemplate.insertAll(customerDtoList);
        return insertResult;
    }

    public UpdateResult update(CustomerDto customerDto) {
        final Query query = buildUserUniqueQuery(customerDto.getCustId());
        Update update = new Update();
        update.set("custName", customerDto.getCustName());
        return mongoTemplate.updateMulti(query, update, UpdateResult.class);
    }

    public DeleteResult delete(int custId) {
        final Query query = buildUserUniqueQuery(custId);
        return mongoTemplate.remove(query, DeleteResult.class);
    }

    private static Query buildUserUniqueQuery(int orderId) {
        return new Query().addCriteria(new Criteria("custId").is(orderId));
    }
}

