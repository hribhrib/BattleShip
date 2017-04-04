package group5.battleship.src.logic;

/**
 * Created by hribhrib on 28.03.2017.
 * <p>
 * This class represent the players
 */

public class Player {
    String name;
    int[][] battleField;
    int[][] ships;
    int MAX_SHIPS = 3;
    int shipsDestroyed = 0;

    public Player(String name) {
        this.name = name;
    }

    public void setShips(String s) {
        for (int i = 0; i < s.length(); i = i + 2) {
            ships[Character.getNumericValue(s.charAt(i))][Character.getNumericValue(s.charAt(i + 1))] = 1;
        }
    }

    public void setShips(Cordinate ship1, Cordinate ship2, Cordinate ship3){
        ships[ship1.x][ship1.y] = 1;
        ships[ship2.x][ship2.y] = 1;
        ships[ship3.x][ship3.y] = 1;
    }

    public int[][] getShips() {
        return this.ships;
    }

    public int getShipByCordinate(Cordinate c){
        return ships[c.x][c.y];
    }

    public void updateBattleField(int x, int y, int state) {
        //-1 = water
        //+1 = ship
        battleField[x][y] = state;
    }

    public int[][] getBattleField() {
        return battleField;
    }

    public int getBattleFieldByCordinate(Cordinate c){
        return battleField[c.x][c.y];
    }

    public int getMaxShips() {
        return MAX_SHIPS;
    }

    public int incShipDestroyed(){
        shipsDestroyed++;
        return shipsDestroyed;
    }

    public String getName(){
        return name;
    }
}
