package com.yiju.ClassClockRoom.widget.dialog;

import android.app.Activity;
import android.app.AlertDialog;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.yiju.ClassClockRoom.BaseApplication;
import com.yiju.ClassClockRoom.R;
import com.yiju.ClassClockRoom.bean.VersionBean;
import com.yiju.ClassClockRoom.util.CommonUtil;
import com.yiju.ClassClockRoom.util.PermissionsChecker;
import com.yiju.ClassClockRoom.util.UIUtils;


/**
 * ----------------------------------------
 * 注释: 版本升级
 * <p/>
 * 作者: cq
 * <p/>
 * 时间: 2016/4/1 14:40
 * ----------------------------------------
 */
public class VersionUpdateDialog implements OnClickListener {

    private Activity mActivity;
    private AlertDialog dialog;
    private VersionBean versionBean;
    private DownloadApkListener listener;

    public VersionUpdateDialog(Activity mActivity, VersionBean versionBean, DownloadApkListener listener) {
        super();
        this.mActivity = mActivity;
        this.versionBean = versionBean;
        this.listener = listener;
    }

    /**
     * 加载选择 Dialog VIew
     * <p/>
     * tab内容提示
     */
    public void createView() {
        View view = LayoutInflater.from(mActivity).inflate(
                R.layout.dailog_updata, null);
        dialog = new AlertDialog.Builder(mActivity, R.style.updateDialog).create();
//        dialog = new AlertDialog.Builder(mActivity, R.style.dateDialogTheme).create();
        Window window = dialog.getWindow();
        if (window != null) {
            window.setGravity(Gravity.CENTER); // 设置dialog显示的位置
//        dialog.setCanceledOnTouchOutside(false);
            dialog.setCancelable(false);
            dialog.show();

            WindowManager.LayoutParams lp = window.getAttributes();
            lp.width = CommonUtil.getScreenWidth(); // 设置宽度
            dialog.setContentView(view, lp);
        }

        // 处理事件
        //版本号
        TextView tv_update_version = (TextView) view.findViewById(R.id.tv_update_version);
        //版本内容
        TextView tv_update_content = (TextView) view.findViewById(R.id.tv_update_content);
        //取消
        TextView tv_update_cancel = (TextView) view.findViewById(R.id.tv_update_cancel);
        //确认
        TextView tv_update_affirm = (TextView) view.findViewById(R.id.tv_update_affirm);

        int update = versionBean.getUpdate();

        if (update == 1) {
            //强制升级
            tv_update_affirm.setOnClickListener(this);
            tv_update_cancel.setTextColor(UIUtils.getColor(R.color.color_gay_aa));
        } else if (update == 2) {
            //提示升级
            tv_update_cancel.setOnClickListener(this);
            tv_update_affirm.setOnClickListener(this);
            tv_update_cancel.setTextColor(UIUtils.getColor(R.color.color_black_33));
        }

        String desc = versionBean.getData().getDesc();
        String version = versionBean.getData().getVersion();
//        StringBuilder sb = new StringBuilder();
//        String[] split = desc.split(",");
//        for (int i = 0; i < split.length; i++) {
//            if (i == split.length - 1) {
//                sb.append("·").append(split[i]).append("\r\n");
//            } else {
//                sb.append("·").append(split[i]).append(";\r\n");
//            }
//        }
        tv_update_version.setText(String.format(UIUtils.getString(R.string.txt_check_version), version));
        tv_update_content.setText(desc);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_update_cancel:
                if (dialog != null) {
                    dialog.dismiss();
                }
                break;
            case R.id.tv_update_affirm:
                if (PermissionsChecker.checkPermission(PermissionsChecker.READ_WRITE_SDCARD_PERMISSIONS)) {
                    //缺少权限,请求打开权限
                    PermissionsChecker.requestPermissions(
                            BaseApplication.getmForegroundActivity(),
                            PermissionsChecker.READ_WRITE_SDCARD_PERMISSIONS
                    );
                } else {
                    dialog.dismiss();
                    listener.download(versionBean.getData().getUrl().trim());
                }
                break;
            default:
                break;
        }
    }

    public interface DownloadApkListener {
        void download(String url);
    }


}
