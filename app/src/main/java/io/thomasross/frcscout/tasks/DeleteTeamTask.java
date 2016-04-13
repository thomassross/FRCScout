package io.thomasross.frcscout.tasks;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import io.thomasross.frcscout.FinishedCallback;
import io.thomasross.frcscout.TeamsOpenHelper;

import java.util.ArrayList;

public class DeleteTeamTask extends AsyncTask<Integer, Void, Void>
{
    Context context;
    ArrayList<FinishedCallback> finishedCallbacks = new ArrayList<>();

    public DeleteTeamTask(Context context)
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
    protected Void doInBackground(Integer... params)
    {
        TeamsOpenHelper teamsDatabase = new TeamsOpenHelper(context);

        SQLiteDatabase writeableDB = teamsDatabase.getWritableDatabase();

        for (int teamNumber : params)
        {
            String where = "TEAMNUMBER = ?";
            String[] whereArgs = {"" + teamNumber};
            writeableDB.delete("Teams", where, whereArgs);
        }

        writeableDB.close();

        return null;
    }
}
