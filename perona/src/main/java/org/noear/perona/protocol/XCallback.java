package org.noear.perona.protocol;

import org.noear.perona.XContext;

/** 通用回调 */
@FunctionalInterface
public interface XCallback {
    /** 处理 */
    void handle(XContext context, Object data) throws Exception;
}
