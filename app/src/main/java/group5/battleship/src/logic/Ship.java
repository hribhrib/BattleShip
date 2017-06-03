package group5.battleship.src.logic;

import java.io.Serializable;

/**
 * Created by seppi on 02.06.2017.
 */

public class Ship implements Serializable {
    private int type;   //1 small, 3 medium, 5 big
    private String direction;   //v = vertical , h = horizontal
    private Cordinate mainCordinate; //input from User



    private String allCordinates; // all Cordinates used for the ship
                                //maybe better as object array containing cordinate objects
    public Ship (int type, String direction, Cordinate mainCordinate){
        this.type = type;
        this.mainCordinate = mainCordinate;
        this.direction=direction;
        allCordinates ="";
        this.allCordinates=allCordinates+mainCordinate.x+mainCordinate.y;
        switch (type){
            case 1:
                break;
            case 3:
                if (direction=="v"){
                    allCordinates=allCordinates+(mainCordinate.x--)+mainCordinate.y+(mainCordinate.x++)+mainCordinate.y;
                } else {
                    allCordinates=allCordinates+mainCordinate.x+(mainCordinate.y++)+mainCordinate.x+(mainCordinate.y--);
                }
            case 5:
                if (direction=="v"){
                    allCordinates=allCordinates+(mainCordinate.x--)+mainCordinate.y+(mainCordinate.x++)+mainCordinate.y+(mainCordinate.x-2)+mainCordinate.y+(mainCordinate.x+2)+mainCordinate.y;
                } else {
                    allCordinates=allCordinates+mainCordinate.x+(mainCordinate.y++)+mainCordinate.x+(mainCordinate.y--)+mainCordinate.x+(mainCordinate.y-2)+mainCordinate.x+(mainCordinate.y+2);
                }
        }
    }
    public String getAllCordinates() {
        return allCordinates;
    }
    // TODO: 03.06.2017 add a function that checks if a shipobject has been destroyed
}
