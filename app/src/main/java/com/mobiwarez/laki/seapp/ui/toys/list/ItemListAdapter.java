package com.mobiwarez.laki.seapp.ui.toys.list;

import android.content.Context;
import android.location.Location;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.mobiwarez.laki.seapp.R;
import com.mobiwarez.laki.seapp.ui.models.ToyViewModel;
import com.mobiwarez.laki.seapp.ui.toys.sharedtoys.SharedToysAdapter;
import com.mobiwarez.laki.seapp.util.ImageLoader;
import com.mobiwarez.laki.seapp.util.ImageLoaderImpl;

import java.util.LinkedList;
import java.util.List;

import javax.inject.Inject;

/**
 * Created by Laki on 27/10/2017.
 */

public class ItemListAdapter extends RecyclerView.Adapter<ItemListAdapter.ItemListViewHolder> {

    private List<ToyViewModel> toyViewModels = new LinkedList<>();

    private Context context;
    private static ImageLoader imageLoader;
    private static Location currentLocation;
    private static ReceiveItems receiver;

    public ItemListAdapter(Context context, ReceiveItems receiveItems) {
        this.context = context;
        imageLoader = new ImageLoaderImpl(context);
        receiver = receiveItems;
    }

    @Override
    public ItemListViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        final View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_toy_item, parent, false);
        return new ItemListAdapter.ItemListViewHolder(itemView);

    }

    @Override
    public void onBindViewHolder(ItemListViewHolder holder, int position) {
        holder.setItem(toyViewModels.get(position));
    }

    @Override
    public int getItemCount() {
        return toyViewModels.size();
    }

    public void onItemsUpdate(final List<ToyViewModel> viewModels, Location location) {
        currentLocation = location;
        this.toyViewModels = viewModels;
        notifyDataSetChanged();
    }



    interface ReceiveItems{
        void claimItem(ToyViewModel toyViewModel);
    }

    static final class ItemListViewHolder extends RecyclerView.ViewHolder {

        //@Inject
        //ImageLoader imageLoader;

        private ToyViewModel toyViewModel;

        ImageView userAvatar;
        ImageView itemImage;
        TextView giverName;
        TextView givenLocation;
        TextView timePosted;
        TextView iteDescription;
        ImageView receiveImage;
        ImageView locationIcon;
        TextView distanceTv;

        public ItemListViewHolder(View itemView) {
            super(itemView);

            itemImage = itemView.findViewById(R.id.toy_image);
            userAvatar = itemView.findViewById(R.id.user_avatar);
            giverName = itemView.findViewById(R.id.toygiver_name);
            givenLocation = itemView.findViewById(R.id.toy_location);
            timePosted = itemView.findViewById(R.id.time_posted);
            iteDescription = itemView.findViewById(R.id.toy_description);
            receiveImage = itemView.findViewById(R.id.receive_btn);
            locationIcon = itemView.findViewById(R.id.locationIcon);
            distanceTv = itemView.findViewById(R.id.distance_tv);
        }

        public void setItem(ToyViewModel toyViewModel){
            this.toyViewModel = toyViewModel;
            giverName.setText(toyViewModel.getGiverName());
            givenLocation.setText(toyViewModel.getToyLocation());
            timePosted.setText(toyViewModel.getTimePosted());
            iteDescription.setText(toyViewModel.getToyDescription());

            if (TextUtils.isEmpty(toyViewModel.getAvatarUrl())){

            }
            else {
                imageLoader.loadImage(toyViewModel.getAvatarUrl(), userAvatar);
            }

            if (!TextUtils.isEmpty(toyViewModel.getToyImageUrl()) && !toyViewModel.getToyImageUrl().equals("no_image")){
                imageLoader.loadImage(toyViewModel.getToyImageUrl(), itemImage);
            }

            Location itemLoc = new Location("");
            itemLoc.setLongitude(toyViewModel.getLongitude());
            itemLoc.setLatitude(toyViewModel.getLatitude());
            double dist = currentLocation.distanceTo(itemLoc);
            distanceTv.setText(Double.toString(dist)+ " km away");


            receiveImage.setOnClickListener(v -> receiver.claimItem(toyViewModel));

        }
    }
}