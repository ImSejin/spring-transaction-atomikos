package io.github.imsejin.study.atomikos.configuration.database;

import io.github.imsejin.study.atomikos.Application;
import io.github.imsejin.study.atomikos.configuration.database.MariadbDataSourceConfiguration.MariadbMapper;
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
//        basePackages = "io.github.imsejin.study.atomikos.order.repository",
        basePackageClasses = Application.class,
        markerInterface = MariadbMapper.class,
        sqlSessionFactoryRef = "mariadbSqlSessionFactory",
        sqlSessionTemplateRef = "mariadbSqlSessionTemplate"
)
public class MariadbDataSourceConfiguration {

    @Bean("mariadbDataSource")
    @ConfigurationProperties("spring.datasource.mariadb.hikari")
    DataSource dataSource() {
        return DataSourceBuilder.create().build();
    }

    @Bean("mariadbSqlSessionFactory")
    SqlSessionFactory sqlSessionFactory() throws Exception {
        SqlSessionFactoryBean factoryBean = new SqlSessionFactoryBean();
        factoryBean.setDataSource(dataSource());
        return factoryBean.getObject();
    }

    @Bean("mariadbSqlSessionTemplate")
    SqlSessionTemplate sqlSessionTemplate() throws Exception {
        return new SqlSessionTemplate(sqlSessionFactory());
    }

    public @interface MariadbMapper {
    }

}
