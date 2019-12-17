package org.noear.perona;


import org.noear.perona.protocol.XCallback;

import java.util.Map;

/** 通用客户端 */
public class XClient {

    /**
     * 调用（用于功能调用）
     */
    public static boolean call(Object requester, String url, Map<String, Object> paramS) {
        return do_handle(requester, url, paramS, null, false);
    }

    public static boolean call(Object requester, String url, Map<String, Object> paramS, XCallback callback) {
        return do_handle(requester, url, paramS, callback, false);
    }

    /**
     * 发送（用于消息广播）
     */
    public static boolean send(Object requester, String url, Map<String, Object> paramS) {
        return do_handle(requester, url, paramS, null, true);
    }

    private static boolean do_handle(Object requester, String url, Map<String, Object> paramS, XCallback callback, boolean isMultiple) {
        //1.生成上下文（会自动在全局状态器里存在；用完后要销毁）
        XContext ctx = new XContext(requester, url, paramS, callback);

        try {
            //2.尝试执行
            return XApp.global().execute(ctx, isMultiple);
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return false;
    }
}
