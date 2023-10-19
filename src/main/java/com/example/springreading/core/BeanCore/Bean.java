package com.example.springreading.core.BeanCore;

import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.BeanDefinitionHolder;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.*;
import org.springframework.context.annotation.*;
import org.springframework.context.support.AbstractApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.core.type.classreading.MetadataReader;
import org.springframework.util.ClassUtils;

import java.beans.Introspector;
import java.io.IOException;
import java.util.Objects;
import java.util.Set;

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
     * 实例化对象（单例）
     * 原型对象会在每次调用时实例一个对象，所以这里不讨论原型Bean的实例化
     * @param registry
     */
    public void instantiateBean(AnnotationConfigApplicationContext registry) {

        // 从配置文件、xml、properties文件等持久化文件中，加载或刷新相关配置，如实例化对象
        registry.refresh();


        // 1. 准备阶段
        // 每次加载或刷新配置时，需要先清理所有单例对象。以确保所有对象都能被顺利的实例化
        // 2. 刷新
        // 当然AnnotationConfigApplicationContext内部的刷新代码没有任何清理操作，因为其内部仅维护了单例beanFactory，不需要对该容器进行清理
        // ConfigurableListableBeanFactory beanFactory = obtainFreshBeanFactory();
        // GenericApplicationContext.beanFactory
        ConfigurableListableBeanFactory beanFactory = new DefaultListableBeanFactory();
        // 有关清理的代码实现可以参考ClassPathXmlApplicationContext容器，它的清理逻辑在其父类AbstractRefreshableApplicationContext#refreshBeanFactory中实现
        // 其它流程暂不考虑：Bean的代理、包装、工厂、处理器、事情，特殊上下文处理等等


        // 3. 实例化
        // registry.finishBeanFactoryInitialization(beanFactory)
        // 这里仅实例化非延迟加载的类（当然，如果一个懒加载类被一个非懒加载类依赖，那么该类依旧会在这里被实例化）

        beanFactory.preInstantiateSingletons();
    }

    /**
     * 包扫描过程，通过注解扫描（简化，保留关键步骤）
     *
     * @param registry
     */
    public void processOfScan(AnnotationConfigApplicationContext registry, String[] basePackages) throws IOException {
        // reader是手动将类注册为Bean对象
        AnnotatedBeanDefinitionReader reader = new AnnotatedBeanDefinitionReader(registry);
        // scanner则是批量扫描类，然后将其注册为Bean对象
        ClassPathBeanDefinitionScanner scanner = new ClassPathBeanDefinitionScanner(registry);
        // 1. 扫描多个包下的类
        int scan = scanner.scan(basePackages);


        // 2. 遍历所有包，依次加载包下面的所有bean对象
        // scanner.scanCandidateComponents && scanner.addCandidateComponentsFromIndex
        Set<BeanDefinition> candidates = scanner.findCandidateComponents(basePackages[0]);
        // 3. 加载每一个class资源
        // resource = scanner.getResourcePatternResolver().getResources(packageSearchPath)
        // String packageSearchPath = ResourcePatternResolver.CLASSPATH_ALL_URL_PREFIX + resolveBasePackage(basePackage) + '/' + this.resourcePattern;
        String packageSearchPath = "classpath*:" + "com/example/springreading/service" + "/" + "BusinessServiceImpl.class";
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
        // containsBeanDefinition(beanName)
        // beanFactory内部维护了一个map容器存放bean定义：Map<String, BeanDefinition> beanDefinitionMap = new ConcurrentHashMap<>(256);
        DefaultListableBeanFactory beanFactory = new DefaultListableBeanFactory();
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
