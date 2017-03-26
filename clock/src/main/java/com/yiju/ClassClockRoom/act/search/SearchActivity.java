package com.yiju.ClassClockRoom.act.search;

import android.content.Intent;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.umeng.analytics.MobclickAgent;
import com.yiju.ClassClockRoom.R;
import com.yiju.ClassClockRoom.act.base.BaseActivity;
import com.yiju.ClassClockRoom.bean.HotSearch;
import com.yiju.ClassClockRoom.common.DataManager;
import com.yiju.ClassClockRoom.common.constant.SharedPreferencesConstant;
import com.yiju.ClassClockRoom.util.KeyBoardManager;
import com.yiju.ClassClockRoom.util.SharedPreferencesUtils;
import com.yiju.ClassClockRoom.util.StringUtils;
import com.yiju.ClassClockRoom.util.UIUtils;
import com.yiju.ClassClockRoom.util.net.ClassEvent;
import com.yiju.ClassClockRoom.util.net.api.HttpClassRoomApi;
import com.yiju.ClassClockRoom.widget.GridViewForScrollView;
import com.yiju.ClassClockRoom.widget.ListViewForScrollView;

import java.util.ArrayList;
import java.util.LinkedList;

/**
 * 作者： 葛立平
 * 搜索首页
 * 2016/3/2 16:45
 */
public class SearchActivity extends BaseActivity {
    private static final int MAX_NOTE_NUMBER = 9;
    @ViewInject(R.id.et_search)
    private EditText et_search;//搜索框
    @ViewInject(R.id.iv_search_delete)
    private ImageView iv_search_delete;//清空搜索框
    @ViewInject(R.id.tv_canlce)
    private TextView tv_canlce;//取消
    @ViewInject(R.id.gv_hot_search)
    private GridViewForScrollView gv_hot_search;//热门搜索列表
    @ViewInject(R.id.tv_notes)
    private TextView tv_notes;//历史记录文字
    @ViewInject(R.id.v_notes_search)
    private View v_notes_search;//历史记录下划线
    @ViewInject(R.id.lv_notes_search)
    private ListViewForScrollView lv_notes_search;//历史记录搜索列表
    @ViewInject(R.id.tv_clean)
    private TextView tv_clean;//清除历史
    @ViewInject(R.id.tv_hot)
    private TextView tv_hot;//热门搜索

    private Gson mGson;
    private LinkedList<String> mNotesList;
    private SearchAdapter searchNoteAdapter;

    private ArrayList<HotSearch> mHotList;
    private SearchHotAdapter searchHotAdapter;

    @Override
    public int setContentViewId() {
        return R.layout.activity_search;
    }

    @Override
    protected void initView() {
        et_search.setImeOptions(EditorInfo.IME_ACTION_SEARCH);
    }

    @Override
    protected void initData() {
        mGson = new Gson();
        initHotGrid();
        initNoteList();
    }

