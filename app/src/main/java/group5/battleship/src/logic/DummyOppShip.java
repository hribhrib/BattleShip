package group5.battleship.src.logic;

import java.util.ArrayList;
import java.util.Random;

/**
 * Created by seppi on 11.06.2017.
 */

public class DummyOppShip extends Ship {
    private ArrayList<Cordinate> allCordinates = new ArrayList<Cordinate>();

    public DummyOppShip (int type){
        //this.allCordinates=Integer.toString(mainCordinate.x)+Integer.toString(mainCordinate.y);
        Random r = new Random();
        Random r_direction = new Random();
        String direction;
        if ((r_direction.nextBoolean())){
            direction = "v";
        }else {
            direction = "h";
        }
        Cordinate mainCordinate = new Cordinate (r.nextInt(8), r.nextInt(8));
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
}