package com.gmart.gmovies.data.model


data class RequestNotification(
    var to: String? = null,
    var data: Data = Data()
) {
    data class Data(
        var title: String? = null,
        var body: String? = null,
        var click_action: String? = null,
        var id: Int? = null
    )
}