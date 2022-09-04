package org.stcs.server.service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import com.mongodb.client.result.DeleteResult;
import com.mongodb.client.result.UpdateResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.stcs.server.dao.OrderDao;
import org.stcs.server.dto.OrderDto;
import org.stcs.server.entity.OrderEntity;

@Service
@Slf4j
public class OrderService {
    private final OrderDao orderDao;

    public OrderService(OrderDao orderDao) {
        this.orderDao = orderDao;
    }

    public OrderEntity find(int orderId) {
        OrderEntity orderEntity = new OrderEntity();
        OrderDto orderDTO = orderDao.find(orderId);
        BeanUtils.copyProperties(orderDTO, orderEntity, "_id");
        return orderEntity;
    }

    public List<OrderEntity> findAll() {
        List<OrderEntity> orderEntities = new ArrayList<>();
        List<OrderDto> orderDtos = orderDao.findAll();
        for (OrderDto orderDto : orderDtos) {
            OrderEntity orderEntity = new OrderEntity();
            BeanUtils.copyProperties(orderDto, orderEntity, "_id");
            orderEntities.add(orderEntity);
        }
        return orderEntities;
    }

    public long add(OrderEntity orderEntity) {
        OrderDto orderDto = new OrderDto();
        BeanUtils.copyProperties(orderEntity, orderDto, "_id");
        final Collection<OrderDto> res = orderDao.insert(orderDto);
        return res.size();
    }

    public long add(List<OrderEntity> orderEntities) {
        final List<OrderDto> orderDtoList = new ArrayList<>();
        for (OrderEntity orderEntity : orderEntities) {
            OrderDto orderDTO = new OrderDto();
            BeanUtils.copyProperties(orderEntity, orderDTO);
            orderDtoList.add(orderDTO);
        }
        final Collection<OrderDto> res = orderDao.insert(orderDtoList);
        return res.size();
    }

    public long update(OrderEntity orderEntity) {
        OrderDto orderDto = new OrderDto();
        BeanUtils.copyProperties(orderEntity, orderDto);
        UpdateResult result = orderDao.update(orderDto);
        return result.getModifiedCount();
    }

    public long delete(int orderId) {
        DeleteResult res = orderDao.delete(orderId);
        log.info("delete result {}", res);
        return res.getDeletedCount();
    }
}
