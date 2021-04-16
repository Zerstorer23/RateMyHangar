package com.clara.bismark439.ratemyhangar;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;

import com.readystatesoftware.sqliteasset.SQLiteAssetHelper;

/**
 * Created by Bismark439 on 24/09/2018.
 */

public class Database extends SQLiteAssetHelper {

    public  Cursor sCursor;
    public  Database db;

    private static final String DATABASE_NAME = "vehicles.db";
    private static final int DATABASE_VERSION = 3;

    public Database(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        setForcedUpgrade();
    }

    public Cursor getTable(String tName) {

        SQLiteDatabase db = getReadableDatabase();
        SQLiteQueryBuilder qb = new SQLiteQueryBuilder();

        String[] sqlSelect = {"*"};

        qb.setTables(tName);
        System.out.println(qb.getTables());
        Cursor c = qb.query(db, sqlSelect, null, null,
                null, null, null);

        c.moveToFirst();
        return c;
    }

}
