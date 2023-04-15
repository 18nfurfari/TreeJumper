package com.example.cmpsc475project1;

import android.app.Application;

import java.util.ArrayList;
import java.util.List;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

public class ScoreViewModel extends AndroidViewModel {
    private final LiveData<List<ScoreEntity>> scores;

    public ScoreViewModel(Application application) {
        super(application);
        scores = ScoreDatabase.getScoreDatabase(getApplication()).scoreDAO().getAll();
    }

    public LiveData<List<ScoreEntity>> getScores() {
        List<ScoreEntity> testScores = new ArrayList<>();
        testScores.add(new ScoreEntity(1,"Doom Guy", 100));
        testScores.add(new ScoreEntity(2, "Master Chief", 200));
        testScores.add(new ScoreEntity(3, "George",300));
        return new MutableLiveData<>(testScores);

    }


}

