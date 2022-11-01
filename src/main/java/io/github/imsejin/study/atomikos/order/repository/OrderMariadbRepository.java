package io.github.imsejin.study.atomikos.order.repository;

import io.github.imsejin.study.atomikos.configuration.database.annotation.MariadbMapper;
import io.github.imsejin.study.atomikos.order.model.GoodsOrder;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@MariadbMapper
public interface OrderMariadbRepository {

    @Select("""
            SELECT ORDER_CD AS orderCode
                 , GOODS_NO AS goodsNumber
              FROM GOODS_ORDER
             WHERE 1 = 1
            """)
    List<GoodsOrder> findAllGoodsOrders();

    @Insert("""
            INSERT INTO GOODS_ORDER (ORDER_CD, GOODS_NO, ORD_DT)
            VALUES (#{orderCode}, #{goodsNumber}, #{orderDateTime})
            """)
    int saveGoodsOrder(GoodsOrder goodsOrder);

}
