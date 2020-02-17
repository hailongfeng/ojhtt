package cn.ouju.htt.http;

/**
 * Created by Administrator on 2017/9/8.
 */

public class BaseService {
    protected static Protocol openHttpProtocol() {
        return new HttpProtocol();
    }
}
