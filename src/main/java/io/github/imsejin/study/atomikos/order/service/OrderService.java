package io.github.imsejin.study.atomikos.order.service;

import io.github.imsejin.study.atomikos.configuration.database.annotation.GlobalTransactional;
import io.github.imsejin.study.atomikos.configuration.database.annotation.MariadbTransactional;
import io.github.imsejin.study.atomikos.configuration.database.annotation.PostgreSqlTransactional;
import io.github.imsejin.study.atomikos.order.model.GoodsOrder;
import io.github.imsejin.study.atomikos.order.repository.OrderMariadbRepository;
import io.github.imsejin.study.atomikos.order.repository.OrderPostgreSqlRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderPostgreSqlRepository postgreSqlRepository;
    private final OrderMariadbRepository mariadbRepository;

    @MariadbTransactional // When @Transactional is duplicated, only the first transaction manager specified is applied.
    @PostgreSqlTransactional
    public List<GoodsOrder> getGoodsOrders() {
        return mariadbRepository.findAllGoodsOrders();
    }

    @GlobalTransactional
    public GoodsOrder saveGoodsOrder(GoodsOrder goodsOrder) {
        // Defensive coding.
        goodsOrder = new GoodsOrder(goodsOrder.getGoodsNumber());

        int order1 = postgreSqlRepository.saveGoodsOrder(goodsOrder);
        int order2 = mariadbRepository.saveGoodsOrder(goodsOrder);
        if (true) throw new RuntimeException("Could not save goods order: " + goodsOrder);

        return goodsOrder;
    }

    @PostgreSqlTransactional
    public GoodsOrder saveGoodsOrderToPostgreSql(GoodsOrder goodsOrder) {
        // Defensive coding.
        goodsOrder = new GoodsOrder(goodsOrder.getGoodsNumber());

        int order1 = postgreSqlRepository.saveGoodsOrder(goodsOrder);
        if (true) throw new RuntimeException("Could not save goods order: " + goodsOrder);

        return goodsOrder;
    }

    @MariadbTransactional
    public GoodsOrder saveGoodsOrderToMariadb(GoodsOrder goodsOrder) {
        // Defensive coding.
        goodsOrder = new GoodsOrder(goodsOrder.getGoodsNumber());

        int order2 = mariadbRepository.saveGoodsOrder(goodsOrder);
        if (true) throw new RuntimeException("Could not save goods order: " + goodsOrder);

        return goodsOrder;
    }

}
