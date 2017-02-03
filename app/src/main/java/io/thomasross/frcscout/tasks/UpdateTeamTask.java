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

public class UpdateTeamTask extends AsyncTask<Team, Void, Void>
{
    private Context context;
    private ArrayList<FinishedCallback> finishedCallbacks = new ArrayList<>();

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
            values.put("TEAMNAME", team.getName());
            values.put("TASKS", team.getTasks());
            values.put("AUTOPTS", team.getAutoPoints());
            String[] whereArgs = {"" + team.getNumber()};
            writeableDB.update(GamesManager.getCurrentTableName(), values, "TEAMNUMBER = ?", whereArgs);
        }

        writeableDB.close();

        return null;
    }
}
