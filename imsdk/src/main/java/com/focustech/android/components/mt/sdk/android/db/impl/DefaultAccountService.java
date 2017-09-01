package com.focustech.android.components.mt.sdk.android.db.impl;

import com.focustech.android.components.mt.sdk.android.db.DBHelper;
import com.focustech.android.components.mt.sdk.android.db.IAccountService;
import com.focustech.android.components.mt.sdk.android.db.gen.Account;
import com.focustech.android.components.mt.sdk.android.db.gen.AccountDao;
import com.focustech.android.components.mt.sdk.android.service.SessionManager;
import com.focustech.android.components.mt.sdk.util.ReflectionUtil;

/**
 * 默认实现
 *
 * @author zhangxu
 */
public class DefaultAccountService implements IAccountService {
    private static AccountDao dao = DBHelper.getInstance().getAccountDao();

    private static final IAccountService INSTANCE = new DefaultAccountService();

    public static IAccountService getInstance() {
        return INSTANCE;
    }

    @Override
    public boolean exist(String userId) {
        return getAccount(userId) != null;
    }

    @Override
    public void delete(String userId) {
        dao.deleteByKey(userId);
    }

    @Override
    public void clean() {
        dao.deleteAll();
    }

    @Override
    public long add(Account account) {
        account.setLastAction(ACTION_EMPTY);
        return dao.insert(account);
    }

    @Override
    public boolean updateSignature(String newSignature) {
        String userId = SessionManager.getInstance().getUserId();

        if (null != userId) {
            Account account = getAccount(userId);
            if (null != account) {
                account.setUserSignature(newSignature);
                account.setLastAction(ACTION_EMPTY);
                dao.update(account);
                return true;
            }
        }

        return false;
    }

    @Override
    public boolean updateNickName(String newNickName) {
        // TODO 修改
        String userId = SessionManager.getInstance().getUserId();

        if (null != userId) {
            Account account = getAccount(userId);
            if (null != account) {
                account.setUserNickName(newNickName);
                account.setLastAction(ACTION_EMPTY);
                dao.update(account);
                return true;
            }
        }

        return false;
    }

    @Override
    public boolean updateHead(long userHeadType, String userHeadId) {
        String userId = SessionManager.getInstance().getUserId();

        if (null != userId) {
            Account account = getAccount(userId);
            if (null != account) {
                account.setUserHeadType(userHeadType);
                account.setUserHeadId(userHeadId);
                account.setLastAction(ACTION_EMPTY);
                dao.update(account);
                return true;
            }
        }

        return false;
    }

    @Override
    public void addOrUpdate(Account account) {
        Account old = getAccount(account.getUserId());

        if (old != null) {
            ReflectionUtil.copyProperties(account, old);
            old.setLastAction(ACTION_EMPTY);
        } else {
            old = account;
        }

        dao.insertOrReplace(old);
    }

    @Override
    public void updateLastAction(String userId, long action) {
        Account old = getAccount(userId);

        if (old != null) {
            old.setLastAction(action);
            dao.update(old);
        }
    }

    @Override
    public Account getAccount(String userId) {
        return dao.load(userId);
    }

    @Override
    public Account getAccountByName(String userName) {
        return dao.queryBuilder().where(AccountDao.Properties.UserName.eq(userName)).unique();
    }
}
