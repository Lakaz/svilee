package com.mobiwarez.laki.seapp.ui.toys.create;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/*
import com.mobiwarez.sache.R;
import com.mobiwarez.sache.models.MyPlace;
*/

import com.mobiwarez.laki.seapp.R;

import java.util.List;

/**
 * Created by Laki on 06/08/2017.
 */

public class PlacesAdapter extends RecyclerView.Adapter<PlacesAdapter.PlacesViewHolder> {

    private List<MyPlace> places;
    private PlacesAdapter.ItemListener mListener;

    public PlacesAdapter(List<MyPlace> places, PlacesAdapter.ItemListener mListener) {
        this.places = places;
        this.mListener = mListener;
    }

    @Override
    public PlacesViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new PlacesAdapter.PlacesViewHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_item_places, parent, false));

    }

    @Override
    public void onBindViewHolder(PlacesViewHolder holder, int position) {
        holder.setData(places.get(position));
        MyPlace place = places.get(position);
        holder.placeName.setText(place.getPlaceName());
        holder.placeAddress.setText(place.getPlaceAddress());
    }

    @Override
    public int getItemCount() {
        return places.size();
    }

    public void replaceData(List<MyPlace> places) {
        setList(places);
        notifyDataSetChanged();
    }

    private void setList(List<MyPlace> tasks) {
        places = tasks;
    }


    public class  PlacesViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public TextView placeName;
        public TextView placeAddress;
        public MyPlace place;

        public PlacesViewHolder(View itemView) {
            super(itemView);

            placeAddress = itemView.findViewById(R.id.place_address);
            placeName = itemView.findViewById(R.id.place_name);
            itemView.setOnClickListener(this);
        }

        public void setData(MyPlace item) {
            this.place = item;
            //imageView.setImageResource(item.getDrawableResource());
            //textView.setText(item.getInterestName());
        }


        @Override
        public void onClick(View view) {
            if (mListener != null) {
                mListener.onItemClick(place);
            }
        }
    }

    public interface ItemListener {
        void onItemClick(MyPlace item);
    }
}
