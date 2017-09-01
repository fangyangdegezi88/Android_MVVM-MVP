package com.focustech.android.photo.qrscan;

import android.app.Activity;

import com.journeyapps.barcodescanner.BarcodeResult;
import com.journeyapps.barcodescanner.CaptureManager;
import com.journeyapps.barcodescanner.DecoratedBarcodeView;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 由zzy封装的 扫码管理类--因为扫码界面涉及具体的ui，和具体的http  交互逻辑细节  这里只存放manager
 *
 * @author : yincaiyu
 * @version : [v1.0.0, 2017/6/28]
 * @see [相关类/方法]
 * @since [V1]
 */
public class MyCaptureManager extends CaptureManager {

    private OnReadStateListener readStateListener;

    private final Logger logger = LoggerFactory.getLogger(getClass());

    public MyCaptureManager(Activity activity, DecoratedBarcodeView barcodeView) {
        super(activity, barcodeView);
    }

    public void setReadStateListener(OnReadStateListener readStateListener) {
        this.readStateListener = readStateListener;
    }

    @Override
    protected void returnResult(final BarcodeResult rawResult) {
        String result = rawResult.toString();

        if (logger.isInfoEnabled()) {
            logger.info("QRCode read success:" + result);
        }

        if (result.isEmpty()) {
            readStateListener.onFailed();
            return;
        }
        readStateListener.onGetScanInfo(result);
    }

    /**
     * 本地扫码结果监听
     */
    public interface OnReadStateListener {
        /**
         * 扫码失败
         * */
        void onFailed();

        /**
         * 扫码获取的信息
         * */
        void onGetScanInfo(String result);

    }
}
