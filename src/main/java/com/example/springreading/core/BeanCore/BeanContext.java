package com.example.springreading.core.BeanCore;

import com.example.springreading.core.SpringFramework;
import com.example.springreading.core.postProcessors.ExtraPropertyPostProcessor;
import com.example.springreading.service.BeanService;
import org.springframework.beans.factory.annotation.AnnotatedBeanDefinition;
import org.springframework.beans.factory.annotation.CustomAutowireConfigurer;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.config.*;
import org.springframework.beans.factory.support.*;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.*;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.core.annotation.AnnotationAttributes;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.core.metrics.StartupStep;
import org.springframework.core.type.classreading.MetadataReader;
import org.springframework.util.ClassUtils;

import java.beans.Introspector;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Bean的扫描、装配、注册、实例化流程
 *
 * @author Wang Junwei
 * @date 2023/10/18 10:32
 */
public class BeanContext extends AnnotationConfigApplicationContext {

    private final String[] basePackages = SpringFramework.basePackages;

    /**
     * Bean扫描、注册、实例化入口
     */
    public static void context() {
        // spring通过两种方式自动扫描和管理bean： 1.注解扫描  2.xml配置扫描
        // 1.注解扫描 @Component @Repository @Controller @Service等等Component注解
        AnnotationConfigApplicationContext annotationConfigApplicationContext = new AnnotationConfigApplicationContext(SpringFramework.basePackages);

        // 2.xml配置文件扫描 通过标签扫描
        ClassPathXmlApplicationContext classPathXmlApplicationContext = new ClassPathXmlApplicationContext("/xml/bean.xml");
    }

    public static void main(String[] args) throws IOException {
//        context();
        BeanContext beanContext = new BeanContext();
        beanContext.processOfScan();
        beanContext.instantiateBean();
    }


    /**
     * Bean的重要容器
     */
    public void beanContainer() {
        // 注册到IoC容器中的原始BeanDefinition
        final Map<String, BeanDefinition> beanDefinitionMap = new ConcurrentHashMap<>(256);
        // BeanDefinition注册后置处理器
        List<BeanFactoryPostProcessor> beanFactoryPostProcessors = new ArrayList<>();
        // BeanFactoryPostProcessor执行后，更新beanDefinitionMap内容的BeanDefinition容器
        final Map<String, RootBeanDefinition> mergedBeanDefinitions = new ConcurrentHashMap<>(256);
    }

    /**
     * 实例化对象（单例）
     * 原型对象会在每次调用时实例一个对象，所以这里不讨论原型Bean的实例化
     */
    public void instantiateBean() {
        // 从配置文件、xml、properties文件等持久化文件中，加载或刷新相关配置，如实例化对象
//        refresh();
        prepareRefresh();


        // 1. 准备阶段
        // 每次加载或刷新配置时，需要先清理所有单例对象。以确保所有对象都能被顺利的实例化
        // 2. 刷新
        // 当然AnnotationConfigApplicationContext内部的刷新代码没有任何清理操作，因为其内部仅维护了单例beanFactory，不需要对该容器进行清理
        ConfigurableListableBeanFactory beanFactory = obtainFreshBeanFactory();
        prepareBeanFactory(beanFactory);
//        ConfigurableListableBeanFactory beanFactory = getBeanFactory();
        // 有关清理的代码实现可以参考ClassPathXmlApplicationContext容器，它的清理逻辑在其父类AbstractRefreshableApplicationContext#refreshBeanFactory中实现
        // 其它流程暂不考虑：Bean的代理、包装、工厂、处理器、事情，特殊上下文处理等等

        // 3.Bean后置处理器
        // 为beanFactory设置注册后的后置处理器，做一些实例化前的预处理操作，比如修改BeanDefinition内容、新注册BeanDefinition等等
        StartupStep beanPostProcess = getApplicationStartup().start("spring.context.beans.post-process");
        postProcessBeanFactory(beanFactory);
        // 这些BeanFactoryPostProcessor处理器不由注解容器（AnnotationConfigApplicationContext）实例化，一般通过配置类容器（ConfigurableApplicationContext）进行实例化
        ConfigurableApplicationContext configurableApplicationContext;
        // 容器通过@Order注解来选择BeanFactoryPostProcessor的执行顺序，或实现org.springframework.core.Ordered类也可以
        // 我们通过手动添加的后置处理器，是根据添加顺序执行的
        List<BeanFactoryPostProcessor> beanFactoryPostProcessors = getBeanFactoryPostProcessors();
        // 4. 在容器中执行BeanFactoryPostProcessor
        // 允许BeanFactoryPostProcessor自定义修改应用程序上下文的bean定义，调整上下文的底层beanFactory中的bean属性值。
        // Spring内部实现了很多修改BeanDefinition的类：
        // - @Qualifier：CustomAutowireConfigurer
        // - 占位符替换：从property文件（yml）、环境变量、系统变量读取值，来替换Class文件代码中的${...}声明变量，例如处理@Value的内容，PropertySourcesPlaceholderConfigurer（5.2前PropertyPlaceholderConfigurer）
        CustomAutowireConfigurer customAutowireConfigurer = new CustomAutowireConfigurer();
        customAutowireConfigurer.setCustomQualifierTypes(Collections.singleton(Qualifier.class));
        addBeanFactoryPostProcessor(customAutowireConfigurer);
        addBeanFactoryPostProcessor(new PropertySourcesPlaceholderConfigurer());
        // BeanDefinitionRegistryPostProcessor将于BeanFactoryPostProcessor之前进行，它注册新的BeanDefinition或修改的BeanDefinition，最终可以影响到BeanFactoryPostProcessor的执行
//        BeanDefinitionRegistryPostProcessor beanDefinitionRegistryPostProcessor = null;
//        addBeanFactoryPostProcessor(beanDefinitionRegistryPostProcessor);
        // 5. 执行后处理器
        // PostProcessorRegistrationDelegate.invokeBeanFactoryPostProcessors(beanFactory, getBeanFactoryPostProcessors());
        invokeBeanFactoryPostProcessors(beanFactory);
        registerBeanPostProcessors(beanFactory);
        beanPostProcess.end();


        // 4. 实例化
        // 这个方法会实例化所有非懒加载的类（当然，如果一个懒加载类被一个非懒加载类依赖，那么该类依旧会在这里被实例化）
        finishBeanFactoryInitialization(beanFactory);
        // - 注入处理器：AutowiredAnnotationBeanPostProcessor


        // 5. 这里会将当前保存的beanDefinitionMap创建一个Key的副本，实例化时是对该副本中bean定义进行实例化
        beanFactory.freezeConfiguration();
        beanFactory.preInstantiateSingletons();
        // 4. 获取已注册的bean名字列表
        // beanFactory.beanDefinitionNames
        // 5. 通过beanName得到关联的bean定义BeanDefinition
        // RootBeanDefinition bd = getMergedLocalBeanDefinition(beanName);

        BeanService bean = getBean(BeanService.class);
        String name = bean.getName();
        BeanService constructorBeanService = getBean("constructorBeanService", BeanService.class);
        String name1 = constructorBeanService.getName();
    }

