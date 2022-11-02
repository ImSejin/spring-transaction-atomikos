package io.github.imsejin.study.atomikos.configuration.database.jta;

import com.atomikos.icatch.jta.UserTransactionImp;
import com.atomikos.icatch.jta.UserTransactionManager;
import io.github.imsejin.study.atomikos.Application;
import io.github.imsejin.study.atomikos.configuration.database.annotation.MariadbMapper;
import io.github.imsejin.study.atomikos.configuration.database.annotation.PostgreSqlMapper;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.SqlSessionTemplate;
import org.mybatis.spring.annotation.MapperScan;
import org.mybatis.spring.boot.autoconfigure.MybatisProperties;
import org.springframework.boot.autoconfigure.jdbc.XADataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.transaction.TransactionAutoConfiguration;
import org.springframework.boot.autoconfigure.transaction.jta.JtaAutoConfiguration;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jta.atomikos.AtomikosDataSourceBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import org.springframework.jdbc.support.JdbcTransactionManager;
import org.springframework.lang.Nullable;
import org.springframework.transaction.TransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.transaction.jta.JtaTransactionManager;

import javax.sql.DataSource;
import javax.transaction.SystemException;
import javax.transaction.UserTransaction;

/**
 * @see JtaAutoConfiguration
 * @see TransactionAutoConfiguration
 * @see XADataSourceAutoConfiguration
 */
@Configuration
@EnableTransactionManagement
public class XaDataSourceConfiguration {

    public static final String XA_TRANSACTION_MANAGER_BEAN_NAME = "globalTransactionManager";

    @Bean(XA_TRANSACTION_MANAGER_BEAN_NAME)
    @DependsOn({
            MariaDB.TRANSACTION_MANAGER_BEAN_NAME,
            PostgreSql.TRANSACTION_MANAGER_BEAN_NAME,
            "postgresSqlXaSqlSessionFactory", "mariadbXaSqlSessionFactory",
            "postgresSqlXaSqlSessionTemplate", "mariadbXaSqlSessionTemplate",
    })
    TransactionManager transactionManager() throws SystemException {
        UserTransaction userTransaction = new UserTransactionImp();
        userTransaction.setTransactionTimeout(3600);
        UserTransactionManager atomikosTransactionManager = new UserTransactionManager();
        atomikosTransactionManager.setForceShutdown(false);

        JtaTransactionManager transactionManager = new JtaTransactionManager(userTransaction, atomikosTransactionManager);
        transactionManager.setGlobalRollbackOnParticipationFailure(true);

        return transactionManager;
    }

    // -------------------------------------------------------------------------------------------------

    @Configuration
    @MapperScan(
            basePackageClasses = Application.class,
            annotationClass = PostgreSqlMapper.class,
            sqlSessionFactoryRef = "postgresSqlXaSqlSessionFactory",
            sqlSessionTemplateRef = "postgresSqlXaSqlSessionTemplate"
    )
    public static class PostgreSql {
        public static final String TRANSACTION_MANAGER_BEAN_NAME = "postgreSqlTransactionManager";

        @Bean("postgreSqlXaDataSource")
        @ConfigurationProperties("spring.jta.atomikos.datasource.mysql")
        DataSource dataSource() {
            return new AtomikosDataSourceBean();
        }

        @Bean("postgresSqlXaSqlSessionFactory")
        SqlSessionFactory sqlSessionFactory(@Nullable MybatisProperties mybatisProperties) throws Exception {
            SqlSessionFactoryBean factoryBean = new SqlSessionFactoryBean();

            factoryBean.setDataSource(dataSource());
            if (mybatisProperties != null) {
                factoryBean.setConfiguration(mybatisProperties.getConfiguration());
                factoryBean.setTypeHandlersPackage(mybatisProperties.getTypeHandlersPackage());
            }

            return factoryBean.getObject();
        }

        @Bean("postgresSqlXaSqlSessionTemplate")
        SqlSessionTemplate sqlSessionTemplate(@Nullable MybatisProperties mybatisProperties) throws Exception {
            return new SqlSessionTemplate(sqlSessionFactory(mybatisProperties));
        }

        // TODO: What is the differences between JdbcTransactionManager and DataSourceTransactionManager?
        @Bean(TRANSACTION_MANAGER_BEAN_NAME)
        TransactionManager transactionManager() {
            return new JdbcTransactionManager(dataSource());
        }
    }

    // -------------------------------------------------------------------------------------------------

    @Configuration
    @MapperScan(
            basePackageClasses = Application.class,
            annotationClass = MariadbMapper.class,
            sqlSessionFactoryRef = "mariadbXaSqlSessionFactory",
            sqlSessionTemplateRef = "mariadbXaSqlSessionTemplate"
    )
    public static class MariaDB {
        public static final String TRANSACTION_MANAGER_BEAN_NAME = "mariadbTransactionManager";

        @Bean("mariadbXaDataSource")
        @ConfigurationProperties("spring.jta.atomikos.datasource.mariadb")
        DataSource dataSource() {
            return new AtomikosDataSourceBean();
        }

        @Bean("mariadbXaSqlSessionFactory")
        SqlSessionFactory sqlSessionFactory(@Nullable MybatisProperties mybatisProperties) throws Exception {
            SqlSessionFactoryBean factoryBean = new SqlSessionFactoryBean();

            factoryBean.setDataSource(dataSource());
            if (mybatisProperties != null) {
                factoryBean.setConfiguration(mybatisProperties.getConfiguration());
                factoryBean.setTypeHandlersPackage(mybatisProperties.getTypeHandlersPackage());
            }

            return factoryBean.getObject();
        }

        @Bean("mariadbXaSqlSessionTemplate")
        SqlSessionTemplate sqlSessionTemplate(@Nullable MybatisProperties mybatisProperties) throws Exception {
            return new SqlSessionTemplate(sqlSessionFactory(mybatisProperties));
        }

        // TODO: What is the differences between JdbcTransactionManager and DataSourceTransactionManager?
        @Bean(TRANSACTION_MANAGER_BEAN_NAME)
        TransactionManager transactionManager() {
            return new JdbcTransactionManager(dataSource());
        }
    }

}
