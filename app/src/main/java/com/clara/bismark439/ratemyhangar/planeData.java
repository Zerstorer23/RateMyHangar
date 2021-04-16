package com.clara.bismark439.ratemyhangar;

import android.content.Context;
import android.database.Cursor;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import static com.clara.bismark439.ratemyhangar.MainActivity.database;
import static com.clara.bismark439.ratemyhangar.crawl.tempList;

/**
 * Created by Bismark439 on 29/07/2017.
 */


public class planeData {

    private ArrayList<plane> planeDB;

    public planeData() {
        this.planeDB = new ArrayList<plane>();

    }

    public void addP(String a, int b) {
        plane temp = new plane(a, b);
        this.planeDB.add(temp);
    }

    public void addP(plane a) {
        this.planeDB.add(a);
    }

    public int getSize() {
        return this.planeDB.size();
    }

    public plane getP(int i) {
        return this.planeDB.get(i);
    }

    public void addP(String a, int b, int c) {
        plane temp = new plane(a, b, c);
        this.planeDB.add(temp);
    }

    public void addP(String a, int b, int c, int d) {
        plane temp = new plane(a, b, c, d);
        this.planeDB.add(temp);
    }

    public plane getPlaneByName(String id) {
        if (id == null || id.equals("")) {
            return null;
        }
        for (int i = 0; i < this.planeDB.size(); i++) {
            //    System.out.println(id+" VS "+this.planeDB.get(i).id);
            if (this.planeDB.get(i).id.equals(id)) {
                // System.out.println("RETURN!!!! "+this.getSize());
                return this.planeDB.get(i);
            }
        }
        return null;
    }

    public void setNames() {
        for (int i = 0; i < tempList.getSize(); i++) {
            this.getPlaneByName(tempList.get(i).id).name = tempList.get(i).name;
        }

    }

    public void addList(planeData ppp) {
        for (int i = 0; i < ppp.getSize(); i++) this.planeDB.add(ppp.getP(i));
    }

    public void reset() {
        this.planeDB = new ArrayList<plane>();
    }

    public void RRplanes(Context context) throws FileNotFoundException {
        this.planeDB = new ArrayList<plane>();
        try {
            InputStream inputStream = context.getResources().openRawResource(R.raw.rareplanes);//.csv
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
            BufferedReader sc = new BufferedReader(inputStreamReader);
            String temp = "";
            String[] checkV = sc.readLine().split(",");
            if (checkV[0] != null) {//inited
                System.out.println("Data version checked");
                while ((temp = sc.readLine()) != null) {//has next line

                    //   System.out.println(tempest.id+" added ");
                }
            }
            inputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Exception!");
        }

    }

    public void GEplanes(Context context) throws FileNotFoundException {
//ID,GECost,Type,Country
        this.planeDB = new ArrayList<plane>();
        try {
            InputStream inputStream = context.getResources().openRawResource(R.raw.premplanes);//.csv
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
            BufferedReader sc = new BufferedReader(inputStreamReader);
            String temp = "";
            String[] checkV = sc.readLine().split(",");
            if (checkV[0] != null) {//inited
                System.out.println("Data version checked");
                while ((temp = sc.readLine()) != null) {//has next line
                    String[] token = temp.split(",");
                    plane tempest = new plane(token[0]);
                    tempest.point = Integer.parseInt(token[1]);//Cost
                    //Name needed
                    tempest.country = Integer.parseInt(token[3]);//Country
                    tempest.stat = Integer.parseInt(token[2]);//Stat
                    tempest.name = token[4];//NAME
                    tempest.rank = Integer.parseInt(token[5]);//RANK

                    tempest.speed = Integer.parseInt(token[6]);
                    tempest.turn = Double.parseDouble(token[7]);
                    tempest.climb = Double.parseDouble(token[8]);
                    tempest.bmass = Double.parseDouble(token[9]);
                    tempest.br = Double.parseDouble(token[10]);
                    tempest.altitude = Integer.parseInt(token[11]);
                    //Determine type
                    if (token[12].equals("A")) {
                        tempest.attacker = true;
                    } else if (token[12].equals("B")) {
                        tempest.bomber = true;
                    } else {
                        tempest.fighter = true;
                        if (token[13].equals("Interceptor")) {
                            tempest.intercept = true;
                        } else if (token[13].equals("Air Defence")) {
                            tempest.AirDef = true;
                        }
                    }

                    //12 = Subtype
                    //13 = Subclass 15 AB/ 16RB
                    tempest.AB = Integer.parseInt(token[14]);
                    tempest.RB = Integer.parseInt(token[15]);
                    tempest.SB = Integer.parseInt(token[16]);
                    this.planeDB.add(tempest);
                    //  System.out.println(tempest.id+" added ");
                    //기총 추가시 여기 수정
                }
            }
            inputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Exception!");
        }

    }

