package com.example.springreading.scanPackages.bean.multipleLoadSequence;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;

/**
 * @author Wang Junwei
 * @since 2024/2/27 14:42
 */
@Slf4j
public class MultipleOrderService implements InitializingBean, DisposableBean {

    private final int order;

    public MultipleOrderService(int order) {
        this.order = order;
    }


    public void call() {
        log.info("调用服务");
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        log.info("业务服务{}初始化……", order);
    }

    @Override
    public void destroy() throws Exception {
        log.info("业务服务{}销毁……", order);
    }
}
