package group5.battleship.src.logic;

/**
 * Created by hribhrib on 28.03.2017.
 *
 * This class represent the players
 */

public class Player {
    String name;
    int[][] battleField;
    int[][] ships;
    int MAX_SHIPS = 3;
    int shipsPlaced = 0;

    public Player(String name){
        this.name = name;
    }

    public void setShips(String s){
        for (int i =0;i<s.length();i=i+2){
            ships[Character.getNumericValue(s.charAt(i))][Character.getNumericValue(s.charAt(i+1))]=1;
        }
    }

    public int[][] getShips(){
        return this.ships;
    }

    public void updateBattleField(int x, int y, int state){
        //-1 = water
        //+1 = ship
        battleField[x][y] = state;
    }

    public int[][] getBattleField(){
        return battleField;
    }
}
