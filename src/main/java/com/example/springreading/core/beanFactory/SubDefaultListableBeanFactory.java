package com.example.springreading.core.beanFactory;

import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanCreationException;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.beans.factory.support.RootBeanDefinition;
import org.springframework.lang.Nullable;

/**
 * 演示源码执行过程使用
 *
 * @author Wang Junwei
 * @date 2023/10/25 16:45
 */
public class SubDefaultListableBeanFactory extends DefaultListableBeanFactory {

    public SubDefaultListableBeanFactory(DefaultListableBeanFactory defaultListableBeanFactory) {
        super(defaultListableBeanFactory);
    }

    public <T> T doGetBean(String name, @Nullable Class<T> requiredType, @Nullable Object[] args, boolean typeCheckOnly) throws BeansException {
        return super.doGetBean(name, requiredType, args, typeCheckOnly);
    }

    public String transformedBeanName(String name) {
        return super.transformedBeanName(name);
    }

    public Object getSingleton(String beanName, boolean allowEarlyReference) {
        return super.getSingleton(beanName, allowEarlyReference);
    }

    public RootBeanDefinition getMergedLocalBeanDefinition(String beanName) throws BeansException {
        return super.getMergedLocalBeanDefinition(beanName);
    }

    public Object createBean(String beanName, RootBeanDefinition mbd, @Nullable Object[] args) throws BeanCreationException {
        return super.createBean(beanName, mbd, args);
    }

    public void addSingleton(String beanName, Object singletonObject) {
        super.addSingleton(beanName, singletonObject);
    }

    public Object resolveBeforeInstantiation(String beanName, RootBeanDefinition mbd) {
        return super.resolveBeforeInstantiation(beanName, mbd);
    }

    public Object doCreateBean(String beanName, RootBeanDefinition mbd, @Nullable Object[] args) {
        return super.doCreateBean(beanName, mbd, args);
    }
    public BeanWrapper createBeanInstance(String beanName, RootBeanDefinition mbd, @Nullable Object[] args) {
        return super.createBeanInstance(beanName, mbd, args);
    }

    public void populateBean(String beanName, RootBeanDefinition mbd, @Nullable BeanWrapper bw) {
        super.populateBean(beanName, mbd, bw);
    }

    public Object initializeBean(String beanName, Object bean, @Nullable RootBeanDefinition mbd){
        return super.initializeBean(beanName, bean, mbd);
    }

    public void registerDisposableBeanIfNecessary(String beanName, Object bean, RootBeanDefinition mbd) {
        super.registerDisposableBeanIfNecessary(beanName, bean, mbd);
    }
}