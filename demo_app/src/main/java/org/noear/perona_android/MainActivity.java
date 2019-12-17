package org.noear.perona_android;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import org.noear.perona.XClient;
import org.noear.perona.XContext;
import org.noear.perona.protocol.XCallback;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        //调用功能（需要回调）
        XClient.call(this,"xapp://x/m1?a=1", null,new XCallback() {
            @Override
            public void handle(XContext c, Object o) throws Exception {
                if(o==null){
                    return;
                }
            }
        });


        //调用功能
        XClient.call(this,"xapp://x/m1", null);

        //发送消息
        XClient.send(this,"msg://t/m1", null);
    }
}
