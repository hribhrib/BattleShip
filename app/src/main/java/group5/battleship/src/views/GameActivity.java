package group5.battleship.src.views;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TabHost;
import android.widget.TextView;

import group5.battleship.R;
import group5.battleship.src.logic.Cordinate;
import group5.battleship.src.logic.Game;
import group5.battleship.src.logic.Player;

import android.os.Vibrator;

public class GameActivity extends AppCompatActivity {
    public Game game;
    private Player myPlayer;
    private Player opponent;
    int[][] routingMyField;
    int[][] routingOpponentField;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        TabHost host = (TabHost) findViewById(R.id.tabHost);
        host.setup();

        //Tab 1
        TabHost.TabSpec spec = host.newTabSpec("MyField");
        spec.setContent(R.id.MyField);
        spec.setIndicator("MyField");
        host.addTab(spec);

        //Tab 2
        spec = host.newTabSpec("OpponentField");
        spec.setContent(R.id.OpponentField);
        spec.setIndicator("OpponentField");
        host.addTab(spec);


        initGame();
        initDummyOpp();
        displayMyShips();
        displayBattleField();
    }

    public void cellClick(View view) {
        TextView tv = (TextView) findViewById(view.getId());

        Cordinate c = getRoutingByIDOpponentField(tv.getId());

        int[][] tmpOpponentShips = opponent.getShips();

        if (tmpOpponentShips[c.x][c.y] == -1) {
            myPlayer.updateBattleField(c.x, c.y, -1);
        } else if (tmpOpponentShips[c.x][c.y] == 1) {
            myPlayer.updateBattleField(c.x, c.y, 1);
        }

        displayBattleField();
    }

    private void initGame() {
        myPlayer = new Player(getIntent().getStringExtra("NAME").toString());
        opponent = new Player("Opponent");
        game = new Game(myPlayer, opponent, 5); //5 = static size

        myPlayer.setShips(getIntent().getStringExtra("SHIPS").toString());

        routingToTableLayout();
    }

    private void initDummyOpp() {
        opponent.setShips("132400");
    }

    private void displayMyShips() {
        int[][] ships = myPlayer.getShips();

        TextView tv;
        for (int i = 0; i < game.getSize(); i++) {
            for (int j = 0; j < game.getSize(); j++) {
                tv = (TextView) findViewById(getRoutingByCordinateMyField(i, j));
                if (ships[i][j] == 1) {
                    tv.setText("o");
                } else {
                    tv.setText("~");
                }
            }
        }
    }

    private void displayBattleField() {
        int[][] battleField = myPlayer.getBattleField();

        TextView tv;
        for (int i = 0; i < game.getSize(); i++) {
            for (int j = 0; j < game.getSize(); j++) {
                tv = (TextView) findViewById(getRoutingByCordinateOpponentField(i, j));
                if (battleField[i][j] == 1) {
                    tv.setText("o");
                } else if (battleField[i][j] == -1) {
                    tv.setText("~");
                } else {
                    tv.setText("");
                }
            }
        }
    }

    private void routingToTableLayout() {
        routingMyField = new int[game.getSize()][game.getSize()];

        routingMyField[0][0] = findViewById(R.id.textView00).getId();
        routingMyField[0][1] = findViewById(R.id.textView01).getId();
        routingMyField[0][2] = findViewById(R.id.textView02).getId();
        routingMyField[0][3] = findViewById(R.id.textView03).getId();
        routingMyField[0][4] = findViewById(R.id.textView04).getId();
        routingMyField[1][0] = findViewById(R.id.textView10).getId();
        routingMyField[1][1] = findViewById(R.id.textView11).getId();
        routingMyField[1][2] = findViewById(R.id.textView12).getId();
        routingMyField[1][3] = findViewById(R.id.textView13).getId();
        routingMyField[1][4] = findViewById(R.id.textView14).getId();
        routingMyField[2][0] = findViewById(R.id.textView20).getId();
        routingMyField[2][1] = findViewById(R.id.textView21).getId();
        routingMyField[2][2] = findViewById(R.id.textView22).getId();
        routingMyField[2][3] = findViewById(R.id.textView23).getId();
        routingMyField[2][4] = findViewById(R.id.textView24).getId();
        routingMyField[3][0] = findViewById(R.id.textView30).getId();
        routingMyField[3][1] = findViewById(R.id.textView31).getId();
        routingMyField[3][2] = findViewById(R.id.textView32).getId();
        routingMyField[3][3] = findViewById(R.id.textView33).getId();
        routingMyField[3][4] = findViewById(R.id.textView34).getId();
        routingMyField[4][0] = findViewById(R.id.textView40).getId();
        routingMyField[4][1] = findViewById(R.id.textView41).getId();
        routingMyField[4][2] = findViewById(R.id.textView42).getId();
        routingMyField[4][3] = findViewById(R.id.textView43).getId();
        routingMyField[4][4] = findViewById(R.id.textView44).getId();

        routingOpponentField = new int[game.getSize()][game.getSize()];

        routingOpponentField[0][0] = findViewById(R.id.opponentTextView00).getId();
        routingOpponentField[0][1] = findViewById(R.id.opponentTextView01).getId();
        routingOpponentField[0][2] = findViewById(R.id.opponentTextView02).getId();
        routingOpponentField[0][3] = findViewById(R.id.opponentTextView03).getId();
        routingOpponentField[0][4] = findViewById(R.id.opponentTextView04).getId();
        routingOpponentField[1][0] = findViewById(R.id.opponentTextView10).getId();
        routingOpponentField[1][1] = findViewById(R.id.opponentTextView11).getId();
        routingOpponentField[1][2] = findViewById(R.id.opponentTextView12).getId();
        routingOpponentField[1][3] = findViewById(R.id.opponentTextView13).getId();
        routingOpponentField[1][4] = findViewById(R.id.opponentTextView14).getId();
        routingOpponentField[2][0] = findViewById(R.id.opponentTextView20).getId();
        routingOpponentField[2][1] = findViewById(R.id.opponentTextView21).getId();
        routingOpponentField[2][2] = findViewById(R.id.opponentTextView22).getId();
        routingOpponentField[2][3] = findViewById(R.id.opponentTextView23).getId();
        routingOpponentField[2][4] = findViewById(R.id.opponentTextView24).getId();
        routingOpponentField[3][0] = findViewById(R.id.opponentTextView30).getId();
        routingOpponentField[3][1] = findViewById(R.id.opponentTextView31).getId();
        routingOpponentField[3][2] = findViewById(R.id.opponentTextView32).getId();
        routingOpponentField[3][3] = findViewById(R.id.opponentTextView33).getId();
        routingOpponentField[3][4] = findViewById(R.id.opponentTextView34).getId();
        routingOpponentField[4][0] = findViewById(R.id.opponentTextView40).getId();
        routingOpponentField[4][1] = findViewById(R.id.opponentTextView41).getId();
        routingOpponentField[4][2] = findViewById(R.id.opponentTextView42).getId();
        routingOpponentField[4][3] = findViewById(R.id.opponentTextView43).getId();
        routingOpponentField[4][4] = findViewById(R.id.opponentTextView44).getId();
    }

    private int getRoutingByCordinateMyField(int x, int y) {
        return routingMyField[x][y];
    }

    private int getRoutingByCordinateOpponentField(int x, int y) {
        return routingOpponentField[x][y];
    }

    private Cordinate getRoutingByIDOpponentField(int id) {
        for (int i = 0; i < game.getSize(); i++) {
            for (int j = 0; j < game.getSize(); j++) {
                if (routingOpponentField[i][j] == id) {
                    return new Cordinate(i, j);
                }
            }
        }
        return new Cordinate(-1, -1);
    }


    private void phoneVibrate(){

        // Get instance of Vibrator from current Context
        Vibrator v = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);

        // Vibrate for 400 milliseconds
        v.vibrate(400);

    }




}
