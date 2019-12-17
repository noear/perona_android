package org.noear.perona.protocol;

import org.noear.perona.XContext;

/** 通用代理 */
@FunctionalInterface
public interface XHandler {
    /** 处理 */
    void handle(XContext context) throws Exception;
}
