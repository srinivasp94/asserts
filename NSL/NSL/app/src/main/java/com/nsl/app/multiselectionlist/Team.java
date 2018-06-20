package com.nsl.app.multiselectionlist;

/**
 * Created by sys on 6/15/2017.
 */

public class Team {
    private String teamName;
    private int teamWins;
    public  Team(String name, int wins)
    {
        teamName = name;
        teamWins = wins;
    }

    public String getTeamName() {
        return teamName;
    }

    public int getTeamWins() {
        return teamWins;
    }
}
