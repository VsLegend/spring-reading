package com.example.springreading.service;

/**
 * @Author Wang Junwei
 * @Date 2023/2/10 16:30
 * @Description
 */
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
