package group5.battleship;

import org.junit.Test;

import group5.battleship.src.logic.Cordinate;
import group5.battleship.src.logic.Player;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class UnitTestLogic {

    Cordinate c = new Cordinate (2,3);

    Player pl = new Player("paul");

    @Test
    public void cordinateIsCorrect() throws Exception {
        assertEquals(c.equals(new Cordinate(2,3)),true);
    }

    @Test
    public void cordinateIsNotCorrect() throws Exception {
        assertEquals(c.equals(new Cordinate(3,2)),false);
    }

    @Test
    public void PlayerName() throws Exception {
        assertEquals(pl.getName(),"paul");
        assertNotEquals(pl.getName(),"franz");
    }

    @Test
    public void PlayerSetShips() throws  Exception{
        pl.setShips("122311");
        Player tmp = new Player("tmp");
        tmp.setShips(new Cordinate(1,2),new Cordinate(2,3), new Cordinate(1,1));

        for (int i = 0;i<5;i++){
            for(int j = 0 ; j<5;j++){
                assertEquals(pl.getShipByCordinate(new Cordinate(i,j)),pl.getShipByCordinate(new Cordinate(i,j)));
            }
        }
    }


}