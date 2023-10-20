package com.example.springreading.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * @Author Wang Junwei
 * @Date 2023/2/10 16:30
 * @Description
 */
@Component(value = "constructorBeanService")
public class ConstructorBeanServiceImpl implements BeanService {

    @Value("${property.name:???}")
    private String name;

    private BeanService beanService;

    public ConstructorBeanServiceImpl(@Autowired @Qualifier("beanService") BeanService beanService) {
        this.beanService = beanService;
    }

    @Override
    public String getName() {
        return name;
    }
}
