package group5.battleship.src.logic;

import android.support.annotation.NonNull;

/**
 * Created by hribhrib on 04.04.2017.
 */

public class Cordinate {
    public int x;
    public int y;

    public Cordinate(int x, int y){
        this.x = x;
        this.y = y;
    }

    public boolean equals(Cordinate c) {
        if(this.x == c.x && this.y ==c.y){
            return true;
        } else {
            return false;
        }
    }
}
