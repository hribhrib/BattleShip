package group5.battleship.src.logic;

import java.util.ArrayList;

/**
 * Created by hribhrib on 28.03.2017.
 * <p>
 * This class represent the players
 */

public class Player {
    String name;
    //-1 = water
    // 0 = undef
    //+1 = ship
    public int[][] battleField;
    int[][] ships;
    int MAX_SHIPS = 6;
    int shipsDestroyed = 0;
    private int randomAttacks = 1;                  // so that random attack only works once a game, for now


    public Player(String name, int gamesize) {
        this.name = name;
        initFieldArrays(gamesize);
    }

    public void setShips(String s) {
        for (int i = 0; i < s.length(); i = i + 2) {
            ships[Character.getNumericValue(s.charAt(i))][Character.getNumericValue(s.charAt(i + 1))] = 1;
        }
    }
    public void setShips(Cordinate ship1, Cordinate ship2, Cordinate ship3) {
        ships[ship1.x][ship1.y] = 1;
        ships[ship2.x][ship2.y] = 1;
        ships[ship3.x][ship3.y] = 1;
    }

    public void setShips(DummyOppShip ship) {
        ArrayList<Cordinate> cordinates = ship.getAllCordinates();
        for (Cordinate c : cordinates) {
            ships[c.x][c.y] = 1;
        }
    }
    public void setShips(Battlefield battlefield){
        ships = battlefield.getBattlefield();
    }

    public int[][] getShips() {
        return this.ships;
    }

    public int getShipByCordinate(Cordinate c) {
        return ships[c.x][c.y];
    }

    public void updateBattleField(Cordinate c, int state) {
        //-1 = water
        //+1 = ship
        battleField[c.x][c.y] = state;

    }

    public int[][] getBattleField() {
        return battleField;
    }

    public int getBattleFieldByCordinate(Cordinate c) {
        return battleField[c.x][c.y];
    }

    public int getMaxShips() {
        return MAX_SHIPS;
    }

    public int incShipDestroyed() {
        shipsDestroyed++;
        return shipsDestroyed;
    }

    public String getName() {
        return name;
    }

    public void decRandomAttacks() {
        this.randomAttacks = randomAttacks - 1;
    }

    public int getRandomAttacks() {
        return randomAttacks;
    }

    private void initFieldArrays(int gamesize) {
        this.battleField = new int[gamesize][gamesize];
        this.ships = new int[gamesize][gamesize];


        for (int i = 0; i < gamesize; i++) {
            for (int j = 0; j < gamesize; j++) {
                this.battleField[i][j] = 0;
                this.ships[i][j] = -1;
            }
        }

    }
}