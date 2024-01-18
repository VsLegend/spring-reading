package com.example.springreading.scanPackages.bfpp;

import com.example.springreading.scanPackages.service.pp.PostProcessorBeanServiceImpl;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.MutablePropertyValues;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.core.Ordered;
import org.springframework.core.PriorityOrdered;
import org.springframework.stereotype.Component;

/**
 * 填充字段（优先级测试）
 *
 * @author Wang Junwei
 * @date 2023/10/20 11:42
 */
@Component
public class PriorityFillPropertyBfpp implements BeanFactoryPostProcessor, PriorityOrdered {

    private final Log logger = LogFactory.getLog(getClass());

    @Override
    public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {
        String beanName = PostProcessorBeanServiceImpl.BEAN_NAME;
        String propertyName = "name";
        try {
            BeanDefinition bd = beanFactory.getBeanDefinition(beanName);
            MutablePropertyValues propertyValues = bd.getPropertyValues();
            if (!propertyValues.contains(propertyName)) {
                propertyValues.add(propertyName, "PriorityOrdered-BeanFactoryPostProcessor");
            }
        } catch (NoSuchBeanDefinitionException ignore) {
            logger.error("Bean not found : " + beanName);
        }
    }

    @Override
    public int getOrder() {
        return Ordered.LOWEST_PRECEDENCE;
    }
}
