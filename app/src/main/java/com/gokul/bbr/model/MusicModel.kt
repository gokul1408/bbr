package com.gokul.bbr.model

import com.google.gson.annotations.SerializedName

data class MusicModel(
        @SerializedName("music")
        var music: List<Music?>? = null
)

data class Music(
        @SerializedName("album")
        var album: String? = null,
        @SerializedName("artist")
        var artist: String? = null,
        @SerializedName("duration")
        var duration: Int? = null,
        @SerializedName("genre")
        var genre: String? = null,
        @SerializedName("image")
        var image: String? = null,
        @SerializedName("site")
        var site: String? = null,
        @SerializedName("source")
        var source: String? = null,
        @SerializedName("title")
        var title: String? = null,
        @SerializedName("totalTrackCount")
        var totalTrackCount: Int? = null,
        @SerializedName("trackNumber")
        var trackNumber: Int? = null,
        var type: Int = 0
)