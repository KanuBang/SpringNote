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
        String[] beanDefNames = ac.getBeanDefinitionNames();
        for(String beanDefName : beanDefNames) {
            Object bean = ac.getBean(beanDefName);
            System.out.println("name: " + beanDefName + " object: " + bean);
        }
    }

    @Test
    @DisplayName("애플리케이션 빈 출력하기")
    void findApplicationBean() {
        String[] beanDefNames = ac.getBeanDefinitionNames();
        for(String beanDefName : beanDefNames) {
            BeanDefinition beanDefinition = ac.getBeanDefinition(beanDefName);
            if (beanDefinition.getRole() == BeanDefinition.ROLE_APPLICATION) {
                System.out.println("name: " + beanDefName + " role: " + beanDefinition.getRole());
            }
            else if(beanDefinition.getRole() == BeanDefinition.ROLE_INFRASTRUCTURE) {
                System.out.println("name: " + beanDefName + " role: " + beanDefinition.getRole());
            } else {
                System.out.println("name: " + beanDefName + " role: " + beanDefinition.getRole());
            }


        }
    }

}
