package com.kelvindu.learning.popmovies.data;

import android.content.ContentProvider;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.Nullable;
import android.util.Log;

/**
 * Created by KelvinDu on 12/1/2016.
 */

public class MovieProvider extends ContentProvider {

    public static final int FILM = 10;
    public static final int FILM_PER_ROW = 20;
    FilmDBHelper filmDBHelper;
    UriMatcher uriMatcher;

    @Override
    public boolean onCreate() {
        filmDBHelper = new FilmDBHelper(getContext());
        uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        uriMatcher.addURI("com.kelvindu.learning.popmovies","film",FILM);
        uriMatcher.addURI("com.kelvindu.learning.popmovies","film/#",FILM_PER_ROW);
//        uriMatcher.addURI("com.kelvindu.learning.popmovies","",FILM);
//        uriMatcher.addURI("com.kelvindu.learning.popmovies","",FILM);
        return true;
    }

    @Nullable
    @Override
    public Cursor query(Uri uri, String[] projection,
                        String selection, String[] selectionArgs,
                        String sortOrder) {
        Cursor cursor = null;
        switch (uriMatcher.match(uri)){
            case FILM:
                cursor = filmDBHelper.getReadableDatabase().query(
                        FilmDBHelper.TABLE_FILM,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder
                );
                cursor.setNotificationUri(getContext().getContentResolver(),uri);
                break;
            case FILM_PER_ROW :
                cursor = filmDBHelper.getReadableDatabase().query(
                        FilmDBHelper.TABLE_FILM,
                        projection, "film.id = ?",
                        new String[]{uri.getPathSegments().get(0)},
                        null,
                        null,
                        sortOrder
                );
                cursor.setNotificationUri(getContext().getContentResolver(),uri);
                break;
        }
        return cursor;
    }

    @Nullable
    @Override
    public String getType(Uri uri) {
        int match = uriMatcher.match(uri);
        switch (match){
            case FILM :
                return ContentResolver.CURSOR_DIR_BASE_TYPE + "/com.kelvindu.learning.popmovies/film";
            case FILM_PER_ROW :
                return ContentResolver.CURSOR_ITEM_BASE_TYPE + "/com.kelvindu.learning.popmovies/film";
        }
        return null;
    }

    @Nullable
    @Override
    public Uri insert(Uri uri, ContentValues contentValues) {
        switch (uriMatcher.match(uri)){
            case FILM :
                long id = filmDBHelper.getWritableDatabase().insert(
                        FilmDBHelper.TABLE_FILM,
                        null,
                        contentValues);
                return Uri.parse("content://com.kelvindu.learning.popmovies/film/row/"+id);
        }
        return null;
    }

    @Override
    public int delete(Uri uri, String s, String[] strings) {
        switch (uriMatcher.match(uri)){
            case FILM :
                int jmlBarisDelete = filmDBHelper.getWritableDatabase().delete(
                        FilmDBHelper.TABLE_FILM,
                        s,
                        strings);
                return jmlBarisDelete;
        }
        return 0;
    }

    @Override
    public int update(Uri uri, ContentValues contentValues, String s, String[] strings) {
        switch (uriMatcher.match(uri)){
            case FILM:
                int jmlBarisUpdate = filmDBHelper.getWritableDatabase().update(
                        FilmDBHelper.TABLE_FILM,
                        contentValues,
                        s,
                        strings);
                return jmlBarisUpdate;
        }
        return 0;
    }

    @Override
    public int bulkInsert(Uri uri, ContentValues[] values) {
        switch (uriMatcher.match(uri)){
            case FILM :
                int returnCount = 0;
                SQLiteDatabase writableDatabase = filmDBHelper.getWritableDatabase();
                writableDatabase.beginTransaction();
                try{
                    for(ContentValues cv : values){
                        writableDatabase.insert(FilmDBHelper.TABLE_FILM,null,cv);
                        Log.d("BulkInsert","Success! inserting " + cv.getAsString(FilmDBHelper.COLUMN_TITLE));
                        returnCount++;
                    }
                }catch(Exception e){
                    Log.e("BulkInsert","ERROR");
                    returnCount = 0;
                }finally {
                    writableDatabase.endTransaction();
                }
                return returnCount;
        }
        return 0;
    }
}
