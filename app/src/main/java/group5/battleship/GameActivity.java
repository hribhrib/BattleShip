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

import org.w3c.dom.Text;

import group5.battleship.*;
import group5.battleship.src.Game;
import group5.battleship.src.Player;

public class GameActivity extends AppCompatActivity {
    public Game game;
    private Player myPlayer;
    int[][] table;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initGame();
        displayMyShips();
    }

    public void colClick(View view) {
        System.out.println("colClick: " + view.getId());

        TextView tv = (TextView) findViewById(view.getId());
        tv.setText("clicked");
    }

    private void initGame() {
        myPlayer = new Player(getIntent().getStringExtra("NAME").toString());
        Player opponent = new Player("Opponent");
        game = new Game(myPlayer, opponent, 5); //5 = static size

        myPlayer.setShips(getIntent().getStringExtra("SHIPS").toString());

        routingToTableLayout();
    }

    private void displayMyShips() {
        int[][] ships = myPlayer.getShips();

        TextView tv;
        for (int i = 0; i < game.getSize(); i++) {
            for (int j = 0; j < game.getSize(); j++) {
                tv = (TextView) findViewById(getRoutingByCordinate(i,j));
                if (ships[i][j] == 1) {
                    tv.setText("o");
                } else {
                    tv.setText("~");
                }
            }
        }
    }

    private void routingToTableLayout(){
        table = new int[game.getSize()][game.getSize()];
        TextView tv;

        table[0][0] = findViewById(R.id.textView00).getId();
        table[0][1] = findViewById(R.id.textView01).getId();
        table[0][2] = findViewById(R.id.textView02).getId();
        table[0][3] = findViewById(R.id.textView03).getId();
        table[0][4] = findViewById(R.id.textView04).getId();
        table[1][0] = findViewById(R.id.textView10).getId();
        table[1][1] = findViewById(R.id.textView11).getId();
        table[1][2] = findViewById(R.id.textView12).getId();
        table[1][3] = findViewById(R.id.textView13).getId();
        table[1][4] = findViewById(R.id.textView14).getId();
        table[2][0] = findViewById(R.id.textView20).getId();
        table[2][1] = findViewById(R.id.textView21).getId();
        table[2][2] = findViewById(R.id.textView22).getId();
        table[2][3] = findViewById(R.id.textView23).getId();
        table[2][4] = findViewById(R.id.textView24).getId();
        table[3][0] = findViewById(R.id.textView30).getId();
        table[3][1] = findViewById(R.id.textView31).getId();
        table[3][2] = findViewById(R.id.textView32).getId();
        table[3][3] = findViewById(R.id.textView33).getId();
        table[3][4] = findViewById(R.id.textView34).getId();
        table[4][0] = findViewById(R.id.textView40).getId();
        table[4][1] = findViewById(R.id.textView41).getId();
        table[4][2] = findViewById(R.id.textView42).getId();
        table[4][3] = findViewById(R.id.textView43).getId();
        table[4][4] = findViewById(R.id.textView44).getId();
    }
    private int getRoutingByCordinate(int x, int y){
        return table[x][y];
    }
}
