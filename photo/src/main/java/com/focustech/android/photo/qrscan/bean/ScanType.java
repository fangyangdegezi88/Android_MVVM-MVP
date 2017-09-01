package com.focustech.android.photo.qrscan.bean;

/**
 * 扫码业务类型
 *
 * @author : yincaiyu
 * @version : [v1.0.0, 2017/3/28]
 * @see [相关类/方法]
 * @since [V1]
 */
public interface ScanType {
    /**
     * 非本校的，无意义
     */
    int NULL = 0;
    /**
     * 纯url跳转   ----> 直接打开一个本地或者系统浏览器的url网页
     */
    int URL = 1;

    /**
     * 扫码签到
     */
    int CONFERENCE_SIGN = 2;

    /**
     * 扫码打印
     */
    int DOWNLOAD = 3;

    int SCHOOL_BRAND_BIND_USER = 4;//校牌绑定用户

    //----化工园code，从100往后排----
    /**
     * 文件查看
     */
    int CLOUD_OPEN_FILE = 100;
    /**
     * 文件分享
     */
    int CLOUD_SHARE_FILE = 101;
    //----下面 班牌特有，从500往后排----
    int BRAND_WITHOUT_NET = 500;//班牌端没有网络
    int BRAND_SCAN_VALUE = 501;//班牌端返回桌面---其实就是密码
    int BRAND_END_MEETING = 502;//结束会议
}
