package group5.battleship.src;

import java.util.List;

/**
 * Created by hribhrib on 28.03.2017.
 *
 * This class is for one game. one game contains each move.
 */

public class Game {
    List<Move> moves;
    int size = 5; // R x C
    Player player1;
    Player player2;


    public Game (Player p1, Player p2, int size){
        player1 = p1;
        player2 = p2;
        //this.size = size;

        player1.battleField = new int[this.size][this.size];
        initBattleField(player1);
        player2.battleField = new int[this.size][this.size];
        initBattleField(player2);
    }

    private void newMove(Move move){
        moves.add(move);
    }

    private void initBattleField(Player p){
        for(int i =0 ; i<size; i++){
            for(int j = 0; j < size; j++){
                p.battleField[i][j] = -1;
            }
        }

    }
}
