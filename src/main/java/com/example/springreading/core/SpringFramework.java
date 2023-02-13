package com.example.springreading.core;

import com.example.springreading.service.BusinessService;
import org.springframework.beans.MutablePropertyValues;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.beans.factory.config.ConstructorArgumentValues;
import org.springframework.beans.factory.support.*;
import org.springframework.beans.factory.xml.XmlBeanDefinitionReader;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.*;
import org.springframework.context.support.AbstractXmlApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

/**
 * @Author Wang Junwei
 * @Date 2023/2/10 15:15
 * @Description Spring框架
 */
public class SpringFramework {

    /**
     * 容器加载bean的过程
     */
    public void process() {
        // 两种IoC容器
        BeanFactory beanFactory;
        ApplicationContext applicationContext;


        // 容器实现类

        // BeanFactory的基础实现类 不具备自动扫描和装配的功能
        DefaultListableBeanFactory defaultListableBeanFactory = new DefaultListableBeanFactory();

        // spring通过两种方式自动扫描和管理bean： 1.注解扫描  2.xml配置扫描
        // 1.注解扫描 @Component @Repository @Controller @Service
        AnnotationConfigApplicationContext annotationConfigApplicationContext = new AnnotationConfigApplicationContext("com.example.springreading");
        // 2.xml配置文件扫描 通过标签扫描
        ClassPathXmlApplicationContext classPathXmlApplicationContext = new ClassPathXmlApplicationContext("/xml");


        // 容器中，封装bean的实体定义与实现类
        BeanDefinition beanDefinition;
        AbstractBeanDefinition abstractBeanDefinition;
        RootBeanDefinition rootBeanDefinition;
        ChildBeanDefinition childBeanDefinition;


        // 把bean封装为一个bean定义
        BeanDefinition businessServiceBean = beanDefinition();

        // 将bean定义注册到容器中
        defaultListableBeanFactory.registerBeanDefinition("businessService", businessServiceBean);

    }


    /**
     * 自动扫描、装配、注册bean，最终将其封装为BeanDefinition
     * BeanDefinitionReader是实际解析和装配BeanDefinition的类，BeanDefinitionRegistry只是将解析的数据保存而已
     *
     * @see AbstractXmlApplicationContext#loadBeanDefinitions(org.springframework.beans.factory.support.DefaultListableBeanFactory)
     */
    public void beanDefinitionReader(BeanDefinitionRegistry registry) {
        BeanDefinitionReader beanDefinitionReader;
        // Properties文件扫描器，5.3被弃用
        PropertiesBeanDefinitionReader propertiesBeanDefinitionReader;
        // xml文件扫描器
        XmlBeanDefinitionReader xmlBeanDefinitionReader = new XmlBeanDefinitionReader(registry);
        xmlBeanDefinitionReader.loadBeanDefinitions("classpath:/xml/bean.xml");
    }


    /**
     * BeanDefinition中的对bean的设置
     */
    public RootBeanDefinition beanDefinition() {
        RootBeanDefinition beanDefinition = new RootBeanDefinition(BusinessService.class);
        // 懒加载 @Lazy(value = false)
        beanDefinition.setLazyInit(false);
        // 作用范围 @Scope(value = ConfigurableBeanFactory.SCOPE_SINGLETON)
        beanDefinition.setScope(ConfigurableBeanFactory.SCOPE_SINGLETON);
        // 设置依赖的bean名 @DependsOn()
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
