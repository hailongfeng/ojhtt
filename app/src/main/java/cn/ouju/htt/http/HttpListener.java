package cn.ouju.htt.http;

/**
 * Created by Administrator on 2017/8/2.
 */

public interface HttpListener {
    /**
     * 访问网络成功
     *
     * @param objects 可变参数，object类型
     */
    void onNetWorkedSuccesses(Object... t);

    //访问网络失败
    void onNetWorkedFail(Object... strings);

    void showProgress();

    void hideProgress();

    void onDatabaseSuccess(Object... db);

    void onDatabaseFail();
}
