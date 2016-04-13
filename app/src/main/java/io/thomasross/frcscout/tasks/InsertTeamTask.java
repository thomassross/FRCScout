package io.thomasross.frcscout.tasks;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import io.thomasross.frcscout.FinishedCallback;
import io.thomasross.frcscout.Team;
import io.thomasross.frcscout.TeamsOpenHelper;

import java.util.ArrayList;

public class InsertTeamTask extends AsyncTask<Team, Void, Void>
{
    Context context;
    ArrayList<FinishedCallback> finishedCallbacks = new ArrayList<>();

    public InsertTeamTask(Context context)
    {
        this.context = context;
    }

    public void addFinishCallback(FinishedCallback callback)
    {
        finishedCallbacks.add(callback);
    }

    @Override
    protected void onPostExecute(Void aVoid)
    {
        for (FinishedCallback callback : finishedCallbacks)
        {
            callback.done();
        }

        super.onPostExecute(aVoid);
    }

    @Override
    protected Void doInBackground(Team... teams)
    {
        TeamsOpenHelper teamsDatabase = new TeamsOpenHelper(context);

        SQLiteDatabase writeableDB = teamsDatabase.getWritableDatabase();

        for (Team team : teams)
        {
            ContentValues values = new ContentValues();
            values.put("TEAMNUMBER", team.number);
            values.put("TEAMNAME", team.name);
            values.put("TASKS", team.tasks);
            values.put("AUTOPTS", team.autoPoints);
            writeableDB.insert("Teams", null, values);
        }

        writeableDB.close();

        return null;
    }
}
