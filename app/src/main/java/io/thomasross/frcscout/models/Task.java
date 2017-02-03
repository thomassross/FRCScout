package io.thomasross.frcscout.models;

public class Task
{
    private String code;
    private String name;
    private boolean isTeamAble;

    public Task(String code, String name, boolean isTeamAble)
    {
        this.code = code;
        this.name = name;
        this.isTeamAble = isTeamAble;
    }

    public String getCode()
    {
        return code;
    }

    public void setCode(String code)
    {
        this.code = code;
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public boolean isTeamAble()
    {
        return isTeamAble;
    }

    public void setTeamAble(boolean teamAble)
    {
        isTeamAble = teamAble;
    }
}
