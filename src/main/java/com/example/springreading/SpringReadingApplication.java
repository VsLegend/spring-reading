package com.example.springreading;

import com.example.springreading.scanPackages.service.BeanService;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
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

    private final Log logger = LogFactory.getLog(getClass());

    public static void main(String[] args) {
        ConfigurableApplicationContext applicationContext = SpringApplication.run(SpringReadingApplication.class, args);
        BeanService ppBeanService = (BeanService) applicationContext.getBean("ppBeanService");
        System.out.println(ppBeanService.getName());

    }

}
