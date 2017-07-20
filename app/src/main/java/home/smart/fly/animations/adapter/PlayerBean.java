package home.smart.fly.animations.adapter;

/**
 * Created by engineer on 2017/7/18.
 */

public class PlayerBean {

    /**
     * person_id : 119
     * person_img : http://img.dongqiudi.com/data/personpic/119.png
     * name : 梅西
     */

    private String person_id;
    private String person_img;
    private String name;
    private boolean isSelected;
    //每个球员在场上的位置[0-10],对应 BallGameView 上的数组
    private int position;

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }

    public String getPerson_id() {
        return person_id;
    }

    public void setPerson_id(String person_id) {
        this.person_id = person_id;
    }

    public String getPerson_img() {
        return person_img;
    }

    public void setPerson_img(String person_img) {
        this.person_img = person_img;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
