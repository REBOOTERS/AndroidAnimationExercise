package home.smart.fly.animations.customview.polygon;

/**
 * Created by engineer on 2017/11/19.
 */

public class Player {

    private int speed;
    private int power;
    private int defense;
    private int pandai;
    private int pass;
    private int shoot;

    public Player(int speed, int power, int defense, int pandai, int pass, int shoot) {
        this.speed = speed;
        this.power = power;
        this.defense = defense;
        this.pandai = pandai;
        this.pass = pass;
        this.shoot = shoot;
    }

    public void setSpeed(int speed) {
        this.speed = speed;
    }

    public void setPower(int power) {
        this.power = power;
    }

    public void setDefense(int defense) {
        this.defense = defense;
    }

    public void setPandai(int pandai) {
        this.pandai = pandai;
    }

    public void setPass(int pass) {
        this.pass = pass;
    }

    public void setShoot(int shoot) {
        this.shoot = shoot;
    }

    public int getSpeed() {
        return speed;
    }

    public int getPower() {
        return power;
    }

    public int getDefense() {
        return defense;
    }

    public int getPandai() {
        return pandai;
    }

    public int getPass() {
        return pass;
    }

    public int getShoot() {
        return shoot;
    }
}
