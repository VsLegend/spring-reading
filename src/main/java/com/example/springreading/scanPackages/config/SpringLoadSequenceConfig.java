package com.example.springreading.scanPackages.config;

import com.example.springreading.scanPackages.bean.multipleLoadSequence.MultipleOrderService;
import com.example.springreading.scanPackages.bean.multipleLoadSequence.OrderSwitch;
import com.example.springreading.scanPackages.bean.singleLoadSequence.BeanLifeCycle;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * spring加载顺序
 * 1. 单个Bean生命周期加载顺序
 * 2. 多个Bean各周期的加载顺序
 *
 * @author Wang Junwei
 * @since 2024/2/27 15:43
 */

@Configuration
public class SpringLoadSequenceConfig {

    /**
     * bean内部生命周期的调用顺序
     */
    @Bean(name = "BeanNamePopulate", initMethod = "init", destroyMethod = "destroy")
    public BeanLifeCycle beanLifeCycle() {
        return new BeanLifeCycle();
    }


    /**
     * 多个bean的调用顺序
     */
//    @Bean
    public OrderSwitch orderSwitch() {
        return new OrderSwitch();
    }

    @Bean
    @ConditionalOnBean(OrderSwitch.class)
    public MultipleOrderService service1() {
        return new MultipleOrderService(1);
    }

    @Bean
    @ConditionalOnBean(OrderSwitch.class)
    public MultipleOrderService service2() {
        return new MultipleOrderService(2);
    }

    @Bean
    @ConditionalOnBean(OrderSwitch.class)
    public MultipleOrderService service3() {
        return new MultipleOrderService(3);
    }
}
