package group5.battleship.src.logic;

/**
 * Created by hribhrib on 28.03.2017.
 * <p>
 * This class represent a move.
 */

public class Move {
    Player attacker;
    Player target;
    Cordinate c;

    public Move(Player attacker, Player target, Cordinate c) {
        this.attacker = attacker;
        this.target = target;
        this.c = new Cordinate(c.x, c.y);
    }
}
