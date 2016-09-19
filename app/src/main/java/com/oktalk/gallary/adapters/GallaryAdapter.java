package com.oktalk.gallary.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by manish on 17/9/16.
 */
public class GallaryAdapter extends RecyclerView.Adapter<GallaryAdapter.ViewHolderGallary> {
    @Override
    public ViewHolderGallary onCreateViewHolder(ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(ViewHolderGallary holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }

    public class ViewHolderGallary extends RecyclerView.ViewHolder {
        public ViewHolderGallary(View itemView) {
            super(itemView);
        }
    }
}
