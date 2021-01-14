package com.gokul.bbr.network;

import com.gokul.bbr.model.MusicModel;

import retrofit2.Call;
import retrofit2.http.GET;

public interface APIInterface {

    @GET("/automotive-media/music.json")
    Call<MusicModel> getPlayList();
}
