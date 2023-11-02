package com.example.springreading.core.BeanCore;

import com.example.springreading.core.SpringFramework;
import com.example.springreading.core.beanFactory.SubDefaultListableBeanFactory;
import com.example.springreading.scanPackages.service.BeanService;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.beans.factory.config.*;
import org.springframework.beans.factory.support.*;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.*;
import org.springframework.context.support.AbstractApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.core.metrics.StartupStep;
import org.springframework.core.type.classreading.MetadataReader;
import org.springframework.util.ClassUtils;
import org.springframework.util.ObjectUtils;

import java.beans.Introspector;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Bean的扫描、装配、注册、前置处理、实例化流程
 *
 * @author Wang Junwei
 * @date 2023/10/18 10:32
 */
public class BeanContext extends AnnotationConfigApplicationContext {

    private final Log logger = LogFactory.getLog(getClass());
    private final String[] basePackages = SpringFramework.basePackages;

    private final SubDefaultListableBeanFactory beanFactory;

    public BeanContext() {
        beanFactory = new SubDefaultListableBeanFactory(getDefaultListableBeanFactory());
    }

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
        // 完整的容器执行流程
//        context();
        BeanContext beanContext = new BeanContext();
        // 扫描流程
        beanContext.processOfScan();
        // 完整的实例化流程
//        beanContext.refresh();
        // 容器上下文实例化流程
        beanContext.instantiateBean1();
        // IoC容器内部实例化流程
        beanContext.instantiateBean2();
    }


    /**
     * Bean的重要容器
     */
    public void beanContainer() {
        // 注册到IoC容器中的原始BeanDefinition
        final Map<String, BeanDefinition> beanDefinitionMap = new ConcurrentHashMap<>(256);
        // BeanDefinition注册后处理器
        List<BeanFactoryPostProcessor> beanFactoryPostProcessors = new ArrayList<>();
        // BeanDefinition合并层级关系，将父子类实例分开，这里只保存最顶层的BeanDefinition，实例化时也是从最顶层开始实例化
        final Map<String, RootBeanDefinition> mergedBeanDefinitions = new ConcurrentHashMap<>(256);

        final Set<String> registeredSingletons = new LinkedHashSet<>(256);

        // Names of beans that are currently in creation.
        final Set<String> singletonsCurrentlyInCreation = Collections.newSetFromMap(new ConcurrentHashMap<>(16));
        // Names of beans currently excluded from in creation checks.
        final Set<String> inCreationCheckExclusions = Collections.newSetFromMap(new ConcurrentHashMap<>(16));
    }

    /**
     * DefaultListableBeanFactory Bean实例化流程处理
     */
    public void instantiateBean2() {
        String name = "beanService";
        Object[] args = null;



        // 1. 实例化核心代码：getBean(beanName)
        // getBean() 核心逻辑 doGetBean()
        Object doGetBean = beanFactory.doGetBean(name, null, null, false);


        // 2. 获取规范的Bean名称，把可能是工厂对象的名称、或对象别名名称转为真正的BeanName（@Component-value）
        String beanName = beanFactory.transformedBeanName(name);


        // 3. 通过缓存获取实例
        // 三级缓存
        // 存放我们注册合并好的Bean定义
        final Map<String, RootBeanDefinition> mergedBeanDefinitions = new ConcurrentHashMap<>(256);
        // Bean实例缓存（已实例化）Cache of singleton objects: bean name to bean instance.
        final Map<String, Object> singletonObjects = new ConcurrentHashMap<>(256);
        // Bean对象工厂实例缓存（已实例化）Cache of singleton factories: bean name to ObjectFactory.
        final Map<String, ObjectFactory<?>> singletonFactories = new HashMap<>(16);
        // Bean正在实例化缓存（实例化中）Cache of early singleton objects: bean name to bean instance.
        final Map<String, Object> earlySingletonObjects = new ConcurrentHashMap<>(16);
        // 为什么国内都叫三级缓存呢？因为所有doGetBean()方法获取Bean实例前，会尝试从这个三个缓存中获取Bean实例，如果能获取到就直接返回该实例了
        // 三级缓存为什么能一定程度解决循环依赖问题呢？
        boolean allowEarlyReference = true;
        Object bean = beanFactory.getSingleton(beanName);
        bean = beanFactory.getSingleton(beanName, allowEarlyReference);
        // 4. 一级缓存
        // 后续的实例化过程中，有很多双检机制，作用都是寻求线程安全-效率的平衡点
        Object singletonObject = singletonObjects.get(beanName);
        if (singletonObject == null && beanFactory.isSingletonCurrentlyInCreation(beanName)) {
            // 5. 二级缓存
            singletonObject = earlySingletonObjects.get(beanName);
            if (singletonObject == null && allowEarlyReference) {
                // 加锁，避免并发问题
                synchronized (singletonObjects) {
                    // 一级缓存的二次查询
                    singletonObject = singletonObjects.get(beanName);
                    if (singletonObject == null) {
                        // 二级缓存的二次查询
                        singletonObject = earlySingletonObjects.get(beanName);
                        if (singletonObject == null) {
                            // 6. 三级缓存
                            ObjectFactory<?> singletonFactory = singletonFactories.get(beanName);
                            if (singletonFactory != null) {
                                singletonObject = singletonFactory.getObject();
                                // 通过三级缓存获取实例后，要将去同步到二级缓存中
                                earlySingletonObjects.put(beanName, singletonObject);
                                singletonFactories.remove(beanName);
                            }
                        }
                    }
                }
            }
        }


        // 7. 从父级容器获取对象实例
        // 在实例化自身容器内的Bean定义前，如果当前IoC容器不存在该BeanName时，会尝试在父容器中获取该对象实例。存在时就直接返回父类中的对象实例了。
        // 父类容器获取Bean实例的流程与当前流程一致
        BeanFactory parentBeanFactory = getParentBeanFactory();
        if (parentBeanFactory != null && !beanFactory.containsBeanDefinition(beanName)) {
            bean = parentBeanFactory.getBean(beanName);
        }

        // 8. 先实例化依赖的组件（@DependsOn）
        // 要注意一下，如果两个或多个类之间，依赖的组件形成了一个循环，那么就会产生循环依赖问题。BeanCreationException
        // 与属性依赖不同，组件之间的循环依赖是无法解决的，所以需要注意@DependsOn的设置是否构成了一个循环
        RootBeanDefinition mbd = beanFactory.getMergedLocalBeanDefinition(beanName);
        String[] dependsOn = mbd.getDependsOn();
        if (!ObjectUtils.isEmpty(dependsOn)) {
            for (String dep : dependsOn) {
                // 为实例化对象注册依赖项
                beanFactory.registerDependentBean(dep, beanName);
                // 然后再实例化该依赖对象
                getBean(dep);
            }
        }

        // 9. 创建实例
        // 创建单例模式和原型模式的实例不同
        // 单例模式：mbd.isSingleton()
        // 这里为什么要定义一个对象工厂，而不是直接创建该对象的实例呢？
        // 1. 实现懒加载，使用对象工厂封装真正的实例化方法，可以延迟创建Bean实例，只有查找或获取该Bean时，才会通过工厂对象执行实例化方法
        // 2. 一定程度解决循环依赖，我们提到的三级缓存的第三级就是保存的这个对象工厂，由于可以延迟创建Bean实例，使用依赖的双方不必一开始就实例化
        ObjectFactory<Object> singletonFactory = () -> {
            try {
                return beanFactory.createBean(beanName, mbd, args);
            } catch (BeansException ex) {
                //
                beanFactory.destroySingleton(beanName);
                throw ex;
            }
        };
        bean = beanFactory.getSingleton(beanName, singletonFactory);


        // 锁住缓存，避免多线程并发问题
        // beanFactory#createBean
        synchronized (singletonObjects) {
            // 10. 一级缓存二次获取
            bean = singletonObjects.get(beanName);
            // 11. 缓存仍然没有的话，就通过工厂创建实例
            // 这期间将会维护两个缓存对象
            // Bean正在实例化中（实例化中） Names of beans that are currently in creation.
            // beforeSingletonCreation(beanName);
            bean = singletonFactory.getObject();
            // 12. createBean方法
            // beanFactory.createBean(beanName, mbd, null);
            bean = beanFactory.resolveBeforeInstantiation(beanName, mbd);
            Class<?> targetType = mbd.getTargetType();
            // 13. 执行后处理器InstantiationAwareBeanPostProcessor#postProcessBeforeInstantiation（BeanPostProcessor）
            // 执行后处理器InstantiationAwareBeanPostProcessor#postProcessAfterInitialization（BeanPostProcessor）
            // 这里允许我们进行扩展，比如创建Bean的代理实例或在此直接返回一个实例对象。如果BeanPostProcessor返回了非null的bean实例（直接实例、代理实例），那么就会直接返回该实例
            if (targetType != null) {
//                bean = beanFactory.applyBeanPostProcessorsBeforeInstantiation(targetType, beanName);
                if (bean != null) {
                    bean = beanFactory.applyBeanPostProcessorsAfterInitialization(bean, beanName);
                }
            }


            //  14. IoC容器自己创建实例 doCreateBean
            bean = beanFactory.doCreateBean(beanName, mbd, args);
            // 包装类
            BeanWrapper instanceWrapper = beanFactory.createBeanInstance(beanName, mbd, args);
            bean = instanceWrapper.getWrappedInstance();
            Class<?> beanType = instanceWrapper.getWrappedClass();
            // 15. 执行后处理器MergedBeanDefinitionPostProcessor#postProcessMergedBeanDefinition（BeanPostProcessor）
            // beanFactory.getBeanPostProcessorCache().mergedDefinition
            MergedBeanDefinitionPostProcessor processor = new CommonAnnotationBeanPostProcessor();
            processor.postProcessMergedBeanDefinition(mbd, beanType, beanName);
            // 16. 初始化bean
            beanFactory.populateBean(beanName, mbd, instanceWrapper);
            Object exposedObject = beanFactory.initializeBean(beanName, bean, mbd);
            // 初始化代码将会先后执行后处理器BeanPostProcessor的两个方法
            // BeanPostProcessor#postProcessBeforeInitialization
            // BeanPostProcessor#postProcessAfterInitialization


            // afterSingletonCreation(beanName);
            // 17. 最后，将创建完的实例添加进缓存容器中，并删除相关的中间缓存
            beanFactory.addSingleton(beanName, singletonObject);

        }


        // 原型模式：mbd.isPrototype()

    }

    /**
     * AnnotationConfigApplicationContext的Bean实例化整体流程，以及BeanFactoryPostProcessor、BeanPostProcessor后处理器
     * 原型对象会在每次调用时实例一个对象，所以这里不讨论原型Bean的实例化
     * Bean的生命周期：初始化 实例化
     *
     * @see AbstractApplicationContext#refresh()
     */
    public void instantiateBean1() {
        // 从配置文件、xml、properties文件等持久化文件中，加载或刷新相关配置，如实例化对象
        // 1. 准备阶段
        // 每次加载或刷新配置时，需要刷新配置，以确保所有对象都能被顺利完整的实例化
        prepareRefresh();
        // 2. 刷新
        // 当然AnnotationConfigApplicationContext内部的刷新代码没有任何清理操作，因为该应用上下文仅允许执行一次刷新和实例化操作，并且其内部仅维护了单例beanFactory，不需要对该容器进行清理
        // 有关清理的代码实现可以参考ClassPathXmlApplicationContext容器，它的清理逻辑在其父类AbstractRefreshableApplicationContext#refreshBeanFactory中实现
        ConfigurableListableBeanFactory beanFactory = obtainFreshBeanFactory();
        // 为IoC容器配置标准特征，比如ClassLoader和默认后置处理器。
        prepareBeanFactory(beanFactory);
        // 这里实际上是应用上下文AnnotationConfigApplicationContext，在初始化beanFactory之后，对beanFactory作的一些后置处理
        postProcessBeanFactory(beanFactory);

        // 注册与执行后处理器
        StartupStep beanPostProcess = getApplicationStartup().start("spring.context.beans.post-process");


        // 3.注册与执行beanFactory容器后处理器（BeanFactoryPostProcessor）
        // 初始化完成后，为beanFactory设置后处理器，做一些注册后实例化前的预处理操作，比如修改BeanDefinition内容、新注册BeanDefinition等等
        // 这些BeanFactoryPostProcessor处理器一般通过配置类容器（ConfigurableApplicationContext）进行实例化
        ConfigurableApplicationContext configurableApplicationContext;
        List<BeanFactoryPostProcessor> beanFactoryPostProcessors = getBeanFactoryPostProcessors();
        // 4. 注册
        // 允许BeanFactoryPostProcessor增加或修改应用程序上下文的bean定义，甚至是调整beanFactory容器中bean的属性值。
        // Spring内部BeanFactoryPostProcessor的实现类例子：
        // - CustomAutowireConfigurer：设置注入Bean的指定限定符（官方限定符注解@Qualifier）
        // - PropertySourcesPlaceholderConfigurer（5.2前PropertyPlaceholderConfigurer）：占位符替换，从property文件（yml）、环境变量、系统变量读取值，来替换Class文件代码中的${...}声明变量，例如处理@Value的内容
        CustomAutowireConfigurer customAutowireConfigurer = new CustomAutowireConfigurer();
        customAutowireConfigurer.setCustomQualifierTypes(Collections.singleton(Qualifier.class));
        addBeanFactoryPostProcessor(customAutowireConfigurer);
        addBeanFactoryPostProcessor(new PropertySourcesPlaceholderConfigurer());
        // BeanDefinitionRegistryPostProcessor将于BeanFactoryPostProcessor之前进行，它注册新的BeanDefinition或修改的BeanDefinition，最终可以影响到BeanFactoryPostProcessor的执行
        // - ConfigurationClassPostProcessor：处理@Configuration注解标注的类，并将其设置为配置类
        BeanDefinitionRegistryPostProcessor beanDefinitionRegistryPostProcessor = new ConfigurationClassPostProcessor();
        addBeanFactoryPostProcessor(beanDefinitionRegistryPostProcessor);
        // 5. 执行
        invokeBeanFactoryPostProcessors(beanFactory);
        // BeanFactoryPostProcessor后处理器执行的先后顺序为：
        // - BeanDefinitionRegistryPostProcessors：1.实现PriorityOrdered的处理器、2.实现Ordered的处理器、3.剩余处理器
        // - BeanFactoryPostProcessors：4.实现PriorityOrdered的处理器、5.实现Ordered的处理器、6.剩余处理器
        // 6. 清理缓存
        // BeanFactory后处理器执行完毕后，BeanDefinition的元数据有被修改的可能，因此需要清理容器中缓存的相关Bean定义
        beanFactory.clearMetadataCache();


        // 7. 注册Bean后处理器（BeanPostProcessor）
        // BeanFactoryPostProcessor能影响BeanDefinition，而BeanPostProcessor则能够影响BeanDefinition的实例化过程
        registerBeanPostProcessors(beanFactory);
        // BeanPostProcessor与普通BeanDefinition初始化方式是相同的，beanFactory容器将会自动检测BeanPostProcessor类型的单例类，并将其注册到容器中
        String[] postProcessorNames = beanFactory.getBeanNamesForType(BeanPostProcessor.class, true, false);
        List<BeanPostProcessor> priorityOrderedPostProcessors = new ArrayList<>();
        // 8. BeanPostProcessor注册
        // BeanPostProcessor有两个方法：1. postProcessBeforeInitialization用于实例化前，如检查标记接口  2. postProcessAfterInitialization用于实例化后，如代理包装
        // - ApplicationListenerDetector：为应用上下文注册事件监听器
        // - AutowiredAnnotationBeanPostProcessor：注入处理器，一般用来处理被@Autowired、@Value、@Inject注解的类，同时支持自定义注解
        BeanPostProcessor beanPostProcessor = new CommonAnnotationBeanPostProcessor();

        // BeanPostProcessor后处理器执行的先后顺序为：
        // - BeanPostProcessor：1.实现PriorityOrdered的处理器、2.实现Ordered的处理器、3.剩余处理器
        // - MergedBeanDefinitionPostProcessor：4. MergedBeanDefinitionPostProcessor类型的处理器
        beanFactory.addBeanPostProcessor(beanPostProcessor);
        // 9. 将上面处理器注册完成后，最后再注册一个后处理器ApplicationListenerDetector
        // 该处理器用来检测ApplicationListener类型的Bean，在该Bean实例化完成后，将其添加到应用上下文（ApplicationContext）中的事件监听器（ApplicationListener）链中
        // 监听器这一节基本上与Bean实例化无关，监听器相关类后续再说
        // beanFactory.addBeanPostProcessor(new ApplicationListenerDetector(this));
        // 注意这里仅注册，相关的BeanPostProcessor类还没有实例化，也没法执行。


        // 结束后处理器操作
        beanPostProcess.end();


        // 10. 实例化
        // 这个方法会实例化所有非懒加载的类（当然，如果一个懒加载类被一个非懒加载类依赖，那么该类依旧会在这里被实例化）
        finishBeanFactoryInitialization(beanFactory);
        // 11. 冻结所有bean定义，已经注册的BeanDefinition不会被修改或处理
        beanFactory.freezeConfiguration();
        // 12. 执行Bean实例化方法
        beanFactory.preInstantiateSingletons();
        // 13. 获取已注册的bean名字列表
        // beanFactory.beanDefinitionNames
        String[] beanNames = beanFactory.getBeanDefinitionNames();
        // 14. 通过beanName得到关联的bean定义，同时将有层级关系的BeanDefinition合并为一个RootBeanDefinition
        // 注意：通过直接扫描Java文件的Bean定义没有父子层级的说法，所有Bean定义都将被转为RootBeanDefinition
        // 因为在Java文件中，解析后元数据（metadata）记录的层机关系很明确（extend、implement），实例化时不需要再根据Bean定义的层级关系来关联父类，它直接从元数据中拿就行
        // 这一点与xml解析的Bean稍有不同，xml中有类似这种的标签来确定父类信息，<bean id="BeanName" class="Class" parent="parentBeanName">
        for (String beanName : beanNames) {
            // 这里的BeanDefinition是我们注册进容器的类：ScannedGenericBeanDefinition
            BeanDefinition beanDefinition = beanFactory.getBeanDefinition(beanName);
            // 这里构造器访问限制为包默认 RootBeanDefinition(BeanDefinition original)
            RootBeanDefinition mbd = new RootBeanDefinition("beanDefinition");
            // 创建完成后放进IoC容器的合并定义map中
            BeanDefinition bd = beanFactory.getMergedBeanDefinition(beanName);
            // 合并完成后，直接实例化
            if (!bd.isAbstract() && bd.isSingleton() && !bd.isLazyInit()) {
                // 有两种实例化方式，一个是通过对象工厂，一种是直接实例化
                if (beanFactory.isFactoryBean(beanName)) {
                    // 工厂模式实例化，需要先实例化这个工厂对象（FactoryBean），然后将实例化Bean的工作交给该工厂来完成。
                    Object bean = getBean(FACTORY_BEAN_PREFIX + beanName);
                    // IoC容器不参与该Bean的实例化，除非这个工厂对象急切完成初始化，或期望初始化它的单例对象
                    boolean isEagerInit = ((SmartFactoryBean<?>) bean).isEagerInit();
                    if (isEagerInit) {
                        getBean(beanName);
                    }
                } else {
                    // 获取实例化对象
                    getBean(beanName);
                }
            }
        }


        // 收尾工作
        finishRefresh();

        BeanService bean = getBean(BeanService.class);
        BeanService constructorBeanService = getBean("constructorBeanService", BeanService.class);
        logger.debug("Get Bean by type: " + bean.getName());
        logger.debug("Get Bean by name with type: " + constructorBeanService.getName());

    }

    /**
     * IoC容器初始化：包扫描过程，通过注解扫描，然后注册至容器
     *
     * @see AnnotationConfigApplicationContext#scan(String...)
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
        String packageSearchPath = "classpath*:" + "com/example/springreading/scanPackages/service" + "/" + "BeanServiceImpl.class";
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
        // 读取注解元数据
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
