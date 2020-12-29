package com.engineer.imitate.ui.activity.ninepoint;

/**
 * Created by shity on 2017/8/23/0023.
 */

public class ChatMsgEntity {
    private String name; //消息来自
    private String date; //消息日期
    private String message; //消息内容
    private boolean isComMsg = true; //是否为收到的消息

    public ChatMsgEntity(){

    }

    public ChatMsgEntity(String name, String date, String message, boolean isComMsg){
        this.name = name;
        this.date = date;
        this.message = message;
        this.isComMsg = isComMsg;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public boolean isComMsg() {
        return isComMsg;
    }

    public void setComMsg(boolean comMsg) {
        isComMsg = comMsg;
    }
}
