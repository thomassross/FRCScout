package io.thomasross.frcscout.tasks;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import io.thomasross.frcscout.FinishedCallback;
import io.thomasross.frcscout.GamesManager;
import io.thomasross.frcscout.models.Team;
import io.thomasross.frcscout.TeamsOpenHelper;

import java.util.ArrayList;

public class InsertTeamTask extends AsyncTask<Team, Void, Void>
{
    private Context context;
    private ArrayList<FinishedCallback> finishedCallbacks = new ArrayList<>();

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
            values.put("TEAMNUMBER", team.getNumber());
            values.put("TEAMNAME", team.getName());
            values.put("TASKS", team.getTasks());
            values.put("AUTOPTS", team.getAutoPoints());
            writeableDB.insert(GamesManager.getCurrentTableName(), null, values);
        }

        writeableDB.close();

        return null;
    }
}
