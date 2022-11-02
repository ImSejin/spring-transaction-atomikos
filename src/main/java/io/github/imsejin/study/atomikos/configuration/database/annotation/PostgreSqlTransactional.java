package io.github.imsejin.study.atomikos.configuration.database.annotation;

import io.github.imsejin.study.atomikos.configuration.database.jta.XaDataSourceConfiguration;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Inherited
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE, ElementType.METHOD})
@Transactional(transactionManager = XaDataSourceConfiguration.PostgreSql.TRANSACTION_MANAGER_BEAN_NAME)
public @interface PostgreSqlTransactional {

    /**
     * @see Transactional#propagation()
     */
    Propagation propagation() default Propagation.REQUIRED;

    /**
     * @see Transactional#isolation()
     */
    Isolation isolation() default Isolation.DEFAULT;

    /**
     * @see Transactional#timeout()
     */
    int timeout() default TransactionDefinition.TIMEOUT_DEFAULT;

    /**
     * @see Transactional#readOnly()
     */
    boolean readOnly() default false;

    /**
     * @see Transactional#rollbackFor()
     */
    Class<? extends Throwable>[] rollbackFor() default {};

}
