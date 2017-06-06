package group5.battleship.src.logic;

import java.io.Serializable;

/**
 * Created by seppi on 02.06.2017.
 */

public class Battlefield implements Serializable {
    private int[][] battlefield;



    public Battlefield(int size) {
        battlefield = new int[size][size];

    }
    public boolean checkSpace (Ship ship) {
        String cordinates = ship.getAllCordinates();
        boolean placeable = true;
        for (int i = 0; i <= cordinates.length() - 2; i=i+2) {
            if (battlefield[cordinates.charAt(i)][cordinates.charAt(i + 1)] == 1) {
                placeable = false;
            }
        }
        return placeable;
    }

    public void addShip(Ship ship) {
        String cordinates = ship.getAllCordinates();
        for (int i = 0; i <= cordinates.length() - 2; i = i+2) {
                battlefield[cordinates.charAt(i)][cordinates.charAt(i + 1)] = 1;
            }
        }
    public int[][] getBattlefield() {
        return battlefield;
    }
    }

