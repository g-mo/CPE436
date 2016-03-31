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
        //ImageView image = (ImageView) findViewById(R.id.image);

        name.setText("Genton Mo");

        description.setText("This is a description of myself.");
        description.setY(name.getY() + 50);
    }
}
