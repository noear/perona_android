package org.noear.demo_module2;


import org.noear.perona.XApp;
import org.noear.perona.XContext;
import org.noear.perona.protocol.XHandler;
import org.noear.perona.protocol.XModule;

public class XModuleImpl implements XModule {
    @Override
    public void start(XApp app) {

        app.application();
        app.paramGet("evn");

        app.reg(this,"xapp://x/m1", new XHandler() {
            @Override
            public void handle(XContext c) throws Exception {

                //c.param("a");
                c.output("m1");

                //c.destroy();
            }
        });

        app.reg(this, "kabao51://code/*", new XHandler() {
            @Override
            public void handle(XContext c) throws Exception {
                String code = c.path();
            }
        });

        //app.unreg(this,"xapp://x/m1");

        app.reg(this, "msg://x/*", new XHandler() {
            @Override
            public void handle(XContext c) throws Exception {
                String p = c.path();

                if("user".equals(p)){

                }

                if("order".equals(p)){

                }
            }
        });

        //XClient.call(this,"msg://x/user",null);
        //XClient.call(this,"msg://x/order",null);
    }
}