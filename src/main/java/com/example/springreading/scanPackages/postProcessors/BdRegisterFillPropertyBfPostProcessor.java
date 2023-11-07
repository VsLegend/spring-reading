package com.example.springreading.scanPackages.postProcessors;

import com.example.springreading.scanPackages.service.pp.AutoPostProcessorBeanServiceImpl;
import com.example.springreading.scanPackages.service.pp.PostProcessorBeanServiceImpl;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.MutablePropertyValues;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.BeanDefinitionRegistryPostProcessor;
import org.springframework.beans.factory.support.RootBeanDefinition;
import org.springframework.core.Ordered;
import org.springframework.stereotype.Component;

import static com.example.springreading.scanPackages.service.pp.PostProcessorBeanServiceImpl.BPPP_BEAN_NAME;

/**
 * 创建Bean定义然后填充字段（测试自动注册BFPP）
 *
 * @author Wang Junwei
 * @date 2023/10/20 11:42
 */
@Component
public class BdRegisterFillPropertyBfPostProcessor implements BeanDefinitionRegistryPostProcessor, Ordered {

    private final Log logger = LogFactory.getLog(getClass());

    @Override
    public void postProcessBeanDefinitionRegistry(BeanDefinitionRegistry registry) throws BeansException {
        if (!registry.containsBeanDefinition(BPPP_BEAN_NAME)) {
            RootBeanDefinition rbd = new RootBeanDefinition(AutoPostProcessorBeanServiceImpl.class);
            registry.registerBeanDefinition(BPPP_BEAN_NAME, rbd);
        }
    }

    @Override
    public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {
        String beanName = AutoPostProcessorBeanServiceImpl.BPPP_BEAN_NAME;
        String propertyName = "name";
        try {
            BeanDefinition bd = beanFactory.getBeanDefinition(beanName);
            MutablePropertyValues propertyValues = bd.getPropertyValues();
            if (!propertyValues.contains(propertyName)) {
                propertyValues.add(propertyName, "BeanFactoryPostProcessor-Create-Bean-Name");
            }
        } catch (NoSuchBeanDefinitionException ignore) {
            logger.error("Bean not found : " + beanName);
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
