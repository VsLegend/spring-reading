package com.example.springreading.core;

import org.springframework.beans.factory.annotation.AnnotatedBeanDefinition;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.BeanDefinitionHolder;
import org.springframework.beans.factory.support.BeanDefinitionReaderUtils;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.BeanNameGenerator;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.context.annotation.*;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.core.annotation.AnnotationAttributes;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.PathResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.core.type.classreading.MetadataReader;
import org.springframework.util.ClassUtils;

import java.beans.Introspector;
import java.io.IOException;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Bean的扫描、装配、注册、实例化流程
 *
 * @author Wang Junwei
 * @date 2023/10/18 10:32
 */
public class Bean {

    public static void main(String[] args) throws IOException {
        Bean bean = new Bean();
        bean.scan();
    }

    public void scan() throws IOException {
        String[] basePackages = new String[]{"com.example.springreading"};
        // spring通过两种方式自动扫描和管理bean： 1.注解扫描  2.xml配置扫描
        // 1.注解扫描 @Component @Repository @Controller @Service等等Component注解
        AnnotationConfigApplicationContext annotationConfigApplicationContext = new AnnotationConfigApplicationContext(basePackages);
        // 2.xml配置文件扫描 通过标签扫描
        ClassPathXmlApplicationContext classPathXmlApplicationContext = new ClassPathXmlApplicationContext("/xml/bean.xml");
        processOfScan(annotationConfigApplicationContext, basePackages);
    }

    /**
     * 包扫描过程，通过注解扫描（简化，保留关键步骤）
     *
     * @param registry
     */
    public void processOfScan(AnnotationConfigApplicationContext registry, String[] basePackages) throws IOException {
        ClassPathBeanDefinitionScanner scanner = new ClassPathBeanDefinitionScanner(registry);
        // 1. 扫描多个包下的类
        int scan = scanner.scan(basePackages);


        // 2. 遍历所有包，依次加载包下面的所有bean对象
        // scanner.scanCandidateComponents && scanner.addCandidateComponentsFromIndex
        Set<BeanDefinition> candidates = scanner.findCandidateComponents(basePackages[0]);
        // 3. 加载每一个class资源
        // resource = scanner.getResourcePatternResolver().getResources(packageSearchPath)
        String packageSearchPath = "classpath*:" + "com/example/springreading/service/BusinessServiceImpl.class";
        ResourcePatternResolver resourcePatternResolver = new PathMatchingResourcePatternResolver();
        Resource[] resources = resourcePatternResolver.getResources(packageSearchPath);
        MetadataReader metadataReader = scanner.getMetadataReaderFactory().getMetadataReader(resources[0]);
        // 4. 将class文件包装为BeanDefinition，完成对Bean的加载
        ScannedGenericBeanDefinition beanDefinition = new ScannedGenericBeanDefinition(metadataReader);


        // 5. 装配bean
        // scanner.doScan(basePackages)
        // 6. 作用域解析，通过解析注解内容，非注解注入类或无注解时为默认配置
        // @Scope(value = "singleton", proxyMode = ScopedProxyMode.DEFAULT)
        // scanner.scopeMetadataResolver.resolveScopeMetadata(candidate)
        ScopeMetadataResolver scopeMetadataResolver = new AnnotationScopeMetadataResolver();
        ScopeMetadata scopeMetadata = scopeMetadataResolver.resolveScopeMetadata(beanDefinition);
        // 读取注解
//        AnnotatedBeanDefinition annDef = (AnnotatedBeanDefinition) beanDefinition;
//        AnnotationAttributes attributes = AnnotationConfigUtils.attributesFor(annDef.getMetadata(), Scope.class);
//        scopeMetadata.setScopeName(attributes.getString("value"));
//        scopeMetadata.setScopedProxyMode(attributes.getEnum("proxyMode"));
        // 7. 设置bean的实例名称：beanName
        // 通过Component和其子注解的value来获取bean名
        // @Component(value = "bean")
        BeanNameGenerator beanNameGenerator = AnnotationBeanNameGenerator.INSTANCE;
        String beanName = beanNameGenerator.generateBeanName(beanDefinition, registry);
        // 注解value为空时，默认使用bean的类名，首字母小写
        String decapitalize = Introspector.decapitalize(ClassUtils.getShortName(Objects.requireNonNull(beanDefinition.getBeanClassName())));
        // 8. 加载bean通用注解
        // @Lazy Primary DependsOn Role Description
        AnnotationConfigUtils.processCommonDefinitionAnnotations(beanDefinition);


        // 9. 校验beanName是否已被注册
        // scanner.checkCandidate(beanName, beanDefinition)
        boolean containsBeanDefinition = registry.containsBeanDefinition(beanName);
        // 内部执行
        registry.getBeanFactory().containsBeanDefinition(beanName);
        // registry.getBeanFactory()
        DefaultListableBeanFactory beanFactory = new DefaultListableBeanFactory();
        // containsBeanDefinition(beanName)
        Map<String, BeanDefinition> beanDefinitionMap = new ConcurrentHashMap<>(256);
        // 10. 将bean交由BeanDefinitionHolder持有
        BeanDefinitionHolder definitionHolder = new BeanDefinitionHolder(beanDefinition, beanName);


        // 11. 将该bean注册registry
        BeanDefinitionReaderUtils.registerBeanDefinition(definitionHolder, registry);
        // 注册
        registry.registerBeanDefinition(beanName, beanDefinition);
        // beanFactory
        beanFactory.registerBeanDefinition(beanName, beanDefinition);
        // this.beanDefinitionMap.put(beanName, beanDefinition);

    }

}
