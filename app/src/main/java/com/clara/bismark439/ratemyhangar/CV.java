package com.clara.bismark439.ratemyhangar;

public class CV {
    public String id;
    int[]data;
    public String name;

    public CV(String name, String id, int[] data) {
        this.id = id;
        this.name = name;
        this.data=data;
    }
    public CV(String id) {
        this.id = id;
    }

    }
