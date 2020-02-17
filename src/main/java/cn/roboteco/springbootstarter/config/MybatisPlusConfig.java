package cn.roboteco.springbootstarter.config;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@EnableTransactionManagement
@Configuration
@MapperScan({ "cn.roboteco.springbootstarter.mapper" })
public class MybatisPlusConfig {

}
