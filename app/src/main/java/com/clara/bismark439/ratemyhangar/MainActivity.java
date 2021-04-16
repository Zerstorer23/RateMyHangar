package com.clara.bismark439.ratemyhangar;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.os.Looper;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import com.clara.bismark439.ratemyhangar.suggestion.suggestion_main;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.MobileAds;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;

import static com.clara.bismark439.ratemyhangar.crawl.MY_PREFS_NAME;
import static com.clara.bismark439.ratemyhangar.crawl.reset;
import static com.clara.bismark439.ratemyhangar.crawl.tempList;
import static com.clara.bismark439.ratemyhangar.crawl.userInfo;
import static com.clara.bismark439.ratemyhangar.suggestion.Suggestion.networkInit;
import static com.clara.bismark439.ratemyhangar.suggestion.Suggestion.readNetwork;

/*
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.games.Games;
import com.google.example.games.basegameutils.BaseGameUtils;*/

public class MainActivity extends AppCompatActivity {// implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener  {
    public static String myNick;
    static int myCurr = 0;
    String[] currency = {"USD", "KRW", "EUR", "RUB"};
    public static String[] dataFiles = {"Choose the data"};
    public static String myFile;
    static int clicks = (int) (Math.random() * 79 + 1);
    public static final String dataVersion = "1.0";
    public static InterstitialAd mInterstitialAd;
    static boolean muteSound = false;
    public static ArrayList<String> modifiedIDs = new ArrayList<>();
    //   static ArrayList<String> modifiedIDremoves = new ArrayList<String>();
    static planeData normal = new planeData();
    static planeData rare = new planeData();
    static planeData premium = new planeData();
    static planeData normalT = new planeData();
    static planeData rareT = new planeData();
    static planeData premiumT = new planeData();
    public static Database database;


    static graphData sGraphData = new graphData();
    public static planeData All = new planeData();
    static String[] soundList = {"m2", "mk103", "breda", "ns23", "hispano"};
    public static double myTotalPracticalMoney = 0;

    static boolean errorCrawl = false;
    static boolean invalidNicknameError = false;
    static String errorMsg;

    static AdRequest request;

