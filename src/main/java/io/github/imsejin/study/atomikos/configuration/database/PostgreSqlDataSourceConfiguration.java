package io.github.imsejin.study.atomikos.configuration.database;

import io.github.imsejin.study.atomikos.Application;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.SqlSessionTemplate;
import org.mybatis.spring.annotation.MapperScan;
import org.mybatis.spring.boot.autoconfigure.MybatisProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.lang.Nullable;

import javax.sql.DataSource;

@Configuration
@MapperScan(
        basePackageClasses = Application.class,
        annotationClass = PostgreSqlMapper.class,
        sqlSessionFactoryRef = "postgreSqlSessionFactory",
        sqlSessionTemplateRef = "postgreSqlSessionTemplate"
)
class PostgreSqlDataSourceConfiguration {

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
    SqlSessionTemplate sqlSessionTemplate(@Nullable MybatisProperties mybatisProperties) throws Exception {
        return new SqlSessionTemplate(sqlSessionFactory(mybatisProperties));
    }

}
