package com.yiju.ClassClockRoom.util;

import android.content.Context;
import android.content.SharedPreferences;

import com.yiju.ClassClockRoom.bean.ClassRoomData;
import com.yiju.ClassClockRoom.bean.IndexResult;
import com.yiju.ClassClockRoom.common.constant.SharedPreferencesConstant;

import java.util.Set;

public class SharedPreferencesUtils {
    // sp = getSharedPreferences("config", 0);
    // Editor edit = sp.edit();
    //
    // edit.putBoolean("isUpdate", mAutoUpDate.isToggle());
    //
    // edit.commit();
    // sp的名字
    private final static String SP_NAME = "config";
    private static SharedPreferences sp;

    public static void saveBoolean(Context context, String key, boolean value) {

        if (sp == null)
            sp = context.getSharedPreferences(SP_NAME, 0);

        sp.edit().putBoolean(key, value).apply();
    }

    public static void saveInt(Context context, String key, int value) {

        if (sp == null)
            sp = context.getSharedPreferences(SP_NAME, 0);

        sp.edit().putInt(key, value).apply();
    }

    public static void saveString(Context context, String key, String value) {

        if (sp == null)
            sp = context.getSharedPreferences(SP_NAME, 0);

        sp.edit().putString(key, value).apply();
    }


    public static void saveSet(Context context, String key, Set<String> value) {

        if (sp == null)
            sp = context.getSharedPreferences(SP_NAME, 0);

        sp.edit().putStringSet(key, value).apply();
    }

    public static int getInt(Context context, String key, int defValue) {
        if (sp == null)
            sp = context.getSharedPreferences(SP_NAME, 0);

        return sp.getInt(key, defValue);

    }

    public static String getString(Context context, String key, String defValue) {
        if (sp == null)
            sp = context.getSharedPreferences(SP_NAME, 0);

        return sp.getString(key, defValue);

    }

    // SharedPreferences sp = getSharedPreferences("config", 0);
    //
    // boolean result = sp.getBoolean("isUpdate", false);

    public static boolean getBoolean(Context context, String key,
                                     boolean defValue) {
        if (sp == null)
            sp = context.getSharedPreferences(SP_NAME, 0);

        return sp.getBoolean(key, defValue);
    }

    public static Set<String> getSet(Context context, String key, Set<String> defValue) {

        if (sp == null)
            sp = context.getSharedPreferences(SP_NAME, 0);

        return sp.getStringSet(key, defValue);
    }

    public static void removeValue(String value) {
        SharedPreferences.Editor edit = sp.edit();
        edit.remove(value);
        edit.apply();
    }

    /**
     * 此方法为清空数据库里的数据，但是本地邮箱是不能清除的，需要保留
     */
    public static void clearData() {
        SharedPreferences.Editor edit = sp.edit();
        edit.remove("isLogin");
        edit.remove("id");
        edit.remove("name");
        edit.remove("sex");
        edit.remove("age");
        edit.remove("mobile");
        edit.remove("email");
        edit.remove("nickname");
        edit.remove("avatar");
        edit.remove("praise");
        edit.remove("credit");
        edit.remove("create_time");
        edit.remove("login_time");
        edit.remove("is_teacher");
        edit.remove("third_id");
        edit.remove("remerber");
        edit.remove("is_remerber");
        edit.remove("is_reserve_remerber");
        edit.remove("is_pay_remerber");
        edit.remove("trouble_btime");
        edit.remove("trouble_etime");
        edit.remove("is_sys_remerber");
        edit.remove("is_order_remerber");
        edit.remove("badge");
        edit.remove("cnum");
        edit.remove("third_qq");
        edit.remove("third_wechat");
        edit.remove("third_weibo");
        edit.remove("real_name");
        edit.remove("show_teacher");
        edit.remove("org_id");
        edit.remove("org_name");
        edit.remove("is_auth");
        edit.remove("org_auth");
        edit.remove("teacher_id");
        edit.remove("teacher_info");
        edit.remove("is_pay");
        edit.remove("black_count");
        edit.remove("accompany_read_pwd");
        edit.remove("ClassRoomData");
        edit.remove("WorkingPayResult");
        edit.remove("username");
        edit.remove("password");
        edit.remove("third_source");
        edit.remove(SharedPreferencesConstant.Shared_Login_Cid);//退出登录的时候清掉当前cid
        edit.apply();
    }

    public static void saveClassRoomData(ClassRoomData classRoomData) {
        SharedPreferences sp = UIUtils.getContext().getSharedPreferences(SP_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putString("ClassRoomData", GsonTools.createGsonString(classRoomData));
        editor.apply();
    }

    public static ClassRoomData getClassRoomData() {
        SharedPreferences sp = UIUtils.getContext().getSharedPreferences(SP_NAME, Context.MODE_PRIVATE);
        String json = sp.getString("ClassRoomData", null);
        ClassRoomData info = null;
        if (json != null) {
            info = GsonTools.changeGsonToBean(json, ClassRoomData.class);
        }
        return info;
    }

    public static void saveIndexObjEntity(IndexResult.ObjEntity objEntity) {
        SharedPreferences sp = UIUtils.getContext().getSharedPreferences(SP_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putString("IndexObjEntity", GsonTools.createGsonString(objEntity));
        editor.apply();
    }

    public static IndexResult.ObjEntity getIndexObjEntity() {
        SharedPreferences sp = UIUtils.getContext().getSharedPreferences(SP_NAME, Context.MODE_PRIVATE);
        String json = sp.getString("IndexObjEntity", null);
        IndexResult.ObjEntity info = null;
        if (json != null) {
            info = GsonTools.changeGsonToBean(json, IndexResult.ObjEntity.class);
        }
        return info;
    }
}