    /* private static int RC_SIGN_IN = 9001;
     private boolean mResolvingConnectionFailure = false;
     private boolean mAutoStartSignInflow = true;
     private boolean mSignInClicked = false;
     private GoogleApiClient mGoogleApiClient;*/
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Set fullscreen
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        // Set No Title
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main);
        load();
        MobileAds.initialize(this, "ca-app-pub-4557475384906138~8437168856");
        mInterstitialAd = new InterstitialAd(this);
        mInterstitialAd.setAdUnitId("ca-app-pub-4557475384906138/8385553183");//ca-app-pub-3940256099942544/1033173712 = test
        request = new AdRequest.Builder()
                .addTestDevice("9CA9AAA0DDA7ED97178D9455C9AC736C")
                .build();
        mInterstitialAd.loadAd(request);
        mInterstitialAd.setAdListener(new AdListener() {
            @Override
            public void onAdLoaded() {
                // Code to be executed when an ad finishes loading.
                Log.i("Ads", "onAdLoaded");
            }

            @Override
            public void onAdFailedToLoad(int errorCode) {
                // Code to be executed when an ad request fails.
                Log.i("Ads", "onAdFailedToLoad");
            }

            @Override
            public void onAdOpened() {
                // Code to be executed when the ad is displayed.
                Log.i("Ads", "onAdOpened");
            }

            @Override
            public void onAdLeftApplication() {
                // Code to be executed when the user has left the app.
                Log.i("Ads", "onAdLeftApplication");
            }

            @Override
            public void onAdClosed() {
                // Code to be executed when when the interstitial ad is closed.
                String help = "You are " + clicks + "th person to click this!";
                Toast.makeText(getApplicationContext(), help, Toast.LENGTH_LONG).show();
                clicks++;
                SharedPreferences.Editor editor = getSharedPreferences(MY_PREFS_NAME, Context.MODE_PRIVATE).edit();
                editor.putInt("clicks", clicks);
                editor.apply();
                AdRequest request = new AdRequest.Builder()
                        .addTestDevice("9CA9AAA0DDA7ED97178D9455C9AC736C")
                        .build();
                mInterstitialAd.loadAd(request);
                Log.i("Ads", "onAdClosed");
            }
        });

        //Currency SPiner
        Spinner spinner = findViewById(R.id.spinner);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, currency);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {


            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                myCurr = position;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                //myCurr= 0;
            }
        });

        //Radio
        RadioGroup radioGroup = findViewById(R.id.radioGroup);
        radioGroup.check(R.id.radioButton);
        final Button yukiButton = findViewById(R.id.yukiButton);
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                // checkedId is the RadioButton selected
                Spinner chooseAcc = findViewById(R.id.chooseAccount);
                EditText edittext = findViewById(R.id.Input);
                switch (checkedId) {
                    case R.id.radioButton:
                        chooseAcc.setVisibility(View.GONE);
                        edittext.setVisibility(View.VISIBLE);
                        yukiButton.setVisibility(View.GONE);
                        break;
                    case R.id.radioButton2:

                        String help = getString(R.string.acchelp);
                        Toast.makeText(getApplicationContext(), help, Toast.LENGTH_LONG).show();
                        updateDataspinner();
                        chooseAcc.setVisibility(View.VISIBLE);
                        edittext.setVisibility(View.GONE);
                        yukiButton.setVisibility(View.VISIBLE);
                        break;

                }
            }
        });
        try {
            database = new Database(getApplicationContext());

            sGraphData.init(getApplicationContext());
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
            mergeAll();
        } catch (FileNotFoundException e) {
            System.out.println("File not found ");
            e.printStackTrace();
            toast(getApplicationContext(), "File not Found: report to developer if problem continues.");
            finish();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        System.out.println("MainActivity resumed");
        if (errorCrawl) {
            toast(getApplicationContext(), "[ERROR] You can't connect to \n" + errorMsg);
        }
    }

    public void updateDataspinner() {
        getTxtList();
        //Data Spinner
        Spinner spinData = findViewById(R.id.chooseAccount);
        String[] nameList = new String[dataFiles.length];
        nameList[0] = dataFiles[0];
        for (int i = 1; i < nameList.length; i++) {
            nameList[i] = dataFiles[i].substring(0, dataFiles[i].length() - 4);
        }
        ArrayAdapter<String> adapterData = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, dataFiles);
        spinData.setAdapter(adapterData);
        spinData.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                myFile = dataFiles[position];
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }

    public void onClickRate(View v) {
        RadioGroup radioGroup = findViewById(R.id.radioGroup);
        CheckBox muteChekced = findViewById(R.id.MuteSound);
        muteSound = muteChekced.isChecked();
        int rid = radioGroup.getCheckedRadioButtonId();
        reset();
        Intent intent = new Intent(getApplicationContext(), crawl.class);
        EditText temp = findViewById(R.id.Input);
        switch (rid) {
            case R.id.radioButton:
                myNick = String.valueOf(temp.getText());
                break;
            case R.id.radioButton2:
                loadOfflineFiles(getApplicationContext());
                userInfo.init = true;
                myNick = userInfo.name;
                break;
        }
        if (invalidNicknameError) {
            toast(getApplicationContext(), "Invalid nickname");
            invalidNicknameError = false;
        } else {
            loadModData();
            startActivity(intent);
        }
    }

    public void onClickStat(View v) {
        Intent intent = new Intent(getApplicationContext(), suggestion_main.class);
        if (!networkInit) {
            readNetwork(getApplicationContext());
            networkInit = true;
        }
        loadOfflineFiles(getApplicationContext());
        if (invalidNicknameError) {
            toast(getApplicationContext(), "Invalid nickname");
            invalidNicknameError = false;
        } else {
            userInfo.init = true;
            myNick = userInfo.name;
            startActivity(intent);
        }
    }

    public void onClickThunder(View v) {
        EditText temp = findViewById(R.id.Input);
        myNick = String.valueOf(temp.getText());
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://thunderskill.com/en/stat/" + myNick));
        if (myNick == null || myNick.equals("Error")) {
            intent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://thunderskill.com/en/squad_top"));
        }
        startActivity(intent);
    }

    public void onClickHelp(View v) {
        Intent intent = new Intent(getApplicationContext(), act_popup.class);
        startActivity(intent);
    }

    private void load() {
        SharedPreferences prefs = getSharedPreferences(MY_PREFS_NAME, Context.MODE_PRIVATE);
        String restoredText = prefs.getString("init", null);
        if (restoredText != null) {
            myNick = prefs.getString("idURL", "Error");
            int rand = (int) (Math.random() * 71 + 1);
            clicks = prefs.getInt("clicks", rand);
            clicks = clicks + rand;
            // muteSound=prefs.getBoolean("Mute",false);
            EditText temp = findViewById(R.id.Input);
            temp.setText(myNick);
        }
    }

    static int getInt(String a) {
        a = a.replaceAll("[^0-9]+", "");  // add ? for -
        //a=a.trim();
        return Integer.parseInt(a);
    }

    public void loadModData() {
        try {
            String playerFileName = myNick + "+-+mods.txt";
            System.out.println("LoadModData: File found!");
            File infile = new File(playerFileName);
            InputStream inputStream = openFileInput(String.valueOf(infile));
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
            BufferedReader sc = new BufferedReader(inputStreamReader);
            String temp = sc.readLine();
            if (temp.equals("ADD")) {
                String[] token = sc.readLine().split(",");
                //ADDs added
                modifiedIDs.addAll(Arrays.asList(token));
            }
            inputStream.close();
        } catch (FileNotFoundException e) {
            System.out.println("LoadModData: File not found");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        database.close();
    }

    public void getTxtList() {
        InputStream inputStream;
        try {
            inputStream = openFileInput("List.txt");
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
            BufferedReader sc = new BufferedReader(inputStreamReader);
            String temp = sc.readLine();
            String[] token = temp.split(",");
            dataFiles = new String[token.length];
            for (int i = 0; i < token.length; i++) {
                if (token[i] != null) {
                    dataFiles[i] = token[i];
                }
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (NullPointerException e) {
            e.printStackTrace();
        }

    }

    public static void loadOfflineFiles(Context mContext) {
        String playerFileName = myFile;
        System.out.println("READING " + myFile);
        File infile = new File(playerFileName);
        tempList.reset();
        try {
            InputStream inputStream = mContext.openFileInput(String.valueOf(infile));
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
            BufferedReader sc = new BufferedReader(inputStreamReader);
            System.out.println(infile.getAbsoluteFile());
            String temp;
            String[] checkV = sc.readLine().split(",");

            if (checkV[0] != null && checkV[1].equals(dataVersion)) {//inited
                userInfo.init = true;
                String[] token = sc.readLine().split(",");
                userInfo.name = token[0];
                userInfo.title = token[1];
                userInfo.level = token[2];
                userInfo.date = token[3];
                userInfo.img = token[4];
                //UserInfo done

                if (sc.readLine().equals("Inventory")) {
                    for (int i = 0; i < 7; i++) {//USA,63,26,13,
                        token = sc.readLine().split(",");
                        for (int x = 0; x < token.length; x++) {
                            userInfo.inventory[i][x] = token[x];
                        }
                    }
                }//Inventory done.
                temp = sc.readLine();
                if (temp.equals("CVList")) {
                    while ((temp = sc.readLine()) != null) {//has next line
                        token = temp.split(",");
                        String name = token[0];
                        String id = token[1];
                        int[] stats = new int[token.length - 2];
                        for (int i = 0; i < stats.length; i++) {
                            stats[i] = Integer.parseInt(token[i + 2]);
                        }
                        CV CVt = new CV(name, id, stats);
                        tempList.add(CVt);
                    }
                }//CVList done.
            }
            inputStream.close();
        } catch (FileNotFoundException e) {
            invalidNicknameError = true;
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    static void toast(Context context, String a) {
//        Looper.prepare();
        Toast.makeText(context, a, Toast.LENGTH_LONG).show();
    }

    public static void mergeAll() {
        All = new planeData();
        All.addList(normal);
        All.addList(normalT);
        All.addList(premium);
        All.addList(premiumT);
        All.addList(rare);
        All.addList(rareT);
        All.sortbyID();
    }
//region GooglePlay
/*
    @Override
    protected void onStart() {
        super.onStart();
        //         mGoogleApiClient.connect();

    }
    @Override
    protected void onStop() {
        super.onStop();
        mGoogleApiClient.disconnect();
    }

    @Override
    public void onConnected(Bundle connectionHint) {
        // The player is signed in. Hide the sign-in button and allow the
        // player to proceed.
        TextView tv=(TextView)findViewById(R.id.signin);
        tv.setVisibility(View.GONE);
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        if (mResolvingConnectionFailure) {
            // already resolving
            return;
        }

        // if the sign-in button was clicked or if auto sign-in is enabled,
        // launch the sign-in flow
        if (mSignInClicked || mAutoStartSignInflow) {
            mAutoStartSignInflow = false;
            mSignInClicked = false;
            mResolvingConnectionFailure = true;

            // Attempt to resolve the connection failure using BaseGameUtils.
            // The R.string.signin_other_error value should reference a generic
            // error string in your strings.xml file, such as "There was
            // an issue with sign-in, please try again later."
            if (!BaseGameUtils.resolveConnectionFailure(this,
                    mGoogleApiClient, connectionResult,
                    RC_SIGN_IN, R.string.signin_other_error)) {
                mResolvingConnectionFailure = false;
            }
        }

        // Put code here to display the sign-in button
    }

    @Override
    public void onConnectionSuspended(int i) {
        // Attempt to reconnect
        mGoogleApiClient.connect();
    }
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        if (requestCode == RC_SIGN_IN) {
            mSignInClicked = false;
            mResolvingConnectionFailure = false;
            if (resultCode == RESULT_OK) {
                mGoogleApiClient.connect();
            } else {
                System.out.println(resultCode+" + "+RESULT_OK);
                // Bring up an error dialog to alert the user that sign-in
                // failed. The R.string.signin_failure should reference an error
                // string in your strings.xml file that tells the user they
                // could not be signed in, such as "Unable to sign in."
                BaseGameUtils.showActivityResultError(this,requestCode, resultCode, R.string.signin_failure);
            }
        }
    }

    // Call when the sign-in button is clicked
    public void signInClicked(View v) {
        mSignInClicked = true;
        mGoogleApiClient.connect();
    }

    // Call when the sign-out button is clicked
    public void signOutclicked(View v) {
        mSignInClicked = false;
        Games.signOut(mGoogleApiClient);
    }
*/
//endregion
}

            /*
            08-10 13:40:46.876 24788-24788/com.clara.bismark439.ratemyhangar I/System.out: UserInfo
08-10 13:40:46.876 24788-24788/com.clara.bismark439.ratemyhangar I/System.out: Levent_1011011,Centurion,100,Registration date : 10.09.2016
08-10 13:40:46.876 24788-24788/com.clara.bismark439.ratemyhangar I/System.out: Inventory
08-10 13:40:46.876 24788-24788/com.clara.bismark439.ratemyhangar I/System.out: USA,63,26,13,
08-10 13:40:46.876 24788-24788/com.clara.bismark439.ratemyhangar I/System.out: USSR,78,37,6,
08-10 13:40:46.876 24788-24788/com.clara.bismark439.ratemyhangar I/System.out: Great Britain,55,24,5,
08-10 13:40:46.876 24788-24788/com.clara.bismark439.ratemyhangar I/System.out: Germany,85,27,5,
08-10 13:40:46.876 24788-24788/com.clara.bismark439.ratemyhangar I/System.out: Japan,55,25,3,
08-10 13:40:46.876 24788-24788/com.clara.bismark439.ratemyhangar I/System.out: Italy,34,20,0,
08-10 13:40:46.876 24788-24788/com.clara.bismark439.ratemyhangar I/System.out: CVList
08-10 13:40:46.876 24788-24788/com.clara.bismark439.ratemyhangar I/System.out: A-20G-25,a-20g,8,8,3,5,8,1,26
            * */