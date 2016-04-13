package io.thomasross.frcscout;

import android.app.Activity;
import android.app.LoaderManager;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.*;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import io.thomasross.frcscout.adapters.TeamListAdapter;
import io.thomasross.frcscout.loaders.TeamLoader;
import io.thomasross.frcscout.models.Team;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;

public class Main extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor>
{
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final Activity us = this;

        FloatingActionButton addTeamBtn = (FloatingActionButton) findViewById(R.id.addTeam);
        addTeamBtn.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent addTeamIntent = new Intent(us, AddTeam.class);
                startActivity(addTeamIntent);
            }
        });
    }

    @Override
    protected void onStart()
    {
        super.onStart();

        getLoaderManager().initLoader(0, null, this).forceLoad();
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args)
    {
        return new TeamLoader(this, null, null, null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data)
    {
        ArrayList<Team> teams = new ArrayList<>();

        if (data.getCount() == 0) { return; }

        while (data.moveToNext())
        {
            String tasksJSON = data.getString(2);

            Type jsonType = new TypeToken<HashMap<String, Boolean>>(){}.getType();
            Gson gson = new GsonBuilder().create();
            HashMap<String, Boolean> tasksMap = gson.fromJson(tasksJSON, jsonType);

            ArrayList<String> tasks = new ArrayList<>();
            tasks.add("portcullis");
            tasks.add("cheval");
            tasks.add("moat");
            tasks.add("ramparts");
            tasks.add("drawbridge");
            tasks.add("sallyport");
            tasks.add("rockwall");
            tasks.add("roughterrain");
            tasks.add("lowbar");
            tasks.add("lowgoal");
            tasks.add("highgoal");
            tasks.add("scaletower");

            int numTasksAble = 0;
            for (String key : tasks)
            {
                if (!tasksMap.containsKey(key))
                {
                    continue;
                }
                if (tasksMap.get(key))
                {
                    numTasksAble++;
                }
            }

            Team team = new Team(data.getInt(0), data.getString(1), data.getString(2), data.getInt(3), numTasksAble, tasks.size());
            teams.add(team);
        }

        TeamListAdapter adapter = new TeamListAdapter(this, R.layout.teamlistitem, teams);
        ListView listView = (ListView) findViewById(R.id.teamListView);
        listView.setAdapter(adapter);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {}
}
