package com.yiju.ClassClockRoom.control.camera;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;

import com.yiju.ClassClockRoom.common.constant.RequestCodeConstant;
import com.yiju.ClassClockRoom.util.DateUtil;
import com.yiju.ClassClockRoom.util.sdcard.FileUtil;

import java.io.File;


/**
 * 照片操作
 *
 * @author Neng
 */
public class CameraImage {

    private Activity mActivity;
    /**
     * 拍照的照片存储位置
     */
    private File PHOTO_DIR = null;
    /**
     * 拍照
     */
    public static final int CAMERA_WITH_DATA = RequestCodeConstant.CAMERA_WITH_DATA;
    /**
     * 相册
     */
    public static final int PHOTO_REQUEST_GALLERY = RequestCodeConstant.PHOTO_REQUEST_GALLERY;

    /**
     * 用来标识拍照剪切后的结果
     */
    public static final int PHOTO_REQUEST_CUT = RequestCodeConstant.PHOTO_REQUEST_CUT;
    public static String tempFile;
    public static Uri uri_tempFile;

    /**
     * 拍照临时文件
     */
    public CameraImage(Activity mActivity) {
        super();
        this.mActivity = mActivity;
    }

    /**
     * 拍照
     */
    public void doTakePhoto() {
        if(PHOTO_DIR==null){
             /* 头像名称 */
            tempFile = getTempFile();
        }
        if (PHOTO_DIR != null && PHOTO_DIR.exists()) {
            Intent intent_camera = mActivity.getPackageManager()
                    .getLaunchIntentForPackage("com.android.camera");
            Intent intent;
            if (intent_camera != null) {
                intent = new Intent();
                intent.setPackage("com.android.camera");
                intent.setAction(MediaStore.ACTION_IMAGE_CAPTURE);
            } else {
                // 激活相机
                intent = new Intent("android.media.action.IMAGE_CAPTURE");
            }
            // 判断存储卡是否可以用，可用进行存储
            uri_tempFile = Uri.parse("file://" + "/" + tempFile);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, uri_tempFile);
            // 开启一个带有返回值的Activity，请求码为PHOTO_REQUEST_CAREMA
            mActivity.startActivityForResult(intent, CAMERA_WITH_DATA);
        }
    }

    /**
     * 相册
     */
    public void gallery() {
        // 激活系统图库，选择一张图片
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        // 开启一个带有返回值的Activity，请求码为PHOTO_REQUEST_GALLERY
        mActivity.startActivityForResult(intent, PHOTO_REQUEST_GALLERY);
    }

    /**
     * 剪切图片
     *
     * @param uri    地址
     * @param width  W
     * @param height H
     */
    public void crop(Uri uri, int width, int height) {
        // 裁剪图片意图
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(uri, "image/*");
        intent.putExtra("crop", "true");
        // 裁剪框的比例，1：1
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        // 裁剪后输出图片的尺寸大小
        intent.putExtra("outputX", width);
        intent.putExtra("outputY", height);
        tempFile = getTempFile();
        uri_tempFile = Uri.parse("file://" + "/" + tempFile);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, uri_tempFile);
        intent.putExtra("outputFormat", "JPEG");// 图片格式
        intent.putExtra("noFaceDetection", true);// 取消人脸识别
        intent.putExtra("return-data", false);
        mActivity.startActivityForResult(intent, CameraImage.PHOTO_REQUEST_CUT);
    }

    /**
     * 获取File
     */
    private String getTempFile() {
        if (FileUtil.isHasSdcard()) {
            String path = Environment.getExternalStorageDirectory().getAbsoluteFile()
                    + "/clock/cameracache";
            if (PHOTO_DIR == null) {
                try {
                    if (FileUtil.isHasSdcard()) {
                        PHOTO_DIR = new File(path);
                        if (!PHOTO_DIR.exists()) {
                            PHOTO_DIR.mkdirs();// 创建照片的存储目录
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            // 判断目录是否存在
            if (!PHOTO_DIR.exists()) {
                PHOTO_DIR.mkdirs();// 创建照片的存储目录
            }
            String fileName = DateUtil.getCurrentDate("yyyyMMddHHmmss")
                    + ".jpg";

            return path + "/" + fileName;
        }
        return null;
    }


//	public static void cleanPhotoCacheDir(){
//		FileUtil.cleanDir("/clock/CameraCache");
//	}

}
