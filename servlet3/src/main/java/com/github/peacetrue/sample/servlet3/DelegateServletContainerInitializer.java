package com.github.peacetrue.sample.servlet3;

import lombok.extern.slf4j.Slf4j;

import javax.servlet.ServletContainerInitializer;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.HandlesTypes;
import java.util.Set;

/**
 * @author : xiayx
 * @since : 2021-06-27 05:02
 **/
@Slf4j
@HandlesTypes(CustomServletContainerInitializer.class)
public class DelegateServletContainerInitializer implements ServletContainerInitializer {

    @Override
    public void onStartup(Set<Class<?>> c, ServletContext ctx) throws ServletException {
        log.info("服务启动，委派{}执行", c);
        if (c == null) return;
        for (Class<?> aClass : c) {
            try {
                CustomServletContainerInitializer instance = (CustomServletContainerInitializer) aClass.newInstance();
                instance.onStartup(ctx);
            } catch (InstantiationException | IllegalAccessException e) {
                log.error("初始[{}]异常", aClass, e);
            }
        }
    }
}
