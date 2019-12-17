package org.noear.demo_module2;


import org.noear.perona.XApp;
import org.noear.perona.XContext;
import org.noear.perona.XRouter;
import org.noear.perona.protocol.XHandler;
import org.noear.perona.protocol.XModule;

public class XModuleImplEx implements XModule, XHandler {
    XRouter _routerList  =new XRouter();

    @Override
    public void start(XApp app) {
        app.reg(this, "xapp://module2/**", this);



        //自建二级路由（只能paas部分进行路由）
        _routerList.add(this, "/xxx/x1", new XHandler() {
            @Override
            public void handle(XContext xContext) throws Exception {

            }
        });

        _routerList.add(this, "/xxx/x2", new XHandler() {
            @Override
            public void handle(XContext xContext) throws Exception {

            }
        });
    }

    @Override
    public void handle(XContext xContext) throws Exception {

        String path = xContext.path(); //    /user

        XHandler handler = _routerList.match(path);

        if(handler!=null){
            handler.handle(xContext);
        }
    }
}
