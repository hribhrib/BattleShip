package group5.battleship.src.logic;

import java.util.Random;

/**
 * Created by seluh on 30.04.2017.
 */
// used for randomAttack method
public class randomWaterCordinate extends Cordinate {

    public randomWaterCordinate (Player opponent){                     //gets random waterfield cordinates
        int[][] tmpOpponentShips = opponent.getShips();
        Random r = new Random();
        boolean help = true;
        while (help == true) {
            x = r.nextInt(5);
            y = r.nextInt(5);
            if (tmpOpponentShips[x][y] == -1) {
                break;
            }
        }
    }
}
