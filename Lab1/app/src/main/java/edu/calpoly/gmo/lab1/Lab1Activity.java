package edu.calpoly.gmo.lab1;

import android.graphics.drawable.Drawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

public class Lab1Activity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lab1);

        TextView name = (TextView) findViewById(R.id.name);
        TextView description = (TextView) findViewById(R.id.description);
        ImageView image = (ImageView) findViewById(R.id.me);

        name.setText("Genton Mo");

        description.setText("I'm a fifth year Computer Engineering major " +
                "from Alhambra, CA, which is about 10 miles east of LA. " +
                "After graduating, I hope to become a mobile developer. " +
                "On my free time, I enjoy eating, going to the gym, and " +
                "playing board games.");
        description.setY(name.getY() + 75);
    }
}
