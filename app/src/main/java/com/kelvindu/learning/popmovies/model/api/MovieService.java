package com.kelvindu.learning.popmovies.model.api;

import com.kelvindu.learning.popmovies.model.list.FilmList;
import com.kelvindu.learning.popmovies.model.review.Review;
import com.kelvindu.learning.popmovies.model.trailer.Trailer;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

/**
 * Created by KelvinDu on 12/1/2016.
 */

public interface MovieService {

    @GET("3/movie/{category}?api_key=ec5404384ba2d4044ffdabb7a1dbc893")
    Call<FilmList> getMovieList(@Path("category") String path);

    @GET("3/movie/{movieId}/videos?api_key=ec5404384ba2d4044ffdabb7a1dbc893")
    Call<Trailer> getTrailer(@Path("movieId") long movieId);

    @GET("3/movie/{movieId}/reviews?api_key=ec5404384ba2d4044ffdabb7a1dbc893")
    Call<Review> getReview(@Path("movieId") long movieId);
}
