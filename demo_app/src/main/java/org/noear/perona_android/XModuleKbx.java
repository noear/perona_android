package org.noear.perona_android;


import org.noear.perona.XApp;
import org.noear.perona.XContext;
import org.noear.perona.protocol.XHandler;
import org.noear.perona.protocol.XModule;

public class XModuleKbx implements XModule {
    @Override
    public void start(XApp app) {
        app.reg(this, "kbx://code/*", new XHandler() {
            @Override
            public void handle(XContext c) throws Exception {
                c.output(c.path());
            }
        });

        app.reg(this, "package://code/*", new XHandler() {
            @Override
            public void handle(XContext c) throws Exception {
                c.output(c.url());
            }
        });
    }
}
