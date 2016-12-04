package com.kelvindu.learning.popmovies.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.kelvindu.learning.popmovies.R;

/**
 * Created by KelvinDu on 12/1/2016.
 */

public class FilmRecyclerAdapter extends RecyclerView.Adapter<FilmRecyclerAdapter.ViewHolder> {

    private final int POSITION_ID = 0;
    private final int POSITION_TITLE = 1;
    private final int POSITION_POSTER = 2;
    private final int POSITION_OV = 3;
    private final int POSITION_DT = 4;

    Cursor cursor;
    Context context;

    public FilmRecyclerAdapter(){}

    public FilmRecyclerAdapter(Cursor cursor){
        this.cursor = cursor;
    }
    public void updateCursor (Cursor cursor){
        this.cursor = cursor;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.movie_cards,parent,false);
        return new ViewHolder(view);
    }

//    holder.cardItem.setOnClickListener(new View.OnClickListener() {
//        @Override
//        public void onClick(View view) {
//            Intent i = new Intent(view.getContext(), DetailActivity.class);
//            i.putExtra("temperature", temp);
//            i.putExtra("icon", icon);
//            i.putExtra("humadity", humadity);
//            i.putExtra("pressure", pressure);
//            view.getContext().startActivity(i);
//        }
//    });

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        context = holder.poster.getContext();
        holder.title.setText(cursor.getString(POSITION_TITLE));
        Glide.with(context).load("http://image.tmdb.org/t/p/w185/" + cursor.getString(POSITION_POSTER))
                .centerCrop()
                .crossFade()
                .into(holder.poster);
        holder.date.setText(cursor.getString(POSITION_DT));
        holder.overview.setText(cursor.getString(POSITION_OV));
        Log.i("ViewHolder", "Successfuly bind data");
        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
    }

    @Override
    public int getItemCount() {
        if (cursor != null)
            return cursor.getCount();
        return 0;
    }

    public class ViewHolder extends RecyclerView.ViewHolder
            implements View.OnClickListener{
        TextView title;
        TextView date;
        TextView overview;
        ImageView poster;
        CardView cardView;

        public ViewHolder(View itemView) {
            super(itemView);
            title = (TextView) itemView.findViewById(R.id.title);
            date = (TextView) itemView.findViewById(R.id.dt);
            overview = (TextView) itemView.findViewById(R.id.ov);
            poster = (ImageView) itemView.findViewById(R.id.poster);
            cardView = (CardView) itemView.findViewById(R.id.movie_cardview);
            Log.i("ViewHolder","Successfully create view");
        }

        @Override
        public void onClick(View view) {

        }
    }
}
