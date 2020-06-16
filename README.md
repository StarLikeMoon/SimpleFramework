# 项目更新日志
- 更新了自研框架的IoC模块，实现了IoC容器，通过URL提取资源，利用注解和反射机制创建需要托管的Bean实例并加载进容器，通过注解进行依赖注入。
- 更新了自研框架的AOP模块，实现了通过注解进行横切逻辑的切入，AOP1.0参考Spring1.0的设计思想，没有引入AspectJ，仅使用CGLIB进行织入，AOP1.0仅支持注解级别的joinpoint横切。

# 项目架构说明
- src/main/java/cn目录下的类都是模拟的业务逻辑，编写的Dao层，Service层，Controller层模拟类。
- src/main/java/org目录下为框架IoC模块。
  - org/core目录下存放的BeanContainer类为框架的IoC容器，也是框架的核心入口。
   - org/core/annotation目录下存放的为Bean加载的自定义注解。
   - org/inject目录下存放的DependencyInjector类用来提供初始化容器和依赖注入功能。
   - org/inject/annotation目录下存放的是依赖注入的自定义注解——Autowired。
   - org/util目录下存放的是工具类。
      - 其中ClassUtil用来处理与类相关的逻辑，比如获取指定路径下的类集合，利用反射将目标类实例化等。
      - 其中ValidationUtil是一个判断工具，用来判断集合、数组等是否为空。
  - org/aop目录下存放的为框架AOP模块
- src/test目录下存放的是单元测试模块。
 
# 博客地址
- 本项目对应的讲解博客正在逐渐更新中，可以移步观看：[Spring框架源码脉络分析系列文章](https://blog.csdn.net/qq_42764725/article/details/106581454)
- 对Spring框架的学习总结与分析（正在更新）
- 对本自研框架的讲解（待更）
