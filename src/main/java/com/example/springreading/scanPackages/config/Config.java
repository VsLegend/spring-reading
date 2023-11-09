package com.example.springreading.scanPackages.config;

import com.example.springreading.scanPackages.postProcessors.ConfigInjectPostProcessor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 配置类
 *
 * @author Wang Junwei
 * @date 2023/11/2 17:37
 */
@Configuration
public class Config {
    /**
     * 注册Bean
     * <p>
     * 因为BeanFactoryPostProcessor（BFPP）对象必须在容器生命周期的早期进行实例化，
     * 所以它们可能会干扰@Configuration类中的@Autowired、@Value和@PostConstruct等注释的处理。
     * 为了避免这些生命周期问题，将返回bfpp的@Bean方法标记为静态。
     *
     * @return
     * @see Bean
     */
    @Bean
    public static ConfigInjectPostProcessor configInjectPostProcessor() {
        return new ConfigInjectPostProcessor();
    }
}
