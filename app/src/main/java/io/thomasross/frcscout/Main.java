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
import io.thomasross.frcscout.loaders.TeamLoader;

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

        ListView listView = (ListView) findViewById(R.id.teamListView);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id)
            {
                TextView textView = (TextView) view;
                String tvText = textView.getText().toString();
                int teamNumber = Integer.parseInt(tvText.substring(0, tvText.indexOf(" "))); // TODO

                Intent intent = new Intent(us, EditTeam.class);
                intent.putExtra("io.thomasross.frcscout.teamnumber", teamNumber);
                startActivity(intent);
            }
        });

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
        ArrayList<String> teamNumbers = new ArrayList<>();

        if (data.getCount() == 0) { return; }

        while (data.moveToNext())
        {
            String tasksJSON = data.getString(2);

            Type jsonType = new TypeToken<HashMap<String, Boolean>>(){}.getType();
            Gson gson = new GsonBuilder().create();
            HashMap<String, Boolean> tasksMap = gson.fromJson(tasksJSON, jsonType);

            ArrayList<String> tasks = new ArrayList<String>();
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

            teamNumbers.add("" + data.getInt(0) + " - " + data.getString(1) + "\n\t"
                    + numTasksAble + "/" + tasks.size() + " tasks, " + data.getInt(3) + " auto points");
        }

        String[] teamNumbersArr = teamNumbers.toArray(new String[teamNumbers.size()]);

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.simpletextview, teamNumbersArr);
        ListView listView = (ListView) findViewById(R.id.teamListView);
        listView.setAdapter(adapter);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {}
}
