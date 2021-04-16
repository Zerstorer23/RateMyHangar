package com.clara.bismark439.ratemyhangar;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ScrollView;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintWriter;

import static android.view.View.VISIBLE;
import static com.clara.bismark439.ratemyhangar.MainActivity.modifiedIDs;
import static com.clara.bismark439.ratemyhangar.MainActivity.normal;
import static com.clara.bismark439.ratemyhangar.MainActivity.normalT;
import static com.clara.bismark439.ratemyhangar.MainActivity.premium;
import static com.clara.bismark439.ratemyhangar.MainActivity.premiumT;
import static com.clara.bismark439.ratemyhangar.MainActivity.rare;
import static com.clara.bismark439.ratemyhangar.MainActivity.rareT;
import static com.clara.bismark439.ratemyhangar.MainActivity.toast;
import static com.clara.bismark439.ratemyhangar.crawl.tempList;
import static com.clara.bismark439.ratemyhangar.crawl.userInfo;
import static com.clara.bismark439.ratemyhangar.detail.whichHangar;

public class Hangar extends AppCompatActivity {
    LinearLayout currentCountry;
    boolean editmode = false;
    int loadCount=0;
    int[] totalCount=new int[7];
    int[] countedCount = new int[7];
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hangar);
        currentCountry = findViewById(R.id.usa);
        // int batchSize;

        //   toast(getApplicationContext(),getString(R.string.hangarHelp));
        if (whichHangar.equals("Air")) {
            // draw();
            draw1();
            draw2();
            draw3();
        } else {
            draw1G();
            draw2G();
            draw3G();
        }
    }


    public void draw1() {
        Thread a1 = new Thread() {
            public void run() {
                for (int i = 0; i < normal.getSize(); i++) {
                    plane temp = normal.getP(i);
                    planeDataView tv = getDataView();//put this in thread. 분할?
                    boolean has =tv.addData(temp);
                    totalCount[temp.country]++;
                    if(has){countedCount[temp.country]++;}
                    final LinearLayout tl = findViewById(getCountry(temp.country));
                    final planeDataView dumi = tv;
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            tl.addView(dumi);
                        }
                    });
                }

                for(int i=0;i<7;i++){
                    final hangarSummView hsv = findViewById(getCountryPage(i)).findViewWithTag("summary"+i);
                    final int id=getDrawablebyC(i);
                    final int index =i;
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {hsv.setData(id,countedCount[index],totalCount[index]);
                        }
                    });
                }
                loadCount++;
                if(loadCount==3){
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            toast(getApplicationContext(),"Loading Finished!");    }
                    });
                }
            }
        };
        a1.start();
    }
    public int getDrawablebyC(int i){
        int ret =0;
        switch (i){
            case 0:
                ret=R.drawable.us;
                break;
            case 1:
                ret=R.drawable.de;
                break;
            case 2:
                ret= R.drawable.ussr;
                break;
            case 3:
                ret=R.drawable.uk;
                break;
            case 4:
                ret=R.drawable.japan;
                break;
            case 5:
                ret=R.drawable.italy;
                break;
            case 6:
                ret=R.drawable.france;
                break;
        }
        return ret;
    }
    public void draw2() {
        Thread a1 = new Thread() {
            public void run() {
                for (int i = 0; i < premium.getSize(); i++) {
                    plane temp = premium.getP(i);
                    planeDataView tv = getDataView();//put this in thread. 분할?
                    tv.addData(temp);
                    final LinearLayout tl = findViewById(getCountryPage(temp.country)).findViewWithTag("prem");
                    final planeDataView dumi = tv;
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            tl.addView(dumi);
                        }
                    });
                }
                loadCount++;
                if(loadCount==3){
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            toast(getApplicationContext(),"Loading Finished!");    }
                    });
                }
            }
        };
        a1.start();
    }

    public void draw3() {
        Thread a1 = new Thread() {
            public void run() {
                for (int i = 0; i < rare.getSize(); i++) {
                    plane temp = rare.getP(i);
                    planeDataView tv = getDataView();//put this in thread. 분할?
                    tv.addData(temp);
                    tv.setVisibility(VISIBLE);
                    final LinearLayout tl = findViewById(R.id.rr);
                    final planeDataView dumi = tv;
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            tl.addView(dumi);
                        }
                    });
                }
                loadCount++;
                if(loadCount==3){
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            toast(getApplicationContext(),"Loading Finished!");    }
                    });
                }
            }
        };
        a1.start();
    }
    public planeDataView getDataView(){
        return new planeDataView(this);
    }
    public void draw1G() {
        Thread a1 = new Thread() {
            public void run() {
                for (int i = 0; i < normalT.getSize(); i++) {
                    plane temp = normalT.getP(i);
                    planeDataView tv = getDataView();//put this in thread. 분할?
                    boolean has =tv.addData(temp);
                    totalCount[temp.country]++;
                    if(has){countedCount[temp.country]++;}
                    final LinearLayout tl = findViewById(getCountry(temp.country));
                    final planeDataView dumi = tv;
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            tl.addView(dumi);
                        }
                    });
                }

                for(int i=0;i<7;i++){
                    final hangarSummView hsv = findViewById(getCountryPage(i)).findViewWithTag("summary"+i);
                    final int id=getDrawablebyC(i);
                    final int index =i;
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {hsv.setData(id,countedCount[index],totalCount[index]);
                        }
                    });
                }
                loadCount++;
                if(loadCount==3){
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            toast(getApplicationContext(),"Loading Finished!");    }
                    });
                }    }
        };
        a1.start();
    }

    public void draw2G() {
        Thread a1 = new Thread() {
            public void run() {
                for (int i = 0; i < premiumT.getSize(); i++) {
                    plane temp = premiumT.getP(i);
                    planeDataView tv = getDataView();//put this in thread. 분할?
                    tv.addData(temp);
                    final LinearLayout tl = findViewById(getCountryPage(temp.country)).findViewWithTag("prem");
                    final planeDataView dumi = tv;
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            tl.addView(dumi);
                        }
                    });
                }
                loadCount++;
                if(loadCount==3){
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            toast(getApplicationContext(),"Loading Finished!");    }
                    });
                }  }
        };
        a1.start();
    }

    public void draw3G() {
        Thread a1 = new Thread() {
            public void run() {
                for (int i = 0; i < rareT.getSize(); i++) {
                    plane temp = rareT.getP(i);
                    planeDataView tv = getDataView();//put this in thread. 분할?
                    tv.addData(temp);
                    tv.setVisibility(VISIBLE);
                    final LinearLayout tl = findViewById(R.id.rr);
                    final planeDataView dumi = tv;
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            tl.addView(dumi);
                        }
                    });
                }
                loadCount++;
                if(loadCount==3){
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            toast(getApplicationContext(),"Loading Finished!");    }
                    });
                }   }
        };
        a1.start();
    }

    public void ontoReport(View v){
        finish();
    }
    public static boolean isIn(String a) {
        for (int i = 0; i < tempList.getSize(); i++) {
            if (tempList.get(i).id.equals(a)) {
                return true;
            }
        }
        return false;
    }

    public int getCountry(int a) {
        switch (a) {
            case 0:
                return R.id.usa;
            case 1:
                return R.id.nazi;
            case 2:
                return R.id.ussr;
            case 3:
                return R.id.uk;
            case 4:
                return R.id.jp;
            case 5:
                return R.id.it;
            case 6:
                return R.id.fr;
            case 7:
                return R.id.rr;
        }
        return 0;
    }
    public int getCountryPage(int a) {
        switch (a) {
            case 0:
                return R.id.usa_scroll;
            case 1:
                return R.id.gr_scroll;
            case 2:
                return R.id.ru_scroll;
            case 3:
                return R.id.uk_scroll;
            case 4:
                return R.id.jp_scroll;
            case 5:
                return R.id.it_scroll;
            case 6:
                return R.id.fr_scroll;
            case 7:
                return R.id.rr_scroll;
        }
        return 0;
    }

    public void onCountry(View v) {
        String tt = (String) v.getTag();
        int t = Integer.parseInt(tt);
        for (int i = 0; i < 8; i++) {
            ScrollView tl = findViewById(getCountryPage(i));
            tl.setVisibility(View.INVISIBLE);
            //    ((ScrollView) tl.getParent()).setVisibility(View.INVISIBLE);
        }
        ScrollView tl = findViewById(getCountryPage(t));
        currentCountry = findViewById(getCountry(t));
        tl.setVisibility(VISIBLE);
        //    ((ScrollView) tl.getParent()).setVisibility(View.VISIBLE);

    }

    public void onEditMode(View v) {
        if(loadCount!=3){
            toast(getApplicationContext(),"Please wait until loading finishes...");
            return;
        }
        if (editmode) {
            editmode = false;
            for (int i = 0; i < currentCountry.getChildCount(); i++) {
                View vv = currentCountry.getChildAt(i);
                if (vv.getTag() != null) {
                    if (vv.getTag().equals("DATA")) {
                        ((planeDataView) vv).exit();
                    }
                }
            }
            saveMods();
            toast(getApplicationContext(),getString(R.string.hangarHelp3));
            ((Button)v).setText("Edit Data");
        } else {
            editmode = true;
            for (int i = 0; i < currentCountry.getChildCount(); i++) {
                View vv = currentCountry.getChildAt(i);
                if (vv.getTag() != null) {
                    if (vv.getTag().equals("DATA")) {
                        ((planeDataView) vv).editmode();
                        if(((planeDataView) vv).needToggle){
                            ((planeDataView) vv).edit.toggle();
                            ((planeDataView) vv).needToggle=false;
                        }//Only calls once
                    }
                }
            }
            ((Button)v).setText("Save");
        }
    }
    public void saveMods(){
        String playerFileName = userInfo.name+"+-+mods.txt";
        File outFile = new File(playerFileName);
        try {
            FileOutputStream os = openFileOutput(String.valueOf(outFile), Context.MODE_PRIVATE);
            PrintWriter printWriter = new PrintWriter (os); //set up a writer
            String content=modsToString();//set up the data to be printed
            printWriter.println (content);//print it
            printWriter.flush();
            printWriter.close ();
            System.out.println("SaveModData: Successfully saved");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            e.getLocalizedMessage();
            System.out.println("Error saving mods");
        }
    }
    static String modsToString(){
        StringBuilder ret= new StringBuilder("ADD\n");
        for(int i=0;i<modifiedIDs.size();i++){
            ret.append(modifiedIDs.get(i));
            if(i<modifiedIDs.size()-1){
                ret.append(",");
            }
        }
        return ret.toString();
    }
}

