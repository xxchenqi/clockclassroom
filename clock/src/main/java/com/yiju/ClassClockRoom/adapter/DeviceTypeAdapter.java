package com.yiju.ClassClockRoom.adapter;

import android.content.Context;
import android.text.Editable;
import android.text.Selection;
import android.text.TextWatcher;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.yiju.ClassClockRoom.R;
import com.yiju.ClassClockRoom.bean.ReservationBean.ReservationDevice;
import com.yiju.ClassClockRoom.util.UIUtils;

import java.util.List;

public class DeviceTypeAdapter extends BaseAdapter {

    private Context mContext;
    private List<ReservationDevice> mList;
    private TextView mTv_price;
    private ImageView iv_item_reduce;
    private ImageView iv_item_add;
    private EditText et_item_device;
    private TextView tv_item_device_name;
    private TextView tv_item_device_content;
    private ReservationDevice data;
    private boolean flag = true;
    private int maxCount;
    private RelativeLayout rl_item_reduce;
    private RelativeLayout rl_item_add;

    public DeviceTypeAdapter(Context context, List<ReservationDevice> devices, TextView tv_price){

        this.mContext = context;
        this.mList = devices;
        this.mTv_price = tv_price;
    }

    @Override
    public int getCount() {
        return mList.size();
    }

    @Override
    public ReservationDevice getItem(int i) {
        return mList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {

        data = getItem(i);
        if(null == view){
            view = View.inflate(mContext, R.layout.item_device_type, null);
        }
        tv_item_device_name = (TextView) view.findViewById(R.id.tv_item_device_name);
        tv_item_device_content = (TextView) view.findViewById(R.id.tv_item_device_content);
        rl_item_reduce = (RelativeLayout) view.findViewById(R.id.rl_item_reduce);
        rl_item_add = (RelativeLayout) view.findViewById(R.id.rl_item_add);
        iv_item_reduce = (ImageView) view.findViewById(R.id.iv_item_reduce);
        iv_item_add = (ImageView) view.findViewById(R.id.iv_item_add);
        et_item_device = (EditText) view.findViewById(R.id.et_item_device);
        rl_item_reduce.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                reduceCount(iv_item_reduce, iv_item_add, et_item_device);
                data.setCount(Integer.valueOf(et_item_device.getText().toString()));
                changePrice();
            }
        });
        rl_item_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addCount(iv_item_reduce, iv_item_add, et_item_device, Integer.valueOf(data.getStock()));
                data.setCount(Integer.valueOf(et_item_device.getText().toString()));
                changePrice();
            }
        });

        if (null != data) {
            Integer count = data.getCount();
            maxCount = Integer.valueOf(data.getStock());
            if (count != Integer.MAX_VALUE) {
                et_item_device.setText(String.valueOf(count));
                if (count == 0) {
                    iv_item_reduce.setImageResource(R.drawable.order_reduce_btn_noclick);
                    rl_item_reduce.setEnabled(false);
                } else {
                    iv_item_reduce.setImageResource(R.drawable.order_reduce_btn_click);
                    rl_item_reduce.setEnabled(true);
                }
            } else {
                et_item_device.setText("0");
                iv_item_reduce.setImageResource(R.drawable.order_reduce_btn_noclick);
            }
            if (et_item_device.getText().toString().equals(data.getStock())) {
                iv_item_add.setImageResource(R.drawable.order_add_btn_noclick);
            } else {
                iv_item_add.setImageResource(R.drawable.order_add_btn_click);
            }
            tv_item_device_name.setText(data.getName());
            tv_item_device_content.setText(String.format(UIUtils.getString(R.string.unit), data.getFee(), data.getUnit()));

            et_item_device.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                    if (!flag) {
                        return;
                    }
                    String counts = charSequence.toString();
                    if (!"".equals(counts)) {
                        if (Integer.valueOf(counts) >= 1) {
                            rl_item_reduce.setEnabled(true);
                            rl_item_add.setEnabled(true);
                            iv_item_reduce.setImageResource(R.drawable.order_reduce_btn_click);
                            if (Integer.valueOf(counts) == maxCount) {
                                iv_item_add.setImageResource(R.drawable.order_add_btn_noclick);
                            } else {
                                iv_item_add.setImageResource(R.drawable.order_add_btn_click);
                            }
                        }
                    } else {
                        iv_item_add.setImageResource(R.drawable.order_reduce_btn_noclick);
                    }
                    if (!"".equals(counts)) {
                        if (Integer.valueOf(counts) >= Integer.valueOf(data.getStock())) {
                            flag = false;
                            et_item_device.setText(data.getStock());
                            data.setCount(Integer.valueOf(data.getStock()));
                            flag = true;
                        } else if (Integer.valueOf(counts) < 1) {
                            flag = false;
                            et_item_device.setText("0");
                            data.setCount(0);
                            flag = true;
                        } else {
                            flag = false;
                            et_item_device.setText(counts);
                            data.setCount(Integer.valueOf(counts));
                            flag = true;
                        }
                    }
                    Selection.setSelection(et_item_device.getText(), et_item_device.length());
                    changePrice();
                }

                @Override
                public void afterTextChanged(Editable editable) {

                }
            });

        }
        return view;
    }

    private void changePrice() {
        int price = 0;
        for (ReservationDevice info :mList) {
            price += (info.getCount()*Double.valueOf(info.getFee()));
        }
        mTv_price.setText("¥"+price);
    }

    /**
     * 减少数量
     *
     * @param ivR 减少按钮
     * @param ivA 增加按钮
     * @param et  显示文本框
     */
    private void reduceCount(ImageView ivR, ImageView ivA, EditText et) {
        rl_item_add.setEnabled(true);
        ivA.setImageResource(R.drawable.order_add_btn_click);
        int count = Integer.valueOf(et.getText().toString());
        count--;
        if (count >= 1) {
            ivR.setImageResource(R.drawable.order_reduce_btn_click);
            et.setText(String.valueOf(count));
            data.setCount(count);
        } else {
            ivR.setImageResource(R.drawable.order_reduce_btn_noclick);
            et.setText("0");
            data.setCount(0);
        }
        Selection.setSelection(et_item_device.getText(), et_item_device.length());
    }

    /**
     * 增加数量
     *
     * @param ivR      减少按钮
     * @param ivA      增加按钮
     * @param et       显示文本框
     * @param maxCount 上限
     */
    private void addCount(ImageView ivR, ImageView ivA, EditText et, int maxCount) {

        if (maxCount > 0) {
            int count = Integer.valueOf(et.getText().toString());
            if (count == 0) {
                ivR.setImageResource(R.drawable.order_reduce_btn_noclick);
            } else {
                ivR.setImageResource(R.drawable.order_reduce_btn_click);
            }
            if (count + 1 >= maxCount) {
                rl_item_add.setEnabled(false);
                ivA.setImageResource(R.drawable.order_add_btn_noclick);
            } else {
                rl_item_add.setEnabled(true);
                ivA.setImageResource(R.drawable.order_add_btn_click);
            }
            if (count == maxCount) {
                et.setText(String.valueOf(count));
                data.setCount(count);
                rl_item_add.setEnabled(false);
                return;
            } else {
                et.setText(String.valueOf(count + 1));
                data.setCount(count + 1);
                ivR.setImageResource(R.drawable.order_reduce_btn_click);
            }
        }
        Selection.setSelection(et_item_device.getText(), et_item_device.length());
    }
}
