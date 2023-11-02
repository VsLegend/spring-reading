package com.example.springreading.scanPackages.config;

import com.example.springreading.scanPackages.postProcessors.ExtraPropertyPostProcessor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 配置类
 *
 * @author Wang Junwei
 * @date 2023/11/2 17:37
 */
@Configuration
public class Config {

    @Bean("extraPropertyPostProcessor")
    public ExtraPropertyPostProcessor processor() {
        return new ExtraPropertyPostProcessor();
    }
}