    public void RPtanks(Context context) throws FileNotFoundException {
        this.planeDB = new ArrayList<plane>();
        try {
            InputStream inputStream = context.getResources().openRawResource(R.raw.rpt);//.csv
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
            BufferedReader sc = new BufferedReader(inputStreamReader);
            String temp = "";
            String[] checkV = sc.readLine().split(",");
            if (checkV[0] != null) {//inited
                System.out.println("Data version checked");
                while ((temp = sc.readLine()) != null) {//has next line
                    String[] token = temp.split(",");
                    plane tempest = new plane(token[0]);
                    tempest.point = Integer.parseInt(token[1]);//RP
                    //Name needed
                    tempest.country = Integer.parseInt(token[3]);//Country
                    tempest.lion = Integer.parseInt(token[2]);//Lion
                    tempest.name = token[4];
                    this.planeDB.add(tempest);
                    //   System.out.println(tempest.id+" added ");
                }
            }
            inputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Exception!");
        }
    }

    public void RRtanks(Context context) throws FileNotFoundException {
        this.planeDB = new ArrayList<plane>();
        try {
            InputStream inputStream = context.getResources().openRawResource(R.raw.raret);//.csv
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
            BufferedReader sc = new BufferedReader(inputStreamReader);
            String temp = "";
            String[] checkV = sc.readLine().split(",");
            if (checkV[0] != null) {//inited
                System.out.println("Data version checked");
                while ((temp = sc.readLine()) != null) {//has next line
                    String[] token = temp.split(",");
                    if (token.length < 1) break;
                    plane tempest = new plane(token[0]);
                    tempest.name = token[3];
                    tempest.stat = 3;
                    this.planeDB.add(tempest);
                    //   System.out.println(tempest.id+" added ");
                }
            }
            inputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Exception!");
        }

    }

    public void GEtanks(Context context) throws FileNotFoundException {
        //  ID,Cost,Type,Country
        this.planeDB = new ArrayList<plane>();
        try {
            InputStream inputStream = context.getResources().openRawResource(R.raw.premtanks);//.csv
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
            BufferedReader sc = new BufferedReader(inputStreamReader);
            String temp = "";
            String[] checkV = sc.readLine().split(",");
            if (checkV[0] != null) {//inited
                System.out.println("Data version checked");
                while ((temp = sc.readLine()) != null) {//has next line
                    String[] token = temp.split(",");
                    plane tempest = new plane(token[0]);
                    tempest.point = Integer.parseInt(token[1]);//Cost
                    //Name needed
                    tempest.country = Integer.parseInt(token[3]);//Country
                    tempest.stat = Integer.parseInt(token[2]);//type
                    tempest.name = token[4];
                    this.planeDB.add(tempest);
                    //  System.out.println(tempest.id+" added ");
                }
            }
            inputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Exception!");
        }

    }

