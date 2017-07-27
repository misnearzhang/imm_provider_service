package com.misnearzhang.grpc.config.autoconfigure;

import com.google.common.collect.Lists;
import com.misnearzhang.grpc.config.annotation.GRpcClient;
import io.grpc.ClientInterceptor;
import io.grpc.ManagedChannel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.aop.framework.Advised;
import org.springframework.aop.support.AopUtils;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanCreationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class GRpcClientBeanPostProcessor implements BeanPostProcessor {
  private static final Logger log = LoggerFactory.getLogger(GRpcClientBeanPostProcessor.class);
  private Map<String, List<Class>> beansToProcess = new HashMap();
  @Autowired
  private DefaultListableBeanFactory beanFactory;
  @Autowired
  private GRpcChannelFactory channelFactory;

  @Autowired
  public GRpcClientBeanPostProcessor() {
    log.debug("GRpcClientBeanPostProcessor");
  }

  public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
    Class clazz = bean.getClass();

    do {
      Field[] var4 = clazz.getDeclaredFields();
      int var5 = var4.length;

      for(int var6 = 0; var6 < var5; ++var6) {
        Field field = var4[var6];
        if(field.isAnnotationPresent(GRpcClient.class)) {
          if(!this.beansToProcess.containsKey(beanName)) {
            this.beansToProcess.put(beanName, new ArrayList());
          }

          ((List)this.beansToProcess.get(beanName)).add(clazz);
        }
      }

      clazz = clazz.getSuperclass();
    } while(clazz != null);

    return bean;
  }

  public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
    if(this.beansToProcess.containsKey(beanName)) {
      Object target = null;
      try {
        target = this.getTargetBean(bean);
      } catch (Exception e) {
        e.printStackTrace();
      }
      Iterator var4 = ((List)this.beansToProcess.get(beanName)).iterator();

      while(var4.hasNext()) {
        Class clazz = (Class)var4.next();
        Field[] var6 = clazz.getDeclaredFields();
        int var7 = var6.length;

        for(int var8 = 0; var8 < var7; ++var8) {
          Field field = var6[var8];
          GRpcClient annotation = (GRpcClient) AnnotationUtils.getAnnotation(field, GRpcClient.class);
          if(null != annotation) {
            List<ClientInterceptor> list = Lists.newArrayList();
            Class[] var12 = annotation.interceptors();
            int var13 = var12.length;

            for(int var14 = 0; var14 < var13; ++var14) {
              Class<? extends ClientInterceptor> clientInterceptorClass = var12[var14];
              ClientInterceptor clientInterceptor;
              if(this.beanFactory.getBeanNamesForType(ClientInterceptor.class).length > 0) {
                clientInterceptor = (ClientInterceptor)this.beanFactory.getBean(clientInterceptorClass);
              } else {
                try {
                  clientInterceptor = (ClientInterceptor)clientInterceptorClass.newInstance();
                } catch (IllegalAccessException | InstantiationException var18) {
                  throw new BeanCreationException("Failed to create interceptor instance", var18);
                }
              }

              list.add(clientInterceptor);
            }

            ManagedChannel channel = this.channelFactory.createChannel(annotation.value(), list);
            ReflectionUtils.makeAccessible(field);
            ReflectionUtils.setField(field, target, channel);
          }
        }
      }
    }

    return bean;
  }

  private Object getTargetBean(Object bean) throws Exception {
    try {
      Object target;
      for(target = bean; AopUtils.isAopProxy(target); target = ((Advised)target).getTargetSource().getTarget()) {
        ;
      }

      return target;
    } catch (Throwable var3) {
      throw var3;
    }
  }
}

