package group5.battleship.src;

/**
 * Created by hribhrib on 28.03.2017.
 *
 * This class represent a move.
 */

public class Move {
    Player attacker;
    Player target;
    int x;
    int y;

    public Move(Player attacker, Player target,int x, int y){
        this.attacker = attacker;
        this.target = target;
        this.x = x;
        this.y = y;
    }

}