    @Override
    public void initListener() {
        super.initListener();
        gv_hot_search.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                if (mHotList != null && mHotList.get(i) != null) {
                    MobclickAgent.onEvent(UIUtils.getContext(), "v3200_254");
                    String content = mHotList.get(i).getWord().trim();
                    updateNoteData(content);
                    skipResult(content);
                }
            }
        });

        lv_notes_search.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                if (mNotesList != null) {
                    MobclickAgent.onEvent(UIUtils.getContext(), "v3200_255");
                    String content = mNotesList.get(i);
                    updateNoteData(content);
                    skipResult(content);
                }
            }
        });
        tv_canlce.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MobclickAgent.onEvent(UIUtils.getContext(), "v3200_253");
                exitSearch();
            }
        });
        tv_clean.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MobclickAgent.onEvent(UIUtils.getContext(), "v3200_256");
                mNotesList.clear();
                refreshListView();
                closeNotes();
                if (!"-1".equals(StringUtils.getUid())) {
                    SharedPreferencesUtils.saveString(SearchActivity.this, SharedPreferencesConstant.Shared_Search_Notes + StringUtils.getUid(), "");
                }
            }
        });

        et_search.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                if (i == EditorInfo.IME_ACTION_SEARCH) {
                    String keyword = et_search.getText().toString();
                    if (!keyword.equals("")) {
                        updateNoteData(keyword);
                        // 跳转activity
                        skipResult(keyword);
                    }
                    return true;
                }
                return false;
            }
        });
        et_search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (charSequence.length() == 0) {
                    iv_search_delete.setVisibility(View.GONE);
                } else {
                    iv_search_delete.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });

        et_search.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_ENTER && event.getAction() == KeyEvent.ACTION_DOWN) {
                    //先隐藏软键盘
                    KeyBoardManager.closeInput(SearchActivity.this, et_search);
                    String keyword = et_search.getText().toString();
                    if (!keyword.equals("")) {
                        updateNoteData(keyword);
                        // 跳转activity
                        skipResult(keyword);
                    }
                    return true;
                }
                return false;
            }
        });
        iv_search_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                et_search.setText("");
            }
        });
    }

    /**
     * 跳转到结果页
     */
    private void skipResult(String keyword) {
        Intent intent = new Intent(SearchActivity.this,
                Search_Result_Activity.class);
        intent.putExtra("keyword", keyword);
        intent.putExtra("flag", "all");
        startActivity(intent);
        overridePendingTransition(R.anim.anim_act_left_in, R.anim.anim_xv);
    }

    /**
     * 刷新历史记录适配器
     */
    private void refreshListView() {
        if (mNotesList == null) {
            return;
        }
        if (searchNoteAdapter != null) {
            searchNoteAdapter.updateDatas(mNotesList);
        } else {
            searchNoteAdapter = new SearchAdapter(SearchActivity.this,
                    mNotesList, R.layout.item_search_note);
            lv_notes_search.setAdapter(searchNoteAdapter);
        }
    }

    /**
     * 更新历史记录数据
     */
    private void updateNoteData(String content) {
        if (mNotesList.contains(content)) {// 排序
            mNotesList.remove(content);
            mNotesList.addFirst(content);
        } else { // 添加
            mNotesList.addFirst(content);
            if (mNotesList.size() > MAX_NOTE_NUMBER) {
                for (int i = mNotesList.size() - 1; i >= MAX_NOTE_NUMBER; i--) {
                    mNotesList.remove(mNotesList.get(i));
                }
            }
        }
        if (!"-1".equals(StringUtils.getUid())) {
            SharedPreferencesUtils
                    .saveString(SearchActivity.this,
                            SharedPreferencesConstant.Shared_Search_Notes + StringUtils.getUid(),
                            mGson.toJson(mNotesList));
        }
        refreshListView();
        if (mNotesList.size() > 0) {
            openNotes();
        }
    }

    /**
     * 初始化热门列表
     */
    private void initHotGrid() {
        HttpClassRoomApi.getInstance().getSearchHot();
    }

    @Override
    public void onRefreshEvent(ClassEvent<Object> event) {
        super.onRefreshEvent(event);
        if (event.getType() == DataManager.Hot_Search_Data) {//刷新热搜数据
            mHotList = (ArrayList<HotSearch>) event.getData();
            searchHotAdapter = new SearchHotAdapter(SearchActivity.this,
                    mHotList, R.layout.item_search_hot);
            gv_hot_search.setAdapter(searchHotAdapter);
            if (mHotList.size() > 0) {
                tv_hot.setVisibility(View.VISIBLE);
                gv_hot_search.setVisibility(View.VISIBLE);
            }
        }
    }

    /**
     * 初始化历史记录列表
     */
    private void initNoteList() {
        String content = "";
        if (!"-1".equals(StringUtils.getUid())) {
            content = SharedPreferencesUtils
                    .getString(SearchActivity.this,
                            SharedPreferencesConstant.Shared_Search_Notes + StringUtils.getUid(), "");
        }
        if (StringUtils.isNullString(content)) {
            closeNotes();
            mNotesList = new LinkedList<>();
        } else {
            openNotes();
            mNotesList = mGson.fromJson(content, new TypeToken<LinkedList<String>>() {
            }.getType());
            refreshListView();
        }
    }


    /**
     * 关闭历史记录
     */
    private void closeNotes() {
        tv_notes.setVisibility(View.GONE);
        v_notes_search.setVisibility(View.GONE);
        lv_notes_search.setVisibility(View.GONE);
        tv_clean.setVisibility(View.GONE);
    }

    /**
     * 打开历史记录
     */
    private void openNotes() {
        tv_notes.setVisibility(View.VISIBLE);
        v_notes_search.setVisibility(View.VISIBLE);
        lv_notes_search.setVisibility(View.VISIBLE);
        tv_clean.setVisibility(View.VISIBLE);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            exitSearch();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    /**
     * 关闭键盘和页面
     */
    private void exitSearch() {
        onBackPressed();
    }

    @Override
    public void onBackPressed() {
        KeyBoardManager.closeKeyBoard(this);
        super.onBackPressed();
    }

    @Override
    public String getPageName() {
        return getString(R.string.title_act_search);
    }
}
