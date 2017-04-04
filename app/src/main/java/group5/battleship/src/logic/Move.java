package group5.battleship.src.logic;

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

    public Move(Player attacker, Player target, Cordinate c){
        this.attacker = attacker;
        this.target = target;
        this.x = c.x;
        this.y = c.y;
    }

}
