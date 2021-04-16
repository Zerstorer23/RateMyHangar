package com.clara.bismark439.ratemyhangar;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;

import static com.clara.bismark439.ratemyhangar.MainActivity.dataFiles;
import static com.clara.bismark439.ratemyhangar.MainActivity.errorCrawl;
import static com.clara.bismark439.ratemyhangar.MainActivity.errorMsg;
import static com.clara.bismark439.ratemyhangar.MainActivity.getInt;
import static com.clara.bismark439.ratemyhangar.MainActivity.modifiedIDs;
import static com.clara.bismark439.ratemyhangar.MainActivity.myCurr;
import static com.clara.bismark439.ratemyhangar.MainActivity.myNick;
import static com.clara.bismark439.ratemyhangar.MainActivity.myTotalPracticalMoney;
import static com.clara.bismark439.ratemyhangar.MainActivity.normal;
import static com.clara.bismark439.ratemyhangar.MainActivity.normalT;
import static com.clara.bismark439.ratemyhangar.MainActivity.premium;
import static com.clara.bismark439.ratemyhangar.MainActivity.premiumT;
import static com.clara.bismark439.ratemyhangar.MainActivity.rare;
import static com.clara.bismark439.ratemyhangar.MainActivity.rareT;
import static com.clara.bismark439.ratemyhangar.MainActivity.request;
import static com.clara.bismark439.ratemyhangar.MainActivity.toast;
import static com.clara.bismark439.ratemyhangar.gameView.ans;

public class crawl extends AppCompatActivity {
    public static final String MY_PREFS_NAME = "com.clara.bismark439.ratemyhangar";
    LinearLayout buttons;
    TextView nic;
    static int totalRP = 0;
    static int totalVehicles = 0;
    static double totalMoney = 0.0;
    static int totalRR = 0;
    static int totalGE = 0;
    static int totalGV = 0;//prem vehicle
    static int totalRPt = 0;
    static int totalVehiclest = 0;
    static double totalMoneyt = 0.0;
    static int totalRRt = 0;
    static int totalGEt = 0;
    static int totalGVt = 0;//prem vehicle
    static int totalSL = 0;
    static int totalSLt = 0;

    public static UserInfo userInfo = new UserInfo();
    static String theURL = "http://thunderskill.com/en/stat/";
    static String theURL2 = "http://thunderskill.com/en/stat/";
    static String theURL3 = "http://thunderskill.com/en/stat/";
    static String URL = "http://warthunder.com/en/community/userinfo/?nick=";
   static String[][] userStat = new String[18][4];//RowID / ColID
    public static CVlist tempList = new CVlist();
    static CVlist theListthatincludesEverything = new CVlist();
    static Bitmap bitmap;
    gameView gv;
    FrameLayout adCont;
    int loading = 0;
    final int loadingMAX=4;
    int randomge=0;
    int randomsl=0;
    int randomrp=0;
    int randomtv=0;
    boolean firstLoad = true;
    loadingView LV ;
    //String URL="http://thunderskill.com/en/vehicles#type=aviation&role=all&country=country_britain";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);//http://thunderskill.com/en/stat/Levent_1011011
        setContentView(R.layout.activity_crawl);
        LV= findViewById(R.id.loading_view);
        adCont=findViewById(R.id.adCont);
        save();
       initAdBanner();
        theURL = theURL + myNick + "/vehicles/a";
        theURL2 = theURL2 + myNick + "/vehicles/r";
        theURL3 = theURL3 + myNick + "/vehicles/s";
        URL = URL + myNick;
        nic = findViewById(R.id.nic);
        nic.setText(myNick);
        buttons = findViewById(R.id.buttons);
        setText();
        errorCrawl=false;
        if(normal.getSize()==0){
                normal.loadSQL("air_normal");
                rare.loadSQL("air_rare");
                premium.loadSQL("air_premium");
                normalT.loadSQL("ground_normal");
                premiumT.loadSQL("ground_premium");
                rareT.loadSQL("ground_rare");
                // normal.RPplanes(getApplicationContext());
           /* rare.RRplanes(getApplicationContext());
            premium.GEplanes(getApplicationContext());
            normalT.RPtanks(getApplicationContext());
            rareT.RRtanks(getApplicationContext());
            premiumT.GEtanks(getApplicationContext());*/
                /*
                normal.RPplanes(getApplicationContext());
                rare.RRplanes(getApplicationContext());
                premium.GEplanes(getApplicationContext());
                normalT.RPtanks(getApplicationContext());
                rareT.RRtanks(getApplicationContext());
                premiumT.GEtanks(getApplicationContext());*/
            LV.setLoaded(0);
        }else{
            LV.setLoaded(0);
        }

