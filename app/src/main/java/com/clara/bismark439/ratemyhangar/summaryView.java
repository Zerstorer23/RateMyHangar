package com.clara.bismark439.ratemyhangar;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.TextView;

import static com.clara.bismark439.ratemyhangar.R.id.RR;

public class summaryView extends LinearLayout {
    TextView GE;
    TextView RP;
    TextView SL;
    TextView normal;
    TextView premium;
    TextView rare;
    public summaryView(Context context) {
        super(context);
        init(context);
    }

    public summaryView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public void init(Context context) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.res_summary_view, this, true);
        GE= (TextView)findViewById(R.id.GE);
        SL= (TextView)findViewById(R.id.SL);
        RP= (TextView)findViewById(R.id.RP);
        normal= (TextView)findViewById(R.id.Normal);
        premium= (TextView)findViewById(R.id.premium);
        rare= (TextView)findViewById(RR);
    }

    public void setData(int Research, int Gold, int Silver, int Normal,int nt, int Premium,int pt, int Rare, int rt) {
        RP.setText(Research+ "");
        GE.setText(Gold + "");
        SL.setText(Silver+"");
        rare.setText(Rare + " / " + rt);
        premium.setText(Premium + " / " + pt);
        normal.setText(Normal+ " / " +nt);
    }
}
