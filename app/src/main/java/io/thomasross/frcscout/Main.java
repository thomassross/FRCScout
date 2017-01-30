package io.thomasross.frcscout;

import android.app.Activity;
import android.app.LoaderManager;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
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
import java.util.Set;

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
    public boolean onCreateOptionsMenu(Menu menu)
    {
        final Main us = this;
        getMenuInflater().inflate(R.menu.menu_main, menu);

        final MenuItem doneBtn = menu.findItem(R.id.action_selectgame_M);
        doneBtn.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener()
        {
            @Override
            public boolean onMenuItemClick(MenuItem item)
            {
                PopupMenu popupMenu = new PopupMenu(us, us.findViewById(R.id.action_selectgame_M));
                popupMenu.getMenuInflater().inflate(R.menu.menu_selectgame, popupMenu.getMenu());

                // TODO: There has to be a better way to do this
                MenuItem stronghold2016 = popupMenu.getMenu().findItem(R.id.menu_stronghold_2016);
                stronghold2016.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener()
                {
                    @Override
                    public boolean onMenuItemClick(MenuItem item)
                    {
                        GamesManager.setSelectedGame(Game.STRONGHOLD_2016);
                        us.refreshTeams();
                        return false;
                    }
                });

                MenuItem steamworks2017 = popupMenu.getMenu().findItem(R.id.menu_steamworks_2017);
                steamworks2017.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener()
                {
                    @Override
                    public boolean onMenuItemClick(MenuItem item)
                    {
                        GamesManager.setSelectedGame(Game.STEAMWORKS_2017);
                        us.refreshTeams();
                        return false;
                    }
                });

                popupMenu.show();
                return false;
            }
        });

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    protected void onStart()
    {
        super.onStart();

        refreshTeams();
    }

    private void refreshTeams()
    {
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

            Set<String> tasks = GamesManager.getTasks().keySet();

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
