package com.clara.bismark439.ratemyhangar.suggestion;

import com.clara.bismark439.ratemyhangar.plane;

public class Entry {
    String name;
    int score = 0;
    plane mPlane;

    public Entry(String name, int score, plane ju87) {
        this.name = name;
        this.score=score;
        this.mPlane = ju87;
    }
    public void increment(int score){
        this.score = this.score+score;
    }
    public String toString(){
        return this.name+","+this.score;
    }
    public int getScore(){
        return this.score;
    }
    public String getName(){
        return this.name;
    }


}
