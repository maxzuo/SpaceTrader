package com.amath.spacetrader.model;

import android.util.Log;

import com.amath.spacetrader.entity.Planet;
import com.amath.spacetrader.entity.SolarSystem;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.List;

public class SolarSystemInteractor extends Interactor {

    protected SolarSystemInteractor(Repository repo) {
        super(repo);
    }

    //Access the Model singleton access with:
    //Model model = Model.getInstance();

    public Map<Planet, Double> loadPlanets(SolarSystem solarSystem) {
        Model model = Model.getInstance();
        Map<Planet, Double> planets = new HashMap<>();
        Log.d("refactor", "Inside load planets");

        Planet currentPlanet = model.getGame().getCurrentPlanet();
        for (Planet planet: solarSystem.getPlanets()) {
            double distanceFromCurrentPlanet
                    = calculateDistanceBetweenPlanets(currentPlanet, planet);
            planets.put(planet, distanceFromCurrentPlanet);
        }
        Log.d("refactor", String.valueOf(planets.size()));
        return planets;
    }

    private double calculateDistanceBetweenPlanets(Planet currentPlanet, Planet otherPlanet) {
        Model model = Model.getInstance();
        String currentSolarSystemName = currentPlanet.getSolarSystem();
        String otherSolarSystemName = otherPlanet.getSolarSystem();

        SolarSystem currentSolarSystem = null;
        SolarSystem otherSolarSystem = null;
        for (SolarSystem system : Model.getInstance().getGame().getSolarSystems()) {
            if (system.getName().equals(currentSolarSystemName)) {
                currentSolarSystem = system;
            }
            if (system.getName().equals(otherSolarSystemName)) {
                otherSolarSystem = system;
            }
        }

        double distance = 0;

        //To calculate the distance between planets in separate solar systems,
        //calculate the distance between the solar systems and then add that number to the distance
        //from each sun in each solar system.
        if (!currentSolarSystem.equals(otherSolarSystem)) {
            distance += model.getUniverseInteractor()
                    .calculateDistanceBetweenSolarSystems(currentSolarSystem, otherSolarSystem);
            distance += currentPlanet.getDistanceFromSun();
            distance += otherPlanet.getDistanceFromSun();
        } else {
            distance = Math.sqrt(Math.pow(otherPlanet.getLocation().getX()
                    - currentPlanet.getLocation().getX(), 2)
                    + Math.pow(otherPlanet.getLocation().getY()
                    - currentPlanet.getLocation().getY(), 2));
        }
        return distance;
    }
}
