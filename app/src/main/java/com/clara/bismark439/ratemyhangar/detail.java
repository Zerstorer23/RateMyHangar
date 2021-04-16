package com.clara.bismark439.ratemyhangar;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TabHost;
import android.widget.TextView;

import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.BarGraphSeries;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

import java.text.DecimalFormat;

import static com.clara.bismark439.ratemyhangar.MainActivity.mInterstitialAd;
import static com.clara.bismark439.ratemyhangar.MainActivity.myCurr;
import static com.clara.bismark439.ratemyhangar.MainActivity.myTotalPracticalMoney;
import static com.clara.bismark439.ratemyhangar.MainActivity.normal;
import static com.clara.bismark439.ratemyhangar.MainActivity.normalT;
import static com.clara.bismark439.ratemyhangar.MainActivity.premium;
import static com.clara.bismark439.ratemyhangar.MainActivity.premiumT;
import static com.clara.bismark439.ratemyhangar.MainActivity.rare;
import static com.clara.bismark439.ratemyhangar.MainActivity.rareT;
import static com.clara.bismark439.ratemyhangar.MainActivity.sGraphData;
import static com.clara.bismark439.ratemyhangar.MainActivity.toast;
import static com.clara.bismark439.ratemyhangar.crawl.calcStars;
import static com.clara.bismark439.ratemyhangar.crawl.tempList;
import static com.clara.bismark439.ratemyhangar.crawl.totalGE;
import static com.clara.bismark439.ratemyhangar.crawl.totalGEt;
import static com.clara.bismark439.ratemyhangar.crawl.totalGV;
import static com.clara.bismark439.ratemyhangar.crawl.totalGVt;
import static com.clara.bismark439.ratemyhangar.crawl.totalMoney;
import static com.clara.bismark439.ratemyhangar.crawl.totalRP;
import static com.clara.bismark439.ratemyhangar.crawl.totalRPt;
import static com.clara.bismark439.ratemyhangar.crawl.totalRR;
import static com.clara.bismark439.ratemyhangar.crawl.totalRRt;
import static com.clara.bismark439.ratemyhangar.crawl.totalSL;
import static com.clara.bismark439.ratemyhangar.crawl.totalSLt;
import static com.clara.bismark439.ratemyhangar.crawl.totalVehicles;
import static com.clara.bismark439.ratemyhangar.crawl.totalVehiclest;
import static com.clara.bismark439.ratemyhangar.crawl.userInfo;

