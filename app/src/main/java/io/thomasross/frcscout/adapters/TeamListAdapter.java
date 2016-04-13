package io.thomasross.frcscout.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import io.thomasross.frcscout.EditTeam;
import io.thomasross.frcscout.R;
import io.thomasross.frcscout.Team;

import java.util.ArrayList;
import java.util.List;

public class TeamListAdapter extends ArrayAdapter<Team>
{
    private ArrayList<Team> teams;
    private Context context;

    private class ViewHolder
    {
        TextView namenum;
        TextView stats;
        ImageButton moreBtn;
    }

    public TeamListAdapter(Context context, int resource, List<Team> objects)
    {
        super(context, resource, objects);

        this.teams = new ArrayList<>();
        this.teams.addAll(objects);

        this.context = context;
    }

    @SuppressLint("DefaultLocale")
    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        final ViewHolder holder;
        final Team team = teams.get(position);

        if (convertView == null)
        {
            LayoutInflater vi = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = vi.inflate(R.layout.teamlistitem, null);

            holder = new ViewHolder();
            holder.namenum = (TextView) convertView.findViewById(R.id.teamlist_namenum);
            holder.stats = (TextView) convertView.findViewById(R.id.teamlist_teamstats);
            holder.moreBtn = (ImageButton) convertView.findViewById(R.id.teamlist_morebtn);
            convertView.setTag(holder);

            convertView.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    Intent intent = new Intent(context, EditTeam.class);
                    intent.putExtra("io.thomasross.frcscout.teamnumber", team.number);
                    context.startActivity(intent);
                }
            });

            holder.moreBtn.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    PopupMenu popupMenu = new PopupMenu(context, holder.moreBtn);
                    popupMenu.getMenuInflater().inflate(R.menu.menu_teampopup, popupMenu.getMenu());

                    MenuItem tbaLink = popupMenu.getMenu().findItem(R.id.menu_tbalink);
                    tbaLink.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener()
                    {
                        @Override
                        public boolean onMenuItemClick(MenuItem item)
                        {
                            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.thebluealliance.com/team/" + team.number));
                            context.startActivity(intent);
                            return false;
                        }
                    });

                    popupMenu.show();
                }
            });
        }
        else
        {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.namenum.setText(String.format("%d - %s", team.number, team.name));
        holder.stats.setText(String.format("%d/%d tasks, %d auto points", team.canDoTasks, team.totalTasks, team.autoPoints));

        return convertView;
    }
}
