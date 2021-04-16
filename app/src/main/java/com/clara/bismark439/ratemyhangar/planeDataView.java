package com.clara.bismark439.ratemyhangar;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import static com.clara.bismark439.ratemyhangar.Hangar.isIn;
import static com.clara.bismark439.ratemyhangar.MainActivity.modifiedIDs;

public class planeDataView extends LinearLayout {
    TextView RP;
    TextView SL;
    TextView f1;
    TextView f2;
    TextView name;
    CheckBox edit;
    boolean owned = false;
    boolean enableEdit = false;
    boolean init=false;
    boolean needToggle=false;

    public planeDataView(Context context) {
        super(context);
        init(context);
    }

    public planeDataView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public void init(Context context) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.res_planedata_view, this, true);
        SL = (TextView) findViewById(R.id.plane_d2);
        RP = (TextView) findViewById(R.id.plane_d1);
        f2 = (TextView) findViewById(R.id.plane_field2);
        f1 = (TextView) findViewById(R.id.plane_field1);
        name = (TextView) findViewById(R.id.plane_name);
        edit = (CheckBox) findViewById(R.id.editMode);
        edit.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                                            @Override
                                            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                                                if (buttonView.isChecked()) {
                                                    name.setTextColor(Color.parseColor("#1AAA1A"));
                                                    if(init){ modifiedIDs.add((String)buttonView.getTag());
                                                    System.out.println("Added!"+(String)buttonView.getTag());
                                                       }else{init=true;}

                                                    //checked
                                                } else {
                                                 //   System.out.println(modifiedIDs.get(0));
                                                    name.setTextColor(Color.parseColor("#000000"));
                                                    System.out.println("ModData: Remove - "+(String)buttonView.getTag()+modifiedIDs.remove((String)buttonView.getTag()));
                                                   // System.out.println(modifiedIDs.get(0));
                                                }
                                            }
                                        }
        );
    }

    public boolean addData(plane temp) {
        boolean has=false;
        name.setText(temp.name + "");
        RP.setText(temp.point + "");
        edit.setTag(temp.id);
        if (temp.stat >= 1) {//For premiums
            switch (temp.stat) {//stat 0= Regular tree, 1 = GE prem, 2 = BUndle Prem, 3= Rare
                case 1:
                    f1.setText("Golden Eagle: ");
                    break;
                case 2:
                    f1.setText("Bundle: $");
                    break;
                case 3:
                    f1.setVisibility(GONE);
                    RP.setVisibility(GONE);
                    break;
            }
            f2.setVisibility(GONE);
            SL.setVisibility(GONE);
        } else {
            SL.setText(temp.lion + "");
        }
        if (isIn(temp.id)) {
            name.setTextColor(Color.parseColor("#0000ff"));
            owned = true;
            has=true;
        }else{
            if(temp.stat==0){
                enableEdit = true;
                this.setTag("DATA");
                for(int i=0;i<modifiedIDs.size();i++){
                    if(modifiedIDs.get(i).equals(temp.id)){
                        name.setTextColor(Color.parseColor("#1AAA1A"));
                        needToggle=true;
                        has=true;
                        i=modifiedIDs.size()+1;
                    }
                }
                if(!needToggle){init=true;}
            }

        }
        return has;
    }

    public void editmode() {
        if (!enableEdit) return;
        edit.setVisibility(VISIBLE);
    }

    public void exit() {
        if (!enableEdit) return;
        edit.setVisibility(GONE);
    }

}
