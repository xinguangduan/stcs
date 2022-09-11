package org.stcs.server.service;

import static org.stcs.server.protocol.STCSProtocolBuilder.checkNotFoundException;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import com.mongodb.client.result.DeleteResult;
import com.mongodb.client.result.UpdateResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.stcs.server.common.Pagination;
import org.stcs.server.dao.OrderDao;
import org.stcs.server.dto.OrderDto;
import org.stcs.server.entity.OrderEntity;
import org.stcs.server.exception.STCSException;
import org.stcs.server.mapper.OrderMapper;

@Service
@Slf4j
public class OrderService {
    private final OrderDao orderDao;

    public OrderService(OrderDao orderDao) {
        this.orderDao = orderDao;
    }

    public OrderEntity find(int orderId) throws STCSException {
        OrderDto orderDto = orderDao.find(orderId);
        checkNotFoundException(orderDto);
        OrderEntity orderEntity = new OrderEntity();
        OrderMapper.converter.toEntity(orderDto, orderEntity);
        return orderEntity;
    }

    public List<OrderEntity> findAll() {
        List<OrderEntity> orderEntities = new ArrayList<>();
        List<OrderDto> orderDtoList = orderDao.findAll();
        OrderMapper.converter.toEntity(orderDtoList, orderEntities);
        return orderEntities;
    }

    public Pagination<OrderEntity> find(Pagination pagination, OrderEntity orderEntity) {
        OrderDto orderDto = new OrderDto();
        OrderMapper.converter.toDto(orderEntity, orderDto);

        Pagination<OrderDto> result = orderDao.find(pagination, orderDto);
        Pagination<OrderEntity> newPagination = new Pagination<>();
        OrderMapper.converter.toPageResult(result, newPagination);
        return newPagination;
    }

    public long add(OrderEntity orderEntity) {
        return add(Arrays.asList(orderEntity));
    }

    public long add(List<OrderEntity> orderEntities) {
        final List<OrderDto> orderDtoList = new ArrayList<>();
        OrderMapper.converter.toDto(orderEntities, orderDtoList);
        final Collection<OrderDto> result = orderDao.insert(orderDtoList);
        log.info("add result {}, {}", result, orderEntities);
        return result.size();
    }

    public long update(OrderEntity orderEntity) {
        OrderDto orderDto = new OrderDto();
        OrderMapper.converter.toDto(orderEntity, orderDto);
        UpdateResult result = orderDao.update(orderDto);
        log.info("update result {}", result);
        return result.getModifiedCount();
    }

    public long delete(int orderId) {
        DeleteResult result = orderDao.delete(orderId);
        log.info("delete result {}", result);
        return result.getDeletedCount();
    }
}
