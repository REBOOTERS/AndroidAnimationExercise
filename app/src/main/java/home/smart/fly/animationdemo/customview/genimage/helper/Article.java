package home.smart.fly.animationdemo.customview.genimage.helper;

import java.io.Serializable;


public class Article implements Serializable{
    //内容
    public String strData;
    //标题
    public String strTitle;

    public Article(String strData, String strTitle) {
        this.strData = strData;
        this.strTitle = strTitle;
    }
}
