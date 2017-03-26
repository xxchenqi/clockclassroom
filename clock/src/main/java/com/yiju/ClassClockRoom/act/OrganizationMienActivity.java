package com.yiju.ClassClockRoom.act;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.yiju.ClassClockRoom.R;
import com.yiju.ClassClockRoom.act.base.BaseActivity;
import com.yiju.ClassClockRoom.adapter.OrganizationMienAdapter;
import com.yiju.ClassClockRoom.bean.MineOrganizationBean;
import com.yiju.ClassClockRoom.bean.PictureWrite;
import com.yiju.ClassClockRoom.bean.result.CodeKey;
import com.yiju.ClassClockRoom.bean.result.CommonResultBean;
import com.yiju.ClassClockRoom.control.camera.CameraDialog;
import com.yiju.ClassClockRoom.control.camera.CameraImage;
import com.yiju.ClassClockRoom.control.camera.ResultCameraHandler;
import com.yiju.ClassClockRoom.util.CommonUtil;
import com.yiju.ClassClockRoom.util.GsonTools;
import com.yiju.ClassClockRoom.util.PermissionsChecker;
import com.yiju.ClassClockRoom.util.SharedPreferencesUtils;
import com.yiju.ClassClockRoom.util.StringUtils;
import com.yiju.ClassClockRoom.util.UIUtils;
import com.yiju.ClassClockRoom.util.net.UrlUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * ----------------------------------------
 * 注释:机构风采
 * <p/>
 * 作者: cq
 * <p/>
 * 时间: on 2016/3/17 17:33
 * ----------------------------------------
 */
public class OrganizationMienActivity extends BaseActivity implements View.OnClickListener {
    //列表
    @ViewInject(R.id.gv_organization_mien)
    private GridView gv_organization_mien;
    //返回
    @ViewInject(R.id.head_back_relative)
    private RelativeLayout head_back_relative;
    //标题
    @ViewInject(R.id.head_title)
    private TextView head_title;
    //请求返回的数据
    private String file_category;
    private String permit_code;
    private String key;
    //适配器
    private OrganizationMienAdapter adapter;
    //bean
    private MineOrganizationBean bean;
    //数据
    private List<MineOrganizationBean.DataEntity.MienEntity> mien = new ArrayList<>();
    //照相
    private CameraImage cameraImage;
    //宽度
    private int mScreenWidth;
    //权限
    private String org_auth;
    //删除
    public static int RESULT_CODE_FROM_ORGANIZATION_MIEN_DELETE_ACT = 1000;
    //编辑
    public static int RESULT_CODE_FROM_ORGANIZATION_MIEN_EDIT_ACT = 1002;

    private String uid ;
    @Override
    public void initIntent() {
        super.initIntent();
        bean = (MineOrganizationBean) getIntent().getSerializableExtra("bean");
    }


    @Override
    protected void onResume() {
        super.onResume();

    }

    @Override
    protected void initView() {
        cameraImage = new CameraImage(this);
        mScreenWidth = CommonUtil.getScreenWidth(this);
        org_auth = SharedPreferencesUtils.getString(getApplicationContext(),
                getResources().getString(R.string.shared_org_auth), "-1");
    }

    @Override
    protected void initData() {
        head_title.setText(R.string.title_institutional_presence);
        uid = SharedPreferencesUtils.getString(UIUtils.getContext(),
                getResources().getString(R.string.shared_id), null);
        getHttpUtils();
    }

