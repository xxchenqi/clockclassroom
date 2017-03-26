package com.yiju.ClassClockRoom.act;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.lidroid.xutils.view.annotation.ViewInject;
import com.yiju.ClassClockRoom.R;
import com.yiju.ClassClockRoom.act.base.BaseActivity;
import com.yiju.ClassClockRoom.adapter.MessageDetailAdapter;
import com.yiju.ClassClockRoom.bean.MessageBox;
import com.yiju.ClassClockRoom.util.UIUtils;

import java.util.ArrayList;
import java.util.List;

public class MessageDetialActivity extends BaseActivity implements View.OnClickListener {

    @ViewInject(R.id.head_back)
    private ImageView head_back;

    @ViewInject(R.id.head_title)
    private TextView head_title;

    @ViewInject(R.id.ll_all)
    private LinearLayout ll_all;
    @ViewInject(R.id.tv_no_message)
    private TextView tv_no_message;

    @ViewInject(R.id.recyclerview)
    private RecyclerView recyclerView;

    private MessageDetailAdapter adapter;
    private int big_type;
    private final int TYPE_ORDER = 1;//订单消息
    private final int TYPE_MECH = 2;//系统提醒
    private final int TYPE_PEIDU = 3;//陪读提醒

    /**
     * 初始化页面
     */
    @Override
    protected void initView() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(UIUtils.getContext());
        recyclerView.setLayoutManager(layoutManager);
        head_back.setOnClickListener(this);

    }

    /**
     * 初始化数据
     */
    @Override
    protected void initData() {
        MessageBox messageBox = (MessageBox) getIntent().getSerializableExtra("messageBox");
        big_type = getIntent().getIntExtra("big_type", 0);
        if (null != messageBox) {
            /**
             * 对消息进行归类（依据后台定义的类型）
             */
//            ll_all.setBackgroundColor(UIUtils.getColor(R.color.white));
            List<MessageBox.MessageData> datas = messageBox.getData();
            ArrayList<MessageBox.MessageData> mOrders = new ArrayList<>();
            ArrayList<MessageBox.MessageData> mMechs = new ArrayList<>();
            ArrayList<MessageBox.MessageData> mPeiDus = new ArrayList<>();
            if (null != datas && datas.size() > 0) {
                for (int i = 0; i < datas.size(); i++) {
                    /*if ("1".equals(datas.get(i).getType()) ||
                            "2".equals(datas.get(i).getType()) ||
                            "7".equals(datas.get(i).getType()) ||
                            "10".equals(datas.get(i).getType()) ||
                            "11".equals(datas.get(i).getType()) ||
                            "12".equals(datas.get(i).getType())) {
                        mOrders.add(datas.get(i));
                    } else if ("3".equals(datas.get(i).getType())) {
                        mPeiDus.add(datas.get(i));
                    } else if ("4".equals(datas.get(i).getType()) ||
                            "5".equals(datas.get(i).getType()) ||
                            "6".equals(datas.get(i).getType()) ||
                            "8".equals(datas.get(i).getType()) ||
                            "9".equals(datas.get(i).getType()) ||
                            "13".equals(datas.get(i).getType())) {
                        mMechs.add(datas.get(i));
                    }
                    */
                    switch (datas.get(i).getBig_type()) {
                        case "" + TYPE_ORDER:
                            mOrders.add(datas.get(i));
                            break;
                        case "" + TYPE_MECH:
                            mMechs.add(datas.get(i));
                            break;
                        case "" + TYPE_PEIDU:
                            mPeiDus.add(datas.get(i));
                            break;
                        default:
                            break;
                    }
                }
                switch (big_type) {
                    case TYPE_ORDER:
                        // 订单
                        showDetialMessaage(getString(R.string.order_mess), mOrders);
                        break;
                    case TYPE_MECH:
                        // 系统
                        showDetialMessaage(getString(R.string.mech_mess), mMechs);
                        break;
                    case TYPE_PEIDU:
                        // 陪读
                        showDetialMessaage(getString(R.string.peidu_mess), mPeiDus);
                        break;
                }
            } else {
                recyclerView.setVisibility(View.GONE);
                tv_no_message.setVisibility(View.VISIBLE);
//                ll_all.setBackgroundResource(R.drawable.empty);
                switch (big_type) {
                    case TYPE_ORDER:
                        // 订单
                        showDetialMessaage(getString(R.string.order_mess), null);
                        break;
                    case TYPE_MECH:
                        // 系统
                        showDetialMessaage(getString(R.string.mech_mess), null);
                        break;
                    case TYPE_PEIDU:
                        // 陪读
                        showDetialMessaage(getString(R.string.peidu_mess), null);
                        break;
                }
            }
        } else {
            recyclerView.setVisibility(View.GONE);
            tv_no_message.setVisibility(View.VISIBLE);
//            ll_all.setBackgroundResource(R.drawable.empty);
        }
    }

    /**
     * 展示消息列表
     *
     * @param s      消息列表页标题
     * @param mLists 消息数据集合
     */
    private void showDetialMessaage(String s, ArrayList<MessageBox.MessageData> mLists) {

        head_title.setText(s);
        if (null != mLists && mLists.size() > 0) {
            recyclerView.setVisibility(View.VISIBLE);
            if (null != adapter) {
                adapter.notifyDataSetChanged();
            } else {
                adapter = new MessageDetailAdapter(mLists);

            }
            recyclerView.setAdapter(adapter);
        } else {
            recyclerView.setVisibility(View.GONE);
//            ll_all.setBackgroundResource(R.drawable.empty);
            tv_no_message.setVisibility(View.VISIBLE);
        }

    }

    @Override
    public String getPageName() {
        if (big_type == TYPE_ORDER) {
            return getString(R.string.title_act_my_message_order);
        } else if (big_type == TYPE_MECH) {
            return getString(R.string.title_act_my_message_mech);
        } else if (big_type == TYPE_PEIDU) {
            return getString(R.string.title_act_my_message_peidu);
        }
        return null;
    }

    @Override
    public int setContentViewId() {
        return R.layout.activity_message_detial;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.head_back:
                finish();
                break;
        }
    }
}
