package com.example.springreading.scanPackages.service.Merge;

import com.example.springreading.scanPackages.service.BeanService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.DependsOn;
import org.springframework.stereotype.Component;

/**
 * @author Wang Junwei
 * @date 2023/10/24 9:32
 */
@DependsOn(value = "parentService")
@Component("childService")
public class ChildServiceImpl extends ParentServiceImpl {

    private final String name;

    public ChildServiceImpl() {
        name = "Child";
    }


    @Override
    public String getName() {
        return name + super.getName();
    }
}
