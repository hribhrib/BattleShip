package group5.battleship.src.logic;

import java.io.InterruptedIOException;
import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by seppi on 02.06.2017.
 */

public class Ship {

    private ArrayList<Cordinate> allCordinates = new ArrayList<Cordinate>(); // all Cordinates used for the ship
    public Ship(){};
    public Ship (int type, String direction, Cordinate mainCordinate){
        //this.allCordinates=Integer.toString(mainCordinate.x)+Integer.toString(mainCordinate.y);
        allCordinates.add(mainCordinate);

        switch (type){
            case 1:
                break;

            case 5:
                if (direction.equals("v")){
                    allCordinates.add(new Cordinate(mainCordinate.x-1,mainCordinate.y));
                    allCordinates.add(new Cordinate(mainCordinate.x+1,mainCordinate.y));
                    allCordinates.add(new Cordinate(mainCordinate.x-2,mainCordinate.y));
                    allCordinates.add(new Cordinate(mainCordinate.x+2,mainCordinate.y));
                } else {
                    allCordinates.add(new Cordinate(mainCordinate.x,mainCordinate.y-1));
                    allCordinates.add(new Cordinate(mainCordinate.x,mainCordinate.y+1));
                    allCordinates.add(new Cordinate(mainCordinate.x,mainCordinate.y-2));
                    allCordinates.add(new Cordinate(mainCordinate.x,mainCordinate.y+2));
                }
            case 3:
                if (direction.equals("v")){
                    allCordinates.add(new Cordinate(mainCordinate.x-1,mainCordinate.y));
                    allCordinates.add(new Cordinate(mainCordinate.x+1,mainCordinate.y));
                } else {
                    allCordinates.add(new Cordinate(mainCordinate.x,mainCordinate.y-1));
                    allCordinates.add(new Cordinate(mainCordinate.x,mainCordinate.y+1));
                }
        }
    }

    public ArrayList<Cordinate> getAllCordinates() {
        return allCordinates;
    }
    // TODO: 03.06.2017 add a function that checks if a shipobject has been destroyed
}
