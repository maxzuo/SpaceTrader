package com.amath.spacetrader.view;

import android.app.Activity;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.amath.spacetrader.R;
import com.amath.spacetrader.entity.Constants;
import com.amath.spacetrader.entity.Planet;
import com.amath.spacetrader.entity.RandomEvent;
import com.amath.spacetrader.entity.SolarSystem;
import com.amath.spacetrader.model.PlanetInteractor;
import com.amath.spacetrader.model.SolarSystemInteractor;
import com.amath.spacetrader.viewmodel.PlanetViewModel;

import java.io.File;
import java.util.Locale;

public class PlanetActivity extends AppCompatActivity {

    PlanetViewModel viewModel;

    Planet currentPlanet;
    Double distance;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_planet);

        viewModel = ViewModelProviders.of(this).get(PlanetViewModel.class);

        Bundle extras = getIntent().getExtras();
        String viewData;
        if (extras != null) {
            currentPlanet = (Planet) extras.get("planet");
            distance = (Double) extras.get("distance");
            Log.e("planetActivity", "Bundle delivered! " + currentPlanet.getName());
            Log.e("planetActivity", "Bundle delivered! distance: " + distance);
            viewData = String.format(Locale.US, "%.2f ly", distance);
            //The key argument here must match that used in the other activity
        } else {
            Double distance = 0.0;
            viewData = String.format(Locale.US, "%.2f ly", distance);
            Log.e("planetActivity", "Unable to fetch current planet");
        }

        // Populate activity
        TextView name = findViewById(R.id.planet_name);
        name.setText(currentPlanet.getName());

        TextView status = findViewById(R.id.planet_status);
        if (currentPlanet.hasStatus()) {
            status.setText(currentPlanet.getStatus().name());
        } else {
            status.setText(String.format(Locale.US, "none"));
        }
        TextView techLevel = findViewById(R.id.planet_techlevel);
        techLevel.setText(String.format(Locale.US, "%d (%s)",
                currentPlanet.getTechLevel().getLevel(),
                currentPlanet.getTechLevel().toString()));

        TextView resourceLevel = findViewById(R.id.planet_resourcelevel);
        resourceLevel.setText(String.format(Locale.US, "%d (%s)",
                currentPlanet.getResourceLevel().getLevel(),
                currentPlanet.getResourceLevel().toString()));

        TextView distanceView = findViewById(R.id.distance2);
        distanceView.setText(viewData);

    }

    // Handlers

    /**
     * Button handler for the fly button
     * Intents are used to move between activities.
     * Please look at the config() under MainActivity.java
     * for an example.
     *
     * @param view the button that was pressed
     */
    public void onFlyPressed(View view) {
//        startActivity(intent);
        Log.i("planetActivity", "Fly Button Pressed");

        try {
            viewModel.fly(currentPlanet, distance, new File(getFilesDir(),
                    Constants.LOCAL_GAME_SERIALIZATION_FILE));


            int randomEventProb = (int) (Math.random() * 100);
            Intent intent = new Intent(this, RandomEventActivity.class);
            final int probBlackHole = 1;
            final int probCrewMutiny = 15;
            final int probShipMalfunction = 20;
            final int probRobbery = 15;
            int minProb = probBlackHole + probCrewMutiny + probShipMalfunction + probRobbery;
            if (randomEventProb < minProb) {
                    //black hole has probability of 1%
                if (randomEventProb < probBlackHole) {
                    intent.putExtra("randomEvent", RandomEvent.BLACK_HOLE);
                    //crew mutiny has probability of 15%
                } else if (randomEventProb < probBlackHole + probCrewMutiny) {
                    intent.putExtra("randomEvent", RandomEvent.CREW_MUTINY);
                    //ship malfunction has probability of 20%
                } else if (randomEventProb < probBlackHole + probCrewMutiny + probShipMalfunction) {
                    intent.putExtra("randomEvent", RandomEvent.SHIP_MALFUNCTION);
                    //robbery has probability of 15%
                } else {
                    intent.putExtra("randomEvent", RandomEvent.ROBBERY);
                }
                startActivityForResult(intent, 1);
            }

            Intent returnIntent = new Intent();
            setResult(Activity.RESULT_OK, returnIntent);
            finish();
        } catch (PlanetViewModel.IllegalFlyException e) {
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_LONG).show();
        }




    }

    public void onBackPressed(View view) {
        Intent returnIntent = new Intent();
        setResult(Activity.RESULT_CANCELED, returnIntent);
        finish();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == 1) {
            if(resultCode == Activity.RESULT_OK){
                Intent returnIntent = new Intent();
                setResult(Activity.RESULT_OK, returnIntent);
                finish();
            }
        }
    }//onActivityResult
}
