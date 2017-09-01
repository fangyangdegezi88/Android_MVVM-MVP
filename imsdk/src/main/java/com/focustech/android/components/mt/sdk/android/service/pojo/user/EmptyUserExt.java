package com.focustech.android.components.mt.sdk.android.service.pojo.user;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by zhangxu on 2015/6/2.
 */
public class EmptyUserExt implements IUserExt {
    private static final String[] EMPTY = new String[0];
    private static final Map<String, String> EMPTY_MAP = new HashMap<>(0);

    @Override
    public Map<String, String> getEntry() {
        return EMPTY_MAP;
    }

    @Override
    public String[] getNames() {
        return EMPTY;
    }

    @Override
    public void create(Map<String, String> values) {

    }
}
