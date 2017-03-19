package home.smart.fly.animationdemo.customview.jianshu.helper;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;

/**
 * Created by engineer on 2017/3/18.
 */

public class HackTool {
    public static HtmlBean getInfoFromUrl(String url) {
        HtmlBean htmlBean = null;

        try {
            //获取指定网址的页面内容
            Document document = Jsoup.connect(url).timeout(50000).get();
            String title = document.getElementsByClass("title").get(0).text();
            String username = document.getElementsByClass("name").get(0).getElementsByTag("a").get(0).text();
            String userImg = document.getElementsByClass("avatar").get(0).getElementsByTag("img").get(0).attr("src");
            String publishTime = document.getElementsByClass("publish-time").text();
            String words = document.getElementsByClass("wordage").text();
            Elements content = document.getElementsByClass("show-content");
            Element element = content.get(0);
            Elements imgs = element.getElementsByTag("img");
            for (Element ele_img : imgs) {
                ele_img.attr("style", "max-width:100%;height:auto;");
            }
            String contentStr = content.toString();
            //
            htmlBean = new HtmlBean();
            htmlBean.setContent(contentStr);
            htmlBean.setUsername(username);
            htmlBean.setTitle(title);
            htmlBean.setUserImg(userImg);
            htmlBean.setPublishTime(publishTime.split(" ")[0]);
            htmlBean.setWords(words.split(" ")[1]);
        } catch (IOException e) {
            e.printStackTrace();
        }



        return htmlBean;
    }
}
