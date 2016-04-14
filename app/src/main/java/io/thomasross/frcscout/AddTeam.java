package io.thomasross.frcscout;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;
import io.thomasross.frcscout.models.Team;
import io.thomasross.frcscout.tasks.InsertTeamTask;

public class AddTeam extends AppCompatActivity implements FinishedCallback
{
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addteam);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        final AddTeam us = this;

        getMenuInflater().inflate(R.menu.menu_addteam, menu);

        MenuItem doneBtn = menu.findItem(R.id.action_checkmark_AT);
        doneBtn.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener()
        {
            @Override
            public boolean onMenuItemClick(MenuItem item)
            {
                us.onDoneClicked();
                return false;
            }
        });

        return super.onCreateOptionsMenu(menu);
    }

    public void onDoneClicked()
    {
        InsertTeamTask task = new InsertTeamTask(this);
        task.addFinishCallback(this);

        String teamNumText = ((EditText) findViewById(R.id.teamnumber_edit_AT)).getText().toString();
        String teamNameText = ((EditText) findViewById(R.id.teamname_edit_AT)).getText().toString();

        if (teamNumText.isEmpty())
        {
            Toast.makeText(this, "Invalid team number", Toast.LENGTH_LONG).show();
            return;
        }

        int teamNumber;
        try
        {
            teamNumber = Integer.parseInt(teamNumText);
        }
        catch (NumberFormatException | NullPointerException ignored)
        {
            Toast.makeText(this, "Invalid team number", Toast.LENGTH_LONG).show();
            return;
        }

        Team team = new Team(
                teamNumber,
                teamNameText,
                "{}",
                0
        );
        task.execute(team);
    }

    public void done()
    {
        finish();
    }
}