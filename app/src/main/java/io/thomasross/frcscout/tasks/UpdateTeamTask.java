package io.thomasross.frcscout.tasks;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import io.thomasross.frcscout.FinishedCallback;
import io.thomasross.frcscout.models.Team;
import io.thomasross.frcscout.TeamsOpenHelper;

import java.util.ArrayList;

public class UpdateTeamTask extends AsyncTask<Team, Void, Void>
{
    Context context;
    ArrayList<FinishedCallback> finishedCallbacks = new ArrayList<>();

    public UpdateTeamTask(Context context)
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
    protected Void doInBackground(Team... params)
    {
        TeamsOpenHelper teamsDatabase = new TeamsOpenHelper(context);

        SQLiteDatabase writeableDB = teamsDatabase.getWritableDatabase();

        for (Team team : params)
        {
            ContentValues values = new ContentValues();
            values.put("TEAMNAME", team.name);
            values.put("TASKS", team.tasks);
            values.put("AUTOPTS", team.autoPoints);
            String[] whereArgs = {"" + team.number};
            writeableDB.update("Teams", values, "TEAMNUMBER = ?", whereArgs);
        }

        writeableDB.close();

        return null;
    }
}
