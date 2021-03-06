package com.amath.spacetrader.model;

import android.util.Log;

import com.amath.spacetrader.entity.Game;
import com.amath.spacetrader.entity.Player;

import java.util.List;

public class ConfigurationInteractor extends Interactor {
    public ConfigurationInteractor(Repository repo) {
        super(repo);
    }

    public void newGame(Game game) {
        getRepository().newGame(game);
    }

//    public Game getGame() {
//        return getRepository().getGame();
//    }

    public void loadPlayer(Player player) {
        Repository repo = getRepository();
        repo.loadPlayer(player);
//        Model model = Model.getInstance();
//        Log.d("notgoodnews", String.valueOf(model));
//        model.loadPlayer(player);
    }

}