public class detail extends AppCompatActivity {
    static String whichHangar;
    static double spadedTotal;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);


        TabHost host = (TabHost)findViewById(R.id.TabHost);
        host.setup();

        //Tab 1
        TabHost.TabSpec spec = host.newTabSpec("Tab One");
        spec.setContent(R.id.All);
        spec.setIndicator(getString(R.string.all));
        host.addTab(spec);

        //Tab 2
        spec = host.newTabSpec("Tab Two");
        spec.setContent(R.id.Ground);
        spec.setIndicator(getString(R.string.groundforce));
        host.addTab(spec);

        //Tab 3
        spec = host.newTabSpec("Tab Three");
        spec.setContent(R.id.Aviation);
        spec.setIndicator(getString(R.string.aircrafts));
        host.addTab(spec);
        //Tab 4
        spec = host.newTabSpec("Tab Four");
        spec.setContent(R.id.Golden);
        spec.setIndicator(getString(R.string.etc));
        host.addTab(spec);

        drawAll();
        drawAviation();
        drawGround();
        setGraph();
    }
    public void drawAll(){
        DecimalFormat df = new DecimalFormat();
        df.setMaximumFractionDigits(2);

        double GEConvert = ((double) (totalGEt+totalGE) / 1000.0) * 6;//GE into money. GE already includes bundle cost. 6usd = 1000GE
        double lionConvert=((double) (totalSL+totalSLt) / 1300000.0)*3000 / 1000 * 6;;//SL to money. 3000GE = 1 300 000 SL, 1000 GE = 6 USD
        double RPConvert = (((double)(totalRPt+totalRP) / 45.0)/1000*6);//RP to money // 45rp =1 ge, 1000ge = 6 usd
        double tempmoney = GEConvert+ lionConvert + RPConvert;
        String[]res =calcCurr(tempmoney,(totalGE+ totalGEt));
        TextView tv = (TextView) findViewById(R.id.all_total_value);
        tv.setText(res[0]);
        tv = (TextView) findViewById(R.id.all_prem);
        tv.setText(res[1]);
        //Aircraft
        RPConvert = (((double)(totalRP) / 45.0)/1000*6);//RP to money // 45rp =1 ge, 1000ge = 6 usd
        tempmoney =RPConvert;
        res =calcCurr(tempmoney,0);
        tv = (TextView) findViewById(R.id.all_aircraft);
        tv.setText(res[0]);
        //Tank
        RPConvert = (((double)(totalRPt) / 45.0)/1000*6);//RP to money // 45rp =1 ge, 1000ge = 6 usd
        tempmoney =RPConvert;
        res =calcCurr(tempmoney,0);
        tv = (TextView) findViewById(R.id.all_ground);
        tv.setText(res[0]);
        //AircraftSL
        lionConvert=((double) (totalSL) / 1300000.0)*3000 / 1000 * 6;;//SL to money. 3000GE = 1 300 000 SL, 1000 GE = 6 USD
        res =calcCurr(lionConvert,0);
        tv = (TextView) findViewById(R.id.all_air_lion);
        tv.setText(res[0]);
        //TankSL
        lionConvert=((double) (totalSLt) / 1300000.0)*3000 / 1000 * 6;;//SL to money. 3000GE = 1 300 000 SL, 1000 GE = 6 USD
        res =calcCurr(lionConvert,0);
        tv = (TextView) findViewById(R.id.all_ground_lion);
        tv.setText(res[0]);

        //Country Table
        df.setMaximumFractionDigits(0);
        spadedTotal=0;
        for(int i =0;i<7;i++){
            String spaded=userInfo.inventory[i][2];
            String vehicles=userInfo.inventory[i][1];
            tv = (TextView) findViewById(R.id.all_v_by_c).findViewWithTag("s"+i);
            tv.setText(spaded);
            tv = (TextView) findViewById(R.id.all_v_by_c).findViewWithTag("v"+i);
            tv.setText(vehicles);
            tv = (TextView) findViewById(R.id.all_v_by_c).findViewWithTag("p"+i);
            try {
                double spadedPerc = (((double) Integer.parseInt(spaded) / (double) Integer.parseInt(vehicles)) * 100);
                spadedTotal=spadedTotal+spadedPerc;
                tv.setText(" = "+df.format(spadedPerc)+" %");
            }catch (NumberFormatException e){
                tv.setText(" = Unknown %");
            }


        }
        tv = (TextView) findViewById(R.id.acc_rating);
        tv.setText("");
        //Account Value
        double value = calcStars((totalVehicles+totalVehiclest),(normal.getSize()+normalT.getSize()), (totalRRt+totalRR), spadedTotal/6,false);
        System.out.println("Account Value: "+value);
        while(value>0.5){
            tv.append("★");
            value--;
        }
        if(value>0){
            tv.append("☆");}



        //V by M table
        int[] VbyM=new int[10];
        int VbyM_binsize=10;
        int[] GEVbyM=new int[6];
        int[] GEVbyC=new int[6];
        planeData asdf = new planeData();
        asdf.addList(premium);
        asdf.addList(premiumT);

        for(int i=0;i<tempList.getSize();i++){
            plane isGE = asdf.getPlaneByName(tempList.get(i).id);
            if(isGE!=null){
                int temp = tempList.get(i).data[0];
                if(temp>=150){
                    GEVbyM[0]++;
                }else if(temp>=100){
                    GEVbyM[1]++;
                }
                else if(temp>=75){
                    GEVbyM[2]++;
                }
                else if(temp>=50){
                    GEVbyM[3]++;
                }
                else if(temp>=30){
                    GEVbyM[4]++;
                }
                else {
                    GEVbyM[5]++;
                }//By Match
                plane isak = asdf.getPlaneByName(isGE.id);
                temp = isak.point;
                if(isak.stat!=2){temp=(int)((double)temp/1000.00*6.00);}
                if(temp>=31){
                    GEVbyC[0]++;
                }else if(temp>=21){
                    GEVbyC[1]++;
                }
                else if(temp>=16){
                    GEVbyC[2]++;
                }
                else if(temp>=11){
                    GEVbyC[3]++;
                }
                else if(temp>=6){
                    GEVbyC[4]++;
                }
                else {
                    GEVbyC[5]++;
                }//By Cost
            }else{//Normal vehicles
                int temp =tempList.get(i).data[0];
                if(temp>=100){
                    temp=99;
                }
                temp=temp/VbyM_binsize;
                VbyM[temp]++;
            }
        }//debug

        for(int i=0;i<6;i++){
            tv = (TextView) findViewById(R.id.gv_by_matches).findViewWithTag("t"+i);
            tv.setText(GEVbyM[i]+"");
        }//GV by match

        for(int i=0;i<6;i++){
            tv = (TextView) findViewById(R.id.gv_by_cost).findViewWithTag("t"+i);
            tv.setText(GEVbyC[i]+"");
        }//GV by cost
        tv = (TextView) findViewById(R.id.all_less10);
        double lessPerc =(((double)(VbyM[0])/(double)tempList.getSize())*100);
        tv.setText(df.format(lessPerc)+" %");

        ////////////////////////////////////////
        ///////////////////////////////////



//////////////////////////
        GraphView graphVbyM = (GraphView) findViewById(R.id.graph_vbym);
        BarGraphSeries<DataPoint> series = new BarGraphSeries<>();
        double x=2.5;
        for(int i=0;i<VbyM.length;i++){
            DataPoint temp = new DataPoint(x,VbyM[i]);
            x=x+VbyM_binsize;
            series.appendData(temp,true,VbyM.length);
        }//debug
        series.setDrawValuesOnTop(true);
        series.setValuesOnTopColor(Color.RED);

        graphVbyM.getViewport().setXAxisBoundsManual(true);
        graphVbyM.getViewport().setMinX(0);
        graphVbyM.getViewport().setMaxX(100);

        // enable scaling and scrolling
        graphVbyM.getViewport().setScrollable(true); // enables horizontal scrolling
        //   graph.getViewport().setScrollableY(true); // enables vertical scrolling
        graphVbyM.getViewport().setScalable(true);
        //   graph.getViewport().setScalableY(true);
        series.setColor(Color.parseColor("#00c800"));
        graphVbyM.addSeries(series);
        graphVbyM.setBackgroundColor(Color.parseColor("#ffffff"));
        graphVbyM.getGridLabelRenderer().setHorizontalAxisTitle("Matches played");
        graphVbyM.getGridLabelRenderer().setVerticalAxisTitle("Number of vehicles");
        graphVbyM.getGridLabelRenderer().setHorizontalAxisTitleColor(Color.parseColor("#00c800"));
        graphVbyM.getGridLabelRenderer().setVerticalAxisTitleColor(Color.parseColor("#00c800"));
    }
    public void drawAviation(){
        double get = ((double) (totalGE) / 1000.0) * 6;
        double tempmoney = totalMoney + get;
        get = (((double)(totalRP) / 45.0)/1000*6);
        tempmoney = tempmoney + get;
        summaryView SV = (summaryView)findViewById(R.id.Avia_summ);
        SV.setData(totalRP,totalGE,totalSL,totalVehicles,normal.getSize(),totalGV,premium.getSize(),totalRR,rare.getSize());

        DecimalFormat df = new DecimalFormat();
        df.setMaximumFractionDigits(2);
        calcCurr(tempmoney,(totalGE));

        ProgressBar rprg = (ProgressBar) findViewById(R.id.aviation_rrcoll);
        ProgressBar nprg = (ProgressBar) findViewById(R.id.aviation_vcoll);
        ProgressBar pprg = (ProgressBar) findViewById(R.id.aviation_premcoll);
        TextView tv1 = (TextView) findViewById(R.id.aviation_tv1);
        TextView tv2 = (TextView) findViewById(R.id.aviation_tv2);
        TextView tv3 = (TextView) findViewById(R.id.aviation_tv3);
        double rarepercent = ((double) (totalRR) / (double) (rare.getSize())) * 100;
        double prempercent = ((double) (totalGV) / (double) (premium.getSize())) * 100;
        double normalpercent = ((double) (totalVehicles) / (double) (normal.getSize())) * 100;
        int a=(int)normalpercent;
        int b=(int)prempercent;
        int c=(int)rarepercent;
        nprg.setProgress(a);
        pprg.setProgress(b);
        rprg.setProgress(c);
        tv1.append(" " + df.format(normalpercent) + " %");
        tv2.append(" " + df.format(rarepercent) + " %");
        tv3.append(" " + df.format(prempercent) + " %");

        //top 10
        tempList.sortByBattles();
        LinearLayout rank=(LinearLayout)findViewById(R.id.air_top10_rank);
        LinearLayout name=(LinearLayout)findViewById(R.id.air_top10_name);
        LinearLayout winrate=(LinearLayout)findViewById(R.id.air_top10_winrate);
        LinearLayout akda=(LinearLayout)findViewById(R.id.air_top10_akda);
        LinearLayout gkda=(LinearLayout)findViewById(R.id.air_top10_gkda);
        int i=0;
        int num=1;
        while(num<11&&i<tempList.getSize()){
            CV temp = tempList.get(i);
            if(!isTank(temp.id)){
                double wr = ((double) temp.data[2] / (double) temp.data[0]) * 100;
                double wa = ((double) temp.data[5] / (double) temp.data[4]);//akda
                double wg = ((double) temp.data[6] / (double) temp.data[4]);//gkda

                TextView tx = new TextView(this);
                tx.setText((num) + ".");

                TextView tx1 = new TextView(this);
                tx1.setText(temp.name);
                TextView tx2 = new TextView(this);
                tx2.setText(df.format(wr) + " %");

                TextView tx3 = new TextView(this);
                tx3.setText(df.format(wa) + "");
                TextView tx4 = new TextView(this);
                tx4.setText(df.format(wg) + "");

                rank.addView(tx);
                name.addView(tx1);
                winrate.addView(tx2);
                akda.addView(tx3);
                gkda.addView(tx4);
                num++;
            }
            i++;
        }
        //Account Value
        TextView  tv = (TextView) findViewById(R.id.aviation_acc_rate);
        tv.setText("");
        double value = calcStars((totalVehicles),(normal.getSize()), (totalRR), spadedTotal/6,true);
        System.out.println("Account Value: "+value);
        while(value>0.5){
            tv.append("★");
            value--;
        }
        if(value>0){
            tv.append("☆");}

    }
    public void drawGround() {
        double get = ((double) (totalGE) / 1000.0) * 6;
        double tempmoney = totalMoney + get;
        get = (((double) (totalRP) / 45.0) / 1000 * 6);
        tempmoney = tempmoney + get;
        summaryView SV = (summaryView)findViewById(R.id.gr_summ);
        SV.setData(totalRPt, totalGEt,totalSLt,totalVehiclest,normalT.getSize(),totalGVt,premiumT.getSize(),totalRRt,rareT.getSize());

        DecimalFormat df = new DecimalFormat();
        df.setMaximumFractionDigits(2);
        calcCurr(tempmoney, (totalGEt));
        ProgressBar rprg = (ProgressBar) findViewById(R.id.ground_rrcoll);
        ProgressBar nprg = (ProgressBar) findViewById(R.id.ground_vcoll);
        ProgressBar pprg = (ProgressBar) findViewById(R.id.ground_premcoll);
        TextView tv1 = (TextView) findViewById(R.id.ground_tv1);
        TextView tv2 = (TextView) findViewById(R.id.ground_tv2);
        TextView tv3 = (TextView) findViewById(R.id.ground_tv3);
        double rarepercent = ((double) (totalRRt) / (double) (rareT.getSize())) * 100;
        double prempercent = ((double) (totalGVt) / (double) (premiumT.getSize())) * 100;
        double normalpercent = ((double) (totalVehiclest) / (double) (normalT.getSize())) * 100;
        int a = (int) normalpercent;
        int b = (int) prempercent;
        int c = (int) rarepercent;
        nprg.setProgress(a);
        pprg.setProgress(b);
        rprg.setProgress(c);
        tv1.append(" " + df.format(normalpercent) + " %");
        tv2.append(" " + df.format(rarepercent) + " %");
        tv3.append(" " + df.format(prempercent) + " %");

        //top 10
        //   tempList.sortByBattles(false);
        LinearLayout rank = (LinearLayout) findViewById(R.id.ground_top10_rank);
        LinearLayout name = (LinearLayout) findViewById(R.id.ground_top10_name);
        LinearLayout winrate = (LinearLayout) findViewById(R.id.ground_top10_winrate);
        LinearLayout akda = (LinearLayout) findViewById(R.id.ground_top10_akda);
        LinearLayout gkda = (LinearLayout) findViewById(R.id.ground_top10_gkda);
        int i=0;
        int num=1;
        while(num<11&&i<tempList.getSize()){
            CV temp = tempList.get(i);
            if(isTank(temp.id)){
                double wr = ((double) temp.data[2] / (double) temp.data[0]) * 100;
                double wa = ((double) temp.data[5] / (double) temp.data[4]);//akda
                double wg = ((double) temp.data[6] / (double) temp.data[4]);//gkda

                TextView tx = new TextView(this);
                tx.setText((num) + ".");

                TextView tx1 = new TextView(this);
                tx1.setText(temp.name);
                TextView tx2 = new TextView(this);
                tx2.setText(df.format(wr) + " %");

                TextView tx3 = new TextView(this);
                tx3.setText(df.format(wa) + "");
                TextView tx4 = new TextView(this);
                tx4.setText(df.format(wg) + "");

                rank.addView(tx);
                name.addView(tx1);
                winrate.addView(tx2);
                akda.addView(tx3);
                gkda.addView(tx4);
                num++;
            }
            i++;
        }
        //Account Value
        TextView  tv = (TextView) findViewById(R.id.ground_acc_rate);
        tv.setText("");
        double value = calcStars((totalVehiclest),(normalT.getSize()), (totalRRt), spadedTotal/6,true);
        System.out.println("Account Value: "+value);
        while(value>0.5){
            tv.append("★");
            value--;
        }
        if(value>0){
            tv.append("☆");}
    }
    public void setGraph(){
        TextView tv1 = (TextView)findViewById(R.id.practical_value);
        String formatted = String.format("%.2f",myTotalPracticalMoney);
        tv1.setText("$"+formatted);
        double myPercentage=sGraphData.calculatePercentile(myTotalPracticalMoney);
        TextView tv = (TextView)findViewById(R.id.upperPerc_inputdata);
        formatted = String.format("%.2f",myPercentage);
        tv.setText(" "+formatted+getString(R.string.upperpectplayer2));
        GraphView graph = (GraphView) findViewById(R.id.graph);
        LineGraphSeries<DataPoint> series = new LineGraphSeries<>();
        int sumY=0;
        int size = sGraphData.data.size();
        for(int i=0;i<size;i++){
            double x=(sGraphData.data.get(i)[0]+sGraphData.data.get(i)[1])/2;
            sumY=sumY+sGraphData.data.get(i)[2];
            DataPoint temp = new DataPoint(x,(sumY/sGraphData.graphSum)*100);
            series.appendData(temp,true,size);
        }
        BarGraphSeries<DataPoint> myPoint = new BarGraphSeries<>(new DataPoint[]{new DataPoint(myTotalPracticalMoney, (sGraphData.mySum/sGraphData.graphSum)*100)});
        myPoint.setColor(Color.parseColor("#0000C8"));
        myPoint.setDrawValuesOnTop(true);
        myPoint.setValuesOnTopColor(Color.RED);
        myPoint.setDataWidth(20);
        graph.addSeries(myPoint);
        // set manual X bounds
        graph.getViewport().setYAxisBoundsManual(true);
        graph.getViewport().setMinY(0);
        graph.getViewport().setMaxY(100);

        graph.getViewport().setXAxisBoundsManual(true);
        graph.getViewport().setMinX(0);
        graph.getViewport().setMaxX(2500);

        // enable scaling and scrolling
        graph.getViewport().setScrollable(true); // enables horizontal scrolling
        //   graph.getViewport().setScrollableY(true); // enables vertical scrolling
        graph.getViewport().setScalable(true);
        //   graph.getViewport().setScalableY(true);

        int green = Color.parseColor("#00c800");
        series.setColor(green);
        graph.addSeries(series);
        graph.setBackgroundColor(Color.parseColor("#ffffff"));
        graph.getGridLabelRenderer().setHorizontalAxisTitle("USD");
        graph.getGridLabelRenderer().setVerticalAxisTitle("%");
        graph.getGridLabelRenderer().setHorizontalAxisTitleColor(green);
        graph.getGridLabelRenderer().setVerticalAxisTitleColor(green);

        graph.getGridLabelRenderer().setGridColor(green);
        graph.getGridLabelRenderer().setHorizontalLabelsColor(green);
        graph.getGridLabelRenderer().setHorizontalLabelsColor(green);
        graph.getGridLabelRenderer().setVerticalLabelsSecondScaleColor(green);
    }
    public String[] calcCurr(double a, int GE){
        String[] res = new String[2];
        DecimalFormat df = new DecimalFormat();
        df.setMaximumFractionDigits(2);
        double tempMoney=a;
        switch (myCurr){
            case 0:
                res[0]="$" + df.format(a);
                res[1]="$" + df.format((double)GE/1000*6);
                break;
            case 1:
                tempMoney=a*1123.97;
                df.setMaximumFractionDigits(0);
                res[0]="₩" + df.format(tempMoney);
                res[1]="₩" + df.format((double)GE/1000*6*1123.97);
                break;
            case 2:
                tempMoney=a*0.85;
                res[0]="€" + df.format(tempMoney);
                res[1]="€" + df.format((double)GE/1000*6*0.85);
                break;
            case 3:
                tempMoney=a*59.51;
                res[0]="py6 " + df.format(tempMoney);
                res[1]="py6 " + df.format((double)GE/1000*6*59.51);
                break;
        }
        return res;
    }

    public boolean isTank(String id){
        boolean result = false;
        String[] token = id.split("_");
        if(token[0].equals("germ")||token[0].equals("us")||token[0].equals("ussr")||token[0].equals("uk")||token[0].equals("jp")||token[0].equals("fr")||token[0].equals("it"))
        {result = true;}
        return result;
    }
    public void onClickHangar(View v){
        toast(getApplicationContext(),"Loading..!");
        whichHangar=(String)v.getTag();
        Intent intent = new Intent(getApplicationContext(),Hangar.class);
        startActivity(intent);
    }

    public void onClickRate(View v){
        Uri uri = Uri.parse("market://details?id=com.clara.bismark439.ratemyhangar");
        Intent goToMarket = new Intent(Intent.ACTION_VIEW, uri);
        // To count with Play market backstack, After pressing back button,
        // to taken back to our application, we need to add following flags to intent.
        goToMarket.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY |
                Intent.FLAG_ACTIVITY_NEW_DOCUMENT |
                Intent.FLAG_ACTIVITY_MULTIPLE_TASK);
        try {
            startActivity(goToMarket);
        } catch (ActivityNotFoundException e) {
            startActivity(new Intent(Intent.ACTION_VIEW,Uri.parse("market://details?id=com.clara.bismark439.ratemyhangar")));
        }
    }

    public void onWatch(View v){
        if (mInterstitialAd.isLoaded()) {
            mInterstitialAd.show();
        } else {
            Log.d("TAG", "The interstitial wasn't loaded yet.");
        }
    }
    public void onDonate(View v){
        Uri uri = Uri.parse("market://details?id=com.haruhi.bismark439.ratemyhangar_donate");
        Intent goToMarket = new Intent(Intent.ACTION_VIEW, uri);
        // To count with Play market backstack, After pressing back button,
        // to taken back to our application, we need to add following flags to intent.
        goToMarket.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY |
                Intent.FLAG_ACTIVITY_NEW_DOCUMENT |
                Intent.FLAG_ACTIVITY_MULTIPLE_TASK);
        try {
            startActivity(goToMarket);
        } catch (ActivityNotFoundException e) {
            e.printStackTrace();
            // startActivity(new Intent(Intent.ACTION_VIEW,Uri.parse("market://details?id=com.clara.bismark439.ratemyhangar")));
        }

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