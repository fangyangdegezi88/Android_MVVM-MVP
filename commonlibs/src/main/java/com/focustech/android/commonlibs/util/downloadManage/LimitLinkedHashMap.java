package com.focustech.android.commonlibs.util.downloadManage;

import java.util.LinkedHashMap;

/**
 * <维护一个固定长度的并且有序的HashMap>
 *
 * @author : liuzaibing
 * @version : [v1.0.0, 2016/7/27]
 * @see [相关类/方法]
 * @since [V1]
 */
public class LimitLinkedHashMap<K, V> extends LinkedHashMap<K, V> {

    /**
     * HashMap最多存放100个数据
     */
    private int MAX_LENGTH = 100;

    public LimitLinkedHashMap() {
        super();
    }

    public LimitLinkedHashMap(int max) {
        super();
        MAX_LENGTH = max;
    }

    /**
     * 删除最老的对象
     *
     * @param eldest
     * @return
     */
    @Override
    protected boolean removeEldestEntry(Entry eldest) {
        return size() > MAX_LENGTH;
    }
}
