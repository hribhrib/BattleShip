package group5.battleship.src.logic;

/**
 * Created by hribhrib on 04.04.2017.
 */

public class Cordinate {

    public int x;
    public int y;

    public Cordinate(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public boolean equals(Cordinate c) {
        if (c != null) {
            if (this.x == c.x && this.y == c.y) {
                return true;
            } else {
                return false;
            }
        } else {
            return false;
        }
    }
    public Cordinate (String tag){
        this.x=Character.getNumericValue(tag.charAt(0));
        this.y=Character.getNumericValue(tag.charAt(1));
    }
    public String cordinateToString (Cordinate cordinate){
        String string =""+ cordinate.x+cordinate.y;
        return string;
    }
}
