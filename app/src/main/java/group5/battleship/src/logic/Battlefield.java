package group5.battleship.src.logic;

import android.view.inputmethod.CorrectionInfo;

import java.io.Serializable;
import java.lang.reflect.Array;
import java.util.ArrayList;

/**
 * Created by seppi on 02.06.2017.
 */

public class Battlefield implements Serializable {
    private int[][] battlefield;



    public Battlefield(int size) {
        battlefield = new int[size][size];
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                this.battlefield[i][j] = 0;
            }
        }

    }
    public boolean checkSpace (Ship ship) {
        ArrayList<Cordinate> cordinates = ship.getAllCordinates();
        boolean placeable = true;
        try{
        for (Cordinate c : cordinates) {
            if (battlefield[c.x][c.y] == 1){
                placeable = false;
            }
        }
        }
        catch (Exception e){
            placeable = false;
        }
        return placeable;
    }

    public void addShip(Ship ship) {
        ArrayList<Cordinate> cordinates = ship.getAllCordinates();
        for (Cordinate c : cordinates) {
            battlefield[c.x][c.y] = 1;
        }
    }
    public void resetBattlefield (){
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                this.battlefield[i][j] = 0;
            }
        }
    }
    public int[][] getBattlefield() {
        return battlefield;
    }

    public int getValueOfCell(Cordinate c){
        return battlefield[c.x][c.y];
    }
    }

