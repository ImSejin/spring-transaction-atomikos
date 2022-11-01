package io.github.imsejin.study.atomikos.order.model;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@ToString
@NoArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class GoodsOrder {

    @EqualsAndHashCode.Include
    private String orderCode;

    private String goodsNumber;

    private LocalDateTime orderDateTime;

    public GoodsOrder(String goodsNumber) {
        this.orderCode = UUID.randomUUID().toString();
        this.goodsNumber = goodsNumber;
        this.orderDateTime = LocalDateTime.now();
    }

}
