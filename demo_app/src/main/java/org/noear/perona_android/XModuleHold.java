package org.noear.perona_android;


import org.noear.perona.XApp;
import org.noear.perona.XContext;
import org.noear.perona.protocol.XHandler;
import org.noear.perona.protocol.XModule;

public class XModuleHold implements XModule {
    @Override
    public void start(XApp app) {
        app.reg(this, "kabao://hold/**", new XHandler() {
            @Override
            public void handle(XContext c) throws Exception {
                String path = c.path();

                switch (path){
                    case "/back":;
                }
            }
        });
    }
}