package home.smart.fly.animations.ui.activity.jianshu.helper;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;

/**
 * Created by engineer on 2017/3/18.
 * <p>
 * <p>
 * <img data-original-src="//upload-images.jianshu.io/upload_images/1115031-cd84121ead151fe6.jpg"
 * data-original-width="1920" data-original-height="1080" data-original-format="image/jpeg"
 * data-original-filesize="466676" class=""style="cursor: zoom-in;"
 * src="//upload-images.jianshu.io/upload_images/1115031-cd84121ead151fe6.jpg?imageMogr2/auto-orient/strip%7CimageView2/2/w/1000/format/webp">
 */

public class HackTool {

    private static final String JIAN_SHU_CSS = "<head>\n" +
            "    <meta charset=\"UTF-8\">\n" +
            "    <style type=\"text/css\">@charset \"UTF-8\";\n" +
            "    .image-package .image-container {\n" +
            "        position: relative;\n" +
            "        z-index: 2;\n" +
            "        background-color: #eee;\n" +
            "        -webkit-transition: background-color .1s linear;\n" +
            "        -o-transition: background-color .1s linear;\n" +
            "        transition: background-color .1s linear;\n" +
            "        margin: 0 auto\n" +
            "    }\n" +
            "\n" +
            "    body.reader-night-mode .image-package .image-container {\n" +
            "        background-color: #545454\n" +
            "    }\n" +
            "\n" +
            "    .image-package .image-container-fill {\n" +
            "        z-index: 1\n" +
            "    }\n" +
            "\n" +
            "    .image-package .image-container .image-view {\n" +
            "        position: absolute;\n" +
            "        top: 0;\n" +
            "        left: 0;\n" +
            "        width: 100%;\n" +
            "        height: 100%;\n" +
            "        overflow: hidden\n" +
            "    }\n" +
            "\n" +
            "    .image-package .image-container .image-view-error:after {\n" +
            "        content: \"图片获取失败，请点击重试\";\n" +
            "        position: absolute;\n" +
            "        top: 50%;\n" +
            "        left: 50%;\n" +
            "        width: 100%;\n" +
            "        -webkit-transform: translate(-50%, -50%);\n" +
            "        -ms-transform: translate(-50%, -50%);\n" +
            "        transform: translate(-50%, -50%);\n" +
            "        color: #888;\n" +
            "        font-size: 14px\n" +
            "    }\n" +
            "\n" +
            "    .image-package .image-container .image-view img.image-loading {\n" +
            "        opacity: .3\n" +
            "    }\n" +
            "\n" +
            "    .image-package .image-container .image-view img {\n" +
            "        -webkit-transition: all .15s linear;\n" +
            "        -o-transition: all .15s linear;\n" +
            "        transition: all .15s linear;\n" +
            "        z-index: 2;\n" +
            "        opacity: 1\n" +
            "    }</style>\n" +
            "</head>";


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
//            for (Element ele_img : imgs) {
//                ele_img.attr("style", "max-width:100%;height:auto;");
//            }
            String contentStr = JIAN_SHU_CSS + content.toString();
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
