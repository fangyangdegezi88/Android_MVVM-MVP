package com.focustech.android.commonuis.view.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.focustech.android.commonlibs.util.GeneralUtils;
import com.focustech.android.commonuis.R;

import org.w3c.dom.Text;


/**
 * <权限提示dialog>
 *
 * @author yanguozhu
 * @version [版本号, 2016/11/8]
 * @see [相关类/方法]
 * @since [V1]
 */

public class SFPermissionDialog extends Dialog implements View.OnClickListener {

    /**
     * 下一步
     */
    private Button mNxtBtn;

    private TextView title, content;

    private String titleTxt, contentTxt;

    private PermissionDialogListener listener;

    public SFPermissionDialog(Context context) {
        super(context, R.style.SFDialog);
        setCancelable(false);
        setCanceledOnTouchOutside(false);
        if (context instanceof PermissionDialogListener)
            listener = (PermissionDialogListener) context;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_sf_permission_alert);
        title = (TextView) findViewById(R.id.per_dialog_title);
        title.setText(GeneralUtils.isNullToEmpty(titleTxt));
        content = (TextView) findViewById(R.id.per_dialog_msg);
        content.setText(GeneralUtils.isNullToEmpty(contentTxt));
        mNxtBtn = (Button) findViewById(R.id.per_dialog_ok);
        mNxtBtn.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (null != listener) {
            listener.nextBtnClick();
            dismiss();
        }
    }

    public SFPermissionDialog setTitle(String app_name) {
        titleTxt = app_name;
        if (null != title)
            title.setText(app_name);
        return this;
    }

    public SFPermissionDialog setContent(String msg) {
        contentTxt = msg;
        if (null != content)
            content.setText(msg);
        return this;
    }

    public void setListener(PermissionDialogListener listener) {
        this.listener = listener;
    }

    /**
     * 权限提醒
     */
    public interface PermissionDialogListener {
        /**
         * 下一步按钮监听
         */
        void nextBtnClick();
    }

}
