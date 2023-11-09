package com.example.springreading.scanPackages.postProcessors;

import com.example.springreading.scanPackages.service.pp.ConfigBeanServiceImpl;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.MutablePropertyValues;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.BeanDefinitionRegistryPostProcessor;
import org.springframework.beans.factory.support.RootBeanDefinition;
import org.springframework.context.annotation.ConfigurationClassPostProcessor;
import org.springframework.core.Ordered;
import org.springframework.core.PriorityOrdered;

import static com.example.springreading.scanPackages.service.pp.PostProcessorBeanServiceImpl.BEAN_NAME;

/**
 * 设置的优先级高于ConfigurationClassPostProcessor，
 * 但是又通过ConfigurationClassPostProcessor注册当前BFPP时
 *
 * @author Wang Junwei
 * @date 2023/10/20 11:42
 * @see ConfigurationClassPostProcessor
 */
public class ConfigInjectPostProcessor implements BeanDefinitionRegistryPostProcessor, PriorityOrdered {

    private final Log logger = LogFactory.getLog(getClass());

    @Override
    public void postProcessBeanDefinitionRegistry(BeanDefinitionRegistry registry) throws BeansException {
        String beanName = ConfigBeanServiceImpl.BEAN_NAME;
        if (!registry.containsBeanDefinition(beanName)) {
            RootBeanDefinition rbd = new RootBeanDefinition(ConfigBeanServiceImpl.class);
            registry.registerBeanDefinition(beanName, rbd);
        }
    }

    @Override
    public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {
        String beanName = ConfigBeanServiceImpl.BEAN_NAME;
        String propertyName = "name";
        try {
            BeanDefinition bd = beanFactory.getBeanDefinition(beanName);
            MutablePropertyValues propertyValues = bd.getPropertyValues();
            if (!propertyValues.contains(propertyName)) {
                propertyValues.add(propertyName, "PriorityOrdered-Before-Config-BeanFactoryPostProcessor");
            }
        } catch (NoSuchBeanDefinitionException ignore) {
            logger.error("Bean not found : " + beanName);
        }
    }

    @Override
    public int getOrder() {
        // 设置的优先级高于ConfigurationClassPostProcessor
        return Ordered.HIGHEST_PRECEDENCE;
    }
}
