package group5.battleship.src;

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
        ships[s.charAt(0)][s.charAt(1)]=1;
        ships[s.charAt(2)][s.charAt(3)]=1;
        ships[s.charAt(4)][s.charAt(5)]=1;
    }
}
