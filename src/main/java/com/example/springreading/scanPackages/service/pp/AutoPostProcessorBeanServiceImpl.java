package com.example.springreading.scanPackages.service.pp;

import com.example.springreading.scanPackages.service.BeanService;

/**
 * 用BeanFactory后置处理器增加Bean定义、修改Bean定义
 *
 *
 * @author Wang Junwei
 * @date 2023/2/10 16:30
 */
public class AutoPostProcessorBeanServiceImpl implements BeanService {

    public static final String BPPP_BEAN_NAME = "autoPpBeanService";

    private String name;

    public AutoPostProcessorBeanServiceImpl() {
    }


    @Override
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
