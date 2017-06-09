package group5.battleship.src.logic;

import java.io.InterruptedIOException;
import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by seppi on 02.06.2017.
 */

public class Ship {
    private int type;   //1 small, 3 medium, 5 big
    private String direction;   //v = vertical , h = horizontal
    private Cordinate mainCordinate; //input from User
    private ArrayList<Cordinate> allCordinates = new ArrayList<Cordinate>(); // all Cordinates used for the ship
                                     //maybe better as object array containing cordinate objects
    public Ship (int type, String direction, Cordinate mainCordinate){
        this.type = type;
        this.mainCordinate = mainCordinate;
        this.direction=direction;
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
