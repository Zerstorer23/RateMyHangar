package com.clara.bismark439.ratemyhangar;

import java.util.ArrayList;

public class CVlist {

    ArrayList<CV> vehicleDB;

    public CVlist() {
        this.vehicleDB = new ArrayList<CV>();
    }
    public CV getCVByName(String id){
        for(int i=0;i<this.vehicleDB.size();i++){
            if(this.vehicleDB.get(i).id.equals(id)){
                return this.vehicleDB.get(i);
            }
        }
        return null;
    }
    public int getSize(){
        return this.vehicleDB.size();
    }
    public CV get(int i){
        return this.vehicleDB.get(i);
    }
    public void add(String a, String b, int[] c) {
        CV temp = new CV(a, b, c);
        this.vehicleDB.add(temp);
    }

    public void add(CV t) {
        this.vehicleDB.add(t);
    }
    public void reset() {
        this.vehicleDB = new ArrayList<CV>();
    }
public void removeDuplicates(){
    for(int i=0;i<vehicleDB.size();i++){
        for(int x=i+1;x<vehicleDB.size();x++){
            CV a=vehicleDB.get(i);
            CV b=vehicleDB.get(x);
            if(i!=x&&a.id.equals(b.id)){
                for(int pp=0;pp<7;pp++){
                 a.data[pp]=a.data[pp]+b.data[pp];
                }
                vehicleDB.remove(x);
                x--;
            }//abcdde
        }
    }
}

public void sortByBattles(){
CV[] test = new CV[this.vehicleDB.size()];
    for(int i=0;i<test.length;i++){
        test[i]=this.vehicleDB.get(i);
    }
    int n=0;
    while(n<test.length-1) {
        if(test[n].data[0]<test[n+1].data[0]) {
            CV temp = test[n];
            test[n]=test[n+1];
            test[n+1]=temp;
            if(n>0){n--;}

        }
        else { n++; }
    }//gnome sorted
    this.reset();
    for(int i=0;i<test.length;i++){
        this.vehicleDB.add(test[i]);
    }
}
public boolean has(String id){
    for(int i=0;i<this.vehicleDB.size();i++){
       if(this.get(i).id.equals(id))return true;
    }
    return false;
}

public String toString(){
    String res ="CVList";
    for(int i=0;i<this.vehicleDB.size();i++){
        CV temp=this.get(i);
        res=res+"\n"+temp.name+","+temp.id;
        for(int x=0;x<temp.data.length;x++){
            res=res+","+temp.data[x];
        }
    }
    return res;
}
}
