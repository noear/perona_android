# perona for android
perona是一个跨平台的服务启动框架。是服务端版本solon的情侣版，有android版本，有ios版本。<br/>
<br/>
在将客户端的作用是实现组件功能的服务化，进而为大项目的组件化开发，提供一套弱偶合的方案。比如像支付宝那种有N多组件组成的项目...<br/>
<br/>
基于客户端的需要，框架里也支持消息总线功能。用于组件之间的消息分发.<br/>
<br/>
最佳的效果：开发一个动态的框架性主项目，所有组件功能全在服务端进行配置。
<br/>

### perona使用说明

#### 1、相关项目添加引用

`implementation 'org.noear:perona:0.1.18' //暂时还不能用，未上传中央仓库：）`

#### 2、主项目启动服务
```java
import org.noear.perona.XApp;  //:不要太纠结大小写的问题：）

public class App extends Application {
    @Override
    public void onCreate() {
        super.onCreate();

        //可以初始化一些参数
        //
        Map<String, Object> args = new HashMap<>();
        args.put("env", 1);

        XApp.start(this,  args);
        //
        //...
        //
    }
}

```

#### 3、组件项目进行适配（例组件名：module2）
##### 3.1 适配XMoudle接口
```java
public class XModule2 implements XModule {
    @Override
    public void start(XApp app) {
        //app.application(); //此对象可用
        
        app.reg(this,"xapp://module2/m1", (c)->{
            //c.param("a");
            c.output("m1");
            c.destroy();//手动控制注销上下文（如果其它地方不需要了）
        });
    }
}
```
##### 3.2 添加配置文件 /assets/perona/mobule2.properties （完成组件自发现的配置）
`perona.xmodule=demo.module2.XModule2`

#### 4、主项目引入组module2（内部配置需要自己完成）
`compile project(':module2')`
或
`implementation 'demo.module2:0.x.x'`

#### 5 调用
##### 5.1 本地代码调用
```java
//调用功能
////不需要回调的
XClient.call(this,"xapp://module2/m1?a=1", null);
////需要回调的
XClient.call(this,"xapp://module2/m1?a=1", null,(c,data)->{
    if(data==null){
        return;
    }
});

//分发消息
XClient.send(this,"msg://topic/m1?a=1", null);
```
##### 5.2 内嵌WEB调用（需要适配一下WebView）
```html
<a href="xapp://module2/xxx?id=1">xxx</a>
```

