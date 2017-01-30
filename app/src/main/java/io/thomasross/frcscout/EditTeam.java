package io.thomasross.frcscout;

import android.app.AlertDialog;
import android.app.LoaderManager;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.*;
import android.widget.*;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import io.thomasross.frcscout.adapters.TaskListAdapter;
import io.thomasross.frcscout.loaders.TeamLoader;
import io.thomasross.frcscout.models.Task;
import io.thomasross.frcscout.models.Team;
import io.thomasross.frcscout.tasks.DeleteTeamTask;
import io.thomasross.frcscout.tasks.UpdateTeamTask;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;

public class EditTeam extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor>,FinishedCallback
{
    int teamNumber;
    TaskListAdapter tasksListAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editteam);

        Intent intent = getIntent();
        teamNumber = intent.getIntExtra("io.thomasross.frcscout.teamnumber", -1);

        Bundle bundle = new Bundle();
        bundle.putInt("teamnumber", teamNumber);
        getLoaderManager().initLoader(0, bundle, this).forceLoad();

        EditText teamNumberET = (EditText) findViewById(R.id.teamnumber_edit_ET);
        teamNumberET.setKeyListener(null);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        final EditTeam us = this;

        getMenuInflater().inflate(R.menu.menu_editteam, menu);

        MenuItem doneBtn = menu.findItem(R.id.action_checkmark_ET);
        doneBtn.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener()
        {
            @Override
            public boolean onMenuItemClick(MenuItem item)
            {
                us.onDoneClicked();
                return false;
            }
        });

        MenuItem deleteBtn = menu.findItem(R.id.action_delete_ET);
        deleteBtn.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener()
        {
            @Override
            public boolean onMenuItemClick(MenuItem item)
            {
                us.onDeleteClicked();
                return false;
            }
        });

        return super.onCreateOptionsMenu(menu);
    }

    private void onDoneClicked()
    {
        ArrayList<Task> tasks = tasksListAdapter.getTaskList();
        HashMap<String, Boolean> tasksMap = new HashMap<>();
        for (Task task : tasks)
        {
            tasksMap.put(task.code, task.isTeamAble);
        }

        Gson gson = new GsonBuilder().create();
        String tasksJSON = gson.toJson(tasksMap);

        EditText teamNameET = (EditText) findViewById(R.id.teamname_edit_ET);

        EditText autoPtsET = (EditText) findViewById(R.id.autonomouspoints_edit_ET);
        int autoPts = Integer.parseInt(autoPtsET.getText().toString());

        UpdateTeamTask task = new UpdateTeamTask(this);
        task.addFinishCallback(this);
        task.execute(new Team(teamNumber, teamNameET.getText().toString(), tasksJSON, autoPts));
    }

    private void onDeleteClicked()
    {
        final EditTeam us = this;
        DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener()
        {
            @Override
            public void onClick(DialogInterface dialog, int which)
            {
                switch (which)
                {
                    case DialogInterface.BUTTON_POSITIVE:
                        DeleteTeamTask task = new DeleteTeamTask(us);
                        task.addFinishCallback(us);

                        task.execute(teamNumber);

                        break;
                }
            }
        };

        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        dialogBuilder.setMessage("Are you sure?")
                     .setPositiveButton("Yes", dialogClickListener)
                     .setNegativeButton("No", dialogClickListener)
                     .show();
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args)
    {
        String where = "TEAMNUMBER = ?";
        String[] whereArgs = {"" + args.getInt("teamnumber")};
        return new TeamLoader(this, null, where, whereArgs);
    }



    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data)
    {
        if (data.getCount() > 0)
        {
            data.moveToNext();

            EditText teamNumberET = (EditText) findViewById(R.id.teamnumber_edit_ET);
            EditText teamNameET = (EditText) findViewById(R.id.teamname_edit_ET);
            EditText autoPointsET = (EditText) findViewById(R.id.autonomouspoints_edit_ET);

            teamNumberET.setText("" + data.getInt(0));
            teamNameET.setText(data.getString(1));
            autoPointsET.setText("" + data.getInt(3));

            String tasksJSON = data.getString(2);

            Type jsonType = new TypeToken<HashMap<String, Boolean>>(){}.getType();
            Gson gson = new GsonBuilder().create();
            HashMap<String, Boolean> tasksMap = gson.fromJson(tasksJSON, jsonType);

            HashMap<String, String> taskDetails = GamesManager.getTasks();

            ArrayList<Task> teamAbleTasks = new ArrayList<>();
            for (String key : taskDetails.keySet())
            {
                if (!tasksMap.containsKey(key))
                {
                    teamAbleTasks.add(new Task(key, taskDetails.get(key), false));
                    continue;
                }
                if (tasksMap.get(key))
                {
                    teamAbleTasks.add(new Task(key, taskDetails.get(key), true));
                }
                else
                {
                    teamAbleTasks.add(new Task(key, taskDetails.get(key), false));
                }
            }

            tasksListAdapter = new TaskListAdapter(this, R.layout.tasklistitem, teamAbleTasks);
            ListView listView = (ListView) findViewById(R.id.tasksList);
            listView.setAdapter(tasksListAdapter);
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {}

    @Override
    public void done()
    {
        finish();
    }
}