package org.stcs.server.common;

import java.util.List;

import lombok.*;

/**
 * mongoDB分页对象
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class Pagination<T> {
    //当前业
    private Integer pageNum;
    //每页的数量
    private Integer pageSize;
    //总共的条数
    private Long total;
    //总共的页数
    private Integer pages;
    //实体类集合
    private List<T> records;
}


