package com.gmart.gmovies.di

import android.content.Context
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.gmart.data.paging.PagingSourceFactoryImpl
import com.gmart.data.repository.AuthRepositoryImpl
import com.gmart.data.repository.ConfigRepositoryImpl
import com.gmart.data.repository.ExplorerRepositoryImpl
import com.gmart.data.repository.GenreRepositoryImpl
import com.gmart.data.repository.ListingRepositoryImpl
import com.gmart.data.repository.MoviesRepositoryImpl
import com.gmart.data.repository.PersonRepositoryImpl
import com.gmart.data.repository.SearchRepositoryImpl
import com.gmart.data.repository.TvShowRepositoryImpl
import com.gmart.data.repository.UserDataStoreImpl
import com.gmart.data.source.remote.ApiService
import com.gmart.data.source.remote.MoviesInterceptor
import com.gmart.data.source.utils.IoDispatcher
import com.gmart.domain.usecase.ConfigUseCase
import com.gmart.domain.usecase.DetailUseCase
import com.gmart.domain.usecase.ExplorerUseCase
import com.gmart.domain.usecase.GenreMoviesUseCase
import com.gmart.domain.usecase.ListingsUseCase
import com.gmart.domain.usecase.MoviesUseCase
import com.gmart.domain.usecase.PersonUseCase
import com.gmart.domain.usecase.SearchUseCase
import com.gmart.domain.usecase.TvShowsUseCase
import com.gmart.domain.usecase.UserUseCase
import com.gmart.gmovies.BuildConfig
import com.gmart.gmovies.utils.NetworkService
import com.gmart.gmovies.utils.NetworkServiceImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Singleton
    @Provides
    fun provideOkHttp(
        @ApplicationContext context: Context,
        dataStore: UserDataStoreImpl
    ): OkHttpClient {
        val loggingInterceptor = HttpLoggingInterceptor(HttpLoggingInterceptor.Logger.DEFAULT)
        val movieInterceptor = MoviesInterceptor(dataStore, context, BuildConfig.API_TOKEN)
        loggingInterceptor.level = HttpLoggingInterceptor.Level.BODY
        return OkHttpClient.Builder()
            .addInterceptor(movieInterceptor)
            .addInterceptor(loggingInterceptor)
            .build()
    }

    @Singleton
    @Provides
    fun provideRetrofitInstance(gson: Gson, okHttpClient: OkHttpClient): Retrofit {

        return Retrofit.Builder()
            .baseUrl(BuildConfig.BASE_URL)
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .addConverterFactory(GsonConverterFactory.create(gson))
            .client(okHttpClient)
            .build()
    }

    @Singleton
    @Provides
    fun provideGson(): Gson = GsonBuilder().create()

    @Module
    @InstallIn(SingletonComponent::class)
    object DispatcherModule {
        @IoDispatcher
        @Provides
        fun providesIoDispatcher(): CoroutineDispatcher = Dispatchers.IO
    }

    @Singleton
    @Provides
    fun provideNetworkConnectivityService(@ApplicationContext context: Context): NetworkService =
        NetworkServiceImpl(context)

    @Provides
    fun providesMoviesApi(retrofit: Retrofit): ApiService {
        return retrofit.create(ApiService::class.java)
    }

    @Singleton
    @Provides
    fun provideMoviesUseCase(repository: MoviesRepositoryImpl): MoviesUseCase {
        return MoviesUseCase(repository)
    }

    @Singleton
    @Provides
    fun provideTvShowsUseCase(repository: TvShowRepositoryImpl): TvShowsUseCase {
        return TvShowsUseCase(repository)
    }

    @Singleton
    @Provides
    fun provideMoviesRepository(api: ApiService): MoviesRepositoryImpl {
        return MoviesRepositoryImpl(api)
    }

    @Singleton
    @Provides
    fun provideExplorerUseCase(repository: ExplorerRepositoryImpl): ExplorerUseCase {
        return ExplorerUseCase(repository)
    }

    @Singleton
    @Provides
    fun provideTvShowRepository(api: ApiService): TvShowRepositoryImpl {
        return TvShowRepositoryImpl(api)
    }

    @Singleton
    @Provides
    fun provideDetailUseCase(
        movieRepository: MoviesRepositoryImpl,
        tvRepository: TvShowRepositoryImpl,
    ): DetailUseCase {
        return DetailUseCase(movieRepository, tvRepository)
    }

    @Singleton
    @Provides
    fun providePagingPagingSourceFactory(api: ApiService): PagingSourceFactoryImpl {
        return PagingSourceFactoryImpl(api)
    }

    @Singleton
    @Provides
    fun provideExplorerRepository(pagingSource: PagingSourceFactoryImpl): ExplorerRepositoryImpl {
        return ExplorerRepositoryImpl(pagingSource)
    }

    @Singleton
    @Provides
    fun provideUserDataStore(@ApplicationContext context: Context): UserDataStoreImpl {
        return UserDataStoreImpl(context)
    }

    @Singleton
    @Provides
    fun provideAuthRepository(api: ApiService): AuthRepositoryImpl {
        return AuthRepositoryImpl(api)
    }

    @Singleton
    @Provides
    fun provideUserUseCase(
        dataStore: UserDataStoreImpl,
        repository: AuthRepositoryImpl
    ): UserUseCase {
        return UserUseCase(dataStore, repository)
    }

    @Singleton
    @Provides
    fun provideGenreRepository(api: ApiService): GenreRepositoryImpl {
        return GenreRepositoryImpl(api)
    }

    @Singleton
    @Provides
    fun provideGenreUseCase(repository: GenreRepositoryImpl): GenreMoviesUseCase {
        return GenreMoviesUseCase(repository)
    }

    @Singleton
    @Provides
    fun provideSearchRepository(api: ApiService): SearchRepositoryImpl {
        return SearchRepositoryImpl(api)
    }

    @Singleton
    @Provides
    fun provideSearchUseCase(repository: SearchRepositoryImpl): SearchUseCase {
        return SearchUseCase(repository)
    }

    @Singleton
    @Provides
    fun provideListingRepository(api: ApiService): ListingRepositoryImpl {
        return ListingRepositoryImpl(api)
    }

    @Singleton
    @Provides
    fun provideListingUseCase(repository: ListingRepositoryImpl): ListingsUseCase {
        return ListingsUseCase(repository)
    }

    @Singleton
    @Provides
    fun provideConfigRepository(api: ApiService): ConfigRepositoryImpl {
        return ConfigRepositoryImpl(api)
    }

    @Singleton
    @Provides
    fun provideConfigUseCase(
        dataStore: UserDataStoreImpl,
        repository: ConfigRepositoryImpl
    ): ConfigUseCase {
        return ConfigUseCase(dataStore, repository)
    }

    @Singleton
    @Provides
    fun providePersonRepository(api: ApiService): PersonRepositoryImpl {
        return PersonRepositoryImpl(api)
    }

    @Singleton
    @Provides
    fun providePersonUseCase(repository: PersonRepositoryImpl): PersonUseCase {
        return PersonUseCase(repository)
    }
}
