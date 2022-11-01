package io.github.imsejin.study.atomikos.order.service;

import io.github.imsejin.study.atomikos.order.model.GoodsOrder;
import io.github.imsejin.study.atomikos.order.repository.OrderMariadbRepository;
import io.github.imsejin.study.atomikos.order.repository.OrderPostgreSqlRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderPostgreSqlRepository postgreSqlRepository;
    private final OrderMariadbRepository mariadbRepository;

    public List<GoodsOrder> getGoodsOrders() {
        return mariadbRepository.findAllGoodsOrders();
    }

    @Transactional
    public GoodsOrder saveGoodsOrder(GoodsOrder goodsOrder) {
        GoodsOrder order1 = postgreSqlRepository.saveGoodsOrder(goodsOrder);
        GoodsOrder order2 = mariadbRepository.saveGoodsOrder(goodsOrder);
        return order1;
    }

}
