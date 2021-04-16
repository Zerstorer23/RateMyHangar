package com.clara.bismark439.ratemyhangar;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

public class hangarSummView extends LinearLayout {
    TextView tv;
    ImageView img;
    ProgressBar collection;
    public hangarSummView(Context context) {
        super(context);
        init(context);
    }

    public hangarSummView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public void init(Context context) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.res_hangar_summview, this, true);
        tv= (TextView)findViewById(R.id.hs_text);
        img= (ImageView)findViewById(R.id.hs_img);
        collection= (ProgressBar)findViewById(R.id.hs_progress);
    }

    public void setData(int drwID, int mine, int max) {
        img.setImageResource(drwID);
        double percent = ((double)mine /(double) max) * 100;
        int a = (int) percent;
        collection.setProgress(a);
        tv.setText("Normal Collection : "+a+"%");
    }
}
