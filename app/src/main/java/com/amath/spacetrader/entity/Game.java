package com.amath.spacetrader.entity;

import java.io.Serializable;
import java.util.List;
import java.util.Set;

public class Game implements Serializable {
    private GameDifficulty gameDifficulty;
    public int uuid;
//    public int uuid2;
    private Player player;
    private Universe universe;

    public Game() {
        this(null, GameDifficulty.EASY, new Universe());
    }

    public Game(Player player, GameDifficulty gameDifficulty, Universe universe) {
        this.player = player;
        this.gameDifficulty = gameDifficulty;
        this.universe = universe;
    }

    /**
     * Loads a player into the game.
     *
     * @param player
     */
    public void loadPlayer(Player player) {
        this.player = player;
    }

    /**
     * Loads universe into game
     *
     * @param universe
     */
    public void loadUniverse(Universe universe) {
        this.universe = universe;
    }

    /**
     * Getter method for player attribute
     *
     * @return the player of the game
     */
    public Player getPlayer() {
        return player;
    }



    /**
     * Changes difficulty of the game
     *
     * @param difficulty the new difficulty to set as
     */
    public void changeDifficulty(GameDifficulty difficulty) {
        this.gameDifficulty = difficulty;
    }

    public GameDifficulty getGameDifficulty() {
        return gameDifficulty;
    }

    public Universe getUniverse() {
        return universe;
    }

    public List<SolarSystem> getSolarSystems() {
        return universe.getSolarSystems();
    }

    public Planet getCurrentPlanet() {
        return universe.getCurrentPlanet();
    }

    public void setCurrentPlanet(Planet currentPlanet) {
        universe.setCurrentPlanet(currentPlanet);
    }
}
