package org.noear.perona;


import android.app.Application;
import android.content.res.AssetManager;

import org.noear.perona.protocol.XHandler;
import org.noear.perona.protocol.XModule;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

/** 通用应用（同时是个通用代理） */
public class XApp {
    private static XApp _global;

    /** 应用全局对象 */
    public static XApp global(){
        return _global;
    }

    /** 启动应用（全局只启动一个） */
    public static XApp start(Application application, XRouter<XHandler> router) {
        return start(application,router,null);
    }
    public static XApp start(Application application, XRouter<XHandler> router, Map<String,Object> params) {
        if (_global == null) {
            _global = new XApp(application, router, params);

            //加载组件
            try {
                _global.loadModules();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }

        return _global;
    }

    /////////////////////////////////////////////////////////////////////////////////

    /** 自动加载模块 */
    private Map<Class<?>, XModule> _modules = new HashMap<>();
    private void loadModules() throws Exception {
        final String dir = "solonboot";

        AssetManager am = _application.getAssets();
        String[] list = am.list(dir);

        for (String uri : list) {

            //加载组件配置
            try {
                Properties prop = new Properties();
                prop.load(am.open(dir + "/" + uri));

                String xmstr = prop.getProperty("solonboot.xmodule");

                if (XUtil.isEmpty(xmstr) == false) {
                    XModule module = XUtil.newClass(xmstr);
                    addModule(module);
                }
            }catch (Exception ex){
                ex.printStackTrace();
            }
        }
    }

    /** 路由器 */
    private XRouter<XHandler> _router;
    /** 当前应用 */
    private Application _application;
    /** 启动参数 */
    private Map<String,Object> _params;

    private XApp(Application application, XRouter<XHandler> router,Map<String,Object> params) {
        _router = router;
        _application = application;
        _params = new HashMap<>();

        if (params != null) {
            _params.putAll(params);
        }
    }

    /** 当前应用 */
    public Application application(){
        return _application;
    }

    /** 启动参数 */
    public boolean paramHas(String key){
        return _params.containsKey(key);
    }
    public Object paramGet(String key){
        return _params.get(key);
    }

    /** 手动添加模块 */
    public void addModule(XModule module) {
        if (module != null ) {
            Class<?> clz = module.getClass();
            if(_modules.containsKey(clz)==false) {
                module.start(this);
                _modules.put(module.getClass(), module);
            }
        }
    }

    public <T extends XModule> T getModule(Class<T> clz){
        return (T)_modules.get(clz);
    }

    /** 注册路由 */
    public void reg(Object obj,String expr, XHandler handler) {
        _router.add(obj,expr, handler);
    }

    /** 注销路由 */
    public void unreg(Object obj, String expr) {
        _router.remove(obj, expr);
    }

    /** 路由执行 */
    public boolean execute(XContext context ,boolean isMultiple) throws Exception {
        String fullpath = context.fullpath();
        boolean is_matched = false;

        if(isMultiple){
            for(XHandler handler : _router.matches(fullpath)){
                try {
                    do_execute(context, handler);
                }catch (Exception ex){
                    ex.printStackTrace();
                }
                is_matched = true;
            }
        }else{
            XHandler handler = _router.match(fullpath);
            do_execute(context,handler);
            is_matched = (handler!=null);
        }

        return is_matched;
    }

    private void do_execute(XContext context, XHandler handler) throws Exception{
        if (handler != null) {
            handler.handle(context);
            context.setHandled(true);
        }
    }
}