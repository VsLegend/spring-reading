package com.example.springreading.scanPackages.service.pp;

import com.example.springreading.scanPackages.service.BeanService;

/**
 * 用配置类注入的BFPP增加Bean定义、修改Bean定义
 *
 * @author Wang Junwei
 * @date 2023/2/10 16:30
 */
public class ConfigBeanServiceImpl implements BeanService {

    public static final String BEAN_NAME = "configPpBeanService";

    private String name;

    public ConfigBeanServiceImpl() {
    }


    @Override
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
