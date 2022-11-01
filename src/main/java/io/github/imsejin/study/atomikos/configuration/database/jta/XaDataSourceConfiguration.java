package io.github.imsejin.study.atomikos.configuration.database.jta;

import com.atomikos.icatch.jta.UserTransactionImp;
import com.atomikos.icatch.jta.UserTransactionManager;
import io.github.imsejin.study.atomikos.Application;
import io.github.imsejin.study.atomikos.configuration.database.MariadbDataSourceConfiguration;
import io.github.imsejin.study.atomikos.configuration.database.PostgreSqlDataSourceConfiguration;
import io.github.imsejin.study.atomikos.configuration.database.annotation.MariadbMapper;
import io.github.imsejin.study.atomikos.configuration.database.annotation.PostgreSqlMapper;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.SqlSessionTemplate;
import org.mybatis.spring.annotation.MapperScan;
import org.mybatis.spring.boot.autoconfigure.MybatisProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jta.atomikos.AtomikosDataSourceBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import org.springframework.lang.Nullable;
import org.springframework.transaction.TransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.transaction.jta.JtaTransactionManager;

import javax.sql.DataSource;
import javax.transaction.SystemException;
import javax.transaction.UserTransaction;

@Configuration
@EnableTransactionManagement
public class XaDataSourceConfiguration {

    public static final String TRANSACTION_MANAGER_BEAN_NAME = "globalTransactionManager";

    // -------------------------------------------------------------------------------------------------

    @Configuration
    @MapperScan(
            basePackageClasses = Application.class,
            annotationClass = PostgreSqlMapper.class,
            sqlSessionFactoryRef = "mysqlXaSqlSessionFactory",
            sqlSessionTemplateRef = "mysqlXaSqlSessionTemplate"
    )
    static class PostgreSql {
        @Bean("mysqlXaDataSource")
        @ConfigurationProperties("spring.jta.atomikos.datasource.mysql")
        DataSource dataSource() {
            return new AtomikosDataSourceBean();
        }

        @Bean("mysqlXaSqlSessionFactory")
        SqlSessionFactory sqlSessionFactory(@Nullable MybatisProperties mybatisProperties) throws Exception {
            SqlSessionFactoryBean factoryBean = new SqlSessionFactoryBean();

            factoryBean.setDataSource(dataSource());
            if (mybatisProperties != null) {
                factoryBean.setConfiguration(mybatisProperties.getConfiguration());
                factoryBean.setTypeHandlersPackage(mybatisProperties.getTypeHandlersPackage());
            }

            return factoryBean.getObject();
        }

        @Bean("mysqlXaSqlSessionTemplate")
        SqlSession sqlSessionTemplate(@Nullable MybatisProperties mybatisProperties) throws Exception {
            return new SqlSessionTemplate(sqlSessionFactory(mybatisProperties));
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
    static class MariaDB {
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
        SqlSession sqlSessionTemplate(@Nullable MybatisProperties mybatisProperties) throws Exception {
            return new SqlSessionTemplate(sqlSessionFactory(mybatisProperties));
        }
    }

    // -------------------------------------------------------------------------------------------------

    @Bean(TRANSACTION_MANAGER_BEAN_NAME)
    @DependsOn({
            MariadbDataSourceConfiguration.TRANSACTION_MANAGER_BEAN_NAME,
            PostgreSqlDataSourceConfiguration.TRANSACTION_MANAGER_BEAN_NAME,
            "mysqlXaSqlSessionFactory", "mariadbXaSqlSessionFactory",
            "mysqlXaSqlSessionTemplate", "mariadbXaSqlSessionTemplate",
            "userTransaction", "userTransactionManager",
    })
    TransactionManager transactionManager() throws SystemException {
        UserTransaction userTransaction = userTransaction();
        UserTransactionManager userTransactionManager = userTransactionManager();

        JtaTransactionManager transactionManager = new JtaTransactionManager(userTransaction, userTransactionManager);
        transactionManager.setGlobalRollbackOnParticipationFailure(true);

        return transactionManager;
    }

    @Bean("userTransaction")
    UserTransaction userTransaction() throws SystemException {
        UserTransaction userTransaction = new UserTransactionImp();
        userTransaction.setTransactionTimeout(3600);

        return userTransaction;
    }

    @Bean("userTransactionManager")
    UserTransactionManager userTransactionManager() {
        UserTransactionManager userTransactionManager = new UserTransactionManager();
        userTransactionManager.setForceShutdown(false);
        return userTransactionManager;
    }

}
