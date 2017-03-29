package group5.battleship;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

public class MainActivity extends AppCompatActivity {
    EditText playername;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_screen);
        playername = (EditText) (findViewById(R.id.editText));
    }


    public void startGame(View view) {
        Intent intent = new Intent(this, SetShipsActivity.class);
        intent.putExtra("NAME", playername.getText().toString());
        startActivity(intent);
    }

}
