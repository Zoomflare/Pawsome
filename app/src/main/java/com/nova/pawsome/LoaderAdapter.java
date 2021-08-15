package com.nova.pawsome;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.core.app.ActivityOptionsCompat;
import androidx.core.util.Pair;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class LoaderAdapter extends RecyclerView.Adapter<LoaderAdapter.LoaderViewHolder>{
    private Context mContext;
    private ArrayList<LoaderItem> mLoaderList;

    public LoaderAdapter(Context context, ArrayList<LoaderItem> loaderList){
        mContext = context;
        mLoaderList = loaderList;

    }

    @Override
    public LoaderViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v= LayoutInflater.from(mContext).inflate(R.layout.card_view,parent,false);
        return new LoaderViewHolder(v);
    }

    @Override
    public void onBindViewHolder(LoaderViewHolder holder, int position) {
        LoaderItem currentItem= mLoaderList.get(position);

        String imageUrl = currentItem.getImageUrl();
        String breedName = currentItem.getBreedName();
        int id = currentItem.getId();

        holder.mBreedName.setText(breedName);
        holder.mId.setText(Integer.toString(id));
        Picasso.with(mContext).load(imageUrl).fit().centerInside().into(holder.mImageView);
        holder.mImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent =new Intent(mContext, DisplayActivity.class);
                intent.putExtra("ID",id);
                intent.putExtra("ImgUrl",imageUrl);
                Pair<View, String> p1 = Pair.create((View)holder.mImageView, "example_transition");
                ActivityOptionsCompat options = ActivityOptionsCompat.
                        makeSceneTransitionAnimation((Activity) mContext, p1);
                mContext.startActivity(intent,options.toBundle());

            }
        });




    }

    @Override
    public int getItemCount() {
        return mLoaderList.size();
    }

    public class LoaderViewHolder extends RecyclerView.ViewHolder{
        public ImageView mImageView,mFavoriteButton;
        public TextView mBreedName;
        public TextView mId;


        public LoaderViewHolder(View itemView) {
            super(itemView);

            mImageView =itemView.findViewById(R.id.image_view);
            mBreedName=itemView.findViewById(R.id.Breed_Name);
            mId=itemView.findViewById(R.id.Breed_Id);
        }
    }
}
