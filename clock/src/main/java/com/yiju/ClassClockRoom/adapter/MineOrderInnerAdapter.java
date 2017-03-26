package com.yiju.ClassClockRoom.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.yiju.ClassClockRoom.R;
import com.yiju.ClassClockRoom.adapter.holder.ViewHolder;
import com.yiju.ClassClockRoom.bean.Order2;
import com.yiju.ClassClockRoom.util.UIUtils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class MineOrderInnerAdapter extends CommonBaseAdapter<Order2> {

    public MineOrderInnerAdapter(Context context, List<Order2> datas,
                                 int layoutId) {
        super(context, datas, layoutId);
    }

    @Override
    public void convert(ViewHolder holder, Order2 o) {
        holder.setText(R.id.tv_item_mineorder_sname, o.getSname())
                .setText(R.id.tv_item_mineorder_use_desc,
                        o.getType_desc())
                .setText(R.id.tv_item_mineorder_room_count,
                        String.format(
                                UIUtils.getString(R.string.max_member_text),
                                o.getMax_member()
                        ))
                .setText(
                        R.id.tv_item_mineorder_date,
                        String.format(
                                UIUtils.getString(R.string.to_symbol),
                                o.getStart_date(),
                                o.getEnd_date()
                        ))
                .setText(R.id.tv_total_hour,
                        String.format(
                                UIUtils.getString(R.string.total_hour_text),
                                o.getTotal_hour()
                        ));

        ImageView iv = holder.getView(R.id.iv_item_mineorder_pic);
        Glide.with(mContext).load(o.getPic_url()).into(iv);

    }


   /* private String getDeviceFree(List<DeviceEntity> device_free) {
        if (device_free != null && device_free.size() != 0) {
            StringBuilder sb = new StringBuilder();
            sb.append("免费设施:");
            for (int i = 0; i < device_free.size(); i++) {
                DeviceEntity dev = device_free.get(i);
                if (i == device_free.size() - 1) {
                    sb.append(dev.getDevice_name()).append(dev.getNum())
                            .append(dev.getUnit());
                } else {
                    sb.append(dev.getDevice_name()).append(dev.getNum()).append(dev.getUnit()).append("; ");
                }
            }
            return sb.toString();
        } else {
            return "免费设施:无";
        }
    }*/

    /*private String getDeviceNoFree(List<DeviceEntity> device_no_free) {
        if (device_no_free != null && device_no_free.size() != 0) {
            StringBuilder sb = new StringBuilder();
            sb.append("收费设施:");
            for (int i = 0; i < device_no_free.size(); i++) {
                DeviceEntity dev = device_no_free.get(i);
                if (i == device_no_free.size() - 1) {
                    sb.append(dev.getDevice_name()).append(dev.getNum())
                            .append(dev.getUnit());
                } else {
                    sb.append(dev.getDevice_name()).append(dev.getNum()).append(dev.getUnit()).append("; ");
                }
            }
            return sb.toString();
        } else {
            return "收费设施:无";
        }
    }*/


//	private String getDevice(ArrayList<Device> device) {
//		if (device != null) {
//
//			StringBuilder sb = new StringBuilder();
//			sb.append("配套设施:");
//			for (int i = 0; i < device.size(); i++) {
//				Device dev = device.get(i);
//				if (i == device.size() - 1) {
//					sb.append(dev.getDevice_name()).append(dev.getNum())
//							.append(dev.getUnit());
//				} else {
//					sb.append(dev.getDevice_name()).append(dev.getNum())
//							.append(dev.getUnit() + "; ");
//				}
//			}
//			return sb.toString();
//		} else {
//			return "配套设施:无";
//		}
//	}

    /*
     * 替换日期格式
     */
    private String replaceDate(String Date) {
        return Date.replaceAll("-", "/");
    }

    /*
     * 重复日期拼接
     */
    private String getRepeatWeek(String repeat) {
        // repeat: "2,3,4,5,6"
        if ("".equals(repeat)) {
            return "每天";
        } else {
            String[] repeats = repeat.split(",");
            StringBuilder sb = new StringBuilder();
            String week = "";

            for (int i = 0; i < repeats.length; i++) {
                Integer valueOf = Integer.valueOf(repeats[i]);

                switch (valueOf) {
                    case 1:
                        week = "周一";
                        break;
                    case 2:
                        week = "周二";
                        break;
                    case 3:
                        week = "周三";
                        break;
                    case 4:
                        week = "周四";
                        break;
                    case 5:
                        week = "周五";
                        break;
                    case 6:
                        week = "周六";
                        break;
                    case 7:
                        week = "周日";
                        break;
                    default:
                        break;
                }

                if (i == repeats.length - 1) {
                    sb.append(week);
                } else {
                    sb.append(week).append("、");
                }

            }
            return sb.toString();

        }

    }

    /*
     * 日期转星期
     */
    @SuppressLint("SimpleDateFormat")
    private String getFullDateWeekTime(String sDate) {
        try {
            String formatS = "yyyy-MM-dd";
            SimpleDateFormat format = new SimpleDateFormat(formatS);
            Date date = format.parse(sDate);
            format.applyPattern("E");
            return format.format(date);
        } catch (Exception ex) {
            return "";
        }
    }

    /*
     * 时间转换
     */
    private String changeTime(String start_time) {
        String h;
        String m;
        if (start_time.length() < 4) {
            h = "0" + start_time.substring(0, 1);
            m = start_time.substring(1);
        } else {
            h = start_time.substring(0, 2);
            m = start_time.substring(2);
        }
        return h + ":" + m;
    }

}
