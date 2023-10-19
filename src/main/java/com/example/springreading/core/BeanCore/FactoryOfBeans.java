package com.example.springreading.core.BeanCore;

import org.springframework.aop.framework.ProxyFactoryBean;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.jndi.JndiObjectFactoryBean;

/**
 * Bean工厂实例化Bean
 *
 * @author Wang Junwei
 * @date 2023/10/19 13:51
 */
public class FactoryOfBeans {

    public void factory() {
        FactoryBean<?> factoryBean;
        ProxyFactoryBean proxyFactoryBean;
        JndiObjectFactoryBean jndiObjectFactoryBean;
    }

}
