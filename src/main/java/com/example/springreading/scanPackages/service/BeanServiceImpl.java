package com.example.springreading.scanPackages.service;

import org.springframework.context.annotation.*;
import org.springframework.stereotype.Component;

/**
 * @Author Wang Junwei
 * @Date 2023/2/10 16:30
 * @Description
 */
@Lazy(value = false)
@DependsOn
@Primary
@Description(value = "Description of bean definition")
@Component(value = "beanService")
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
