package org.simpleframework.aop;

import cn.chm.Controller.superadmin.HeadLineOperationController;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.simpleframework.core.BeanContainer;
import org.simpleframework.inject.DependencyInjector;

public class AspectWeaverTest {

    @Test
    @DisplayName("织入通用逻辑测试：doAop")
    public void doAopTest() {
        // 初始化容器
        BeanContainer beanContainer = BeanContainer.getInstance();
        // 加载Bean
        beanContainer.loadBeans("cn.chm");
        // Aop织入
        new AspectWeaver().doAop();
        // 依赖注入
        new DependencyInjector().doIoc();

        HeadLineOperationController controller =
                (HeadLineOperationController) beanContainer.getBean(HeadLineOperationController.class);

        controller.addHeadLine(null, null);
    }

}
