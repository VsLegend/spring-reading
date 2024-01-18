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
import org.springframework.stereotype.Component;

/**
 * 填充字段（优先级测试）
 *
 * @author Wang Junwei
 * @date 2023/10/20 11:42
 */
@Component
public class FillPropertyBfpp implements BeanFactoryPostProcessor, Ordered {

    private final Log logger = LogFactory.getLog(getClass());

    @Override
    public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {
        String beanName = PostProcessorBeanServiceImpl.BEAN_NAME;
        String propertyName = "name";
        try {
            BeanDefinition bd = beanFactory.getBeanDefinition(beanName);
            MutablePropertyValues propertyValues = bd.getPropertyValues();
            // 这里直接覆盖高于当前优先级的BFPP
            propertyValues.add(propertyName, "Ordered-BeanFactoryPostProcessor");
        } catch (NoSuchBeanDefinitionException ignore) {
            logger.error("Bean not found : " + beanName);
        }
    }

    @Override
    public int getOrder() {
        return Ordered.LOWEST_PRECEDENCE;
    }
}
