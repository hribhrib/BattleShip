package group5.battleship;

import org.junit.Test;

import java.util.ArrayList;

import group5.battleship.src.logic.Cordinate;
import group5.battleship.src.logic.Game;
import group5.battleship.src.logic.Move;
import group5.battleship.src.logic.Player;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class UnitTestLogic {

    Cordinate c = new Cordinate(2, 3);

    Player pl = new Player("paul");

    @Test
    public void cordinateIsCorrect() throws Exception {
        assertEquals(c.equals(new Cordinate(2, 3)), true);
    }

    @Test
    public void cordinateIsNotCorrect() throws Exception {
        assertEquals(c.equals(new Cordinate(3, 2)), false);
    }

    @Test
    public void cordinateIsNull() throws Exception {
        Cordinate tmp = null;
        assertEquals(c.equals(tmp), false);
    }

    @Test
    public void PlayerName() throws Exception {
        assertEquals(pl.getName(), "paul");
        assertNotEquals(pl.getName(), "franz");
    }

    @Test
    public void PlayerInitFields() throws Exception {
        Player tmp = new Player("tmp");

        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 5; j++) {
                assertEquals(pl.getShipByCordinate(new Cordinate(i, j)), pl.getShipByCordinate(new Cordinate(i, j)));
            }
        }
    }

    @Test
    public void PlayerSetShips() throws Exception {
        pl.setShips("122311");
        Player tmp = new Player("tmp");
        tmp.setShips(new Cordinate(1, 2), new Cordinate(2, 3), new Cordinate(1, 1));

        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 5; j++) {
                assertEquals(pl.getShipByCordinate(new Cordinate(i, j)), pl.getShipByCordinate(new Cordinate(i, j)));
            }
        }
    }

    @Test
    public void PlayerGetShipByCordinate() throws Exception {
        pl.setShips("122311");
        assertEquals(pl.getShipByCordinate(new Cordinate(1, 2)), 1);
        assertEquals(pl.getShipByCordinate(new Cordinate(2, 3)), 1);
        assertEquals(pl.getShipByCordinate(new Cordinate(1, 1)), 1);
    }

    @Test
    public void PlayerUpdateBattleField() throws Exception {
        pl.updateBattleField(new Cordinate(2, 2), -1);

        assertEquals(pl.getBattleFieldByCordinate(new Cordinate(2, 2)), -1);
    }

    @Test
    public void PlayerIncShipDestroyed() throws Exception {
        assertEquals(pl.incShipDestroyed(), 1);
        assertEquals(pl.incShipDestroyed(), 2);
        assertEquals(pl.incShipDestroyed(), 3);

    }

    @Test
    public void PlayerDecRandomAttacks() throws Exception {
        assertEquals(pl.getRandomAttacks(), 1);
        pl.decRandomAttacks();
        assertEquals(pl.getRandomAttacks(), 0);
    }

    @Test
    public void PlayerGetMaxShips() throws Exception {
        assertEquals(pl.getMaxShips(),3);
    }

    @Test
    public void PlayerGetBattleField() throws Exception {
        Player p1 = new Player("p1");

        int [][] bla = p1.getBattleField();

        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 5; j++) {
                assertEquals(pl.getBattleFieldByCordinate(new Cordinate(i, j)), bla[i][j]);
            }
        }
    }

    @Test
    public void Move() throws Exception {
        Move m = new Move(pl, new Player("hallo"), new Cordinate(2, 2));
        assertNotNull(m);
    }

    @Test
    public void Game() throws Exception {
        Player p1 = new Player("p1");
        Player p2 = new Player("p2");

        Move m1 = new Move(p1, p2, new Cordinate(2, 2));
        Move m2 = new Move(p2, p1, new Cordinate(2, 2));
        Move m3 = new Move(p1, p2, new Cordinate(3, 3));

        Game g = new Game(p1, p2);

        assertEquals(g.getPlayer1(), p1);
        assertEquals(g.getPlayer2(), p2);
        assertEquals(g.getSize(), 5);

        g.newMove(m1);
        g.newMove(m2);
        g.newMove(m3);

        ArrayList moves = g.getMoves();

        assertEquals(moves.get(0), m1);
        assertEquals(moves.get(1), m2);
        assertEquals(moves.get(2), m3);

    }

}