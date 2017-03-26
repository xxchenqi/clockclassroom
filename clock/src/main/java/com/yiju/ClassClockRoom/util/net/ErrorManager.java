package com.yiju.ClassClockRoom.util.net;

import com.yiju.ClassClockRoom.R;
import com.yiju.ClassClockRoom.util.UIUtils;

/**
 * 请求错误管理类
 * Created by geliping on 2016/8/9.
 */
public class ErrorManager {

    /**
     * 报名课程返回错误码处理
     */
    public static void showApplyCourseError(int code){
        switch (code) {
            case 70005:
                UIUtils.showToastSafe(R.string.toast_show_course_no_exist);
                break;
            case 70015:
                UIUtils.showToastSafe(R.string.course_not_audit);
                break;
            case 70016:
                UIUtils.showToastSafe(R.string.course_canceled);
                break;
            case 70017:
                UIUtils.showToastSafe(R.string.apply_over_hint);
                break;
            case 70018:
                UIUtils.showToastSafe(R.string.course_over_hint);
                break;
            default:
                UIUtils.showToastSafe(R.string.fail_network_request);
                break;
        }
    }
}
