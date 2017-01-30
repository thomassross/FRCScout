package io.thomasross.frcscout.loaders;

import android.content.AsyncTaskLoader;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import io.thomasross.frcscout.GamesManager;
import io.thomasross.frcscout.TeamsOpenHelper;

public class TeamLoader extends AsyncTaskLoader<Cursor>
{
    private TeamsOpenHelper teamsDatabase;

    private String[] columns;
    private String where;
    private String[] whereArgs;

    public TeamLoader(Context context, String[] columns, String where, String[] whereArgs)
    {
        super(context);

        this.teamsDatabase = new TeamsOpenHelper(context);

        this.columns = columns;
        this.where = where;
        this.whereArgs = whereArgs;
    }

    @Override
    public Cursor loadInBackground()
    {
        SQLiteDatabase readableDB = teamsDatabase.getReadableDatabase();

        return readableDB.query(GamesManager.getCurrentTableName(),
                                columns,
                                where,
                                whereArgs,
                                null,
                                null,
                                null);
    }
}
