package com.hquery.hrpc.export;

import com.hquery.hrpc.annotation.RegisterRpcServer;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;

/**
 * @author hquery.huang
 * 2019/3/25 17:33:17
 */
@Component
public class ExportServerScanner extends SomeTest {

//    @Nullable
//    @Override
//    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
//        Class<?> beanClass = bean.getClass();
//        RegisterRpcServer annotation = beanClass.getAnnotation(RegisterRpcServer.class);
//        if (annotation == null) {
//            return bean;
//        }
//        beanClass.getSuperclass();
//        return null;
//    }

    public static void main(String[] args) {
        ExportServerScanner exportServerScanner = new ExportServerScanner();
        Class<? extends ExportServerScanner> scannerClass = exportServerScanner.getClass();
//        System.out.println(scannerClass.getSuperclass().getName());
        Class<?> superclass = scannerClass.getSuperclass();
        Class<?>[] interfaces = scannerClass.getInterfaces();
        if (interfaces.length == 0 && superclass == null) {

        }
        for (Class<?> anInterface : interfaces) {
            System.out.println(anInterface.getName());
        }
    }

}
