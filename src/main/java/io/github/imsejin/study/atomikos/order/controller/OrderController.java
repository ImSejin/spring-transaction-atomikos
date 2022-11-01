package io.github.imsejin.study.atomikos.order.controller;

import io.github.imsejin.study.atomikos.order.model.GoodsOrder;
import io.github.imsejin.study.atomikos.order.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/orders")
@RequiredArgsConstructor
class OrderController {

    private final OrderService service;

    @GetMapping
    Object getAllOrders() {
        return service.getGoodsOrders();
    }

    @PostMapping
    Object saveOrder(@RequestBody GoodsOrder goodsOrder) {
        GoodsOrder order;

        order = service.saveGoodsOrder(goodsOrder);
//        order = service.saveGoodsOrderToPostgreSql(goodsOrder);
//        order = service.saveGoodsOrderToMariadb(goodsOrder);

        return order;
    }

}
