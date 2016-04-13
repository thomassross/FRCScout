package io.thomasross.frcscout;

public class Team
{
    public int number;
    public String name;
    public String tasks;
    public int autoPoints;

    public Team(int number, String name, String tasks, int autoPoints)
    {
        this.number = number;
        this.name = name;
        this.tasks = tasks;
        this.autoPoints = autoPoints;
    }
}