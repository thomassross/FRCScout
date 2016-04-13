package io.thomasross.frcscout.models;

public class Team
{
    public int number;
    public String name;
    public String tasks;
    public int autoPoints;

    public int canDoTasks;
    public int totalTasks;

    public Team(int number, String name, String tasks, int autoPoints)
    {
        this.number = number;
        this.name = name;
        this.tasks = tasks;
        this.autoPoints = autoPoints;
    }

    public Team(int number, String name, String tasks, int autoPoints, int canDoTasks, int totalTasks)
    {
        this.number = number;
        this.name = name;
        this.tasks = tasks;
        this.autoPoints = autoPoints;
        this.canDoTasks = canDoTasks;
        this.totalTasks = totalTasks;
    }
}