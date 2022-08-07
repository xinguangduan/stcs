package org.stcs.server.service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.stcs.server.dao.OrderInfoDAO;
import org.stcs.server.dto.OrderInfoDTO;
import org.stcs.server.entity.OrderInfoEntity;

@Service
public class OrderInfoService {
    private final OrderInfoDAO orderInfoDAO;

    public OrderInfoService(OrderInfoDAO orderInfoDAO) {
        this.orderInfoDAO = orderInfoDAO;
    }

    public OrderInfoEntity find(int orderId) {
        OrderInfoEntity orderInfoEntity = new OrderInfoEntity();
        OrderInfoDTO orderInfoDTO = orderInfoDAO.find(orderId);
        BeanUtils.copyProperties(orderInfoDTO, orderInfoEntity, "_id");
        return orderInfoEntity;
    }

    public List<OrderInfoEntity> findAll() {
        List<OrderInfoEntity> orderInfoEntities = new ArrayList<>();
        List<OrderInfoDTO> orderInfoDTOS = orderInfoDAO.findAll();
        for (OrderInfoDTO orderInfoDTO : orderInfoDTOS) {
            OrderInfoEntity orderInfoEntity = new OrderInfoEntity();
            BeanUtils.copyProperties(orderInfoDTO, orderInfoEntity, "_id");
            orderInfoEntities.add(orderInfoEntity);
        }
        return orderInfoEntities;
    }

    public List<OrderInfoEntity> findByKeys(int custId) {
        List<OrderInfoEntity> orderInfoEntities = new ArrayList<>();
        List<OrderInfoDTO> orderInfoDTOS = orderInfoDAO.findByKeys(custId);
        for (OrderInfoDTO orderInfoDTO : orderInfoDTOS) {
            OrderInfoEntity orderInfoEntity = new OrderInfoEntity();
            BeanUtils.copyProperties(orderInfoDTO, orderInfoEntity, "_id");
            orderInfoEntities.add(orderInfoEntity);
        }
        return orderInfoEntities;
    }

    public long add(OrderInfoEntity orderInfoEntity) {
        OrderInfoDTO orderInfoDTO = new OrderInfoDTO();
        BeanUtils.copyProperties(orderInfoEntity, orderInfoDTO, "_id");
        final Collection<OrderInfoDTO> res = orderInfoDAO.insert(orderInfoDTO);
        return res.size();
    }

    public long add(List<OrderInfoEntity> orderEntities) {
        final List<OrderInfoDTO> orderInfoDTOList = new ArrayList<>();
        for (OrderInfoEntity orderInfoEntity : orderEntities) {
            OrderInfoDTO orderInfoDTO = new OrderInfoDTO();
            BeanUtils.copyProperties(orderInfoEntity, orderInfoDTO);
            orderInfoDTOList.add(orderInfoDTO);
        }
        final Collection<OrderInfoDTO> res = orderInfoDAO.insert(orderInfoDTOList);
        return res.size();
    }

    public void update(OrderInfoEntity orderInfoEntity) {
        OrderInfoDTO orderInfoDTO = new OrderInfoDTO();
        BeanUtils.copyProperties(orderInfoEntity, orderInfoDTO);
        orderInfoDAO.update(orderInfoDTO);
    }
}
