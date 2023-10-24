package com.example.springreading.scanPackages.service.Merge;

import com.example.springreading.scanPackages.service.BeanService;
import org.springframework.stereotype.Component;

/**
 * @author Wang Junwei
 * @date 2023/10/24 9:32
 */
@Component("parentService")
public class ParentServiceImpl implements BeanService {

    private final String name;

    public ParentServiceImpl() {
        name = "Parent";
    }


    @Override
    public String getName() {
        return name;
    }
}
