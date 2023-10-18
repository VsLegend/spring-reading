package com.example.springreading.service;

import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Component;

/**
 * @Author Wang Junwei
 * @Date 2023/2/10 16:30
 * @Description
 */
@Component(value = "bus")
@Scope(value = "singleton", proxyMode = ScopedProxyMode.DEFAULT)
public class BusinessServiceImpl implements BusinessService {

    private String name;
    private String value;

    public BusinessServiceImpl() {}

    public BusinessServiceImpl(String name) {
        this.name = name;
    }


    @Override
    public String getName() {
        return name;
    }
}
