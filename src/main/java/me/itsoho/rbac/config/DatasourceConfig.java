package me.itsoho.rbac.config;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;

@Configuration
public class DatasourceConfig {
	
	
	@Bean
	@ConfigurationProperties(prefix="datasource.druid")
    public DataSource createDruidDatasource(){
    	return new com.alibaba.druid.pool.DruidDataSource();
    }
	
//	@Bean
//	@Autowired
//	public JdbcTemplate jdbcTemplate(DataSource dataSource){
//		return new JdbcTemplate(dataSource);
//	}
}
