package group5.battleship;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TableLayout;
import android.widget.TextView;
import group5.*;

public class SetShipsActivity extends AppCompatActivity {
    String ships = ""; //XYXYXY
    int MAX_SHIPS = 3;
    int currentShips = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_ships);
    }

    public void colClick(View view) {
        TextView tv = (TextView) findViewById(view.getId());

        if(currentShips<MAX_SHIPS){
            ships = (ships + (String) view.getTag());
            System.out.println(ships);
            currentShips++;
            tv.setText("SHIP");
        } else if(currentShips==MAX_SHIPS){
            Intent intent = new Intent(this, GameActivity.class);
            intent.putExtra("SHIPS", ships);
            intent.putExtra("NAME", getIntent().getStringExtra("NAME"));
            startActivity(intent);
        }
    }
}
