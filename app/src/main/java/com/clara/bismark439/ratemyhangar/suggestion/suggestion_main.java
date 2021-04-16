package com.clara.bismark439.ratemyhangar.suggestion;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.clara.bismark439.ratemyhangar.CV;
import com.clara.bismark439.ratemyhangar.CVlist;
import com.clara.bismark439.ratemyhangar.R;
import com.clara.bismark439.ratemyhangar.plane;

import static com.clara.bismark439.ratemyhangar.MainActivity.All;
import static com.clara.bismark439.ratemyhangar.crawl.tempList;
import static com.clara.bismark439.ratemyhangar.suggestion.Suggestion.buildNetwork;
import static com.clara.bismark439.ratemyhangar.suggestion.Suggestion.myNet;


public class suggestion_main extends AppCompatActivity {
    int[]countryG = new int[7];
    int[]countryA = new int[7];
    public static int[] weightA = new int[10];
    public static int[] weightG = new int[10];
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_suggestion_main);
        tempList.sortByBattles();
        drawTop10();
        buildNetwork(tempList,10);
        drawSuggestion(tempList);
    }
    public void drawTop10(){
    //top 10
    LinearLayout airTop10 =  findViewById(R.id.air_top10);
    System.out.println("Drawing top10");
    weightA = new int[10];
    weightG = new int[10];
    int i=0;
    int numG=1;
    int numA=1;
    while((numA<11||numG<11)&&i<tempList.getSize()){
        CV temp = tempList.get(i);
        if(isTank(temp.id)){
            if(numG<11) {
                numG++;
            }
        }else if(numA<11){
            TextView tx = new TextView(this);
            tx.setText((numA) + "."+temp.name);
            airTop10.addView(tx);
            numA++;
            plane ju87 = All.getPlaneByName(temp.id);
            countryA[ju87.getCountry()]++;
            weightA[ju87.getRank()]++;
        }
        i++;
    }
}

    public void drawSuggestion(CVlist list){
        LinearLayout airTop = (LinearLayout) findViewById(R.id.air_10recco);
        LinearLayout airTop10 = (LinearLayout) findViewById(R.id.air_10recco2);
        int suggestedAir = 1;
        int suggestedGr = 1;
        int suggestedAir2 = 1;
        int suggestedGr2 = 1;
        int index=0;
        boolean flag_reachedMax=false;
        while(!flag_reachedMax){
            Entry temp = myNet.get(index);
            TextView tx = new TextView(this);
            CV ju87= list.getCVByName(temp.getName());
            if(isTank(temp.getName())){
                if(ju87==null){//I dont have it
                    if(suggestedGr2<11){
                        suggestedGr2++;
                    }
                }else{
                    if(suggestedGr<11){
                        suggestedGr++;
                    }
                }
            }else{
                if(ju87==null){//I dont have it
                    if(suggestedAir2<11){
                        plane bf = All.getPlaneByName(temp.getName());
                        tx.setText(suggestedAir2+". "+bf.getName());//+" : "+temp.getScore());
                        suggestedAir2++;
                        airTop10.addView(tx);
                    }
                }else{
                    if(suggestedAir<11){
                        tx.setText(suggestedAir+". "+ju87.name);//+" : "+temp.getScore());
                        suggestedAir++;
                        airTop.addView(tx);
                  }
                }
            }
            index++;
            if(index>=myNet.size()||(suggestedAir>=11)&&(suggestedGr2>=11)&&(suggestedGr>=11)&&(suggestedAir2>=11)){
                flag_reachedMax=true;
            }
        }
    }
    public boolean isTank(String id){
       // return All.getPlaneByName(id).isTank();
        boolean result = false;
        String[] token = id.split("_");
        if(token[0].equals("germ")||token[0].equals("us")||token[0].equals("ussr")||token[0].equals("uk")||token[0].equals("jp")||token[0].equals("fr")||token[0].equals("it"))
        {result = true;}
        return result;
    }
}
