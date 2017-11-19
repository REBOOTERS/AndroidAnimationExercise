package home.smart.fly.animations.customview.polygon;

/**
 * Created by engineer on 2017/11/19.
 */

public class PlayerAbility {
    private int value;
    private String name;

    public PlayerAbility(String name, int value) {
        this.value = value;
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }
}
