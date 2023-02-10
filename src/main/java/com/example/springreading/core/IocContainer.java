package com.example.springreading.core;

import com.example.springreading.service.BusinessService;
import com.example.springreading.service.BusinessServiceImpl;
import org.springframework.beans.MutablePropertyValues;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.ConstructorArgumentValues;
import org.springframework.beans.factory.support.AbstractBeanDefinition;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.beans.factory.support.RootBeanDefinition;
import org.springframework.context.ApplicationContext;

/**
 * @Author Wang Junwei
 * @Date 2023/2/10 15:15
 * @Description IoC容器
 */
public class IocContainer {

    /**
     * 容器加载过程
     */
    public void process() {
        // 两种IoC容器
        BeanFactory beanFactory;
        ApplicationContext applicationContext;

        // 容器实现类
        DefaultListableBeanFactory defaultListableBeanFactory = new DefaultListableBeanFactory();


        // 容器中，封装bean的实体定义与实现类
        BeanDefinition beanDefinition;
        AbstractBeanDefinition abstractBeanDefinition;
        RootBeanDefinition rootBeanDefinition;

        // 把bean封装为一个bean定义
        BeanDefinition businessServiceBean = beanInject(BusinessService.class);

        // 将bean定义注册到容器中
        defaultListableBeanFactory.registerBeanDefinition("businessService", businessServiceBean);

    }


    /**
     * 关于bean定义中的设置
     *
     * @param beanClass
     */
    public RootBeanDefinition beanInject(Class<?> beanClass) {
        RootBeanDefinition beanDefinition = new RootBeanDefinition(beanClass);
        // 懒加载
        beanDefinition.setLazyInit(false);
        // 作用范围
        beanDefinition.setScope(BeanDefinition.SCOPE_SINGLETON);
        // 设置依赖的bean名
        beanDefinition.setDependsOn();

        // 设置bean定义的依赖注入方式，默认情况下调用无参构造函数初始化bean
        // 1. 构造方法注入
        ConstructorArgumentValues constructorArguments = new ConstructorArgumentValues();
        constructorArguments.addIndexedArgumentValue(0, "ConstructorName");
        beanDefinition.setConstructorArgumentValues(constructorArguments);

        // 2. setter方法注入
        MutablePropertyValues propertyValues = new MutablePropertyValues();
        propertyValues.add("name", "PropertyName");
        propertyValues.add("value", "PropertyValue");
        beanDefinition.setPropertyValues(propertyValues);

        return beanDefinition;
    }
}
