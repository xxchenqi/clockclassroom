package com.yiju.ClassClockRoom.control.camera;

import android.app.Activity;
import android.app.AlertDialog;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;

import com.umeng.analytics.MobclickAgent;
import com.yiju.ClassClockRoom.R;
import com.yiju.ClassClockRoom.util.CommonUtil;
import com.yiju.ClassClockRoom.util.UIUtils;


/**
 * 照片方式 Dialog VIew
 *
 * @author Neng
 */
public class CameraDialog implements OnClickListener {

    private Activity mActivity;
    private AlertDialog dialog;

    /**
     * 照相Image
     */
    private CameraImage mCameraImage;

    public CameraDialog(Activity mActivity, CameraImage mCameraImage) {
        super();
        this.mActivity = mActivity;
        this.mCameraImage = mCameraImage;
    }

    /**
     * 加载选择 Dialog VIew
     * <p/>
     * tab内容提示
     */
    public void creatView() {
        View view = LayoutInflater.from(mActivity).inflate(
                R.layout.dailog_personalcenter_photo, null);
        dialog = new AlertDialog.Builder(mActivity, R.style.dateDialogTheme).create();
        Window window = dialog.getWindow();
        if (window != null) {
            window.setGravity(Gravity.BOTTOM); // 设置dialog显示的位置
//		window.setWindowAnimations(R.style.share_dialog_mystyle);
            dialog.setCanceledOnTouchOutside(true);
            dialog.show();

            WindowManager.LayoutParams lp = window.getAttributes();
            lp.width = CommonUtil.getScreenWidth(mActivity); // 设置宽度
            dialog.setContentView(view, lp);
        }

        // 处理事件
        Button takePhoto = (Button) view.findViewById(R.id.btn_take_photo);
        Button selectPhoto = (Button) view.findViewById(R.id.btn_pick_photo);
        Button cancle = (Button) view.findViewById(R.id.btn_photo_cancel);

        takePhoto.setOnClickListener(this);
        selectPhoto.setOnClickListener(this);
        cancle.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_take_photo:
                // 拍照
                MobclickAgent.onEvent(UIUtils.getContext(), "v3200_061");
                if (dialog != null) {
                    dialog.dismiss();
                }
                if (mCameraImage != null) {
                    mCameraImage.doTakePhoto();
                }
                break;
            case R.id.btn_pick_photo:
                MobclickAgent.onEvent(UIUtils.getContext(), "v3200_062");
                // 照片
                if (dialog != null) {
                    dialog.dismiss();
                }
                if (mCameraImage != null) {
                    mCameraImage.gallery();
                }
                break;
            case R.id.btn_photo_cancel:
                MobclickAgent.onEvent(UIUtils.getContext(), "v3200_063");
                // 取消
                if (dialog != null) {
                    dialog.dismiss();
                }
                break;
            default:
                break;
        }
    }
}
