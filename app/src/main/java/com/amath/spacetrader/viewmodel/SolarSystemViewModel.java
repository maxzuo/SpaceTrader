package com.amath.spacetrader.viewmodel;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.support.annotation.NonNull;

import com.amath.spacetrader.entity.Planet;
import com.amath.spacetrader.entity.SolarSystem;
import com.amath.spacetrader.model.Model;
import com.amath.spacetrader.model.SolarSystemInteractor;

import java.util.Map;

public class SolarSystemViewModel extends AndroidViewModel {

    private final SolarSystemInteractor interactor;

    public SolarSystemViewModel(@NonNull Application application) {
        super(application);
        interactor = Model.getInstance().getSolarSystemInteractor();
    }

    public Map<Planet, Double> getPlanetDistances(SolarSystem solarSystem) {
        return interactor.loadPlanets(solarSystem);
    }
}
