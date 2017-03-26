package com.yiju.ClassClockRoom.bean.result;

import com.yiju.ClassClockRoom.bean.MessageBoxNoRead;
import com.yiju.ClassClockRoom.bean.base.BaseEntity;

/**
 * 消息阅读处理返回结果
 * Created by geliping on 2016/8/11.
 */
public class MessageBoxNoReadResult extends BaseEntity{
   private MessageBoxNoRead data;

    public MessageBoxNoRead getData() {
        return data;
    }

    public void setData(MessageBoxNoRead data) {
        this.data = data;
    }
}
