package com.focustech.android.components.mt.sdk.android.service.pojo.status;

import com.focustech.tm.open.sdk.messages.protobuf.Messages;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 用户状态数据
 *
 * @author zhangxu
 */
public class UserStatusData {
    private String userId;
    private Map<Messages.Equipment, Messages.Status> statusMap = new HashMap<>();

    public UserStatusData() {
    }

    public UserStatusData(String userId, Map<Messages.Equipment, Messages.Status> statusMap) {
        this.userId = userId;
        this.statusMap = statusMap;
    }

    public UserStatusData(String userId) {
        this.userId = userId;
    }

    public void reset(List<Messages.EquipmentStatus> equipmentStatuses) {
        statusMap.clear();

        for (Messages.EquipmentStatus equipmentStatus : equipmentStatuses) {
            statusMap.put(equipmentStatus.getEquipment(), equipmentStatus.getStatus());
        }
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public Map<Messages.Equipment, Messages.Status> getStatusMap() {
        return statusMap;
    }

    public void setStatusMap(Map<Messages.Equipment, Messages.Status> statusMap) {
        this.statusMap = statusMap;
    }
}
