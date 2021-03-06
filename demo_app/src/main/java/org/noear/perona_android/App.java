package org.noear.perona_android;


import android.app.Application;

import org.noear.perona.XApp;
import org.noear.perona.XClient;
import org.noear.perona.XContext;
import org.noear.perona.protocol.XCallback;

import java.util.HashMap;
import java.util.Map;

public class App extends Application {
    @Override
    public void onCreate() {
        super.onCreate();

        //可以初始化一些参数
        //
        Map<String, Object> args = new HashMap<>();
        args.put("env", 1);

        XApp.start(this,  args);

        XApp.global().addModule(new XModuleKbx());

        XClient.call(this, "kbx://code/121?mobile=xx&ugroupId=xx&callback=xxxx", null, new XCallback() {
            @Override
            public void handle(XContext c, Object o) throws Exception {
                Object callback = c.param("callback");

                if (callback != null) {
                    //webview.exec("callback({xxxx})");
                }
            }
        });
    }
}
