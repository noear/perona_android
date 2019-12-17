package org.noear.perona;


import org.noear.perona.protocol.XCallback;

import java.io.Serializable;
import java.net.URI;
import java.net.URLDecoder;
import java.util.HashMap;
import java.util.Map;

/** 通用上下文 */
public class XContext implements Serializable {
    private static HashMap<String,XContext> contextState=new HashMap<>(); //仅安桌版特有

    //需要序列化
    private String __guid; //仅安桌版特有
    private String _originalUrl;
    private Map<String,Object> _paramMap;

    //不需要序列化
    private transient boolean _handled;
    private transient XCallback _callback;
    private transient Object _requester;
    private transient Map<String,Object> _attrMap;

    public XContext(Object requester, String url, Map<String,Object> params, XCallback callback) {
        __guid = XUtil.guid();
        _originalUrl = url;

        _paramMap = new HashMap<>();
        _attrMap = new HashMap<>();
        _requester = requester;
        _callback = callback;

        if(params!=null) {
            _paramMap.putAll(params);
        }

        //将queryString的参数进行整合
        String query = url().getRawQuery(); //不能用query()会被自动解码
        {
            String val = null;
            if (query != null) {
                String[] ss = query.split("&");
                for (String s : ss) {
                    String[] kv = s.split("=");
                    if (kv.length == 2) {
                        val = URLDecoder.decode(kv[1]);
                        if(val == null){
                            val = kv[1];
                        }

                        _paramMap.put(kv[0], val);
                    }
                }
            }
        }

        if(_paramMap.containsKey("callback")){
            _attrMap.put("callback", _paramMap.get("callback"));
        }

        contextState.put(__guid,this);
    }

    /** 是否已处理 */
    public void setHandled(boolean handled){
        contextState.get(__guid)._handled = handled;
    }
    public boolean getHandled(){
        return contextState.get(__guid)._handled;
    }

    /** 获取请求对象 */
    public Object requester(){
        return contextState.get(__guid)._requester;
    }

    ////////////////////////////////////////////

    /** 获取请求的URL源码字符串 */
    public String originalUrl(){
        return _originalUrl;
    }

    /** 获取请求的URL */
    private transient URI _url;
    public URI url(){
        if(_url==null) {
            _url = URI.create(this._originalUrl);
        }
        return _url;
    }

    /** 获取请求协议 */
    public String protocol(){
        return url().getScheme();
    }

    /** 获取请求的host */
    public String host(){
        return url().getHost();
    }

    /** 获取请求的path */
    public String path(){
        return url().getPath();
    }

    /** 获取请求的path分段 */
    public String[] pathSegments() {
        return path().substring(1).split("/");
//        String[] ary = path().split("/");
//        return Arrays.copyOfRange(ary, 1, ary.length);
    }

    /** 获取请求的fullpath */
    public String fullpath(){
        return (protocol() +"://" + host() + path()).toLowerCase();
    }

    /** 获取参数 */
    public Object param(String key){
        return _paramMap.get(key);
    }
    public Object param(String key, Object def){
        Object temp = _paramMap.get(key);

        if(key == null){
            return def;
        }else{
            return temp;
        }
    }
    /** 获取所有参数 */
    public Map<String,Object> paramMap(){
        return _paramMap;
    }

    /** 是否存在某个参数 */
    public boolean paramHas(String key){
        return _paramMap.containsKey(key);
    }

    /** 设置附加特性 */
    public void attrSet(String key, Object val) {
        XContext cxt = contextState.get(__guid);
        if (cxt != null) {
            cxt._attrMap.put(key, val);
        }
    }


    /** 获取附加特性 */
    public Object attrGet(String key){
        XContext cxt = contextState.get(__guid);
        if (cxt != null) {
            return cxt._attrMap.get(key);
        }else{
            return null;
        }
    }

    ///////////////////////////////////////

    /** 输出内容 */
    public void output(Object data) {
        try {
            XContext cxt = contextState.get(__guid);

            if (cxt != null) {
                XCallback _callback = cxt._callback;

                if (_callback != null) {
                    _callback.handle(cxt, data);
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    /** 销毁上下文(仅限内部操作) */
    public void destroy(){
        contextState.remove(__guid);
    }
}