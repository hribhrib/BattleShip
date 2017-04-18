package group5.battleship.src.views;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import group5.battleship.R;

public class SetShipsActivity extends AppCompatActivity {
    String ships = ""; //XYXYXY
    int MAX_SHIPS = 3;
    int currentShips = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_ships);

        Context context = getApplicationContext();
        CharSequence text = "Click on the area to set 3 ships!";
        int duration = Toast.LENGTH_SHORT;

        Toast.makeText(context, text, duration).show();
    }

    public void cellClick(View view) {
        TextView tv = (TextView) findViewById(view.getId());


        if (currentShips < MAX_SHIPS) {
            if (ships.length() < 2) {
                ships = (ships + (String) view.getTag());
                currentShips++;
                tv.setText("o");
            } else {
                boolean shipSet = false;
                boolean dublicate = false;
                for (int i = 0; i < ships.length()-1; i = i + 2) {
                    String tmp = (ships.substring(i,i+2));
                    if (!tmp.equals((String) view.getTag())) {
                        shipSet = true;
                    }else{
                        dublicate = true;
                    }
                }
                if(shipSet==true && dublicate ==false){
                    ships = (ships + (String) view.getTag());
                    currentShips++;
                    tv.setText("o");
                }
            }

        } else if (currentShips == MAX_SHIPS) {
            Intent intent = new Intent(this, GameActivity.class);
            intent.putExtra("SHIPS", ships);
            intent.putExtra("NAME", getIntent().getStringExtra("NAME"));
            startActivity(intent);
        }
    }
}
