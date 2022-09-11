package org.example.di;

import org.example.annotation.Controller;
import org.example.annotation.Service;
import org.example.controller.UserController;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.reflections.Reflections;

import java.lang.annotation.Annotation;
import java.util.HashSet;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class BeanFactoryTest {
    private Reflections reflections;
    private BeanFactory beanFactory;

    @BeforeEach
    void setUp()// 테스트 메소드 호출전 세팅하는 메소드
    {
        reflections = new Reflections("org.example");
        // userController, UserService --> return
        Set<Class<?>> preInstantiatedClasses = // 탑 다운 방식 :
                // 정의되지 않은 파라미터를 추가한 후 정의를 하는 방식으로 코딩
        getTyoeAnnotaionWith(Controller.class, Service.class);
        beanFactory = new BeanFactory(preInstantiatedClasses);
    }

    private Set<Class<?>> getTyoeAnnotaionWith(Class<? extends Annotation>... anotations) {
        Set<Class<?>> beans = new HashSet<>();
        for (Class<? extends Annotation> temp : anotations) {
            beans.addAll(reflections.getTypesAnnotatedWith(temp));
        } // 에노테이션을 받아 해당 에노테이션 객체를 조회(컨트롤러 클래스 타입 객체, 서비스 클래스 타입 객체)
        return beans;
    }

    @Test
    void diTest() {
        UserController userController =  beanFactory.getBean(UserController.class);
        assertThat(userController).isNotNull();
        assertThat(userController.getUserService()).isNotNull();
    }
}