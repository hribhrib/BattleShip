package group5.battleship;

import android.annotation.SuppressLint;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.os.Handler;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TableLayout;
import android.widget.TextView;


public class GameActivity extends AppCompatActivity {
    TableLayout tl;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        System.out.println("test");

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tl = (TableLayout) findViewById(R.id.table);
    }

    public void colClick(View view) {
        System.out.println("colClick: "+view.getId());
        TextView tv = (TextView) findViewById(view.getId());
        tv.setText("clicked");
    }

}
