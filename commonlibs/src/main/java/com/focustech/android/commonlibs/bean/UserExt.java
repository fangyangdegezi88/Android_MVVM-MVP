package com.focustech.android.commonlibs.bean;

import com.alibaba.fastjson.JSONObject;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 用户扩展
 *
 * @author zhangxu
 */
public class UserExt implements IUserExt, Serializable {
    private static final String FILED_MOBILE = "mobile";
    private static final String FIELD_CHILDREN = "children";
    private static final String[] NAMES = new String[]{FILED_MOBILE, FIELD_CHILDREN};


    private String userName;

    public boolean isUserNameChanged() {
        return userNameChanged;
    }

    private boolean userNameChanged;
    private Role role;//角色枚举值 1 家长 2 老师 3 TM老师

    private Relation relation;   //关系 1.自定义 2.爸爸 3.妈妈 4.爷爷 5.奶奶 6.外公 7.外婆
    private String relationName;   //自定义关系

    private String mobile;
    private List<Child> children;

    /***
     "WISDOM_STUDENT_PARENT",
     "ALL_SCHOOLBAGS",
     "GET_VISITING_MANAGEMENT",
     "PUT_LEAVE_APPROVAL",
     "PUT_ATTENDANCE",
     "LEARNING_SITUATION_ANALYSIS_SELF"
     * */
    private List<String> perms;//定义了家长所有的权限

    public boolean isHasChilds() {
        return isHasChilds;
    }

    public void setHasChilds(boolean hasChilds) {
        isHasChilds = hasChilds;
    }

    private boolean isHasChilds;

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public boolean getUserNameChanged() {
        return userNameChanged;
    }

    public void setUserNameChanged(boolean userNameChanged) {
        this.userNameChanged = userNameChanged;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public List<Child> getChildren() {
        return children;
    }

    public void setChildren(List<Child> children) {
        this.children = children;
    }

    public List<String> getPerms() {
        return perms;
    }

    public void setPerms(List<String> perms) {
        this.perms = perms;
    }

    @Override
    public void create(Map<String, String> map) {
        if (null != map) {
            setMobile(map.get(FILED_MOBILE));

            if (map.containsKey(FIELD_CHILDREN)) {
                setChildren(JSONObject.parseArray(map.get(FIELD_CHILDREN), Child.class));
            }
        }
    }

    @Override
    public Map<String, String> getEntry() {
        Map<String, String> value = new HashMap<>();
        value.put(FILED_MOBILE, getMobile());

        if (null != getChildren()) {
            value.put(FIELD_CHILDREN, JSONObject.toJSONString(children));
        }

        return value;
    }

    @Override
    public String[] getNames() {
        return NAMES;
    }

    public String getRelationName() {
        return relationName;
    }

    public void setRelationName(String relationName) {
        this.relationName = relationName;
    }

    public Relation getRelation() {
        return relation;
    }

    public void setRelation(Relation relation) {
        this.relation = relation;
    }

    public static class Child implements Serializable {
        private String id;
        private String name; // 学生姓名
        private Relation relation;
        private String clazzId;
        private String number;
        private String[] extendPerm; //权限

        public String getClazz() {
            return clazz;
        }

        public void setClazz(String clazz) {
            this.clazz = clazz;
        }

        private String clazz;   //学生班级

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public Relation getRelation() {
            return relation;
        }

        public void setRelation(Relation relation) {
            this.relation = relation;
        }

        public String getClazzId() {
            return clazzId;
        }

        public void setClazzId(String clazzId) {
            this.clazzId = clazzId;
        }

        public String[] getExtendPerm() {
            return extendPerm;
        }

        public void setExtendPerm(String[] extendPerm) {
            this.extendPerm = extendPerm;
        }
    }

    public enum Relation {  //1 自定义 2 父亲 3 母亲 4 爷爷 5 奶奶 6 外公 7 外婆 enum从0开始 定义一个0
        ERROR, CUSTOM, FATHER, MATHER, GRANDDAD, GRANDMAMI, GRANDFATHER, GRANDMOTHER
    }

    public enum Role {//角色枚举值 1 家长 2 老师 3 TM老师 enum从0开始 定义一个0
        ERROR, PARENT, TEACHER, MTTEACH
    }
}
