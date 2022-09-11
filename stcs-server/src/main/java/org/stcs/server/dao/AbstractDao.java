package org.stcs.server.dao;

import java.util.List;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Query;
import org.stcs.server.common.Pagination;

public class AbstractDao<T> {
    @Setter
    @Getter
    private MongoTemplate mongoTemplate;

    /**
     * MongDb分页公共方法
     *
     * @param clazz    实体类的class对象
     * @param pageSize 每页的数量
     * @param pageNum  当前的页数
     * @param query    query是啥不知道赶紧去查下，相当于sql语句
     * @return
     */
    public Pagination<T> pagination(Class<T> clazz, int pageSize, int pageNum, Query query) {
        long total = this.mongoTemplate.count(query, clazz);
        Integer pages = (int) Math.ceil((double) total / (double) pageSize);
        if (pageNum <= 0 || pageNum > pages) {
            pageNum = 1;
        }
        int skip = pageSize * (pageNum - 1);
        query.skip(skip).limit(pageSize);
        final List<T> list = mongoTemplate.find(query, clazz);
        Pagination pagination = new Pagination();
        pagination.setTotal(total);
        pagination.setPages(pages);
        pagination.setPageSize(pageSize);
        pagination.setPageNum(pageNum);
        pagination.setRecords(list);
        return pagination;
    }

}