    public void loadSQL(String table_name) {
        Cursor c = database.getTable(table_name);
        switch (table_name) {
            case "air_normal":
                parse_air_normal(c);
                break;
            case "air_premium":
                parse_air_prem(c);
                break;
            case "air_rare":
                parse_air_rare(c);
                break;
            case "ground_normal":
                parse_ground_normal(c);
                break;
            case "ground_premium":
                parse_ground_prem(c);
                break;
            case "ground_rare":
                parse_ground_rare(c);
                break;
        }

    }

    private void parse_air_normal(Cursor c) {
        boolean canNext;
        do {
            plane tempest = new plane(c.getString(0));
            tempest.point = c.getInt(1);//RP
            //Name needed
            tempest.country = c.getInt(3);//Country
            tempest.lion = c.getInt(2);//Lion
            tempest.name = c.getString(4);
            //System.out.println(tempest.id+" added with size "+token.length);
            //기총 추가시 여기 수정
            tempest.speed = c.getInt(5);
            tempest.turn = c.getDouble(6);
            tempest.climb = c.getDouble(7);
            tempest.bmass = c.getDouble(8);
            if (tempest.bmass < 0) {
                tempest.bmass = 0;
            } else {
                //  tempest.altitude=Integer.parseInt(token[10]);
            }
            tempest.br = c.getDouble(9);
            //11 = type
            //Determine type
            String role = c.getString(11);
            if (role.equals("A")) {
                tempest.attacker = true;
            } else if (role.equals("B")) {
                tempest.bomber = true;
            } else {
                tempest.fighter = true;
                if (c.getString(12).equals("Interceptor")) {
                    tempest.intercept = true;
                } else if (c.getString(12).equals("Air Defence")) {
                    tempest.AirDef = true;
                }
            }

            //12 = Subtype
            //13 = Subclass 15 AB/ 16RB
            tempest.rank = c.getInt(14);
            tempest.AB = c.getInt(15);
            tempest.RB = c.getInt(16);
            tempest.SB = c.getInt(17);
            this.planeDB.add(tempest);
          //  System.out.println(tempest.toString());
            canNext = c.moveToNext();
        } while (canNext);
    }

    private void parse_air_prem(Cursor c) { //Same as normal
        boolean canNext;
        do {
            plane tempest = new plane(c.getString(0));
            tempest.point = c.getInt(1);//Cost
            //Name needed
            tempest.country = c.getInt(3);//Country
            tempest.stat = c.getInt(2);//Stat
            tempest.name = c.getString(4);//NAME
            tempest.rank = c.getInt(5);//RANK

            tempest.speed = c.getInt(6);
            tempest.turn = c.getDouble(7);
            tempest.climb = c.getDouble(8);
            tempest.bmass = c.getDouble(9);
            tempest.br = c.getDouble(10);
            tempest.altitude = c.getInt(11);
            //Determine type
            String role = c.getString(12);
            if (role.equals("A")) {
                tempest.attacker = true;
            } else if (role.equals("B")) {
                tempest.bomber = true;
            } else {
                tempest.fighter = true;
                String subRole = c.getString(13);
                if (subRole.equals("Interceptor")) {
                    tempest.intercept = true;
                } else if (subRole.equals("Air Defence")) {
                    tempest.AirDef = true;
                }
            }

            //12 = Subtype
            //13 = Subclass 15 AB/ 16RB
            tempest.AB = c.getInt(14);
            tempest.RB =c.getInt(15);
            tempest.SB = c.getInt(16);
            this.planeDB.add(tempest);
          //  System.out.println(tempest.toString());
            canNext = c.moveToNext();
        } while (canNext);
    }

    private void parse_air_rare(Cursor c) {
        boolean canNext;
        do {
            plane tempest = new plane(c.getString(0));
            tempest.name = c.getString(3);
            tempest.stat = 3;
            this.planeDB.add(tempest);
        //    System.out.println(tempest.toString());
            canNext = c.moveToNext();
        } while (canNext);
    }

