package com.yiju.ClassClockRoom.act.search;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;

import com.baidu.location.BDLocation;
import com.yiju.ClassClockRoom.R;
import com.yiju.ClassClockRoom.act.base.BaseActivity;
import com.yiju.ClassClockRoom.act.MainActivity;
import com.yiju.ClassClockRoom.common.NetWebViewClient;
import com.yiju.ClassClockRoom.control.map.LocationSingle;
import com.yiju.ClassClockRoom.util.KeyBoardManager;
import com.yiju.ClassClockRoom.util.NetWorkUtils;
import com.yiju.ClassClockRoom.util.net.UrlUtils;

public class Search_Result_Activity extends BaseActivity implements
        OnClickListener, OnEditorActionListener,
        TextWatcher, View.OnKeyListener {

    private WebView search_view;
    private WebSettings setting;
    private ImageView iv_search_back;
    // private boolean isLogin;
    private String url;
    private EditText et_input_keywords;
    private String lng;
    private String lat;
    private String keyword = "";
    private TextView tv_search_cancel;
    private ImageView iv_search_delete;
    private String flag;

    /**
     * 无WIFI显示界面
     */
    private RelativeLayout ly_wifi;
    /**
     * 刷新
     */
    private Button btn_no_wifi_refresh;
    /**
     * 显示透明背景
     */
    private View view_lucency;

    @Override
    public int setContentViewId() {
        return R.layout.activity_search_result;
    }

    @SuppressLint("SetJavaScriptEnabled")
    @Override
    public void initView() {
        BDLocation currentLocation = LocationSingle.getInstance()
                .getCurrentLocation();

        if (currentLocation != null) {
            lng = currentLocation.getLongitude() + "";
            lat = currentLocation.getLatitude() + "";
        } else {
            lng = "";
            lat = "";
        }

        view_lucency = findViewById(R.id.view_lucency);
        view_lucency.setOnClickListener(this);

        iv_search_delete = (ImageView) findViewById(R.id.iv_search_delete);
        iv_search_delete.setOnClickListener(this);

        iv_search_back = (ImageView) findViewById(R.id.iv_search_back);
        iv_search_back.setOnClickListener(this);

        et_input_keywords = (EditText) findViewById(R.id.et_input_keywords);
        et_input_keywords.setOnEditorActionListener(this);
        et_input_keywords.setImeOptions(EditorInfo.IME_ACTION_SEARCH);
        et_input_keywords.addTextChangedListener(this);
        et_input_keywords.setOnClickListener(this);
        et_input_keywords.setOnKeyListener(this);

        tv_search_cancel = (TextView) findViewById(R.id.tv_search_cancel);
        tv_search_cancel.setOnClickListener(this);

        ly_wifi = (RelativeLayout) findViewById(R.id.ly_wifi);
        btn_no_wifi_refresh = (Button) findViewById(R.id.btn_no_wifi_refresh);
        btn_no_wifi_refresh.setOnClickListener(this);

        search_view = (WebView) findViewById(R.id.search_view);
        setting = search_view.getSettings();
        setting.setJavaScriptEnabled(true);
        setting.setDefaultTextEncodingName("GBK");
        NetWebViewClient netWebViewClient = new NetWebViewClient();
        search_view.setWebViewClient(netWebViewClient);

        flag = getIntent().getStringExtra("flag");

        if ("all".equals(flag)) {
            try {
                // keyword = URLEncoder.encode(
                // getIntent().getStringExtra("keyword"), "UTF-8");

                keyword = getIntent().getStringExtra("keyword");
            } catch (Exception e) {
                e.printStackTrace();
            }
            url = UrlUtils.SERVER_WEB_SEARCH_RESULT + "keyword=" + keyword
                    + "&type=all" + "&lng=" + lng + "&lat=" + lat;
        } else if ("teacher".equals(flag)) {
            url = UrlUtils.SERVER_WEB_SEARCH_RESULT + "keyword="
                    + "&type=teacher" + "&lng=" + lng + "&lat=" + lat;
        }

        et_input_keywords.setText(keyword);

        if (NetWorkUtils.getNetworkStatus(this)) {
            ly_wifi.setVisibility(View.GONE);
            search_view.setVisibility(View.VISIBLE);
            search_view.loadUrl(url);
        } else {
            search_view.setVisibility(View.GONE);
            ly_wifi.setVisibility(View.VISIBLE);
        }

    }

    @Override
    public void initData() {

    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.iv_search_back:
                KeyBoardManager.closeInput(this, et_input_keywords);
                finish();
                overridePendingTransition(R.anim.anim_xv, R.anim.anim_act_right_out);
                break;
            case R.id.tv_search_cancel:
                KeyBoardManager.closeInput(this, et_input_keywords);
                et_input_keywords.clearFocus();
                Intent intent = new Intent(Search_Result_Activity.this, MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                finish();
                overridePendingTransition(R.anim.anim_xv, R.anim.anim_act_right_out);
                break;

            case R.id.iv_search_delete:
                et_input_keywords.setText("");
                iv_search_delete.setVisibility(View.GONE);
                break;

            case R.id.btn_no_wifi_refresh:
                if (NetWorkUtils.getNetworkStatus(this)) {
                    ly_wifi.setVisibility(View.GONE);
                    search_view.setVisibility(View.VISIBLE);
                    search_view.loadUrl(url);
                } else {
                    search_view.setVisibility(View.GONE);
                    ly_wifi.setVisibility(View.VISIBLE);
                }
                break;
            case R.id.view_lucency:
                view_lucency.setVisibility(View.GONE);
                break;
            case R.id.et_input_keywords:
//                view_lucency.setVisibility(View.VISIBLE);
                break;
            default:
                break;
        }
    }

    @Override
    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
        if (actionId == EditorInfo.IME_ACTION_SEARCH) {
            // 先隐藏键盘
            KeyBoardManager.closeInput(this, et_input_keywords);
            String keyword = et_input_keywords.getText().toString();
            if (!keyword.equals("")) {
                // 重新加载url
                if ("all".equals(flag)) {
                    url = UrlUtils.SERVER_WEB_SEARCH_RESULT + "keyword="
                            + keyword + "&type=all" + "&lng=" + lng + "&lat="
                            + lat;
                } else if ("teacher".equals(flag)) {
                    url = UrlUtils.SERVER_WEB_SEARCH_RESULT + "keyword="
                            + keyword + "&type=teacher" + "&lng=" + lng
                            + "&lat=" + lat;
                }
                search_view.loadUrl(url);

            }
            et_input_keywords.clearFocus();

            return true;
        }
        return false;
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count,
                                  int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        if (s.length() == 0) {
            iv_search_delete.setVisibility(View.GONE);
        } else {
            iv_search_delete.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void afterTextChanged(Editable s) {

    }

    @Override
    public String getPageName() {
        return getString(R.string.title_act_search_result);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (ev.getAction() == MotionEvent.ACTION_DOWN) {
            View v = getCurrentFocus();
            if (isShouldHideInput(v, ev)) {

                InputMethodManager imm =
                        (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                if (imm != null) {
                    imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                }
            }
            return super.dispatchTouchEvent(ev);
        }
        // 必不可少，否则所有的组件都不会有TouchEvent了
        return getWindow().superDispatchTouchEvent(ev) || onTouchEvent(ev);
    }

    private boolean isShouldHideInput(View v, MotionEvent event) {
        if (v != null && (v instanceof EditText)) {
            int[] leftTop = {0, 0};
            // 获取输入框当前的location位置
            v.getLocationInWindow(leftTop);
            int left = leftTop[0];
            int top = leftTop[1];
            int bottom = top + v.getHeight();
            int right = left + v.getWidth();
            return !(event.getX() > left && event.getX() < right
                    && event.getY() > top && event.getY() < bottom);
        }
        return false;
    }

    @Override
    public boolean onKey(View v, int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_ENTER && event.getAction() == KeyEvent.ACTION_DOWN) {
            // 先隐藏键盘
            KeyBoardManager.closeInput(Search_Result_Activity.this, et_input_keywords);
            String keyword = et_input_keywords.getText().toString();
            if (!keyword.equals("")) {
                // 重新加载url
                if ("all".equals(flag)) {
                    url = UrlUtils.SERVER_WEB_SEARCH_RESULT + "keyword="
                            + keyword + "&type=all" + "&lng=" + lng + "&lat="
                            + lat;
                } else if ("teacher".equals(flag)) {
                    url = UrlUtils.SERVER_WEB_SEARCH_RESULT + "keyword="
                            + keyword + "&type=teacher" + "&lng=" + lng
                            + "&lat=" + lat;
                }
                search_view.loadUrl(url);

            }
            et_input_keywords.clearFocus();

            return true;
        }
        return false;
    }
}
