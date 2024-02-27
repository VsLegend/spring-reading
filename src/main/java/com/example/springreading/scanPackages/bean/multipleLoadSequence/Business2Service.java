package com.example.springreading.scanPackages.bean.multipleLoadSequence;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;

/**
 * @author Wang Junwei
 * @since 2024/2/27 14:42
 */
@Slf4j
public class Business2Service implements InitializingBean, DisposableBean {

    public void call() {
        log.info("调用服务");
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        log.info("业务服务2初始化……");
    }
    @Override
    public void destroy() throws Exception {
        log.info("业务服务2销毁……");
    }
}
