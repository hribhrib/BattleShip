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
    private int randomAttacks = 1;                  // so that random attack only works once a game, for now

    public Player(String name) {
        this.name = name;
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

    public int[][] getShips() {
        return this.ships;
    }

    public int getShipByCordinate(Cordinate c) {
        return ships[c.x][c.y];
    }

    public void updateBattleField(int x, int y, int state) {
        //-1 = water
        //+1 = ship
        battleField[x][y] = state;
    }
    public void updateBattleField(Cordinate c, int state) {
        int x = c.x;
        int y = c.y;
        battleField[x][y]= state;
    }

    public int[][] getBattleField() {
        return battleField;
    }

    public void setBattleField (int[][] battleField1) {
        battleField = battleField1;
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

    public void setRandomAttacks() {
        this.randomAttacks = randomAttacks -1 ;
    }
    public int getRandomAttacks() {
        return randomAttacks;
    }
}