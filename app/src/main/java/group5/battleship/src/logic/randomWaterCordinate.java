package group5.battleship.src.logic;

import java.util.Random;

/**
 * Created by seluh on 30.04.2017.
 */
// used for randomAttack method
public class randomWaterCordinate {
    public Cordinate c = new Cordinate(0,0);

    public randomWaterCordinate (Player opponent){                     //gets random waterfield cordinates
        int[][] tmpOpponentShips = opponent.getShips();
        Random r = new Random();
        boolean help = true;
        while (help == true) {
            c.x = r.nextInt(5);
            c.y = r.nextInt(5);
            if (tmpOpponentShips[c.x][c.y] == -1) {
                break;
            }
        }
    }
}
