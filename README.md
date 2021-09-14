## 模块

- **app:** 

  **app壳** 工程，是依赖所有组件的壳，该模块不应该包含任何代码，它只作为一个空壳存在，由于项目中使用了EventBusAPT技术，需要索引到各业务组件的对应的APT生成类，所以在 **app壳** 内有这一部分的代码。


- **buildSrc:** 

  这是一个特殊的文件夹，负责项目的构建，里面存放着一些项目构建时用到的东西，比如项目配置，依赖。这里面还是存放 **Gradle** 插件的地方，一些自定义的 **Gradle** 的插件都需要放在此处。

- **lib_base:** 

  项目的基础公共模块，存放着各种基类封装、对远程库的依赖、以及工具类、三方库封装，该组件是和项目业务无关的，和项目业务相关的公共部分需要放在 **lib_common** 中。

- **lib_common:** 

  项目的业务公共模块，这里面存放着项目里各个业务组件的公共部分，还有一些项目特定需要的一些文件等，该组件是和项目业务有关系的。



  EventBus使用
  1、在Activity上添加@EventBusRegister注解才可以注册Eventbus
  2、添加注册方法@Subscribe(threadMode = ThreadMode.MAIN)
  3、发送事件EventBusUtils