    private void parse_ground_normal(Cursor c) {
        boolean canNext;
        do {
            plane tempest = new plane(c.getString(0));
            tempest.point = c.getInt(1);//RP
            //Name needed
            tempest.country = c.getInt(3);//Country
            tempest.lion = c.getInt(2);//Lion
            tempest.name = c.getString(4);
            this.planeDB.add(tempest);
      //      System.out.println(tempest.toString());
            canNext = c.moveToNext();
        } while (canNext);
    }

    private void parse_ground_prem(Cursor c) {
        boolean canNext;
        do {
            plane tempest = new plane(c.getString(0));
            tempest.point = c.getInt(1);//Cost
            //Name needed
            tempest.country = c.getInt(3);//Country
            tempest.stat = c.getInt(2);//type
            tempest.name = c.getString(4);
            this.planeDB.add(tempest);
          //  System.out.println(tempest.toString());
            canNext = c.moveToNext();
        } while (canNext);
    }

    private void parse_ground_rare(Cursor c) {
        boolean canNext;
        do {
            plane tempest = new plane(c.getString(0));
            tempest.name = c.getString(3);
            tempest.stat = 3;
            this.planeDB.add(tempest);
        //    System.out.println(tempest.toString());
            canNext = c.moveToNext();
        } while (canNext);
    }


    public int getRandom() {
        if (this.getSize() < 1) {
            return -1;
        }
        int randIndex = (int) (Math.random() * (this.getSize()));
        return randIndex;
    }

    public plane removeAt(int i) {
        return this.planeDB.remove(i);
    }

    public ArrayList<plane> getPlaneDB() {
        return planeDB;
    }

    public void sortbyID() {
        Collections.sort(this.planeDB, new Comparator<plane>() {
            @Override
            public int compare(plane o1, plane o2) {
                return o1.id.compareTo(o2.id);
            }
        });
    }
}


class DataCompare implements Comparator<CV> {

    @Override
    public int compare(CV o1, CV o2) {
        return o1.id.compareTo(o2.id);
    }
}

class graphData {
    ArrayList<int[]> data;
    double graphSum;
    double mySum;

    public graphData() {
        this.data = new ArrayList<int[]>();//min max point
        double graphSum = 0;
    }

    public void init(Context context) throws FileNotFoundException {
        try {
            InputStream inputStream = context.getResources().openRawResource(R.raw.stat1);//.csv
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
            BufferedReader sc = new BufferedReader(inputStreamReader);
            String temp = "";
            String[] checkV = sc.readLine().split(",");
            if (checkV[0] != null) {//inited
                System.out.println("STAT1: Data version checked");
                while ((temp = sc.readLine()) != null) {//has next line
                    String[] token = temp.split(",");
                    int[] tempest = new int[3];
                    tempest[0] = Integer.parseInt(token[0]);//Min
                    tempest[1] = Integer.parseInt(token[1]);//Max
                    tempest[2] = Integer.parseInt(token[2]);//point
                    //  System.out.println("STAT1: Adding "+tempest[1]);
                    this.graphSum = this.graphSum + tempest[2];
                    data.add(tempest);
                }
            }
            inputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Exception!");
        }

    }

    public double calculatePercentile(double mine) {
        if (this.data.size() == 0) {
            return 12.5;
        }
        int i = 0;
        System.out.println("CalcPerc: my money" + mine);
        this.mySum = 0;
        if (mine > 2500) {
            mine = data.get(data.size() - 1)[2] - 10;
        }
        while (mine > data.get(i)[1]) {//i++ if bigger than this max
            mySum = mySum + data.get(i)[2];
            i++;//at this point i indicates my bin
        }

        System.out.println("CalcPerc: found percentile " + data.get(i)[0] + "~ " + data.get(i)[1]);
        double ju87 = (mine - (data.get(i)[0])) / (data.get(i)[1] - data.get(i)[0]) * data.get(i)[2];
        mySum = mySum + ju87;
        double result = 1 - (mySum / this.graphSum);
        return result * 100;
    }
}