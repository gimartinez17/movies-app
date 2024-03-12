package com.gmart.data.source.utils

import javax.inject.Qualifier

@Target(AnnotationTarget.FUNCTION)
@Retention(AnnotationRetention.RUNTIME)
annotation class RegionFilterRequired

@Target(AnnotationTarget.FUNCTION)
@Retention(AnnotationRetention.RUNTIME)
annotation class Authorization

@Retention(AnnotationRetention.BINARY)
@Qualifier
annotation class IoDispatcher