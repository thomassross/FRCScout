package io.thomasross.frcscout.models;

public class Team
{
    private int number;
    private String name;
    private String tasks;
    private int autoPoints;

    private int canDoTasks;

    public Team(int number, String name, String tasks, int autoPoints)
    {
        this.number = number;
        this.name = name;
        this.tasks = tasks;
        this.autoPoints = autoPoints;
    }

    public Team(int number, String name, String tasks, int autoPoints, int canDoTasks)
    {
        this.number = number;
        this.name = name;
        this.tasks = tasks;
        this.autoPoints = autoPoints;
        this.canDoTasks = canDoTasks;
    }

    public int getNumber()
    {
        return number;
    }

    public void setNumber(int number)
    {
        this.number = number;
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public String getTasks()
    {
        return tasks;
    }

    public void setTasks(String tasks)
    {
        this.tasks = tasks;
    }

    public int getAutoPoints()
    {
        return autoPoints;
    }

    public void setAutoPoints(int autoPoints)
    {
        this.autoPoints = autoPoints;
    }

    public int getCanDoTasks()
    {
        return canDoTasks;
    }

    public void setCanDoTasks(int canDoTasks)
    {
        this.canDoTasks = canDoTasks;
    }
}