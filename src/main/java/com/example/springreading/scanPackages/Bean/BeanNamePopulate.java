package com.example.springreading.scanPackages.Bean;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.beans.PropertyValues;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.BeanNameAware;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.beans.factory.config.InstantiationAwareBeanPostProcessor;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import javax.annotation.PostConstruct;

/**
 * 持有上下文环境对象
 *
 * @author Wang Junwei
 * @date 2024/1/11 16:54
 */
@Slf4j
public class BeanNamePopulate implements ApplicationContextAware, BeanFactoryAware, BeanNameAware,
        BeanPostProcessor, InstantiationAwareBeanPostProcessor,
        InitializingBean {

    @Override
    public Object postProcessBeforeInstantiation(Class<?> beanClass, String beanName) throws BeansException {
        log.info("调用BPP#postProcessBeforeInstantiation");
        return InstantiationAwareBeanPostProcessor.super.postProcessBeforeInstantiation(beanClass, beanName);
    }

    @Override
    public boolean postProcessAfterInstantiation(Object bean, String beanName) throws BeansException {
        log.info("调用BPP#postProcessAfterInstantiation");
        return InstantiationAwareBeanPostProcessor.super.postProcessAfterInstantiation(bean, beanName);
    }

    @Override
    public PropertyValues postProcessProperties(PropertyValues pvs, Object bean, String beanName) throws BeansException {
        log.info("调用BPP#postProcessProperties");
        return InstantiationAwareBeanPostProcessor.super.postProcessProperties(pvs, bean, beanName);
    }

    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        log.info("调用BPP#postProcessBeforeInitialization");
        return bean;
    }

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        log.info("调用BPP#postProcessAfterInitialization");
        if (bean instanceof BeanNamePopulateProxy) {
            return ((BeanNamePopulateProxy) bean).getInstance();
        }
        return bean;
    }

    public BeanNamePopulate() {
        log.info("调用构造器");
    }

    @PostConstruct
    public void postConstruct() {
        log.info("调用PostConstruct");
    }

    public void init() {
        log.info("调用init");
    }

    public void destroy() {
        log.info("调用destroy");
    }

    @Override
    public void setBeanName(String name) {
        log.info("调用BeanNameAware");
    }

    @Override
    public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
        log.info("调用BeanFactoryAware");
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        log.info("调用ApplicationContextAware");
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        log.info("调用InitializingBean");
    }
}
