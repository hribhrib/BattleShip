package group5.battleship.src.logic;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by hribhrib on 28.03.2017.
 * <p>
 * This class is for one game. one game contains each move.
 * array sates:
 * -1 = water
 * 0 = undef
 * +1 = ship
 */

public class Game {
    ArrayList<Move> moves;
    int size = 5; // R x C
    Player player1;
    Player player2;

    public Game(Player p1, Player p2) {
        player1 = p1;
        player2 = p2;

        moves = new ArrayList<>();
    }

    public void newMove(Move move) {
        moves.add(move);
    }

    public Player getPlayer1() {
        return player1;
    }

    public Player getPlayer2() {
        return player2;
    }

    public int getSize() {
        return size;
    }

    public ArrayList<Move> getMoves() {
        return moves;
    }
}
