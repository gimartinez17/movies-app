package com.gmart.data.model

import com.google.gson.annotations.SerializedName
import com.gmart.domain.model.ListType
import com.gmart.domain.model.UserList

data class UserListEntity(
    val description: String,
    @SerializedName("favorite_count")
    val favoriteCount: Int,
    val id: Int,
    @SerializedName("item_count")
    val itemCount: Int,
    @SerializedName("iso_639_1")
    val iso6391: String,
    @SerializedName("list_type")
    val listType: String,
    val name: String,
    val public: Int,
    @SerializedName("sort_by")
    val sortBy: Int?,
    @SerializedName("poster_path")
    val posterPath: String?,
    @SerializedName("created_at")
    val createdAt: String?
)

fun UserListEntity.mapToModel() = UserList(
    description = description,
    favoriteCount = favoriteCount,
    id = id,
    itemCount = itemCount,
    language = iso6391,
    listType = ListType.USER,
    name = name,
    public = public == 1,
    sortBy = sortBy,
    posterPath = posterPath,
    createdAt = createdAt,
)