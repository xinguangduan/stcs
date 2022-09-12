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
import org.stcs.server.dto.CustomerDto;

@Slf4j
@Service
public class CustomerDao extends AbstractDao<CustomerDto> {
    private final MongoTemplate mongoTemplate;

    @Autowired
    public CustomerDao(MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
        super.setMongoTemplate(mongoTemplate);
    }

    public CustomerDto find(int custId) {
        Query query = buildUniqueQuery(custId);
        return mongoTemplate.findOne(query, CustomerDto.class);
    }

    public List<CustomerDto> findAll() {
        return mongoTemplate.findAll(CustomerDto.class);
    }

    public Pagination<CustomerDto> find(Pagination pagination, CustomerDto customerDto) {
        Query query = buildQuery(customerDto);
        return pagination(CustomerDto.class, pagination.getPageSize(), pagination.getPageNum(), query);
    }

    public Collection<CustomerDto> insert(CustomerDto req) {
        return insert(Arrays.asList(req));
    }

    public Collection<CustomerDto> insert(List<CustomerDto> customerDtoList) {
        return mongoTemplate.insertAll(customerDtoList);
    }

    public UpdateResult update(CustomerDto customerDto) {
        final Query query = buildUniqueQuery(customerDto.getCustId());
        return mongoTemplate.updateMulti(query, Update.update("custName", customerDto.getCustName()), CustomerDto.class);
    }

    public DeleteResult delete(int custId) {
        final Query query = buildUniqueQuery(custId);
        return mongoTemplate.remove(query, CustomerDto.class);
    }

    private Query buildQuery(CustomerDto customerDto) {
        if (customerDto.getCustId() == 0) {
            return new Query();
        }
        return new Query().addCriteria(new Criteria("custName").regex((".*?" + customerDto.getCustName() + ".*?")));
    }

    private Query buildUniqueQuery(int custId) {
        return new Query().addCriteria(new Criteria("custId").is(custId));
    }
}

