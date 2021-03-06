package com.guoan.demo;

import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.tomcat.jdbc.pool.DataSource;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;

@EnableAutoConfiguration
@SpringBootApplication
@ComponentScan
@MapperScan("com.guoan.demo.mapper")
public class ApplicationEntrance {
	
    @Bean
    @ConfigurationProperties(prefix="spring.datasource")
    public DataSource dataSource() {
    	DataSource dataSource = new DataSource();
    	dataSource.setUrl("jdbc:mysql:///my_demo");
    	dataSource.setUsername("root");
    	dataSource.setPassword("123");
    	dataSource.setDriverClassName("com.mysql.jdbc.Driver");
        return dataSource;
    }

    @Bean
    public SqlSessionFactory sqlSessionFactoryBean() throws Exception {

        SqlSessionFactoryBean sqlSessionFactoryBean = new SqlSessionFactoryBean();
        sqlSessionFactoryBean.setDataSource(dataSource());

        PathMatchingResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();

        sqlSessionFactoryBean.setMapperLocations(resolver.getResources("classpath:/mybatis/*.xml"));

        return sqlSessionFactoryBean.getObject();
    }
    
    @Bean
    public PlatformTransactionManager transactionManager() {
        return new DataSourceTransactionManager(dataSource());
    }

	public static void main(String[] args) {
		SpringApplication.run(ApplicationEntrance.class, args);
	}
		
}
