package io.github.imsejin.study.atomikos.configuration.database;

import io.github.imsejin.study.atomikos.Application;
import io.github.imsejin.study.atomikos.configuration.database.annotation.PostgreSqlMapper;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.SqlSessionTemplate;
import org.mybatis.spring.annotation.MapperScan;
import org.mybatis.spring.boot.autoconfigure.MybatisProperties;
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
        annotationClass = PostgreSqlMapper.class,
        sqlSessionFactoryRef = "postgreSqlSessionFactory",
        sqlSessionTemplateRef = "postgreSqlSessionTemplate"
)
public class PostgreSqlDataSourceConfiguration {

    public static final String TRANSACTION_MANAGER_BEAN_NAME = "postgreSqlTransactionManager";

    @Bean("postgreSqlDataSource")
    @ConfigurationProperties("spring.datasource.mysql")
//    @ConfigurationProperties("spring.datasource.postgresql")
    DataSource dataSource() {
        return DataSourceBuilder.create().build();
    }

    @Bean("postgreSqlSessionFactory")
    SqlSessionFactory sqlSessionFactory(@Nullable MybatisProperties mybatisProperties) throws Exception {
        SqlSessionFactoryBean factoryBean = new SqlSessionFactoryBean();

        factoryBean.setDataSource(dataSource());
        if (mybatisProperties != null) {
            factoryBean.setConfiguration(mybatisProperties.getConfiguration());
            factoryBean.setTypeHandlersPackage(mybatisProperties.getTypeHandlersPackage());
        }

        return factoryBean.getObject();
    }

    @Bean("postgreSqlSessionTemplate")
    SqlSession sqlSessionTemplate(@Nullable MybatisProperties mybatisProperties) throws Exception {
        return new SqlSessionTemplate(sqlSessionFactory(mybatisProperties));
    }

    // TODO: What is the differences between JdbcTransactionManager and DataSourceTransactionManager?
    @Bean(TRANSACTION_MANAGER_BEAN_NAME)
    TransactionManager transactionManager() {
        return new JdbcTransactionManager(dataSource());
    }

}
