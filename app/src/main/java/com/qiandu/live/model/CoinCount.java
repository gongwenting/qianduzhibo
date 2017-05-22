package com.qiandu.live.model;

/**
 * Created by zhao on 2017/4/17.
 */

public class CoinCount {

    public int money;
    private int score;
    public int getMoney() {
        return money;
    }

    public void setMoney(int money) {
//        this.money = money;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    @Override
    public String toString() {
        return "CoinCount{" +
//                "money='" + money + '\'' +
                ", score='" + score + '\'' +
                '}';
    }
}
