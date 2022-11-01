package io.github.imsejin.study.atomikos.order.repository;

import io.github.imsejin.study.atomikos.configuration.database.PostgreSqlMapper;
import io.github.imsejin.study.atomikos.order.model.GoodsOrder;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@PostgreSqlMapper
public interface OrderPostgreSqlRepository {

    @Select("""
            SELECT ORDER_CD AS orderCode
                 , GOODS_NO AS goodsNumber
              FROM TMP_GOODS_ORDER
             WHERE 1 = 1
            """)
    List<GoodsOrder> findAllGoodsOrders();

    @Insert("""
            INSERT INTO TMP_GOODS_ORDER (ORDER_CD, GOODS_NO)
            VALUES (#{orderCode}, #{goodsNumber})
            """)
    GoodsOrder saveGoodsOrder(GoodsOrder goodsOrder);

}
