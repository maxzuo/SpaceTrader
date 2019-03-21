package com.amath.spacetrader.viewmodel;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.support.annotation.NonNull;

import com.amath.spacetrader.model.Model;
import com.amath.spacetrader.model.SolarSystemInteractor;

public class SolarSystemViewModel extends AndroidViewModel {

    private SolarSystemInteractor interactor;

    public SolarSystemViewModel(@NonNull Application application) {
        super(application);
        interactor = Model.getInstance().getSolarSystemInteractor();
    }
}