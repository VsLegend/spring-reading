package com.example.springreading.scanPackages.service;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.*;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * @author Wang Junwei
 * @date 2023/2/10 16:30
 */
@Lazy
@Primary
@Component(value = "beanService")
@Description(value = "Description of bean definition")
@Scope(value = "singleton", proxyMode = ScopedProxyMode.DEFAULT)
public class BeanServiceImpl implements BeanService {

    private String name;

    public BeanServiceImpl() {
        name = BeanServiceImpl.class.getName();
    }

    public BeanServiceImpl(String name) {
        this.name = name;
    }


    @Override
    public String getName() {
        return name;
    }
}