        //region skillArcade
        Thread skillArcade = new Thread() {
            public void run() {
                try {
                    LV.setPending(2);
                    System.out.println("Parsing thunderskill : Arcade");
                        Document doc = Jsoup.connect(theURL).maxBodySize(0).get();
                        crawl_addTemplist(doc);
                        loading++;
                        LV.setLoaded(2);
               //     toast(getApplicationContext(),"ARCADE loaded");
                    System.out.println("Parsing thunderskill : Arcade FINISHED");
                    if(loading==loadingMAX){
                        crawl_updateRes();
                    }else{
                        if(firstLoad){crawl_runSIM();
                        firstLoad=false;
                        }
                    }
                } catch (IOException e) {
                    errorMsg=e.getLocalizedMessage();
                    System.out.println("Error?!" + e.getLocalizedMessage());
                    errorCrawl=true;
                    errorMsg=e.getLocalizedMessage();
                    finish();
                }catch (Exception e){
                    System.out.println("Error?!" + e.getLocalizedMessage());
                    e.printStackTrace();
                }
            }
        };
        //endregion
        //region skillReal
        Thread skillReal = new Thread() {
            public void run() {
                try {
                    LV.setPending(3);
                    System.out.println("Parsing thunderskill : Real");
                        Document doc = Jsoup.connect(theURL2).maxBodySize(0).get();
                        crawl_addTemplist(doc);
                        loading++;
                         LV.setLoaded(3);
              //      toast(getApplicationContext(),"REAL loaded");
                    System.out.println("Parsing thunderskill : Real FINISHED");
                    if(loading==loadingMAX){
                        crawl_updateRes();
                    }else{
                        if(firstLoad){crawl_runSIM();
                        firstLoad=false;
                        }
                    }
                } catch (IOException e) {
                    System.out.println("Error?!" + e.getLocalizedMessage());
                    errorCrawl=true;
                    errorMsg=e.getLocalizedMessage();
                    finish();
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        };
        //endregion
        //region thunder.com
        Thread thunderCrawl = new Thread() {
            public void run() {
                Document doc;
                try {
                    LV.setPending(1);
                    if (!userInfo.init) {
                        doc = Jsoup.connect(URL).maxBodySize(0).get();
                        //User Info
                        //  <ul class="user-profile__data-list">
                        Element userInfoTable = doc.select("ul[class=user-profile__data-list]").first();// tbody
                        Elements datainfo = userInfoTable.select("li");
                        userInfo.name = datainfo.get(0).text();
                        userInfo.title = datainfo.get(1).text();
                        userInfo.level = datainfo.get(2).text();
                        userInfo.date = datainfo.get(3).text();


                        //Countries Vehicle counters
                        //   <div class="user-profile__score profile-score">
                        Elements vehicleinfo = doc.select("ul[class=profile-score__list-col]");// tbody
                        for(int i=0;i<3;i++){
                            Elements thisMode =vehicleinfo.get(i).select("li[class=profile-score__list-item]");
                            for(int j=0;j<7;j++){
                            userInfo.inventory[j][i+1]=thisMode.get(j).text();
                            }
                        }

                        //My Image
                        //<img class="user-profile__ava-img" src="//static.warthunder.com/i/avatar/.jpg">
                        Element img = doc.select("img[class=user-profile__ava-img]").first();
                        userInfo.img = img.absUrl("src");
                    }
                    try {
                        InputStream input = new java.net.URL(userInfo.img).openStream();
                        System.out.println(userInfo.img);
                        bitmap = BitmapFactory.decodeStream(input);
                    }catch (FileNotFoundException e){
                        System.out.println(userInfo.img+" not found: IOException");
                        bitmap =BitmapFactory.decodeResource(getResources(),R.drawable.profile);
                    }

                    LV.setLoaded(1);
               //     toast(getApplicationContext(),"Profile loaded");
                    System.out.println("Profile LOADED");
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            loading++;
                            if(loading==loadingMAX){
                                crawl_updateRes();
                            }
                            setText();
                            ImageView profile = findViewById(R.id.profile);
                            profile.setImageBitmap(bitmap);
                            System.out.println("bitmap set");

                        }
                    });
                }  catch (Exception e) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getApplicationContext(), "WT profile not loaded", Toast.LENGTH_LONG).show();
                        }
                    });
                    System.out.println("There was an error");
                    //  System.out.println(e.getLocalizedMessage());
                    e.printStackTrace();
                    loading++;
                    if(loading==loadingMAX){
                        crawl_updateRes();
                    }
                }


            }
        };
        //endregion
        int number=(60)*1000;
        CountDownTimer cdt = new CountDownTimer(number, 1350) {//1000
            public void onTick(long millisUntilFinished) {
                if(loading<loadingMAX){
                    crawl_intermittent();}
            }
            public void onFinish() {
            }
        };
        gv = findViewById(R.id.playthegame);
        if (!userInfo.init) {
           skillArcade.start();
            skillReal.start();
            thunderCrawl.start();
           // gv.promptInit();
            cdt.start();
        }else{
            LV.setLoaded(1);
            LV.setLoaded(2);
            LV.setLoaded(3);
            LV.setLoaded(4);
            gv.setVisibility(View.INVISIBLE);
            adCont.setVisibility(View.GONE);
            loading=loadingMAX;
            Thread temporaryThread = new Thread() {
                public void run() {
                    crawl_updateRes();
                }
            };
            temporaryThread.start();
        }



    }
    public void initAdBanner(){
      //  adview = findViewById(R.id.adView);
       AdView adView = new AdView(this);
        adView.setAdSize(AdSize.BANNER);
        adView.setAdUnitId("ca-app-pub-4557475384906138/7372297914");
       if(request==null){ request =  new AdRequest.Builder()
                .addTestDevice("9CA9AAA0DDA7ED97178D9455C9AC736C")
                .build();}
        adView.loadAd(request);
        adCont.addView(adView);
    }
    public void crawl_runSIM(){
        //region skillSim
        Thread skillSim = new Thread() {
            public void run() {
                try {
                    LV.setPending(4);
                    System.out.println("Parsing thunderskill : SIM");
                    Document doc = Jsoup.connect(theURL3).maxBodySize(0).get();
                    crawl_addTemplist(doc);
                    LV.setLoaded(4);
                  //  toast(getApplicationContext(),"SIM LOADED");
                    System.out.println("Parsing thunderskill : SIM FINISHED");
                    loading++;
                    System.out.println("loading"+loading+"vs "+loadingMAX+" MAX");
                    if(loading==loadingMAX){
                        System.out.println("Update Res called");
                        crawl_updateRes();
                    }else{
                        //crawl_intermittent();
                    }
                } catch (IOException e) {
                    System.out.println("Error?!" + e.getLocalizedMessage());
                    errorCrawl=true;
                    errorMsg=e.getLocalizedMessage();
                    finish();
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        };
        //endregion
skillSim.start();
    }
    public void crawl_updateRes(){
    tempList.removeDuplicates();//Found all data from network
    theListthatincludesEverything.vehicleDB.addAll(tempList.vehicleDB);//Add data modified by user
    for (int i = 0; i < modifiedIDs.size(); i++) {
        if (theListthatincludesEverything.has(modifiedIDs.get(i))) {//This is duplicate, remove from modList
            modifiedIDs.remove(i);
            i--;
        } else {
            CV temp = new CV(modifiedIDs.get(i));
            theListthatincludesEverything.add(temp);
        }
    }
    crawl_calculate();
    runOnUiThread(new Runnable() {
        @Override
        public void run() {
            System.out.println("UI Update called  ");
            buttons.setVisibility(View.VISIBLE);
            setText();
        }
    });

}
    public void crawl_calculate() {
        totalRP = 0;
        totalVehicles = 0;
        totalMoney = 0.0;
        totalRR = 0;
        totalGE = 0;
        totalGV = 0;
        totalRPt = 0;
        totalVehiclest = 0;
        totalMoneyt = 0.0;
        totalRRt = 0;
        totalGEt = 0;
        totalGVt = 0;
        totalSL = 0;
        totalSLt = 0;
        LV.setPending(5);
        System.out.println("Parsing Calculate called  ");
        for (int i = 0; i < theListthatincludesEverything.getSize(); i++) {
            plane temp = normal.getPlaneByName(theListthatincludesEverything.get(i).id);
            if (temp != null) {
                totalVehicles++;//check
                totalRP = totalRP + temp.point;
                totalSL = totalSL + temp.lion;
            }
        }//6usd =1000ge
        for (int i = 0; i < theListthatincludesEverything.getSize(); i++) {
            plane temp = normalT.getPlaneByName(theListthatincludesEverything.get(i).id);
          //  System.out.println(temp+" VS "+theListthatincludesEverything.get(i).id);
            if (temp != null) {
                totalVehiclest++;//check
                totalRPt = totalRPt + temp.point;
                totalSLt = totalSLt + temp.lion;
            }
        }//6usd =1000ge
System.out.println("Vehiclet, SPt, SLt "+totalVehiclest+" "+totalRPt+" "+totalSLt);
        for (int i = 0; i < theListthatincludesEverything.getSize(); i++) {
            plane temp = premium.getPlaneByName(theListthatincludesEverything.get(i).id);
            if (temp != null) {
                totalGV++;//check
                if (temp.stat == 2) {
                    totalMoney = totalMoney + (double) temp.point;
                    totalGE = totalGE + (int) ((double) temp.point) / 6 * 1000;
                } else {
                    totalGE = totalGE + temp.point;
                }
            }
        }//Premium
        for (int i = 0; i < theListthatincludesEverything.getSize(); i++) {
            plane temp = premiumT.getPlaneByName(theListthatincludesEverything.get(i).id);
            if (temp != null) {
                totalGVt++;//check
                if (temp.stat == 2) {
                    totalMoneyt = totalMoneyt + (double) temp.point;
                    totalGEt = totalGEt + (int) ((double) temp.point) / 6 * 1000;
                } else {
                    totalGEt = totalGEt + temp.point;
                }
            }
        }//Premium

        for (int i = 0; i < theListthatincludesEverything.getSize(); i++) {
            plane temp = rare.getPlaneByName(theListthatincludesEverything.get(i).id);
            if (temp != null) {
                totalRR++;
            }

        }
        for (int i = 0; i < theListthatincludesEverything.getSize(); i++) {
            plane temp = rareT.getPlaneByName(theListthatincludesEverything.get(i).id);
            if (temp != null) {
                totalRRt++;
            }
        }
        LV.setLoaded(5);
    }
    public void crawl_intermittent(){
    runOnUiThread(new Runnable() {
        @Override
        public void run() {
        //     randomge=randomge+(int)(Math.random()*131+1);
            randomsl=randomsl+(int)(Math.random()*300799+1);
            randomrp=randomrp+(int)(Math.random()*398099+1);
            randomtv=randomtv+(int)(Math.random()*21+1);
//System.out.println("Randomised values : " +randomge+","+randomrp+","+randomsl+","+randomtv);
            summaryView SV = findViewById(R.id.crawl_summ);
            SV.setData(randomrp, 0, randomsl,randomtv, (normalT.getSize() + normal.getSize()),
                   0, (premiumT.getSize() + premium.getSize()), 0, (rareT.getSize() + rare.getSize()));
        }
    });
}
    public void crawl_addTemplist(Document doc) {
        ArrayList<String> namelist = new ArrayList<>();
        ArrayList<String> linklist = new ArrayList<>();
        ArrayList<int[]> datalist = new ArrayList<>();
        Elements table4 = doc.select("tbody");//
        System.out.println(table4.html());
        Elements links = table4.select("a[href]");//
        Elements rows = table4.select("tr");
        System.out.println("Table 4 size = "+rows.size());
        for (int i = 0; i < rows.size(); i++) {
            Element row = rows.get(i);
            Elements cols = row.select("td");
            //Name
            Element vName = cols.select("td[class=vehicle]").get(0);
            namelist.add(vName.attr("data-sort"));
            System.out.println("Added "+vName.attr("data-sort"));
            // Stats
            Elements special = cols.get(1).select("li");
            int[] dataTemp = new int[7];//special.size
            for (int y = 0; y < 5; y++) {
                String temp = special.get(y).text();
                String[] ttoken = temp.split("\\+");
                dataTemp[y] = getInt(ttoken[0]);
            }
            for (int y = 5; y < special.size(); y++) {
                String temp = special.get(y).text();
                temp = temp.replaceAll("[^A-Za-z]+", "");
                if (temp.equals("Overallairfrags")) {
                    String[] ttoken = special.get(y).text().split("\\+");
                    dataTemp[5] = getInt(ttoken[0]);
                }
                if (temp.equals("Overallgroundfrags")) {
                    String[] ttoken = special.get(y).text().split("\\+");
                    dataTemp[6] = getInt(ttoken[0]);
                }
            }
            datalist.add(dataTemp);
        }
        for (Element link : links) {
            String url = link.attr("href");
            String[] tokens = url.split("/");
            linklist.add(tokens[tokens.length - 1]);
        }

        for (int i = 0; i < linklist.size(); i++) {
            CV temp = new CV(namelist.get(i), linklist.get(i), datalist.get(i));
            tempList.add(temp);
        }

    }
    public void setText() {
        summaryView SV = findViewById(R.id.crawl_summ);
        //Money calculation
        double GEConvert = ((double) (totalGEt + totalGE) / 1000.0) * 6;//GE into money. GE already includes bundle cost. 6usd = 1000GE
        double lionConvert = ((double) (totalSL + totalSLt) / 1300000.0) * 3000 / 1000 * 6;
        //SL to money. 3000GE = 1 300 000 SL, 1000 GE = 6 USD
        double RPConvert = (((double) (totalRPt + totalRP) / 45.0) / 1000 * 6);//RP to money // 45rp =1 ge, 1000ge = 6 usd
        double tempmoney = GEConvert + RPConvert + lionConvert;//Total of all
        DecimalFormat df = new DecimalFormat();
        df.setMaximumFractionDigits(2);
        calcCurr(tempmoney, (totalGE + totalGEt));//All money, Real money

        ProgressBar rprg = findViewById(R.id.RareProgress);
        ProgressBar nprg = findViewById(R.id.NormalProgress);
        ProgressBar pprg = findViewById(R.id.PremBar);

        LinearLayout fetch = findViewById(R.id.FETCH);
        LinearLayout percent = findViewById(R.id.PERCENT);
        TextView title = findViewById(R.id.Title);
        TextView lvl = findViewById(R.id.lvl);
        title.setText(userInfo.title);
        lvl.setText(userInfo.level);
        System.out.println("LOADING : "+loading);
        if (loading!=loadingMAX) {
            fetch.setVisibility(View.VISIBLE);
            percent.setVisibility(View.GONE);
        } else {
            SV.setData(totalRP + totalRPt, totalGEt + totalGE, totalSL + totalSLt, (totalVehiclest + totalVehicles), (normalT.getSize() + normal.getSize()),
                (totalGVt + totalGV), (premiumT.getSize() + premium.getSize()), (totalRRt + totalRR), (rareT.getSize() + rare.getSize()));

            savehelp();
            fetch.setVisibility(View.GONE);
            percent.setVisibility(View.VISIBLE);
            TextView tv1 = findViewById(R.id.tv1);
            TextView tv2 = findViewById(R.id.tv2);
            TextView tv3 = findViewById(R.id.tv3);
            double rarepercent = ((double) (totalRRt + totalRR) / (double) (rare.getSize() + rareT.getSize())) * 100;
            double prempercent = ((double) (totalGV + totalGVt) / (double) (premium.getSize() + premiumT.getSize())) * 100;
            double normalpercent = ((double) (totalVehicles + totalVehiclest) / (double) (normal.getSize() + normalT.getSize())) * 100;
            int a = (int) normalpercent;
            int b = (int) prempercent;
            int c = (int) rarepercent;
            nprg.setProgress(a);
            pprg.setProgress(b);
            rprg.setProgress(c);

            tv1.setText(getString(R.string.your_vehicle_collection)+" " + df.format(normalpercent) + " %");
            tv2.setText(getString(R.string.your_rare_vehicle_collection)+" " + df.format(rarepercent) + " %");
            tv3.setText(getString(R.string.your_premium_vehicle_collection)+" " + df.format(prempercent) + " %");
            //Account Value
            double spT = 0;
            for (int i = 0; i < 6; i++) {
                String spaded = userInfo.inventory[i][2];
                String vehicles = userInfo.inventory[i][1];
                try {
                    double spadedPerc = (((double) Integer.parseInt(spaded) / (double) Integer.parseInt(vehicles)) * 100);
                    spT = spT + spadedPerc;
                } catch (NumberFormatException e) {
                }
            }
            TextView tv = findViewById(R.id.crawl_acc_rate);
            tv.setText("");
            double value = calcStars((totalVehicles + totalVehiclest), (normal.getSize() + normalT.getSize()), (totalRRt + totalRR), spT / 6, false);
            System.out.println("Account Value: " + value);
            while (value > 0.5) {
                tv.append("★");
                value--;
            }
            if (value > 0) {
                tv.append("☆");
            }
        }

    }

    private void save() {
        SharedPreferences.Editor editor = getSharedPreferences(MY_PREFS_NAME, Context.MODE_PRIVATE).edit();
        editor.putString("init", "done");
        editor.putString("idURL", myNick);
        editor.apply();
    }

    public void calcCurr(double a, int GE) {
        DecimalFormat df = new DecimalFormat();
        df.setMaximumFractionDigits(2);
        TextView USD = findViewById(R.id.USD);
        double tempMoney = a;
        String onlyPrem = "";
        myTotalPracticalMoney=((double) GE / 1000 * 6);
        switch (myCurr) {
            case 0:
                USD.setText("$" + df.format(a));
                onlyPrem = "$" + df.format((double) GE / 1000 * 6);
                break;
            case 1:
                tempMoney = a * 1123.97;
                df.setMaximumFractionDigits(0);
                USD.setText("₩" + df.format(tempMoney));
                onlyPrem = "₩" + df.format((double) GE / 1000 * 6 * 1123.97);
                break;
            case 2:
                tempMoney = a * 0.85;
                USD.setText("€" + df.format(tempMoney));
                onlyPrem = "€" + df.format((double) GE / 1000 * 6 * 0.85);
                break;
            case 3:
                tempMoney = a * 59.51;
                USD.setText("py6 " + df.format(tempMoney));
                onlyPrem = "py6 " + df.format((double) GE / 1000 * 6 * 59.51);
                break;
        }
        TextView onlyP = findViewById(R.id.Eff_USD);
        onlyP.setText(onlyPrem);
    }

    public void onHangar(View v) {
        Intent intent = new Intent(getApplicationContext(), detail.class);
        startActivity(intent);
    }

    public void onOtherAccount(View v) {
        finish();
    }

    public void onAnswer(View v) {
        int myAns = Integer.parseInt((String) v.getTag());
        if (myAns == ans) {
            gv.answer.setText("Correct!");
            gv.answer.setTextColor(Color.parseColor("#00ff00"));
            gameView.myScore++;
            gv.updateScore();
        } else {
            gv.answer.setText("Wrong!");
            gv.answer.setTextColor(Color.parseColor("#ff0000"));
        }
        gv.playAudio();
    }

    public void onPost(View v) {
        Toast.makeText(getApplicationContext(), "If you feel unsafe, call 111", Toast.LENGTH_LONG).show();
    }

    static double calcStars(int myV, int totalV, int rares, double Spaded, boolean mini) {
        double hurricane = 4.3 * (((double) myV / (double) totalV));
        if (hurricane > 3) hurricane = 3;
        double spitfire = Spaded / 100;
        double meteor = 0;
        if (mini) rares = rares * 2;
        if (rares > 19) {
            meteor = 1;
        } else if (rares > 9) {
            meteor = 0.5;
        }//Rares, 0.5 for 5

        if (hurricane > 3) hurricane = 3.0;
        if (spitfire > 0.5) {
            spitfire = 1;
        } else {
            spitfire = spitfire * 2;
        }//More than 50% = 1 star

        double hunter = hurricane + spitfire + meteor;
        return Math.round(hunter * 2) / 2.0;

    }

    static void reset() {
        totalRP = 0;
        totalVehicles = 0;
        totalMoney = 0.0;
        totalRR = 0;
        totalGE = 0;
        totalGV = 0;
        totalRPt = 0;
        totalVehiclest = 0;
        totalMoneyt = 0.0;
        totalRRt = 0;
        totalGEt = 0;
        totalGVt = 0;
        totalSL = 0;
        totalSLt = 0;
       /* normal.reset();
        rare.reset();
        premium.reset();

        normalT.reset();
        rareT.reset();
        premiumT.reset();*/
        userInfo = new UserInfo();
        theURL = "http://thunderskill.com/en/stat/";
        theURL2 = "http://thunderskill.com/en/stat/";
        theURL3 = "http://thunderskill.com/en/stat/";
        URL = "http://warthunder.com/en/community/userinfo/?nick=";
        userStat = new String[9][4];//RowID / ColID
        tempList.reset();
        theListthatincludesEverything.reset();
        // modifiedIDremoves=new ArrayList<String >();
        modifiedIDs = new ArrayList<>();
    }

    public void savehelp() {
        String playerFileName = userInfo.name + ".txt";
        File outFile = new File(playerFileName);
        try {
            FileOutputStream os = openFileOutput(String.valueOf(outFile), Context.MODE_PRIVATE);
            PrintWriter printWriter = new PrintWriter(os); //set up a writer
            String content = userInfo.toString();
            System.out.println("Temp size is " + tempList.getSize() + " and thelist size is " + theListthatincludesEverything.getSize());
            content = content + tempList.toString();//set up the data to be printed
            printWriter.println(content);//print it
            printWriter.flush();
            printWriter.close();
            saveTxtList(playerFileName);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            e.getLocalizedMessage();
            System.out.println("Error saving");
        }
    }

    public void getTxtList(String Filename) {
        InputStream inputStream;
        try {
            inputStream = openFileInput("List.txt");
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
            BufferedReader sc = new BufferedReader(inputStreamReader);
            String temp = sc.readLine();
            if (temp != null) {
                boolean duplicate = false;
                String[] token = temp.split(",");
                for (String aToken : token) {
                    if (aToken.contains(Filename)) {
                        duplicate = true;
                    }
                }
                if (!duplicate) {
                    dataFiles = new String[token.length + 1];
                    System.arraycopy(token, 0, dataFiles, 0, token.length);
                    dataFiles[token.length] = Filename;
                } else {
                    dataFiles = new String[token.length];
                    for (int i = 0; i < dataFiles.length; i++) {
                        dataFiles[i] = token[i];
                    }
                }
                Arrays.sort(dataFiles);
            } else {
                dataFiles = new String[1];
                dataFiles[0] = Filename;
            }
        } catch (NullPointerException | IOException e) {
            e.printStackTrace();
        }

    }

    public void saveTxtList(String FileName) throws FileNotFoundException {
        getTxtList(FileName);
        FileOutputStream os = openFileOutput("List.txt", Context.MODE_PRIVATE);
        PrintWriter printWriter = new PrintWriter(os); //set up a writer

        for (String dataFile : dataFiles) {
            printWriter.print(dataFile + ",");
        }
        //set up the data to be printed
        printWriter.flush();
        printWriter.close();
    }


}
