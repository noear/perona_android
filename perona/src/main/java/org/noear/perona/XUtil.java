package org.noear.perona;

import java.util.UUID;

/** 内部用工具 */
class XUtil {
    /** 生成UGID */
    public static String guid(){
        return UUID.randomUUID().toString().replaceAll("-","");
    }

    /** 检查字符串是否为空 */
    public static boolean isEmpty(String str) {
        return str == null || str.length() == 0;
    }

    /** 根据字符串生成一个类 */
    public static <T> T newClass(String classFullname) {
        try {
            Class cls = Class.forName(classFullname);
            return (T) cls.newInstance();
        }catch (Exception ex){
            ex.printStackTrace();
            return null;
        }
    }
}