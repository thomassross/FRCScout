package io.thomasross.frcscout.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import io.thomasross.frcscout.R;
import io.thomasross.frcscout.models.Task;

import java.util.ArrayList;
import java.util.List;

public class TaskListAdapter extends ArrayAdapter<Task>
{
    private ArrayList<Task> taskList;
    private Context context;

    public TaskListAdapter(Context context, int resource, List<Task> objects)
    {
        super(context, resource, objects);

        this.taskList = new ArrayList<>();
        this.taskList.addAll(objects);

        this.context = context;
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
            LayoutInflater vi = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
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
