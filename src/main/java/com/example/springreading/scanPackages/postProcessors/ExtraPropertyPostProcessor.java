package com.example.springreading.scanPackages.postProcessors;

import org.springframework.beans.BeansException;
import org.springframework.beans.MutablePropertyValues;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.core.Ordered;
import org.springframework.core.PriorityOrdered;
import org.springframework.stereotype.Component;

/**
 * 后处理器
 *
 * @author Wang Junwei
 * @date 2023/10/20 11:42
 */
public class ExtraPropertyPostProcessor implements BeanFactoryPostProcessor, PriorityOrdered {

    @Override
    public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {
        //
    }

    @Override
    public int getOrder() {
        /**
         * 最低优先级
         */
        return Ordered.LOWEST_PRECEDENCE;
    }
}
