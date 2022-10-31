package io.github.imsejin.study.atomikos.goods.model;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Goods {

    @EqualsAndHashCode.Include
    private String goodsNumber;

    private String goodsName;

}
