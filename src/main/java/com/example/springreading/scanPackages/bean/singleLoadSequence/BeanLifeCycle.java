package com.example.springreading.scanPackages.bean.singleLoadSequence;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.BeanNameAware;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

/**
 * Bean生命周期回调执行顺序观察
 *
 * @author Wang Junwei
 * @date 2024/1/11 16:54
 */
@Slf4j
public class BeanLifeCycle implements ApplicationContextAware, BeanFactoryAware, BeanNameAware, InitializingBean {
    public BeanLifeCycle() {
        log.info("调用构造器");
    }

    @PostConstruct
    public void postConstruct() {
        log.info("调用@PostConstruct");
    }

    public void init() {
        log.info("调用init");
    }

    @PreDestroy
    public void preDestroy() {
        log.info("调用@PreDestroy");
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
