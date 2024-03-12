package com.gmart.data.model

import com.gmart.domain.model.Video
import com.google.gson.annotations.SerializedName


class VideosEntity(
    var id: String,
    @SerializedName("iso_639_1")
    var iso6391: String,
    @SerializedName("iso_3166_1")
    var iso31661: String,
    var key: String,
    var name: String,
    var site: String,
    var size: Int,
    var type: String
)

fun VideosEntity.mapToModel() = Video(
    id = id,
    iso6391 = iso6391,
    iso31661 = iso31661,
    key = key,
    name = name,
    site = site,
    size = size,
    type = type
)