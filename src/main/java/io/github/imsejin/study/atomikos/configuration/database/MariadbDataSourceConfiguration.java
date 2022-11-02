package io.github.imsejin.study.atomikos.configuration.database;

import io.github.imsejin.study.atomikos.Application;
import io.github.imsejin.study.atomikos.configuration.database.annotation.MariadbMapper;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.SqlSessionTemplate;
import org.mybatis.spring.annotation.MapperScan;
import org.mybatis.spring.boot.autoconfigure.MybatisProperties;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.support.JdbcTransactionManager;
import org.springframework.lang.Nullable;
import org.springframework.transaction.TransactionManager;

import javax.sql.DataSource;

//@Configuration
@Deprecated
@MapperScan(
        basePackageClasses = Application.class,
        annotationClass = MariadbMapper.class,
        sqlSessionFactoryRef = "mariadbSqlSessionFactory",
        sqlSessionTemplateRef = "mariadbSqlSessionTemplate"
)
public class MariadbDataSourceConfiguration {

    public static final String TRANSACTION_MANAGER_BEAN_NAME = "mariadbTransactionManager";

    @Bean("mariadbDataSource")
    @ConfigurationProperties("spring.datasource.mariadb")
    DataSource dataSource() {
        return DataSourceBuilder.create().build();
    }

    /**
     * This method avoids to meet exception for {@link NoSuchBeanDefinitionException},
     * even though you don't specify {@link MybatisProperties#MYBATIS_PREFIX} in application properties
     * due to annotation {@link Nullable @Nullable} which is equal to {@link Autowired @Autowired(required = false)}.
     *
     * @param mybatisProperties properties of MyBatis
     * @return sql session factory
     * @throws Exception if failed to initialized datasource
     */
    @Bean("mariadbSqlSessionFactory")
    SqlSessionFactory sqlSessionFactory(@Nullable MybatisProperties mybatisProperties) throws Exception {
        SqlSessionFactoryBean factoryBean = new SqlSessionFactoryBean();

        factoryBean.setDataSource(dataSource());
        if (mybatisProperties != null) {
            factoryBean.setConfiguration(mybatisProperties.getConfiguration());
            factoryBean.setTypeHandlersPackage(mybatisProperties.getTypeHandlersPackage());
        }

        return factoryBean.getObject();
    }

    @Bean("mariadbSqlSessionTemplate")
    SqlSession sqlSessionTemplate(@Nullable MybatisProperties mybatisProperties) throws Exception {
        return new SqlSessionTemplate(sqlSessionFactory(mybatisProperties));
    }

    // TODO: What is the differences between JdbcTransactionManager and DataSourceTransactionManager?
    @Bean(TRANSACTION_MANAGER_BEAN_NAME)
    TransactionManager transactionManager() {
        return new JdbcTransactionManager(dataSource());
    }

}
