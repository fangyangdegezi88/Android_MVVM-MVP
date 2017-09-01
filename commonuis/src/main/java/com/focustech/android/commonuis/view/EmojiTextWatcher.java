package com.focustech.android.commonuis.view;

import android.text.Editable;
import android.text.Selection;
import android.text.TextWatcher;
import android.widget.EditText;

import com.focustech.android.commonlibs.util.EmojiFilter;

import java.lang.ref.WeakReference;

/**
 * <实现过滤表情的TextWatcher>
 *
 * @author yanguozhu
 * @version [版本号, 2016/7/6]
 * @see [相关类/方法]
 * @since [V1]
 */
public class EmojiTextWatcher implements TextWatcher {

    /**
     * EditText的软引用
     */
    WeakReference<EditText> mRef;

    /**
     * 回调
     */
    CallBack mCallBack;

    /**
     * 输入前的长度
     */
    private int beforeTextLength = 0;
    /**
     * 文字的长度
     */
    private int onTextLength = 0;
    /**
     * 正在发生改变
     */
    private boolean changing = false;

    /**
     * 匹配出来的url
     */
    private StringBuffer buffer = new StringBuffer();

    public EmojiTextWatcher(EditText et) {
        mRef = new WeakReference<EditText>(et);
    }

    public EmojiTextWatcher(EditText et, CallBack mCallBack) {
        mRef = new WeakReference<EditText>(et);
        this.mCallBack = mCallBack;
    }

    @Override
    public void beforeTextChanged(CharSequence text, int start, int count, int after) {
        beforeTextLength = text.length();
        if (buffer.length() > 0) {
            buffer.delete(0, buffer.length());
        }
    }

    @Override
    public void onTextChanged(CharSequence text, int start, int before, int count) {
        onTextLength = text.length();
        buffer.append(text.toString());

        if (onTextLength == beforeTextLength || changing) {
            changing = false;
            return;
        }

        changing = true;
    }

    @Override
    public void afterTextChanged(Editable s) {
        if (changing) {
            EditText et = mRef.get();
            int start = et.getSelectionStart();
            for (int i = 0; i < s.length(); i++) {
                if (EmojiFilter.isEmojiCharacter(s.charAt(i))) {
                    String noEmojiStr = EmojiFilter.filterEmoji(s.toString());
                    start = start - (s.length() - noEmojiStr.length());
                    et.setText(noEmojiStr);
                    break;
                }
            }
            /**
             * 这里需要重新getText一次，不然selection会设置到先前的Editable上了，那就没效了
             */
            Editable etable = et.getText();
            Selection.setSelection(etable, start);
            if (null != mCallBack) {
                mCallBack.afterTextChanged(s.length());
            }
            changing = false;
        }
    }


    /**
     * afterTextChanged之后的回调
     */
    public interface CallBack {
        /**
         * @param length 输入框文字的长度
         */
        public void afterTextChanged(int length);
    }
}
