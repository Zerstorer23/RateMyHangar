package com.clara.bismark439.ratemyhangar;

/**
 * Created by Bismark439 on 03/01/2018.
 */
public class plane {
    public String type;
    public String id;
    int point;
    int stat; //2 == bundle
    int lion;
    double climb;
    double br;
    double turn;
    double bmass;
    int speed;
    int altitude;
    int RB;
    int AB;
    int SB;
    double appPerc;
    boolean AirDef=false;
    boolean intercept=false;
    boolean fighter=false;
    boolean bomber=false;
    boolean attacker=false;
    boolean isTank = false;
    public int rank;
    int country;//0 USA 1 Germany 2 USSR 3 Brit 4 JAP 5 Italy 6 France
    String name;
    public plane (String id){
        this.id=id;
    }
    public plane(String id, int point) {
        this.id = id;
        this.point = point;
    }

    public plane(String id, int point, int stat, int country) {
        this.id = id;
        this.point = point;
        this.stat = stat;
        this.country=country;
    }
    public plane(String id, int point, int country) {
        this.id = id;
        this.point = point;
        this.country = country;
    }

    public String getId() {
        return id;
    }

    public int getPoint() {
        return point;
    }

    public int getStat() {
        return stat;
    }

    public double getAppPerc() {
        return appPerc;
    }

    public void setAppPerc(double appPerc) {
        this.appPerc = appPerc;
    }

    public int getRank() {
        return rank;
    }

    public int getLion() {
        return lion;
    }

    public double getClimb() {
        return climb;
    }

    public double getBr() {
        return br;
    }

    public double getTurn() {
        return turn;
    }

    public double getBmass() {
        return bmass;
    }

    public int getSpeed() {
        return speed;
    }

    public boolean isFighter() {
        return fighter;
    }

    public boolean isBomber() {
        return bomber;
    }

    public boolean isAttacker() {
        return attacker;
    }

    public int getAltitude() {
        return altitude;
    }

    public int getRB() {
        return RB;
    }

    public int getAB() {
        return AB;
    }

    public int getSB() {
        return SB;
    }

    public boolean isAirDef() {
        return AirDef;
    }

    public boolean isIntercept() {
        return intercept;
    }

    public int getCountry() {
        return country;
    }

    public boolean isTank() {
        return isTank;
    }

    public String getName() {
        return name;
    }

    public String toString(){
    String aa = this.name+" "+this.getId()+" "+this.point+" "+this.lion;
    return aa;
    }
}