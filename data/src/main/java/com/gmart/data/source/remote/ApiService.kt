package com.gmart.data.source.remote

import com.gmart.data.model.*
import com.gmart.data.source.utils.Authorization
import com.gmart.data.source.utils.RegionFilterRequired
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.HTTP
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {

    /* Authorization */

    @POST("4/auth/request_token")
    suspend fun createRequestToken(@Body body: RequestTokenRequest): Response<RequestTokenResponse>

    @POST("4/auth/access_token")
    suspend fun getAccessToken(@Body body: AccessTokenRequest): Response<AccessTokenResponse>

    @DELETE("4/auth/access_token")
    suspend fun signOut(@Body body: SignOutRequest): Response<AccessTokenResponse>

    /* Config API */
    @GET("3/configuration/primary_translations")
    suspend fun getPrimaryTranslations(): Response<List<String>>

    @GET("3/configuration/countries")
    suspend fun getCountries(): Response<List<CountryResponse>>

    @GET("3/configuration/languages")
    suspend fun getLanguages(): Response<List<LanguageResponse>>

    /* Account API */

    @GET("3/account/{account_id}")
    suspend fun getAccountDetails(@Path("account_id") accountId: String): Response<AccountResponse>

    @Authorization
    @GET("4/account/{account_id}/lists")
    suspend fun getMyLists(
        @Path("account_id") accountId: String,
        @Query("page") page: Int = 1,
    ): Response<UserListsResponse>

    @Authorization
    @GET("4/account/{account_id}/tv/favorites")
    suspend fun getFavouriteTvShows(
        @Path("account_id") accountId: String,
        @Query("page") page: Int = 1,
    ): Response<MoviesResponse>

    @Authorization
    @GET("4/account/{account_id}/movie/favorites")
    suspend fun getFavouriteMovies(
        @Path("account_id") accountId: String,
        @Query("page") page: Int = 1,
    ): Response<MoviesResponse>

    @Authorization
    @POST("3/account/{account_id}/favorite")
    suspend fun postFavourite(
        @Path("account_id") accountId: String,
        @Body body: FavouriteRequest
    ): Response<ApiResponse>

    /* List API */

    @Authorization
    @GET("4/list/{list_id}")
    suspend fun getListDetails(@Path("list_id") id: Int): Response<ListDetailsResponse>

    @Authorization
    @POST("4/list")
    suspend fun createList(@Body body: CreateListRequest): Response<ApiResponse>

    @Authorization
    @PUT("4/list/{list_id}")
    suspend fun editList(
        @Path("list_id") id: Int,
        @Body body: CreateListRequest
    ): Response<ApiResponse>

    @Authorization
    @POST("4/list/{list_id}/items")
    suspend fun addItems(
        @Path("list_id") listId: Int,
        @Body body: ListItemRequest
    ): Response<ApiResponse>

    @Authorization
    @HTTP(method = "DELETE", path = "4/list/{list_id}", hasBody = false)
    suspend fun deleteList(@Path("list_id") listId: Int): Response<ApiResponse>

    @Authorization
    @HTTP(method = "DELETE", path = "4/list/{list_id}/items", hasBody = true)
    suspend fun removeItem(
        @Path("list_id") listId: Int,
        @Body body: ListItemRequest
    ): Response<ApiResponse>

    /* All media API */

    @GET("3/trending/all/week")
    suspend fun getTrendingAll(@Query("page") page: Int): Response<MediaResponse>

    /* Movie API */

    @GET("3/movie/{movie_id}")
    suspend fun getMovieDetails(
        @Path("movie_id") id: Int,
        @Query("append_to_response") appendToResponse: String = "videos,credits,recommendations"
    ): Response<MovieEntity>

    @GET("3/movie/{movie_id}/credits")
    suspend fun getMovieCast(@Path("movie_id") id: Int): Response<CreditsEntity>

    @GET("3/movie/{movie_id}/account_states")
    suspend fun getMovieAccountStates(@Path("movie_id") id: Int): Response<AccountStateResponse>

    @GET("3/movie/{movie_id}/watch/providers")
    suspend fun getMovieProviders(@Path("movie_id") id: Int): Response<WatchLocaleEntity>

    @GET("3/movie/now_playing")
    suspend fun getNowPlaying(@Query("page") page: Int): Response<MoviesResponse>

    @GET("3/movie/popular")
    suspend fun getPopularMovies(@Query("page") page: Int): Response<MoviesResponse>

    @GET("3/movie/upcoming")
    suspend fun getUpcomingMovies(@Query("page") page: Int): Response<MoviesResponse>

    @GET("3/movie/top_rated")
    suspend fun getTopRatedMovies(
        @Query("page") page: Int,
        @Query("region") region: String
    ): Response<MoviesResponse>

    @GET("3/tv/top_rated")
    suspend fun getTopRatedTvShows(@Query("page") page: Int): Response<TvShowsResponse>

    @GET("3/genre/movie/list")
    suspend fun getMovieGenres(): Response<GenresResponse>

    @GET("3/discover/movie")
    suspend fun getMoviesByGenre(
        @Query("with_genres") withGenres: Int,
        @Query("page") page: Int,
        @Query("sort_by") sortBy: String = "popularity.desc",
        @Query("include_adult") includeAdult: Boolean = false
    ): Response<MoviesResponse>

    @GET("3/search/movie")
    suspend fun searchMovies(
        @Query("query") query: String,
        @Query("page") page: Int
    ): Response<MediaResponse>

    /* Tv Show API */

    @GET("3/tv/{show_id}")
    suspend fun getTvShowDetails(
        @Path("show_id") id: Int,
        @Query("append_to_response") appendToResponse: String = "videos,credits,recommendations"
    ): Response<TvShowEntity>

    @GET("3/tv/{show_id}/credits")
    suspend fun getTvCast(@Path("show_id") id: Int): Response<CreditsEntity>

    @GET("3/tv/{show_id}/account_states")
    suspend fun getTvShowAccountStates(@Path("show_id") id: Int): Response<AccountStateResponse>

    @GET("3/tv/{show_id}/watch/providers")
    suspend fun getTvProviders(@Path("show_id") id: Int): Response<WatchLocaleEntity>

    @RegionFilterRequired
    @GET("3/discover/tv")
    suspend fun getAiringToday(
        @Query("page") page: Int,
        @Query("air_date.gte") gte: String,
        @Query("air_date.lte") lte: String,
        @Query("sort_by") sortBy: String = "popularity.desc",
        @Query("with_watch_monetization_types") monType: String = "flatrate|free|ads|rent|buy",
    ): Response<TvShowsResponse>

    @RegionFilterRequired
    @GET("3/discover/tv")
    suspend fun getPopularTv(
        @Query("page") page: Int,
        @Query("sort_by") sortBy: String = "popularity.desc",
        @Query("with_watch_monetization_types") monType: String = "flatrate|free|ads|rent|buy"
    ): Response<TvShowsResponse>

    @RegionFilterRequired
    @GET("3/discover/tv")
    suspend fun getOnTheAir(
        @Query("page") page: Int,
        @Query("air_date.gte") gte: String,
        @Query("air_date.lte") lte: String,
        @Query("with_watch_monetization_types") monType: String = "flatrate|free|ads|rent|buy"
    ): Response<TvShowsResponse>

    @GET("3/genre/tv/list")
    suspend fun getTvGenres(): Response<GenresResponse>

    @RegionFilterRequired
    @GET("3/discover/tv")
    suspend fun getTvShowsByGenre(
        @Query("with_genres") withGenres: Int,
        @Query("page") page: Int,
        @Query("sort_by") sortBy: String = "popularity.desc",
        @Query("include_adult") includeAdult: Boolean = false
    ): Response<MoviesResponse>

    @RegionFilterRequired
    @GET("3/discover/tv")
    suspend fun getTopRatedMoviesByGenre(
        @Query("page") page: Int,
        @Query("with_genres") genre: Int,
        @Query("sort_by") sortBy: String = "vote_average.desc",
        @Query("vote_count.gte") gte: Int = 200
    ): Response<MoviesResponse>

    @GET("3/search/tv")
    suspend fun searchTvShows(
        @Query("query") query: String,
        @Query("page") page: Int
    ): Response<MediaResponse>

    /* Person API */

    @GET("3/person/{person_id}")
    suspend fun gePersonDetails(
        @Path("person_id") id: Int,
        @Query("append_to_response") appendToResponse: String = "combined_credits,external_ids"
    ): Response<PersonEntity>

}