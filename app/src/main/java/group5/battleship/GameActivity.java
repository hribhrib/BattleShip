package group5.battleship;

import android.annotation.SuppressLint;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.os.Handler;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.TableLayout;
import android.widget.TextView;
import group5.battleship.*;
import group5.battleship.src.Game;
import group5.battleship.src.Player;

public class GameActivity extends AppCompatActivity {
    public Game game;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //Initialize the Game
        //EditText et = (EditText) findViewById(R.id.editText);
        //Player myPlayer = new Player(et.getText().toString());
        Player myPlayer = new Player("asdf");
        Player opponent = myPlayer;
        game = new Game(myPlayer,opponent,5);

        myPlayer.setShips(getIntent().getStringExtra("SHIPS").toString());


        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    int i = 12;
        TextView tv = (TextView) findViewById(R.id.textView+i);
        tv.setText("test");
    }

    public void colClick(View view) {
        System.out.println("colClick: "+view.getId());

        TextView tv = (TextView) findViewById(view.getId());
        tv.setText("clicked");
    }
}
