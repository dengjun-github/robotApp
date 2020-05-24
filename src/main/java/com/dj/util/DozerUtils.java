package com.dj.util;

import com.github.dozermapper.core.DozerBeanMapperBuilder;
import com.github.dozermapper.core.Mapper;
import com.github.dozermapper.core.loader.api.BeanMappingBuilder;

public class DozerUtils {

    public static Mapper mapper;

    static{
        BeanMappingBuilder beanMappingBuilder = new BeanMappingBuilder() {
            @Override
            protected void configure() {
//                mapping(AddressDomain.class, AddressVo.class)
//                        .fields("detail", "detailAddr");

            }
        };

        mapper = DozerBeanMapperBuilder.create()
                //指定 dozer mapping 的配置文件(放到 resources 类路径下即可)，可添加多个 xml 文件，用逗号隔开
                .withMappingFiles("dozerBeanMapping.xml")
                .withMappingBuilder(beanMappingBuilder)
                .build();
    }
}
