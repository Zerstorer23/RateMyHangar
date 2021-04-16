package com.clara.bismark439.ratemyhangar;

import android.support.annotation.NonNull;

/**
 * Created by Bismark439 on 03/08/2017.
 */

public class UserInfo {
    public String name;
    public boolean init = false;
    public String title;
    public String level;
    public String date;
    public String img;
    public String[][]inventory= new String[7][4];//[CountryID][cName + Vehicle = 4]  0-ID 1-Vehicles 2-Spaded 3- medals
    public UserInfo(){

    }
    @NonNull
    public String toString(){
        String res="UserInfo,1.0\n";
        res=res+this.name+","+this.title+","+this.level+","+this.date+","+this.img+"\nInventory\n";

        for(int y=0;y<7;y++){
            for(int x=0;x<4;x++){
            res=res+this.inventory[y][x]+",";
            }
            res=res+"\n";
        }

        return res;
    }
}
