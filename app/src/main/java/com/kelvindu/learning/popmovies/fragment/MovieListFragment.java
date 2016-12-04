package com.kelvindu.learning.popmovies.fragment;


import android.app.Activity;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.CursorLoader;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.kelvindu.learning.popmovies.R;
import com.kelvindu.learning.popmovies.adapter.FilmRecyclerAdapter;
import com.kelvindu.learning.popmovies.data.FilmDBHelper;
import com.kelvindu.learning.popmovies.model.api.MovieService;
import com.kelvindu.learning.popmovies.model.list.FilmList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * A simple {@link Fragment} subclass.
 */
public class MovieListFragment extends Fragment implements android.app.LoaderManager.LoaderCallbacks<Cursor> {

    public static final int LOADER_FILM = 10;
    String path;
    MovieService movieService;
    TextView textView;
    RecyclerView recyclerView;
    FilmRecyclerAdapter filmRecyclerAdapter;

    public MovieListFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_movie_list, container, false);
        textView = (TextView) view.findViewById(R.id.testingtext);
//        recyclerView = (RecyclerView) view.findViewById(R.id.movie_recycler);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        path = getArguments().getString("path");

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://api.themoviedb.org/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        movieService = retrofit.create(MovieService.class);
        movieService.getMovieList(path).enqueue(new Callback<FilmList>() {
            @Override
            public void onResponse(Call<FilmList> call, Response<FilmList> response) {
                FilmList filmList = response.body();
                ContentValues[] contentValues = new ContentValues[filmList.getResults().size()];
                for(int i = 0; i < filmList.getResults().size();i++){
                    ContentValues cv = new ContentValues();
                    cv.put(FilmDBHelper.ID,filmList.getResults().get(i).getId());
                    cv.put(FilmDBHelper.COLUMN_TITLE,filmList.getResults().get(i).getTitle());
                    cv.put(FilmDBHelper.COLUMN_POSTER,filmList.getResults().get(i).getPosterPath());
                    cv.put(FilmDBHelper.COLUMN_OVERVIEW,filmList.getResults().get(i).getOverview());
                    cv.put(FilmDBHelper.COLUMN_DATE,filmList.getResults().get(i).getReleaseDate());
                    cv.put(FilmDBHelper.COLUMN_RATING,filmList.getResults().get(i).getVoteAverage());
                    contentValues[i] = cv;
                    Log.d("Factory","Success entering " + cv.getAsString(FilmDBHelper.COLUMN_TITLE));
                }
                Uri uri = Uri.parse("content://com.kelvindu.learning.popmovies/film");
                getActivity().getContentResolver().delete(
                        uri,
                        null,
                        null);
                getActivity().getContentResolver().bulkInsert(uri,contentValues);
                getActivity().getContentResolver().notifyChange(uri,null);
                Log.d("Content Provider","Sucess inputtin provicer!");
            }

            @Override
            public void onFailure(Call<FilmList> call, Throwable t) {
                Log.e("Factory","Failed" + t.getMessage());
            }
        });
        getActivity().getLoaderManager().initLoader(LOADER_FILM, null, this);
//        RecyclerView.LayoutManager llm =
//                new LinearLayoutManager(getContext(),LinearLayoutManager.VERTICAL,false);
//        recyclerView.setLayoutManager(llm);
//        filmRecyclerAdapter = new FilmRecyclerAdapter();
//        recyclerView.setAdapter(filmRecyclerAdapter);
    }

    @Override
    public android.content.Loader<Cursor> onCreateLoader(int id, Bundle args) {
        if(id == LOADER_FILM){
            return new CursorLoader(
                    getActivity(),
                    Uri.parse("content://com.kelvindu.learning.popmovies/film"),
                    new String[]{
                            FilmDBHelper.ID,
                            FilmDBHelper.COLUMN_TITLE,
                            FilmDBHelper.COLUMN_POSTER,
                            FilmDBHelper.COLUMN_OVERVIEW,
                            FilmDBHelper.COLUMN_DATE,
                            FilmDBHelper.COLUMN_RATING
                    },
                    null,
                    null,
                    null);
        }
        return null;
    }

    @Override
    public void onLoadFinished(android.content.Loader<Cursor> loader, Cursor data) {
        Log.i("CursorFill","Cursor has "+data.getCount()+" datas ");
        data.moveToPosition(0);
        textView.setText(data.getString(0));

//        Log.i("Method","print");
//        filmRecyclerAdapter.updateCursor(data);
//        Log.i("CursorLoader","Cursor now is filled "+data.getCount());
//        filmRecyclerAdapter.notifyDataSetChanged();
    }

    @Override
    public void onLoaderReset(android.content.Loader<Cursor> loader) {

    }
}
