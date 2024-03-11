package hello.core.beanfind;

import hello.core.AppConfig;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class ApplicationContextInfoTest {
    AnnotationConfigApplicationContext ac = new AnnotationConfigApplicationContext(AppConfig.class);

    @Test
    @DisplayName("모든 빈 출력하기")
    void findAllBean() {
        String[] beanDefinitionNames = ac.getBeanDefinitionNames();
        for (String beanDefName : beanDefinitionNames) {
            Object bean = ac.getBean(beanDefName);
            System.out.println("BeanDefName: " + beanDefName + " Bean: " + bean);

        }
    }

    @Test
    @DisplayName("애플리케이션 빈 출력하기")
    void findAppBean() {
        String[] beanDefinitionNames = ac.getBeanDefinitionNames();
        for (String beanDefName : beanDefinitionNames) {
            BeanDefinition beanDefinition = ac.getBeanDefinition(beanDefName);
            if (beanDefinition.getRole() == BeanDefinition.ROLE_APPLICATION) {
                Object bean = ac.getBean(beanDefName);
                System.out.println("BeanDefName: " + beanDefName + " Bean: " + bean);
            }
        }
    }

}
