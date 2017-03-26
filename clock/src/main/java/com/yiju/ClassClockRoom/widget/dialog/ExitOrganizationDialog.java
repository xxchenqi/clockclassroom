package com.yiju.ClassClockRoom.widget.dialog;

import android.app.Activity;
import android.app.AlertDialog;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;

import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;
import com.yiju.ClassClockRoom.R;
import com.yiju.ClassClockRoom.bean.result.CommonResultBean;
import com.yiju.ClassClockRoom.util.CommonUtil;
import com.yiju.ClassClockRoom.util.GsonTools;
import com.yiju.ClassClockRoom.util.StringUtils;
import com.yiju.ClassClockRoom.util.UIUtils;
import com.yiju.ClassClockRoom.util.net.UrlUtils;


/**
 * 照片方式 Dialog VIew
 *
 * @author Neng
 */
public class ExitOrganizationDialog implements OnClickListener {

    private Activity mActivity;
    private AlertDialog dialog;
    private ExitOrganizationListener listener;

    public ExitOrganizationDialog(Activity mActivity, ExitOrganizationListener listener) {
        super();
        this.mActivity = mActivity;
        this.listener = listener;
    }

    /**
     * 加载选择 Dialog VIew
     * <p/>
     * tab内容提示
     */
    public void createView() {
        View view = LayoutInflater.from(mActivity).inflate(
                R.layout.dailog_exit_organization, null);
        dialog = new AlertDialog.Builder(mActivity, R.style.dateDialogTheme).create();
        Window window = dialog.getWindow();
        if (window != null) {
            window.setGravity(Gravity.BOTTOM); // 设置dialog显示的位置
//        window.setWindowAnimations(R.style.share_dialog_mystyle);
            dialog.setCanceledOnTouchOutside(true);
            dialog.show();

            WindowManager.LayoutParams lp = window.getAttributes();
            lp.width = CommonUtil.getScreenWidth(mActivity); // 设置宽度
            dialog.setContentView(view, lp);
        }

        // 处理事件
        Button btn_exit_organization = (Button) view.findViewById(R.id.btn_exit_organization);
        LinearLayout ll_organization_exit_and_backlist = (LinearLayout) view.findViewById(R.id.ll_organization_exit_and_backlist);
        Button btn_organization_cancel = (Button) view.findViewById(R.id.btn_organization_cancel);

        btn_exit_organization.setOnClickListener(this);
        ll_organization_exit_and_backlist.setOnClickListener(this);
        btn_organization_cancel.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_exit_organization:// 退出机构
                getHttpUtilsForExitOrganization(0);

                break;
            case R.id.ll_organization_exit_and_backlist:// 退出机构并加入黑名单
//                UIUtils.showToastSafe("退出机构并加入黑名单");
                getHttpUtilsForExitOrganization(1);
                break;
            case R.id.btn_organization_cancel:
                // 取消
                if (dialog != null) {
                    dialog.dismiss();
                }
                break;
            default:
                break;
        }
    }

    //退出机构
    private void getHttpUtilsForExitOrganization(int add_black) {
        HttpUtils httpUtils = new HttpUtils();
        RequestParams params = new RequestParams();
        params.addBodyParameter("action", "exit_organization");
        if (!"-1".equals(StringUtils.getUid())) {
            params.addBodyParameter("uid", StringUtils.getUid());
        }
        params.addBodyParameter("username", StringUtils.getUsername());
        params.addBodyParameter("password", StringUtils.getPassword());
        params.addBodyParameter("third_source", StringUtils.getThirdSource());
        params.addBodyParameter("add_black", add_black + "");

        httpUtils.send(HttpRequest.HttpMethod.POST, UrlUtils.SERVER_USER_API, params,
                new RequestCallBack<String>() {
                    @Override
                    public void onFailure(HttpException arg0, String arg1) {
                    }

                    @Override
                    public void onSuccess(ResponseInfo<String> arg0) {
                        // 处理返回的数据
                        processData(arg0.result);
                    }
                });
    }

    private void processData(String result) {
        CommonResultBean resultData = GsonTools.fromJson(result, CommonResultBean.class);
        if ("1".equals(resultData.getCode())) {
            UIUtils.showToastSafe(resultData.getMsg());
            listener.exitOrganization();
        } else {
            UIUtils.showToastSafe(resultData.getMsg());
        }
        if (dialog != null) {
            dialog.dismiss();
        }
    }

    public interface ExitOrganizationListener {
        void exitOrganization();
    }

}
