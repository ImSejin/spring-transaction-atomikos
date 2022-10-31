package io.github.imsejin.study.atomikos.configuration.database;

import io.github.imsejin.study.atomikos.Application;
import io.github.imsejin.study.atomikos.configuration.database.PostgreSqlDataSourceConfiguration.PostgreSqlMapper;
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
        markerInterface = PostgreSqlMapper.class,
        sqlSessionFactoryRef = "postgreSqlSessionFactory",
        sqlSessionTemplateRef = "postgreSqlSessionTemplate"
)
public class PostgreSqlDataSourceConfiguration {

    @Bean("postgreSqlDataSource")
    @ConfigurationProperties("spring.datasource.postgresql.hikari")
    DataSource dataSource() {
        return DataSourceBuilder.create().build();
    }

    @Bean("postgreSqlSessionFactory")
    SqlSessionFactory sqlSessionFactory() throws Exception {
        SqlSessionFactoryBean factoryBean = new SqlSessionFactoryBean();
        factoryBean.setDataSource(dataSource());
        return factoryBean.getObject();
    }

    @Bean("postgreSqlSessionTemplate")
    SqlSessionTemplate sqlSessionTemplate() throws Exception {
        return new SqlSessionTemplate(sqlSessionFactory());
    }

    public @interface PostgreSqlMapper {
    }

}
