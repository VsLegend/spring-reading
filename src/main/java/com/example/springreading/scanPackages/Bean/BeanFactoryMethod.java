package com.example.springreading.scanPackages.Bean;

import org.springframework.beans.factory.FactoryBean;

/**
 * @author Wang Junwei
 * @date 2024/1/11 17:22
 */
public class BeanFactoryMethod implements FactoryBean<Object> {

    private BeanFactoryMethod() {}

    public BeanFactoryMethod createInstance() {
        return new BeanFactoryMethod();
    }

    @Override
    public Object getObject() throws Exception {
        return null;
    }

    @Override
    public Class<?> getObjectType() {
        return null;
    }
}
