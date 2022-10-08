# spring bean 作用域
1. singleton
	1. 一个 beanFactory 有且仅有一个实例
	2. 主要通过 beanDefinition 中的 isSingleton ()
2. prototype
	1. 原型作用域，每次 DI 和 DL 注入都会生成新的 Bean 对象
	2. spring 没有办法管理其完整生命周期，也没有办法记录实例的存在。一旦 DI 或 DL 完成后就与 spring 脱钩，只有它的 beanDefinition。销毁方法不会执行，可以利用 BeanPostProcessor 进行清扫工作。
3. request
	1. 将 spring bean 存储在 servletRequest 的上下文中
4. session
	1. 将 spring bean 存储在 session 中
5. application
	1. 将 spring bean 存储在 servletContext 中

其中
1. singleton 和 prototype 可能同时存在
2. request、session、application 主要应用在服务端页面渲染中，如 jsp、模版引擎