    @Override
    public void initListener() {
        super.initListener();
        head_back_relative.setOnClickListener(this);
        gv_organization_mien.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (mien == null) {
                    return;
                }
                if ("2".equals(org_auth)) {
                    if (position == mien.size() - 1) {//添加
                        //检测是否打开读写权限
                        if (!PermissionsChecker.checkPermission(
                                PermissionsChecker.READ_WRITE_SDCARD_PERMISSIONS)) {
                            new CameraDialog(OrganizationMienActivity.this, cameraImage).creatView();
                        } else {
                            PermissionsChecker.requestPermissions(
                                    OrganizationMienActivity.this,
                                    PermissionsChecker.READ_WRITE_SDCARD_PERMISSIONS
                            );
                        }
                    } else {
                        Intent editIntent = new Intent(
                                OrganizationMienActivity.this,
                                OrganizationMienEditActivity.class);
                        editIntent.putExtra("bean", bean);
                        editIntent.putExtra("pos", position);
                        editIntent.putExtra("title", getString(R.string.title_institutional_presence));
                        editIntent.putExtra("edit_flag", true);
                        startActivityForResult(editIntent, 0);
                    }
                } else {
                    Intent editIntent = new Intent(
                            OrganizationMienActivity.this,
                            OrganizationMienEditActivity.class);
                    editIntent.putExtra("bean", bean);
                    editIntent.putExtra("pos", position);
                    editIntent.putExtra("title", getString(R.string.title_institutional_presence));
                    startActivity(editIntent);
                }
            }
        });
    }

    @Override
    public String getPageName() {
        return getString(R.string.title_act_my_org_mine);
    }

    @Override
    public int setContentViewId() {
        return R.layout.activity_organization_mien;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.head_back_relative:
//                if ("2".equals(org_auth)) {
//                    StringBuffer sb = new StringBuffer();
//                    for (int i = 0; i < mien.size() - 1; i++) {
//                        String pic = mien.get(i).getPic();
//                        if (i == mien.size() - 2) {
//                            sb.append(pic);
//                        } else {
//                            sb.append(pic + ",");
//                        }
//                    }
//                    Intent intent = new Intent();
//                    intent.putExtra("content", sb.toString());
//                    this.setResult(1002, intent);
//                }
//                finish();
                onBackPressed();
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // 照片返回
        if (requestCode == CameraImage.CAMERA_WITH_DATA
                || requestCode == CameraImage.PHOTO_REQUEST_CUT
                || requestCode == CameraImage.PHOTO_REQUEST_GALLERY) {
            ResultCameraHandler
                    .getInstance()
                    .setCrop(true)
                    .getPhotoFile(OrganizationMienActivity.this,
                            data, requestCode, resultCode, cameraImage,
                            new ResultCameraHandler.CameraResult() {
                                @Override
                                public void result(File imageUri) {
                                    getHttpUtilsForGetImgKey(imageUri);
                                }
                            }
                    );
        } else if (resultCode == RESULT_CODE_FROM_ORGANIZATION_MIEN_DELETE_ACT) {
            //删除照片
            mien.clear();
            String imgs = data.getStringExtra("imgs");
            if (!"".equals(imgs)) {
                String[] split = imgs.split(",");
                for (String aSplit : split) {
                    MineOrganizationBean.DataEntity.MienEntity mienEntity
                            = new MineOrganizationBean().new DataEntity().new MienEntity();
                    mienEntity.setPic(aSplit);
                    mien.add(mienEntity);
                }
            }
            mien.add(new MineOrganizationBean().new DataEntity().new MienEntity());
            adapter.updateDatas(mien);
        }
    }

    /**
     * 获取机构信息
     */
    private void getHttpUtils() {
        HttpUtils httpUtils = new HttpUtils();
        RequestParams params = new RequestParams();
        params.addBodyParameter("action", "get_organization_info");
        if (!"-1".equals(StringUtils.getUid())) {
            params.addBodyParameter("uid", StringUtils.getUid());
        }
        params.addBodyParameter("username", StringUtils.getUsername());
        params.addBodyParameter("password", StringUtils.getPassword());
        params.addBodyParameter("third_source", StringUtils.getThirdSource());
        httpUtils.send(HttpRequest.HttpMethod.POST, UrlUtils.SERVER_USER_API, params,
                new RequestCallBack<String>() {

                    @Override
                    public void onFailure(HttpException arg0, String arg1) {
                        UIUtils.showToastSafe(R.string.fail_network_request);
                    }

                    @Override
                    public void onSuccess(ResponseInfo<String> arg0) {
                        // 处理返回的数据
                        processData(arg0.result);

                    }
                });
    }

    private void processData(String result) {
        bean = GsonTools.changeGsonToBean(result, MineOrganizationBean.class);
        if (bean == null) {
            return;
        }
        if ("1".equals(bean.getCode())) {
            MineOrganizationBean.DataEntity data = bean.getData();
            mien = data.getMien();
            if ("2".equals(org_auth)) {
                mien.add(new MineOrganizationBean().new DataEntity().new MienEntity());
            }
            adapter = new OrganizationMienAdapter(this, mien, R.layout.item_member_detail, mScreenWidth);
            gv_organization_mien.setAdapter(adapter);
        }
    }


    /**
     * 获取图片key
     *
     * @param imageUri 图片路径
     */
    public void getHttpUtilsForGetImgKey(final File imageUri) {
        HttpUtils httpUtils = new HttpUtils();
        RequestParams params = new RequestParams();
        params.addBodyParameter("action", "getcodekey");

        httpUtils.send(HttpRequest.HttpMethod.POST, UrlUtils.SERVER_USER_API, params,
                new RequestCallBack<String>() {

                    @Override
                    public void onFailure(HttpException arg0, String arg1) {
                        UIUtils.showToastSafe(R.string.fail_network_request);
                    }

                    @Override
                    public void onSuccess(ResponseInfo<String> arg0) {
                        // 处理返回的数据
                        processDataForGetImgKey(arg0.result, imageUri);
                    }
                });
    }

    private void processDataForGetImgKey(String result, File imageUri) {
        CodeKey codeKey = GsonTools.changeGsonToBean(result, CodeKey.class);
        if (codeKey == null) {
            return;
        }
        if (codeKey.getCode().equals("1")) {
            CodeKey.Data data = codeKey.getData();
            file_category = data.getFile_category();
            key = data.getKey();
            permit_code = data.getPermit_code();

            // 请求上传头像
            getHttpUtilsForUploadMineImg(imageUri);

        } else {
            UIUtils.showToastSafe(R.string.toast_request_fail);
        }
    }

    /**
     * 上传图片
     *
     * @param imageUri 图片路径
     */
    private void getHttpUtilsForUploadMineImg(File imageUri) {
        HttpUtils httpUtils = new HttpUtils();
        RequestParams params = new RequestParams();
        params.addBodyParameter("permit_code", permit_code);
        params.addBodyParameter("file_category", file_category);
        params.addBodyParameter("key", key);
        params.addBodyParameter("pfile", imageUri);

        httpUtils.send(HttpRequest.HttpMethod.POST, UrlUtils.PIC_WIRTE, params,
                new RequestCallBack<String>() {

                    @Override
                    public void onFailure(HttpException arg0, String arg1) {
                        UIUtils.showToastSafe(R.string.fail_network_request);
                    }

                    @Override
                    public void onSuccess(ResponseInfo<String> arg0) {
                        // 处理返回的数据
                        processDataForUploadMineImg(arg0.result);

                    }
                }
        );
    }

    private void processDataForUploadMineImg(String result) {
        PictureWrite pictureWrite = GsonTools.changeGsonToBean(result,
                PictureWrite.class);
        if (pictureWrite == null) {
            return;
        }
        if (pictureWrite.isFlag()) {
            StringBuilder sb = new StringBuilder(pictureWrite.getResult()
                    .getPic_id());
            String headUrl = "http://get.file.dc.cric.com/"
                    + sb.insert(sb.length() - 4, "_320X240_0_0_0").toString();
            getHttpUtilsForEditOrganizationInfo(headUrl);
        } else {
            UIUtils.showToastSafe(R.string.toast_request_fail);
        }
    }

    String url = "";

    /**
     * 编辑机构资料接口请求_添加机构风采图片
     *
     * @param headUrl 图片路径
     */
    private void getHttpUtilsForEditOrganizationInfo(String headUrl) {
        url = headUrl;
        StringBuilder imgs = new StringBuilder();
        if (bean.getData().getMien() == null || bean.getData().getMien().size() <= 0) {
            return;
        }
        for (int i = 0; i < bean.getData().getMien().size(); i++) {
            if (!TextUtils.isEmpty(bean.getData().getMien().get(i).getPic())) {
                imgs.append(bean.getData().getMien().get(i).getPic().trim()).append(",");
            }
        }
        imgs.append(url);
        HttpUtils httpUtils = new HttpUtils();
        RequestParams params = new RequestParams();
        params.addBodyParameter("action", "edit_organization_info");
        if (!"-1".equals(StringUtils.getUid())) {
            params.addBodyParameter("org_uid", StringUtils.getUid());
        }
        params.addBodyParameter("info", bean.getData().getInfo());
        params.addBodyParameter("tags", bean.getData().getTags());
        params.addBodyParameter("mien_pic", imgs.toString());
        params.addBodyParameter("uid", uid);
        params.addBodyParameter("username", StringUtils.getUsername());
        params.addBodyParameter("password", StringUtils.getPassword());
        params.addBodyParameter("third_source", StringUtils.getThirdSource());
        httpUtils.send(HttpRequest.HttpMethod.POST, UrlUtils.SERVER_USER_API, params,
                new RequestCallBack<String>() {

                    @Override
                    public void onFailure(HttpException arg0, String arg1) {
                        UIUtils.showToastSafe(R.string.fail_network_request);
                    }

                    @Override
                    public void onSuccess(ResponseInfo<String> arg0) {
                        // 处理返回的数据
                        processDataForEditOrganizationInfo(arg0.result);
                    }
                }
        );
    }

    private void processDataForEditOrganizationInfo(String result) {
        CommonResultBean resultData = GsonTools.fromJson(result, CommonResultBean.class);
        UIUtils.showToastSafe(resultData.getMsg());
        if ("1".equals(resultData.getCode())) {
            //修改成功
            MineOrganizationBean.DataEntity.MienEntity mienEntity = new MineOrganizationBean().new DataEntity().new MienEntity();
            mienEntity.setPic(url);
            mien.add(mien.size() - 1, mienEntity);
            adapter.updateDatas(mien);
            MineOrganizationActivity.organization_mien_flag = true;
        }
    }


    @Override
    public void onBackPressed() {

        if ("2".equals(org_auth)) {
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < mien.size() - 1; i++) {
                String pic = mien.get(i).getPic();
                if (i == mien.size() - 2) {
                    sb.append(pic);
                } else {
                    sb.append(pic).append(",");
                }
            }
            Intent intent = new Intent();
            intent.putExtra("content", sb.toString());
            this.setResult(RESULT_CODE_FROM_ORGANIZATION_MIEN_EDIT_ACT, intent);
        }
        super.onBackPressed();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (!(requestCode == PermissionsChecker.REQUEST_EXTERNAL_STORAGE
                && PermissionsChecker.hasAllPermissionsGranted(grantResults))) {
            //权限未获取
//        } else {
            UIUtils.showToastSafe(getString(R.string.toast_permission_read_write_sdcard));
        }
    }

}
