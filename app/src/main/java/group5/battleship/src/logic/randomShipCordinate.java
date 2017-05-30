package group5.battleship.src.logic;

/**
 * Created by seluh on 30.04.2017.
 */
// used for randomAttack method
public class randomShipCordinate {
    public Cordinate c;

    public randomShipCordinate(Player opponent, Game game) {
        int[][] tmpOpponentShips = opponent.getShips();
        for (int i = 0; i < game.getSize(); i++) {
            for (int j = 0; j < game.getSize(); j++) {      // keine ahnung wie ich überprüfe ob das Feld schon beschossen
                if (tmpOpponentShips[i][j] == 1) {          // wurde, gleiches gilt fürs random watercordinate
                    c = new Cordinate(i, j);
                    break;
                }
            }
        }
    }
}
