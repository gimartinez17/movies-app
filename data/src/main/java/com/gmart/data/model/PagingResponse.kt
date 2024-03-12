package com.gmart.data.model

interface PagingResponse<T> {
    fun toModel(): T
}