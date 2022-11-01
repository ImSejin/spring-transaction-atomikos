package io.github.imsejin.study.atomikos.configuration.database;

import io.github.imsejin.study.atomikos.Application;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.SqlSessionTemplate;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;

@Configuration
@MapperScan(
        basePackageClasses = Application.class,
        annotationClass = MariadbMapper.class,
        sqlSessionFactoryRef = "mariadbSqlSessionFactory",
        sqlSessionTemplateRef = "mariadbSqlSessionTemplate"
)
public class MariadbDataSourceConfiguration {

    @Bean("mariadbDataSource")
    @ConfigurationProperties("spring.datasource.mariadb.hikari")
    static DataSource dataSource() {
        return DataSourceBuilder.create().build();
    }

    @Bean("mariadbSqlSessionFactory")
    static SqlSessionFactory sqlSessionFactory() throws Exception {
        SqlSessionFactoryBean factoryBean = new SqlSessionFactoryBean();
        factoryBean.setDataSource(dataSource());
        return factoryBean.getObject();
    }

    @Bean("mariadbSqlSessionTemplate")
    static SqlSessionTemplate sqlSessionTemplate() throws Exception {
        return new SqlSessionTemplate(sqlSessionFactory());
    }

}
