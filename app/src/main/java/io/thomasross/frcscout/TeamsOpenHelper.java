package io.thomasross.frcscout;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class TeamsOpenHelper extends SQLiteOpenHelper
{
    private static final int DATABASE_VERSION = 5;
    private static final String DATABASE_NAME = "Teams";

    public TeamsOpenHelper(Context context)
    {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db)
    {
        db.execSQL("CREATE TABLE " + GamesManager.GAME_2016_TABLE_NAME + " (" +
                   "TEAMNUMBER INT NOT NULL, " +
                   "TEAMNAME TEXT, " +
                   "TASKS TEXT, " +
                   "AUTOPTS INT" +
                   ");");

        db.execSQL("CREATE TABLE " + GamesManager.GAME_2017_TABLE_NAME + " (" +
                   "TEAMNUMBER INT NOT NULL, " +
                   "TEAMNAME TEXT, " +
                   "TASKS TEXT, " +
                   "AUTOPTS INT" +
                   ");");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
    {
        if (oldVersion < 4)
        {
            db.execSQL("ALTER TABLE Teams " +
                       "ADD COLUMN AUTOPTS INT;");
        }

        if (oldVersion == 4 && newVersion == 5)
        {
            db.execSQL("ALTER TABLE Teams RENAME TO " + GamesManager.GAME_2016_TABLE_NAME);

            db.execSQL("CREATE TABLE " + GamesManager.GAME_2017_TABLE_NAME + " (" +
                       "TEAMNUMBER INT NOT NULL, " +
                       "TEAMNAME TEXT, " +
                       "TASKS TEXT, " +
                       "AUTOPTS INT" +
                       ");");
        }
    }
}
