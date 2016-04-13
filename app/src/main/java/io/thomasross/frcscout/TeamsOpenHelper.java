package io.thomasross.frcscout;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class TeamsOpenHelper extends SQLiteOpenHelper
{
    private static final int DATABASE_VERSION = 4;
    private static final String DATABASE_NAME = "Teams";
    private static final String DATABASE_TABLE_NAME = "Teams";

    public TeamsOpenHelper(Context context)
    {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db)
    {
        db.execSQL("CREATE TABLE " + DATABASE_TABLE_NAME + " (" +
                "TEAMNUMBER INT NOT NULL, " +
                "TEAMNAME TEXT, " +
                "TASKS TEXT, " +
                "AUTOPTS INT" +
                ");");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
    {
        db.execSQL("ALTER TABLE " + DATABASE_TABLE_NAME + " " +
                "ADD COLUMN AUTOPTS INT;");
    }
}
