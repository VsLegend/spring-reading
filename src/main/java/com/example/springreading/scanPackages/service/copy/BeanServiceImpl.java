package com.example.springreading.scanPackages.service.copy;

import com.example.springreading.scanPackages.service.BeanService;
import org.springframework.context.annotation.*;
import org.springframework.stereotype.Component;

import javax.annotation.ManagedBean;
import javax.inject.Named;

/**
 * 通过类型获取Bean且有多个Bean的类型相同，注入的依赖优先选择@Primary注解注释的，都没约@Primary注解就会报错
 *
 * @author Wang Junwei
 * @date 2023/2/10 16:30
 * @see org.springframework.beans.factory.NoUniqueBeanDefinitionException
 */

@Component
public class BeanServiceImpl implements BeanService {

    private String name;

    public BeanServiceImpl() {
        name = BeanServiceImpl.class.getName();
    }

    public BeanServiceImpl(String name) {
        this.name = name;
    }


    @Override
    public String getName() {
        return name;
    }
}
