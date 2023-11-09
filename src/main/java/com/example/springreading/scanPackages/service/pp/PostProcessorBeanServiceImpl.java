package com.example.springreading.scanPackages.service.pp;

import com.example.springreading.scanPackages.service.BeanService;
import org.springframework.stereotype.Component;

/**
 * 用BFPP修改Bean定义
 *
 *
 * @author Wang Junwei
 * @date 2023/2/10 16:30
 */
@Component(value = PostProcessorBeanServiceImpl.BEAN_NAME)
public class PostProcessorBeanServiceImpl implements BeanService {

    public static final String BEAN_NAME = "ppBeanService";

    private String name;

    public PostProcessorBeanServiceImpl() {
    }


    @Override
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
