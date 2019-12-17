package org.noear.perona;


import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

/** 通用路由器（基于表达式） */
public class XRouter<T> {
    private final Map<String,XRuleListener<T>> _routerList = new Hashtable<>();

    /** 添加路由 */

    public void add(Object obj,String path, T target) {
        String key = obj.hashCode()+"_"+path;
        String expr = path;

        //做开始预处理
        if (expr.startsWith("**") == false) {
            expr = "^" + expr;
        }

        //做结束预处理
        if (expr.endsWith("**")) {
            expr = expr.substring(0, expr.length() - 2); //不限内容
        } else {
            if (expr.endsWith("$") == false) { //结束不要加
                expr = expr + "$";
            }
        }

        //替换*值
        expr = expr.replaceAll("\\*", "[^/]+");

        _routerList.put(key,new XRuleListener(path, expr, target));
    }


    public void remove(Object obj, String expr) {
        String key = obj.hashCode() + "_" + expr;
        _routerList.remove(key);
    }

    /** 匹配目标 */
    public T match(String path) {
        for (XRuleListener<T> l : _routerList.values()) {
            if (l.matched(path)) {
                return l.target;
            }
        }

        return null;
    }

    public List<T> matches(String path) {
        List<T> list  =new ArrayList<>();

        for (XRuleListener<T> l : _routerList.values()) {
            if (l.matched(path)) {
                list.add(l.target);
            }
        }

        return list;
    }

    /** 通用监听者 */
    class XRuleListener<T> {
        public XRuleListener(String path, String expr, T target) {
            this._path = path;
            this._expr = expr;
            this._rule = Pattern.compile(expr, Pattern.CASE_INSENSITIVE);
            this.target = target;
        }

        private String _path;
        private String _expr;
        private Pattern _rule;
        public T target;

        public boolean matched(String path) {
            if(path==null){
                return false;
            }else {
                if("**".equals(_path)){
                    return true;
                }

                if(_path.equals(path)){
                    return true;
                }

                return _rule.matcher(path).find();
            }
        }
    }
}