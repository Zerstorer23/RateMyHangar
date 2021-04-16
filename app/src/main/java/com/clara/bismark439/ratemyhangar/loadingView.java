package com.clara.bismark439.ratemyhangar;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.TextView;


public class loadingView extends LinearLayout {
    LinearLayout lin ;
    int green = Color.parseColor("#00c800");
    int blue = Color.parseColor("#0000C8");
    Context mContext ;
    public loadingView(Context context) {
        super(context);
        init(context);
    }

    public loadingView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public void init(Context context) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.res_loading_view, this, true);
        lin = findViewById(R.id.load_cont);
        mContext = context;
    }

    public void setLoaded(int index) {
        final int i = index;
        ((Activity) mContext).runOnUiThread(new Runnable() {
            @Override
            public void run() {
                TextView tv = lin.findViewWithTag(i+"");
                tv.setTextColor(green);
                tv.append(" √");
            }
        });
    }
    public void setPending(int index) {
        final int i = index;
        ((Activity) mContext).runOnUiThread(new Runnable() {
            @Override
            public void run() {
                TextView tv = lin.findViewWithTag(i+"");
                tv.setTextColor(blue);
            }
        });
    }
}
