/*
 10.8.3-MariaDB
 */
DROP TABLE IF EXISTS GOODS_ORDER;
CREATE TABLE GOODS_ORDER
(
    ORDER_CD VARCHAR(36) NOT NULL,
    GOODS_NO VARCHAR(14) NOT NULL,
    PRIMARY KEY (ORDER_CD)
);
CREATE
INDEX GOODS_ORDER_GOODS_NO_INDEX
ON GOODS_ORDER (GOODS_NO);

INSERT INTO GOODS_ORDER (ORDER_CD, GOODS_NO)
VALUES ('0be71f3d-917f-4c48-94bd-d54ce86b9968', '8000380205486');
INSERT INTO GOODS_ORDER (ORDER_CD, GOODS_NO)
VALUES ('bfa44346-f033-4b99-9036-dacbacee7ec9', '8801068407044');
