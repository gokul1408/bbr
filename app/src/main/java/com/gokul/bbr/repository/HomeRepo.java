package com.gokul.bbr.repository;

import android.app.Application;
import android.content.Context;

import androidx.lifecycle.MutableLiveData;

import com.gokul.bbr.model.Music;
import com.gokul.bbr.model.MusicModel;
import com.gokul.bbr.network.APIClient;
import com.gokul.bbr.network.APIInterface;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeRepo {

    private MutableLiveData<List<Music>> musicModelMutableLiveData = new MutableLiveData<>();
    private MutableLiveData<Boolean> progress = new MutableLiveData<>();
    private Context mContext;

    public HomeRepo(Application application) {
        this.mContext = application;
    }

    public void getMusicData() {
        progress.setValue(true);
        APIInterface apiInterface = APIClient.getClient().create(APIInterface.class);
        apiInterface.getPlayList().enqueue(new Callback<MusicModel>() {
            @Override
            public void onResponse(Call<MusicModel> call, Response<MusicModel> response) {
                if (response.isSuccessful()) {
                    if (response.body() != null) {
                        //checkForLocalAudioFiles
                        List<Music> musicList = new ArrayList<>();
                        File dir = new File(mContext.getExternalCacheDir().getAbsolutePath());
                        if (dir.exists()) {
                            for (File f : dir.listFiles()) {
                                f.getName();
                                Music m = new Music();
                                m.setType(1);
                                String name = f.getName().replace(".mp3", "");
                                m.setTitle(name);
                                String source = mContext.getExternalCacheDir().getAbsolutePath() + "/" + name + ".mp3";
                                m.setSource(source);
                                musicList.add(m);
                            }
                        }
                        musicList.addAll(response.body().getMusic());
                        musicModelMutableLiveData.postValue(musicList);
                    }
                }
            }

            @Override
            public void onFailure(Call<MusicModel> call, Throwable t) {

            }
        });
    }

    public MutableLiveData<List<Music>> getMusicModelMutableLiveData() {
        return musicModelMutableLiveData;
    }

    public MutableLiveData<Boolean> getProgress() {
        return progress;
    }
}
