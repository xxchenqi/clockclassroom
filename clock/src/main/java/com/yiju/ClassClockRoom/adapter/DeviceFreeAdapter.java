package com.yiju.ClassClockRoom.adapter;

import android.text.Editable;
import android.text.Selection;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.yiju.ClassClockRoom.R;
import com.yiju.ClassClockRoom.bean.EditDeviceListNewResult;
import com.yiju.ClassClockRoom.util.StringUtils;
import com.yiju.ClassClockRoom.util.UIUtils;

import java.util.List;

/**
 * 免费设备适配器
 * Created by wh on 2016/5/13.
 */
public class DeviceFreeAdapter extends BaseAdapter {

    private boolean flag = true;
    private List<EditDeviceListNewResult.DataEntity> datas;

    public DeviceFreeAdapter(List<EditDeviceListNewResult.DataEntity> datas) {
        this.datas = datas;
    }

    @Override
    public int getCount() {
        return datas.size();
    }

    @Override
    public Object getItem(int position) {
        return datas.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final ViewHolder holder;
        convertView = null;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = View.inflate(UIUtils.getContext(), R.layout.item_device_free, null);
            holder.tv_device = (TextView) convertView.findViewById(R.id.tv_device);
            holder.tv_device_desc = (TextView) convertView.findViewById(R.id.tv_device_desc);
            holder.rl_reduce = (RelativeLayout) convertView.findViewById(R.id.rl_reduce);
            holder.rl_add = (RelativeLayout) convertView.findViewById(R.id.rl_add);
            holder.iv_reduce = (ImageView) convertView.findViewById(R.id.iv_reduce);//减号
            holder.iv_add = (ImageView) convertView.findViewById(R.id.iv_add);//加号
            holder.et_count = (EditText) convertView.findViewById(R.id.et_count);//数量编辑
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        final EditDeviceListNewResult.DataEntity dataEntity = datas.get(position);
        holder.tv_device.setText(
                String.format(
                        UIUtils.getString(R.string.format_count),
                        dataEntity.getName()
                ));
        holder.tv_device_desc.setText(
                String.format(
                        UIUtils.getString(R.string.format_count_more),
                        dataEntity.getName()
                ));
        holder.et_count.setText(String.valueOf(dataEntity.getCurrentCount()));
        final int max = dataEntity.getLocalMax();

        holder.rl_reduce.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                flag = false;
                if (StringUtils.isNullString(holder.et_count.getText().toString().trim())) {
                    holder.et_count.setText("0");
                }
                // 减少
                dataEntity.setCurrentCount(Integer.parseInt(holder.et_count.getText().toString().trim()) - 1);
                holder.et_count.setText(String.valueOf(dataEntity.getCurrentCount()));

                // 设置减少能否点击
                if (dataEntity.getCurrentCount() > 0) {
                    holder.rl_reduce.setClickable(true);
                    holder.iv_reduce.setImageResource(R.drawable.order_reduce_btn_click);

                } else {
                    holder.rl_reduce.setClickable(false);
                    holder.iv_reduce.setImageResource(R.drawable.order_reduce_btn_noclick);
                }

                // 设置增加能否点击
                if (dataEntity.getCurrentCount() >= max) {
                    holder.rl_add.setClickable(false);
                    holder.iv_add.setImageResource(R.drawable.order_reduce_btn_noclick);
                } else {
                    holder.rl_add.setClickable(true);
                    holder.iv_add.setImageResource(R.drawable.order_add_btn_click);
                }
                flag = true;
                Selection.setSelection(holder.et_count.getText(), holder.et_count.length());
            }
        });

        holder.rl_add.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                flag = false;
                if (StringUtils.isNullString(holder.et_count.getText().toString().trim())) {
                    holder.et_count.setText("0");
                }
                // 增加
                dataEntity.setCurrentCount(Integer.parseInt(holder.et_count.getText().toString().trim()) + 1);
                holder.et_count.setText(String.valueOf(dataEntity.getCurrentCount()));

                // 设置减少能否点击
                if (dataEntity.getCurrentCount() > 0) {
                    holder.rl_reduce.setClickable(true);
                    holder.iv_reduce.setImageResource(R.drawable.order_reduce_btn_click);
                } else {
                    holder.rl_reduce.setClickable(false);
                    holder.iv_reduce.setImageResource(R.drawable.order_reduce_btn_noclick);
                }
                // 设置增加能否点击
                if (dataEntity.getCurrentCount() >= max) {
                    holder.rl_add.setClickable(false);
                    holder.iv_add.setImageResource(R.drawable.order_add_btn_noclick);
                } else {
                    holder.rl_add.setClickable(true);
                    holder.iv_add.setImageResource(R.drawable.order_add_btn_click);
                }
                flag = true;
                Selection.setSelection(holder.et_count.getText(), holder.et_count.length());
            }
        });
        // 如果当前值>=max 增加不可点击
        if (dataEntity.getCurrentCount() >= max) {
            holder.rl_add.setClickable(false);
            holder.iv_add.setImageResource(R.drawable.order_add_btn_noclick);
            // 如果max=0，et设置0
            if (max == 0) {
                dataEntity.setCurrentCount(0);
                holder.et_count.setText("0");
            }
        } else {
            holder.rl_add.setClickable(true);
            holder.iv_add.setImageResource(R.drawable.order_add_btn_click);
        }

        // 设置减少能否点击
        if (dataEntity.getCurrentCount() > 0) {
            holder.rl_reduce.setClickable(true);
            holder.iv_reduce.setImageResource(R.drawable.order_reduce_btn_click);
        } else {
            holder.rl_reduce.setClickable(false);
            holder.iv_reduce.setImageResource(R.drawable.order_reduce_btn_noclick);
        }

        flag = true;

        holder.et_count.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!flag) {
                    return;
                }

                // 变化
                if ("".equals(s + "")) {
                    dataEntity.setCurrentCount(0);
                } else {
                    int editCount = Integer.valueOf(s + "");
                    if (editCount > max) {
                        dataEntity.setCurrentCount(Integer.valueOf(dataEntity.getRecommend()));
                        UIUtils.showToastSafe(UIUtils.getString(R.string.toast_recommend_count));
                    } else {
                        dataEntity.setCurrentCount(editCount);
                    }
                }

                // =============================================================
                // 设置减少能否点击
                if (dataEntity.getCurrentCount() > 0) {
                    holder.rl_reduce.setClickable(true);
                    holder.iv_reduce.setImageResource(R.drawable.order_reduce_btn_click);
                } else {
                    holder.rl_reduce.setClickable(false);
                    holder.iv_reduce.setImageResource(R.drawable.order_reduce_btn_noclick);
                }
                // 设置增加能否点击
                if (dataEntity.getCurrentCount() >= max) {
                    holder.rl_add.setClickable(false);
                    holder.iv_add.setImageResource(R.drawable.order_add_btn_noclick);
                } else {
                    holder.rl_add.setClickable(true);
                    holder.iv_add.setImageResource(R.drawable.order_add_btn_click);
                }

                flag = false;
                holder.et_count.setText(String.valueOf(dataEntity.getCurrentCount()));
                flag = true;
                Selection.setSelection(holder.et_count.getText(), holder.et_count.length());
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });


        return convertView;
    }

    public void setFlag(boolean flag) {
        this.flag = flag;
    }

    class ViewHolder {
        private TextView tv_device;
        private TextView tv_device_desc;
        private ImageView iv_reduce;
        private ImageView iv_add;
        private EditText et_count;
        public RelativeLayout rl_reduce;
        public RelativeLayout rl_add;
    }

}
