package io.thomasross.frcscout;

import java.util.HashMap;

public class GamesManager
{
    public static final String GAME_2016_TABLE_NAME = "Teams2016";
    public static final String GAME_2017_TABLE_NAME = "Teams2017";

    private static Game selectedGame = Game.STEAMWORKS_2017;

    public static void setSelectedGame(Game game)
    {
        GamesManager.selectedGame = game;
    }

    public static String getCurrentTableName()
    {
        switch (GamesManager.selectedGame)
        {
            case STRONGHOLD_2016:
                return GAME_2016_TABLE_NAME;
            case STEAMWORKS_2017:
            default:
                return GAME_2017_TABLE_NAME;
        }
    }

    public static HashMap<String, String> getTasks()
    {
        HashMap<String, String> tasks = new HashMap<>();
        switch (GamesManager.selectedGame)
        {
            case STRONGHOLD_2016:
                tasks.put("portcullis", "Portcullis");
                tasks.put("cheval", "Cheval de Frise");
                tasks.put("moat", "Moat");
                tasks.put("ramparts", "Ramparts");
                tasks.put("drawbridge", "Drawbridge");
                tasks.put("sallyport", "Sally Port");
                tasks.put("rockwall", "Rock Wall");
                tasks.put("roughterrain", "Rough Terrain");
                tasks.put("lowbar", "Low Bar");
                tasks.put("lowgoal", "Low Goal");
                tasks.put("highgoal", "High Goal");
                tasks.put("scaletower", "Scale Tower");
                break;
            case STEAMWORKS_2017:
            default:
                tasks.put("highgoal", "High Goal");
                tasks.put("lowgoal", "Low Goal");
                tasks.put("gears", "Gears");
                tasks.put("pickup-hopper", "Hopper Fuel Pick up");
                tasks.put("pickup-ground", "Ground Fuel Pick up");
                tasks.put("hang", "Hang");
                // Doesn't get moved easily?
        }
        return tasks;
    }
}
