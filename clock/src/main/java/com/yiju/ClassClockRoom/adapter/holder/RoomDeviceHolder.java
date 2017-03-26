package com.yiju.ClassClockRoom.adapter.holder;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.yiju.ClassClockRoom.R;
import com.yiju.ClassClockRoom.BaseApplication;
import com.yiju.ClassClockRoom.bean.ReservationThree.Desc;
import com.yiju.ClassClockRoom.util.StringUtils;
import com.yiju.ClassClockRoom.util.UIUtils;

import java.util.Map;

public class RoomDeviceHolder extends BaseHolder<Desc> implements
        OnClickListener, TextWatcher {

    private TextView tv_three_name;
    private TextView tv_three_count;
    private TextView tv_three_price;
    private ImageView iv_three_add;
    private ImageView iv_three_reduce;
    private EditText et_three_choose;
    private Desc desc;
    private int roomCount;

    public RoomDeviceHolder(Context context, int roomCount) {
        super(context);
        this.roomCount = roomCount;
    }

    @Override
    public View initView(Context context) {
        // 加载布局
        View view = View.inflate(context, R.layout.item_room_filter, null);
        tv_three_name = (TextView) view.findViewById(R.id.tv_three_name);
        tv_three_count = (TextView) view.findViewById(R.id.tv_three_count);
        tv_three_price = (TextView) view.findViewById(R.id.tv_three_price);
        iv_three_add = (ImageView) view.findViewById(R.id.iv_three_add);
        iv_three_reduce = (ImageView) view.findViewById(R.id.iv_three_reduce);
        et_three_choose = (EditText) view.findViewById(R.id.et_three_choose);

        return view;
    }

    @Override
    public void refreshView() {
        desc = getData();
        if (desc == null) {
            return;
        }
        tv_three_name.setText(desc.getName());
        tv_three_count.setText(String.format(UIUtils.getString(R.string.use_desc_content), desc.getUnit()));
        if (Float.valueOf(desc.getFee()) == 0) {
            tv_three_price.setText(R.string.free);
        } else {
            BaseApplication context = (BaseApplication) UIUtils.getContext()
                    .getApplicationContext();
            tv_three_price.setText(String.format(context.getResources()
                    .getString(R.string.unit_price), desc.getFee(), desc
                    .getUnit()));
        }
        int defaultValue;
        int id = Integer.valueOf(desc.getId());// ID = 8 投影仪租赁每小时

        defaultValue = desc.getRecommend();

        if (defaultValue <= 0) {
            iv_three_reduce
                    .setImageResource(R.drawable.order_reduce_btn_noclick);
            iv_three_add.setImageResource(R.drawable.order_add_btn_click);
        } else {
            if (id == 8 && roomCount > 0) {
                if (defaultValue >= roomCount || defaultValue >= desc.getMax()) {
                    iv_three_add
                            .setImageResource(R.drawable.order_add_btn_noclick);
                }
            }
            iv_three_reduce.setImageResource(R.drawable.order_reduce_btn_click);
        }
        et_three_choose.setText(String.valueOf(defaultValue));
        BaseApplication context = (BaseApplication) UIUtils.getContext()
                .getApplicationContext();
        Map<String, Integer> pro = context.getPro();
        pro.put(desc.getId(), defaultValue);

        et_three_choose.addTextChangedListener(this);
        iv_three_add.setOnClickListener(this);
        iv_three_reduce.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        String three_text = et_three_choose.getText().toString().trim();
        int editCount = StringUtils.isNotNullString(three_text) ? Integer
                .valueOf(three_text) : 0;
        int id = Integer.valueOf(desc.getId());
        // 处理点击事件
        switch (v.getId()) {
            case R.id.iv_three_add:
                BaseApplication context = (BaseApplication) UIUtils.getContext()
                        .getApplicationContext();
                Map<String, Integer> pro = context.getPro();
                if (desc == null) {
                    return;
                }
                int maxCount = desc.getMax();

                if (id == 8 && roomCount <= editCount) {
                    editCount = roomCount;
                    iv_three_add.setImageResource(R.drawable.order_add_btn_noclick);
                    iv_three_add.setEnabled(false);
                } else {
                    if (editCount >= maxCount) {
                        Toast.makeText(
                                context,
                                String.format(
                                        context.getResources().getString(
                                                R.string.toast_max), maxCount + "",
                                        desc.getUnit()), Toast.LENGTH_SHORT).show();
                        editCount = maxCount;
                        iv_three_add
                                .setImageResource(R.drawable.order_add_btn_noclick);
                        // iv_three_add.setEnabled(false);
                    } else {
                        editCount++;
                        iv_three_add
                                .setImageResource(R.drawable.order_add_btn_click);
                        // iv_three_add.setEnabled(true);
                    }
                }

                et_three_choose.setText(String.valueOf(editCount));
                pro.put(desc.getId(), editCount);
                break;
            case R.id.iv_three_reduce:
                BaseApplication context1 = (BaseApplication) UIUtils.getContext()
                        .getApplicationContext();
                Map<String, Integer> pro1 = context1.getPro();
                if (id == 8 && roomCount < editCount) {
                    editCount = roomCount;
                }
                if (editCount <= 0) {
                    iv_three_reduce
                            .setImageResource(R.drawable.order_reduce_btn_noclick);
                    // iv_three_reduce.setEnabled(false);
                } else {
                    editCount--;
                    iv_three_reduce
                            .setImageResource(R.drawable.order_reduce_btn_click);
                    // iv_three_reduce.setEnabled(true);
                }
                et_three_choose.setText(String.valueOf(editCount));
                pro1.put(desc.getId(), editCount);
                break;

            default:
                break;
        }
    }

    @Override
    public void afterTextChanged(Editable s) {

    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count,
                                  int after) {
    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        BaseApplication context = (BaseApplication) UIUtils.getContext()
                .getApplicationContext();
        Map<String, Integer> pro = context.getPro();
        String text = s.toString().trim();
        int textCount = StringUtils.isNullString(text) ? 0 : Integer
                .valueOf(text);
        if (desc == null) {
            return;
        }
        int maxCount = desc.getMax();
        if (textCount < 0) {
            et_three_choose.setText(String.valueOf(desc.getRecommend()));
            textCount = desc.getRecommend();
        }

        if (textCount >= maxCount || (desc.getId().equals("8") && textCount >= roomCount)) {
            iv_three_add.setImageResource(R.drawable.order_add_btn_noclick);
            iv_three_add.setEnabled(false);
        } else {
            iv_three_add.setImageResource(R.drawable.order_add_btn_click);
            iv_three_add.setEnabled(true);
        }
        if (textCount <= 0) {
            iv_three_reduce
                    .setImageResource(R.drawable.order_reduce_btn_noclick);
            iv_three_reduce.setEnabled(false);
        } else {
            iv_three_reduce.setImageResource(R.drawable.order_reduce_btn_click);
            iv_three_reduce.setEnabled(true);
        }
        if (textCount > maxCount) {
            Toast.makeText(
                    context,
                    String.format(
                            context.getResources()
                                    .getString(R.string.toast_max), maxCount + "",
                            desc.getUnit()), Toast.LENGTH_SHORT).show();
            et_three_choose.setText(String.valueOf(maxCount));
            textCount = maxCount;
        }
        pro.put(desc.getId(), textCount);
    }

}
