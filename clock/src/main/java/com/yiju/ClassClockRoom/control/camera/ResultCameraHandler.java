package com.yiju.ClassClockRoom.control.camera;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;

import com.yiju.ClassClockRoom.util.CommonUtil;
import com.yiju.ClassClockRoom.util.StringUtils;
import com.yiju.ClassClockRoom.util.UIUtils;
import com.yiju.ClassClockRoom.util.sdcard.FileUtil;
import com.yiju.ClassClockRoom.util.ui.ImageUtil;

import java.io.File;


/**
 * 相机返回处理类
 */
public class ResultCameraHandler {
    private static ResultCameraHandler _sInstance;
    private static boolean isCrop;
    private int widthCrop = 320;                    //截图框宽
    private int heightCrop = 320;                   //截图框高

    private int widthScreen = 720;                  //屏幕框宽
    private int heightScreen = 1280;                //屏幕框高

    private ResultCameraHandler() {
        this.widthScreen = CommonUtil.getScreenWidth();
        this.heightScreen = CommonUtil.getScreenHeight();
        this.widthCrop = widthScreen;
        this.heightCrop = widthScreen;
    }

    public static ResultCameraHandler getInstance() {
        if (_sInstance == null) {
            _sInstance = new ResultCameraHandler();
        }
        isCrop = false;
        return _sInstance;
    }

    /**
     * 获得照片文件
     * @param mActivity ac
     * @param data d
     * @param requestCode   请求码
     * @param resultCode    返回码
     * @param cameraImage   相机操作类
     * @param cameraResult  回调接口
     */
    public void getPhotoFile(Activity mActivity, Intent data, int requestCode,
                             int resultCode, CameraImage cameraImage, CameraResult cameraResult) {
        Uri uriFile = null;
        if (resultCode == 0) {
            return;
        }
        if (data != null) {
            uriFile = data.getData();
        }
        if (uriFile == null) {
            if (CameraImage.tempFile != null) {
                uriFile = Uri.fromFile(CameraImage.tempFile);
            }
        }
        if (uriFile != null) {
            switch (requestCode) {
                case CameraImage.CAMERA_WITH_DATA: // 照相功能
                    if (isCrop) {
                        cameraImage.crop(uriFile, widthCrop, heightCrop);
                    } else {
                        cameraResult.result(ImageUtil.zoomFileXY(CameraImage.tempFile, widthScreen, heightScreen));
                    }
                    break;
                case CameraImage.PHOTO_REQUEST_CUT: // 剪切完毕
                    cameraResult.result(CameraImage.tempFile);
                    break;
                case CameraImage.PHOTO_REQUEST_GALLERY: // 相册
                    dataHandler(mActivity, isCrop, cameraImage,
                            cameraResult, uriFile);
                    break;
            }
        } else {
            UIUtils.showToastSafe("获取图片失败!");
        }
    }

    /**
     *
     * @param mActivity ac
     * @param isCrop        是否裁剪
     * @param cameraImage   相机操作类
     * @param cameraResult  回调接口
     * @param uriFile       文件地址
     */
    private void dataHandler(Activity mActivity, boolean isCrop,
                             CameraImage cameraImage,
                             CameraResult cameraResult, Uri uriFile) {
        if (!FileUtil.isHasSdcard()) {
            UIUtils.showToastSafe("未找到存储卡！");
        } else {
            if (isCrop) {
                cameraImage.crop(uriFile, widthCrop, heightCrop);
            } else {
                String path = ImageUtil.getRealPathFromURI(mActivity, uriFile);
                if (StringUtils.isNotNullString(path)) {
                    File file = new File(path);
                    cameraResult.result(ImageUtil.zoomFileXY(file, widthScreen, heightScreen));
                }
            }
        }
    }

    /**
     *  设置是否裁剪
     * @param isCrop b
     */
    public ResultCameraHandler setCrop(boolean isCrop) {
        ResultCameraHandler.isCrop = isCrop;
        return _sInstance;
    }

    /**
     * 设置裁剪宽
     */
    public void setWidthCrop(int width) {
        this.widthCrop = width;
    }

    /**
     * 设置裁剪高
     */
    public void setHeightCrop(int height) {
        this.heightCrop = height;
    }

    public interface CameraResult {
        void result(File file);
    }

}
