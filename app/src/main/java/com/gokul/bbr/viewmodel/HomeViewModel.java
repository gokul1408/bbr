package com.gokul.bbr.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.gokul.bbr.model.Music;
import com.gokul.bbr.repository.HomeRepo;

import java.util.List;

public class HomeViewModel extends AndroidViewModel {
    public HomeRepo homeRepo;
    private MutableLiveData<List<Music>> musicModelMutableLiveData;
    private MutableLiveData<Boolean> progress;
    private MutableLiveData<Music> audio;

    public HomeViewModel(@NonNull Application application) {
        super(application);
        homeRepo = new HomeRepo(application);
    }


    public MutableLiveData<List<Music>> getMusicModelMutableLiveData() {
        if (musicModelMutableLiveData == null) {
            musicModelMutableLiveData = new MutableLiveData<>();
            musicModelMutableLiveData = homeRepo.getMusicModelMutableLiveData();
            homeRepo.getMusicData();
        }
        return musicModelMutableLiveData;
    }

    public MutableLiveData<Boolean> getProgress() {
        if (progress == null) {
            progress = new MutableLiveData<>();
            progress = homeRepo.getProgress();
        }
        return progress;
    }

    public MutableLiveData<Music> getAudio() {
        if (audio == null) {
            audio = new MutableLiveData<>();
        }
        return audio;
    }

}

