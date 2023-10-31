package com.example.springreading.scanPackages.postProcessors;

import org.springframework.beans.BeansException;
import org.springframework.beans.MutablePropertyValues;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.stereotype.Component;

/**
 * 多余属性的后处理器
 *
 * @author Wang Junwei
 * @date 2023/10/20 11:42
 */
@Component
public class ExtraPropertyPostProcessor implements BeanFactoryPostProcessor {

    public ExtraPropertyPostProcessor() {
        extraProperty = "sensitive";
    }
    private final String extraProperty;

    @Override
    public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {
        String[] beanNames = beanFactory.getBeanDefinitionNames();
        for (String beanName : beanNames) {
            BeanDefinition beanDefinition = beanFactory.getBeanDefinition(beanName);
            if (beanDefinition.hasPropertyValues()) {
                MutablePropertyValues propertyValues = beanDefinition.getPropertyValues();
                propertyValues.removePropertyValue(extraProperty);
                System.out.println("移除属性：" + beanName);
            }
        }
    }
}
