//package com.yiju.ClassClockRoom.adapter;
//
//import android.content.Context;
//import android.text.Editable;
//import android.text.Selection;
//import android.text.TextWatcher;
//import android.view.View;
//import android.view.View.OnClickListener;
//import android.widget.EditText;
//import android.widget.ImageView;
//
//import com.yiju.ClassClockRoom.R;
//import com.yiju.ClassClockRoom.bean.EditDeviceListNewResult;
//import com.yiju.ClassClockRoom.util.UIUtils;
//
//import java.util.List;
//
//public class OrderEditDetailAdapter extends CommonBaseAdapter<EditDeviceListNewResult.DataEntity> {
//
//    private boolean flag = true;
//    private boolean have; //haveDeviceFree :true 就布置过, false 第一次布置
//
//    public OrderEditDetailAdapter(Context context, List<EditDeviceListNewResult.DataEntity> datas,
//                                  int layoutId,boolean have) {
//        super(context, datas, layoutId);
//        this.have = have;
//    }
//
//    @Override
//    public void convert(final ViewHolder holder, final EditDeviceListNewResult.DataEntity t) {
//        ViewHolder.setFlag(true);
//
//        holder.setText(R.id.tv_device, t.getName() + "数量")
//                .setText(R.id.tv_device_desc,"上课需要" + t.getName() + "的数量");
//
//        flag = false;
//
//        final ImageView iv_item_order_edit_subtract = holder
//                .getView(R.id.iv_reduce);
//        final ImageView iv_item_order_edit_add = holder
//                .getView(R.id.iv_add);
//        final EditText et_item_order_edit_count = holder
//                .getView(R.id.et_count);
//
////        if (have) {//布置过
////            et_item_order_edit_count.setText(t.getNum());
////        } else {//第一次布置
////            et_item_order_edit_count.setText(t.getRecommend());
////        }
////        if (StringUtils.isNotNullString(et_item_order_edit_count.getText().toString().trim())) {
////            t.setCurrentCount(Integer.parseInt(et_item_order_edit_count.getText().toString().trim()));
////        }
//
//        et_item_order_edit_count.setText(String.valueOf(t.getCurrentCount()));
//        final int max = t.getLocalMax();
//
//        iv_item_order_edit_subtract.setOnClickListener(new OnClickListener() {
//
//            @Override
//            public void onClick(View v) {
//                flag = false;
//                // 减少
//                t.setCurrentCount(t.getCurrentCount() - 1);
//                et_item_order_edit_count.setText(String.valueOf(t.getCurrentCount()));
//
//                // 设置减少能否点击
//                if (t.getCurrentCount() > 0) {
//                    iv_item_order_edit_subtract.setClickable(true);
//                    iv_item_order_edit_subtract
//                            .setImageResource(R.drawable.order_reduce_btn_click);
//
//                } else {
//                    iv_item_order_edit_subtract.setClickable(false);
//                    iv_item_order_edit_subtract
//                            .setImageResource(R.drawable.order_reduce_btn_noclick);
//                }
//
//                // 设置增加能否点击
//                if (t.getCurrentCount() >= max) {
//                    iv_item_order_edit_add.setClickable(false);
//                    iv_item_order_edit_add
//                            .setImageResource(R.drawable.order_reduce_btn_noclick);
//                } else {
//                    iv_item_order_edit_add.setClickable(true);
//                    iv_item_order_edit_add
//                            .setImageResource(R.drawable.order_add_btn_click);
//                }
//                flag = true;
//                Selection.setSelection(et_item_order_edit_count.getText(), et_item_order_edit_count.length());
//
//            }
//        });
//
//        iv_item_order_edit_add.setOnClickListener(new OnClickListener() {
//
//            @Override
//            public void onClick(View v) {
//                flag = false;
//                // 增加
//                t.setCurrentCount(t.getCurrentCount() + 1);
//                et_item_order_edit_count.setText(String.valueOf(t.getCurrentCount()));
//
//                // 设置减少能否点击
//                if (t.getCurrentCount() > 0) {
//                    iv_item_order_edit_subtract.setClickable(true);
//                    iv_item_order_edit_subtract
//                            .setImageResource(R.drawable.order_reduce_btn_click);
//                } else {
//                    iv_item_order_edit_subtract.setClickable(false);
//                    iv_item_order_edit_subtract
//                            .setImageResource(R.drawable.order_reduce_btn_noclick);
//                }
//                // 设置增加能否点击
//                if (t.getCurrentCount() >= max) {
//                    iv_item_order_edit_add.setClickable(false);
//                    iv_item_order_edit_add
//                            .setImageResource(R.drawable.order_add_btn_noclick);
//                } else {
//                    iv_item_order_edit_add.setClickable(true);
//                    iv_item_order_edit_add
//                            .setImageResource(R.drawable.order_add_btn_click);
//                }
//                flag = true;
//                Selection.setSelection(et_item_order_edit_count.getText(), et_item_order_edit_count.length());
//            }
//        });
//
//        // 如果当前值>=max 增加不可点击
//        if (t.getCurrentCount() >= max) {
//            iv_item_order_edit_add.setClickable(false);
//            iv_item_order_edit_add
//                    .setImageResource(R.drawable.order_add_btn_noclick);
//            // 如果max=0，et设置0
//            if (max == 0) {
//                et_item_order_edit_count.setText("0");
//                t.setCurrentCount(0);
//            }
//        } else {
//            iv_item_order_edit_add.setClickable(true);
//            iv_item_order_edit_add
//                    .setImageResource(R.drawable.order_add_btn_click);
//        }
//
//        // 设置减少能否点击
//        if (t.getCurrentCount() > 0) {
//            iv_item_order_edit_subtract.setClickable(true);
//            iv_item_order_edit_subtract
//                    .setImageResource(R.drawable.order_reduce_btn_click);
//        } else {
//            iv_item_order_edit_subtract.setClickable(false);
//            iv_item_order_edit_subtract
//                    .setImageResource(R.drawable.order_reduce_btn_noclick);
//        }
//
//        flag = true;
//
//        et_item_order_edit_count.addTextChangedListener(new TextWatcher() {
//
//            @Override
//            public void onTextChanged(CharSequence s, int start, int before,
//                                      int count) {
//                if (!flag) {
//                    return;
//                }
//
//                // 变化
//                if ("".equals(s + "")) {
//                    t.setCurrentCount(0);
//                } else {
//                    int editCount = Integer.valueOf(s + "");
//                    if (editCount > max) {
//                        t.setCurrentCount(Integer.valueOf(t.getNum()));
//                        UIUtils.showToastSafe("编辑数量已超过库存数量,已为您设置推荐数量");
//                    } else {
//                        t.setCurrentCount(editCount);
//                    }
//                }
//
//                // =============================================================
//                // 设置减少能否点击
//                if (t.getCurrentCount() > 0) {
//                    iv_item_order_edit_subtract.setClickable(true);
//                    iv_item_order_edit_subtract
//                            .setImageResource(R.drawable.order_reduce_btn_click);
//                } else {
//                    iv_item_order_edit_subtract.setClickable(false);
//                    iv_item_order_edit_subtract
//                            .setImageResource(R.drawable.order_reduce_btn_noclick);
//                }
//                // 设置增加能否点击
//                if (t.getCurrentCount() >= max) {
//                    iv_item_order_edit_add.setClickable(false);
//                    iv_item_order_edit_add
//                            .setImageResource(R.drawable.order_add_btn_noclick);
//                } else {
//                    iv_item_order_edit_add.setClickable(true);
//                    iv_item_order_edit_add
//                            .setImageResource(R.drawable.order_add_btn_click);
//                }
//
//                flag = false;
//                et_item_order_edit_count.setText(String.valueOf(t.getCurrentCount()));
//                flag = true;
//
//                Selection.setSelection(et_item_order_edit_count.getText(), et_item_order_edit_count.length());
//
//            }
//
//            @Override
//            public void beforeTextChanged(CharSequence s, int start, int count,
//                                          int after) {
//
//            }
//
//            @Override
//            public void afterTextChanged(Editable s) {
//
//            }
//        });
//
//    }
//
//
//}
