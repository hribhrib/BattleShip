package group5.battleship.src.logic;

import android.os.SystemClock;
import android.widget.TabHost;

/**
 * Created by Susanne on 11.05.2017.
 */

public class WaitingThread extends Thread {
    int waitingTime;

    public WaitingThread(int time){
        this.waitingTime = time;
    }

    public void setWaitingTime(int time){
        this.waitingTime = time;
    }

    @Override
    public void run() {
        super.run();
        //try {
            SystemClock.sleep(waitingTime);
            //Thread.sleep(waitingTime);
            //wait(waitingTime);
        //} catch (InterruptedException e) {
        //    e.printStackTrace();
        //}
    }

    public void run(TabHost th, int tab) {
        super.run();
        //try {
            SystemClock.sleep(waitingTime);
            //Thread.sleep(waitingTime);
            //wait(waitingTime);
        //} catch (InterruptedException e) {
        //    e.printStackTrace();
        //}

        th.setCurrentTab(tab);
    }
}
