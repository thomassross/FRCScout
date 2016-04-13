package io.thomasross.frcscout;

import android.app.AlertDialog;
import android.app.LoaderManager;
import android.content.Context;
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
import io.thomasross.frcscout.loaders.TeamLoader;
import io.thomasross.frcscout.tasks.DeleteTeamTask;
import io.thomasross.frcscout.tasks.UpdateTeamTask;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

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

    private class Task //TODO: move to its own file
    {
        String code;
        String name;
        boolean isTeamAble;

        public Task(String code, String name, boolean isTeamAble)
        {
            this.code = code;
            this.name = name;
            this.isTeamAble = isTeamAble;
        }
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

            HashMap<String, String> taskDetails = new HashMap<>();
            taskDetails.put("portcullis", "Portcullis");
            taskDetails.put("cheval", "Cheval de Frise");
            taskDetails.put("moat", "Moat");
            taskDetails.put("ramparts", "Ramparts");
            taskDetails.put("drawbridge", "Drawbridge");
            taskDetails.put("sallyport", "Sally Port");
            taskDetails.put("rockwall", "Rock Wall");
            taskDetails.put("roughterrain", "Rough Terrain");
            taskDetails.put("lowbar", "Low Bar");
            taskDetails.put("lowgoal", "Low Goal");
            taskDetails.put("highgoal", "High Goal");
            taskDetails.put("scaletower", "Scale Tower");

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

    private class TaskListAdapter extends ArrayAdapter<Task>
    {
        private ArrayList<Task> taskList;

        public TaskListAdapter(Context context, int resource, List<Task> objects)
        {
            super(context, resource, objects);

            this.taskList = new ArrayList<>();
            this.taskList.addAll(objects);
        }

        private class ViewHolder
        {
            CheckBox able;
        }


        @Override
        public View getView(int position, View convertView, ViewGroup parent)
        {
            ViewHolder holder;

            if (convertView == null)
            {
                LayoutInflater vi = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                convertView = vi.inflate(R.layout.tasklistitem, null);

                holder = new ViewHolder();
                holder.able = (CheckBox) convertView.findViewById(R.id.checkBox);
                convertView.setTag(holder);

                holder.able.setOnClickListener(new View.OnClickListener()
                {
                    @Override
                    public void onClick(View v)
                    {
                        CheckBox checkBox = (CheckBox) v;
                        Task task = (Task) v.getTag();
                        task.isTeamAble = checkBox.isChecked();
                    }
                });
            }
            else
            {
                holder = (ViewHolder) convertView.getTag();
            }

            Task task = taskList.get(position);
            holder.able.setChecked(task.isTeamAble);
            holder.able.setText(task.name);
            holder.able.setTag(task);

            return convertView;
        }

        public ArrayList<Task> getTaskList()
        {
            return taskList;
        }
    }
}