    /**
     * 包扫描过程，通过注解扫描（简化，保留关键步骤）
     */
    public void processOfScan() throws IOException {
        // reader通过类型信息注册BeanDefinition
        AnnotatedBeanDefinitionReader reader = new AnnotatedBeanDefinitionReader(this);
        // scanner通过扫描包注册BeanDefinition
        ClassPathBeanDefinitionScanner scanner = new ClassPathBeanDefinitionScanner(this);
        // 1. 扫描多个包下的类
        // 该方法内部执行了下面的所有操作
        int scan = scanner.scan(basePackages);


        // 2. 遍历所有包，依次加载包下面的所有bean对象
        // scanner.scanCandidateComponents && scanner.addCandidateComponentsFromIndex
        Set<BeanDefinition> candidates = scanner.findCandidateComponents(basePackages[0]);
        // 3. 加载每一个class资源
        // resource = scanner.getResourcePatternResolver().getResources(packageSearchPath)
        // String packageSearchPath = ResourcePatternResolver.CLASSPATH_ALL_URL_PREFIX + resolveBasePackage(basePackage) + '/' + this.resourcePattern;
        String packageSearchPath = "classpath*:" + "com/example/springreading/service" + "/" + "BeanServiceImpl.class";
        ResourcePatternResolver resourcePatternResolver = getResourcePatternResolver();
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
        AnnotatedBeanDefinition annDef = (AnnotatedBeanDefinition) beanDefinition;
        // AnnotationAttributes attributes = AnnotationConfigUtils.attributesFor(annDef.getMetadata(), Scope.class);
        // scopeMetadata.setScopeName(attributes.getString("value"));
        // scopeMetadata.setScopedProxyMode(attributes.getEnum("proxyMode"));
        // 7. 设置bean的实例名称：beanName
        // 通过Component和其子注解的value来获取bean名
        // @Component(value = "bean")
        BeanNameGenerator beanNameGenerator = AnnotationBeanNameGenerator.INSTANCE;
        String beanName = beanNameGenerator.generateBeanName(beanDefinition, this);
        // 注解value为空时，默认使用bean的类名，首字母小写
        String decapitalize = Introspector.decapitalize(ClassUtils.getShortName(Objects.requireNonNull(beanDefinition.getBeanClassName())));
        // 8. 加载bean通用注解
        // @Lazy Primary DependsOn Role Description
        AnnotationConfigUtils.processCommonDefinitionAnnotations(beanDefinition);


        // 9. 校验beanName是否已被注册
        // scanner.checkCandidate(beanName, beanDefinition)
        boolean containsBeanDefinition = containsBeanDefinition(beanName);
        // 内部执行
        // beanFactory内部维护了一个map容器存放bean定义：Map<String, BeanDefinition> beanDefinitionMap = new ConcurrentHashMap<>(256);
        getBeanFactory().containsBeanDefinition(beanName);
        // 10. 将bean交由BeanDefinitionHolder持有
        BeanDefinitionHolder definitionHolder = new BeanDefinitionHolder(beanDefinition, beanName);


        // 11. 将该bean注册registry
        BeanDefinitionReaderUtils.registerBeanDefinition(definitionHolder, this);
        // 注册
        registerBeanDefinition(beanName, beanDefinition);
        // beanFactory
        getDefaultListableBeanFactory().registerBeanDefinition(beanName, beanDefinition);
        // this.beanDefinitionMap.put(beanName, beanDefinition);

    }

}
