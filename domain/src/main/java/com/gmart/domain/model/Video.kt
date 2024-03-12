package com.gmart.domain.model

data class Video(
    var id: String? = null,
    var iso6391: String? = null,
    var iso31661: String? = null,
    var key: String,
    var name: String,
    var site: String,
    var size: Int = 0,
    var type: String
)
