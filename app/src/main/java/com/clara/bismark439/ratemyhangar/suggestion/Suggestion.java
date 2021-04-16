package com.clara.bismark439.ratemyhangar.suggestion;


import android.content.Context;

import com.clara.bismark439.ratemyhangar.CVlist;
import com.clara.bismark439.ratemyhangar.R;
import com.clara.bismark439.ratemyhangar.plane;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import static com.clara.bismark439.ratemyhangar.MainActivity.All;
import static com.clara.bismark439.ratemyhangar.suggestion.suggestion_main.weightA;
import static com.clara.bismark439.ratemyhangar.suggestion.suggestion_main.weightG;


public class Suggestion {
    public static boolean networkInit = false;
    public static ArrayList<Entry> myNet = new ArrayList<>();
    static int[][] decrypted;
    static String[] header;

    public static void initialiseDB(){
        decrypted = new int[All.getSize()][All.getSize()];
        header= new String[All.getSize()];
        //Initialise
        for(int i=0;i<header.length;i++){
            header[i]=All.getP(i).id;
        }//Init header
    }
    public static void updateScore(int parent, int child, int score){
        decrypted[parent][child] =decrypted[parent][child]+score;
    }
    public static void buildNetwork(CVlist list, int max){
        //Suggest vehicles based on max num of input. Save to myNet
        myNet = new ArrayList<>();
        if(list.getSize()<max)max = list.getSize();
        //Init first elements
            int index = findIndex(list.get(0).id);
            for(int x=0;x<All.getSize();x++){
                plane bf = All.getP(x);
                Entry g91=new Entry(bf.id,0,bf);
                myNet.add(g91);
            }
//Append next lists
        for(int i=0;i<max;i++){
            index = findIndex(list.get(i).id);
            for(int x=0;x<All.getSize();x++){
                int score = decrypted[index][x];
               // System.out.println(All.getP(x).id+": "+score);
                double ratio = (max-i)/max;
                int weight;
                Entry temp =myNet.get(x);
                if(temp.mPlane.isTank()){
                    weight = weightG[temp.mPlane.rank];
                }else{
                    weight = weightA[temp.mPlane.rank];
                }
                myNet.get(x).score=temp.score+(int)(score *weight/10*ratio);
            }
        }//Add only necessary rows

        //Sort
        Collections.sort(myNet, new Comparator<Entry>() {
            @Override
            public int compare(Entry o1, Entry o2) {
                return o2.score-o1.score;
            }
        });

    }
    public static void readNetwork(Context context) {
        try {
        InputStream inputStream = context.getResources().openRawResource(R.raw.network);//.txt
        InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
        BufferedReader sc = new BufferedReader(inputStreamReader);
            initialiseDB();
            System.out.println("ALLSIZE:"+All.getSize());
            String temp = sc.readLine();
            //Data check
            boolean compat = true;
            String[] header = temp.split(",");
            int head = 0;
            for(int i=0;i<All.getSize();i++){
                if(!All.getP(i).id.equals(header[head])){
                    System.out.println("Mismatch at "+i+": "+header[head]+" vs All:"+All.getP(i).id);
                    compat = false;
                //    head--;
                }else{
                    head++;
                }
            }
//Load Table
            for(int i=0;i<header.length;i++){
                temp = sc.readLine();
                if(temp==null)break;
                String[] field = temp.split(",");
                for(int x=0;x<field.length;x++){
                    decrypted[i][x] = Integer.parseInt(field[x]);
                }
            }
            System.out.println("NET.read finished: ");
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Exception!");
        }
    }
    private static int findIndex(String id){
        int index = 0;
        boolean found = false;
        while(!found){
            if(header[index].equals(id)){
                return index;
            }else{
                index++;
            }
            if(index>=header.length){
                found =true;
                return -1;
            }
        }//Find index
        return index;
    }

}

/*
*  A6M2-N
 0. Battles7
 1. Respawns7
 2. Victories5
 3. Defeats2
 4. Deaths6
 5.Overall air frags0
 6. Overall ground frags0

t: 0. Battles155
08-08 05:55:09.211 23792-23792/com.clara.bismark439.ratemyhangar I/System.out: 1. Respawns157
08-08 05:55:09.211 23792-23792/com.clara.bismark439.ratemyhangar I/System.out: 2. Victories85
08-08 05:55:09.211 23792-23792/com.clara.bismark439.ratemyhangar I/System.out: 3. Defeats70
08-08 05:55:09.211 23792-23792/com.clara.bismark439.ratemyhangar I/System.out: 4. Deaths136
08-08 05:55:09.211 23792-23792/com.clara.bismark439.ratemyhangar I/System.out: 5.
08-08 05:55:09.211 23792-23792/com.clara.bismark439.ratemyhangar I/System.out: 6. Air frags / battle0.8
08-08 05:55:09.211 23792-23792/com.clara.bismark439.ratemyhangar I/System.out: 7. Air frags / death0.9
08-08 05:55:09.211 23792-23792/com.clara.bismark439.ratemyhangar I/System.out: 8. Overall air frags129
08-08 05:55:09.211 23792-23792/com.clara.bismark439.ratemyhangar I/System.out: 9.
08-08 05:55:09.211 23792-23792/com.clara.bismark439.ratemyhangar I/System.out: 10. Ground frags / battle0.7-0.11
08-08 05:55:09.211 23792-23792/com.clara.bismark439.ratemyhangar I/System.out: 11. Ground frags / death0.8-0.1
08-08 05:55:09.211 23792-23792/com.clara.bismark439.ratemyhangar I/System.out: 12. Overall ground frags112
08-08 05:55:09.211 23792-23792/com.clara.bismark439.ratemyhangar I/System.out: 13. General statistics for He 219 A-7
*/