package com.example.springreading;

import com.example.springreading.scanPackages.service.BeanService;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.ComponentScan;

/**
 * @author Wang Junwei
 */
// spring 组件扫描
@ComponentScan(value = {"com.example.springreading.scanPackages",})
// spring boot 组件扫描
@SpringBootApplication(scanBasePackages = {"com.example.springreading.scanPackages",})
public class SpringReadingApplication {

    public static void main(String[] args) {
        ConfigurableApplicationContext applicationContext = SpringApplication.run(SpringReadingApplication.class, args);
        BeanService beanService = applicationContext.getBean("beanService", BeanService.class);
        System.out.println(beanService.getName());
    }

}
