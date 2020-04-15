package home.smart.fly.animations.sugar.bean;

/**
 * @authro: Rookie
 * @since: 2018-12-27
 */
public class Item {
    private String title;
    private String subTitle;

    public Item(String title, String subTitle) {
        this.title = title;
        this.subTitle = subTitle;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSubTitle() {
        return subTitle;
    }

    public void setSubTitle(String subTitle) {
        this.subTitle = subTitle;
    }
}
