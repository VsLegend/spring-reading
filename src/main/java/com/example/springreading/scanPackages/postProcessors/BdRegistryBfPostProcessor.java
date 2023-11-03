package com.example.springreading.scanPackages.postProcessors;

import com.example.springreading.scanPackages.service.pp.PostProcessorBeanServiceImpl;
import org.springframework.beans.BeansException;
import org.springframework.beans.MutablePropertyValues;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.RootBeanDefinition;
import org.springframework.core.Ordered;
import org.springframework.core.PriorityOrdered;

import static com.example.springreading.scanPackages.service.pp.PostProcessorBeanServiceImpl.BPPP_BEAN_NAME;

/**
 * Bean定义注册
 *
 * @author Wang Junwei
 * @date 2023/10/20 11:42
 */
public class BdRegistryBfPostProcessor implements BeanFactoryPostProcessor, PriorityOrdered {

    @Override
    public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {
        if (beanFactory instanceof BeanDefinitionRegistry) {
            BeanDefinitionRegistry registry = (BeanDefinitionRegistry) beanFactory;
            if (!registry.containsBeanDefinition(BPPP_BEAN_NAME)) {
                RootBeanDefinition rbd = new RootBeanDefinition(PostProcessorBeanServiceImpl.class);
                registry.registerBeanDefinition(BPPP_BEAN_NAME, rbd);
            }
        }
    }

    @Override
    public int getOrder() {
        /**
         * 最低优先级
         */
        return Ordered.LOWEST_PRECEDENCE;
    }
}
