package com.kelvindu.learning.popmovies.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by KelvinDu on 12/1/2016.
 */

public class FilmDBHelper extends SQLiteOpenHelper {

    public static final String MOVIES_DB = "movies.db";
    public static final int VERSION = 1;
    public static final String TABLE_FILM = "film";
    public static final String ID = "id";
    public static final String COLUMN_TITLE = "title";
    public static final String COLUMN_POSTER = "poster";
    public static final String COLUMN_OVERVIEW = "overview";
    public static final String COLUMN_DATE = "date";
    public static final String COLUMN_RATING = "rating";

    public FilmDBHelper(Context context) {
        super(context, MOVIES_DB,null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL("create table " + TABLE_FILM + " (" +
                ID + " int not null, " +
                COLUMN_TITLE + " text, " +
                COLUMN_POSTER + " text, " +
                COLUMN_OVERVIEW + " text, " +
                COLUMN_DATE + " text, " +
                COLUMN_RATING + " real, " +
                "unique(" + ID + ") on conflict replace)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        refreshDB(sqLiteDatabase);
    }
    public void refreshDB(SQLiteDatabase sqLiteDatabase){
        sqLiteDatabase.execSQL("drop table if exist");
        onCreate(sqLiteDatabase);
    }
}
