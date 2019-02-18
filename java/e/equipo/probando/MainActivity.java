package e.equipo.probando;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.graphics.Point;
import android.os.Bundle;
import android.view.Display;

public class MainActivity extends AppCompatActivity {

    View view;


     @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        Display display = getWindowManager().getDefaultDisplay();



        // Carga la resolución en un objeto Point
        Point size = new Point();
        display.getSize(size);
        // Crea una nueva View basada en la clase SnakeView
        SharedPreferences myPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor=myPreferences.edit();
        view = new View(this, size,myPreferences,editor);
        setContentView(view);



    }

    @Override
    protected void onResume() {
        super.onResume();
        view.resume();
    }


    @Override
    protected void onPause() {
        super.onPause();
        view.pause();
    }




}
