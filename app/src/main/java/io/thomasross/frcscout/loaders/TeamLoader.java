package io.thomasross.frcscout.loaders;

import android.content.AsyncTaskLoader;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import io.thomasross.frcscout.TeamsOpenHelper;

public class TeamLoader extends AsyncTaskLoader<Cursor>
{
    TeamsOpenHelper teamsDatabase;

    String[] columns;
    String where;
    String[] whereArgs;

    public TeamLoader(Context context, String[] columns, String where, String[] whereArgs)
    {
        super(context);

        teamsDatabase = new TeamsOpenHelper(context);

        this.columns = columns;
        this.where = where;
        this.whereArgs = whereArgs;
    }

    @Override
    public Cursor loadInBackground()
    {
        SQLiteDatabase readableDB = teamsDatabase.getReadableDatabase();

        Cursor results = readableDB.query("Teams",
                columns,
                where,
                whereArgs,
                null,
                null,
                null);

        return results;
    }
}
