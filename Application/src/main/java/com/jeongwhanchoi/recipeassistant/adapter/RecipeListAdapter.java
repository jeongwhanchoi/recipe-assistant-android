/*
 * Copyright (C) 2014 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.jeongwhanchoi.recipeassistant.adapter;

import android.content.res.TypedArray;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.DecelerateInterpolator;
import android.widget.ImageView;
import android.widget.TextView;


import com.jeongwhanchoi.recipeassistant.R;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.display.SimpleBitmapDisplayer;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;

import java.util.List;

public class RecipeListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int VIEW_TYPE_RECIPE = 1;
    private static final int VIEW_TYPE_FOOTER = 2;

    private List<RecipeModel> mRecipeList;
    private List<Object> mFooterList;
    private RecipeViewHolder.OnItemClickListener mListener;
    private int mGridSpanCount;
    private boolean mAnimationEnabled = true;
    private int mAnimationPosition = -1;
    private ImageLoader mImageLoader = ImageLoader.getInstance();
    private DisplayImageOptions mDisplayImageOptions;
    private ImageLoadingListener mImageLoadingListener;

    public RecipeListAdapter(List<RecipeModel> recipeList, List<Object> footerList, RecipeViewHolder.OnItemClickListener listener, int gridSpanCount)
    {
        mRecipeList = recipeList;
        mFooterList = footerList;
        mListener = listener;
        mGridSpanCount = gridSpanCount;
        setupImageLoader();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());

        // inflate view and create view holder
        if(viewType == VIEW_TYPE_RECIPE)
        {
            View view = inflater.inflate(R.layout.fragment_recipe_list_item, parent, false);
            return new RecipeViewHolder(view, mListener, mImageLoader, mDisplayImageOptions, mImageLoadingListener);
        }
        else if(viewType == VIEW_TYPE_FOOTER)
        {
            View view = inflater.inflate(R.layout.fragment_recipe_list_footer, parent, false);
            return new FooterViewHolder(view);
        }
        else
        {
            throw new RuntimeException("There is no view type that matches the type " + viewType);
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position)
    {
        // bind data
        if(viewHolder instanceof RecipeViewHolder)
        {
            // entity
            RecipeModel recipe = mRecipeList.get(getRecipePosition(position));

            // bind data
            if(recipe != null)
            {
                ((RecipeViewHolder) viewHolder).bindData(recipe);
            }
        }
        else if(viewHolder instanceof FooterViewHolder)
        {
            // entity
            Object object = mFooterList.get(getFooterPosition(position));

            // bind data
            if(object != null)
            {
                ((FooterViewHolder) viewHolder).bindData(object);
            }
        }

        // set item margins
        setItemMargins(viewHolder.itemView, position);

        // set animation
        setAnimation(viewHolder.itemView, position);
    }

    @Override
    public int getItemCount()
    {
        int size = 0;
        if(mRecipeList != null) size += mRecipeList.size();
        if(mFooterList != null) size += mFooterList.size();
        return size;
    }


    @Override
    public int getItemViewType(int position)
    {
        int recipes = mRecipeList.size();
        int footers = mFooterList.size();

        if(position < recipes) return VIEW_TYPE_RECIPE;
        else if(position < recipes + footers) return VIEW_TYPE_FOOTER;
        else return -1;
    }


    public int getRecipeCount()
    {
        if(mRecipeList != null) return mRecipeList.size();
        return 0;
    }


    public int getFooterCount()
    {
        if(mFooterList != null) return mFooterList.size();
        return 0;
    }


    public int getRecipePosition(int recyclerPosition)
    {
        return recyclerPosition;
    }


    public int getFooterPosition(int recyclerPosition)
    {
        return recyclerPosition - getRecipeCount();
    }


    public int getRecyclerPositionByRecipe(int recipePosition)
    {
        return recipePosition;
    }


    public int getRecyclerPositionByFooter(int footerPosition)
    {
        return footerPosition + getRecipeCount();
    }


    public void refill(List<RecipeModel> recipeList, List<Object> footerList, RecipeViewHolder.OnItemClickListener listener, int gridSpanCount)
    {
        mRecipeList = recipeList;
        mFooterList = footerList;
        mListener = listener;
        mGridSpanCount = gridSpanCount;
        notifyDataSetChanged();
    }


    public void stop()
    {
    }


    public void setAnimationEnabled(boolean animationEnabled)
    {
        mAnimationEnabled = animationEnabled;
    }


    private void setupImageLoader()
    {
        mDisplayImageOptions = new DisplayImageOptions.Builder()
                .showImageOnLoading(android.R.color.transparent)
                .showImageForEmptyUri(R.drawable.placeholder_photo)
                .showImageOnFail(R.drawable.placeholder_photo)
                .cacheInMemory(true)
                .cacheOnDisk(true)
                .displayer(new SimpleBitmapDisplayer())
                .build();
        mImageLoadingListener = new AnimateImageLoadingListener();
    }


    private void setAnimation(final View view, int position)
    {
        if(mAnimationEnabled && position > mAnimationPosition)
        {
            view.setScaleX(0F);
            view.setScaleY(0F);
            view.animate()
                    .scaleX(1F)
                    .scaleY(1F)
                    .setDuration(300)
                    .setInterpolator(new DecelerateInterpolator());

            mAnimationPosition = position;
        }
    }


    private void setItemMargins(View view, int position)
    {
        int marginTop = 0;

        if(position < mGridSpanCount)
        {
            TypedArray a = CookbookApplication.getContext().obtainStyledAttributes(null, new int[]{android.R.attr.actionBarSize}, 0, 0);
            marginTop = (int) a.getDimension(0, 0);
            a.recycle();
        }

        ViewGroup.MarginLayoutParams marginParams = (ViewGroup.MarginLayoutParams) view.getLayoutParams();
        marginParams.setMargins(0, marginTop, 0, 0);
    }


    public static final class RecipeViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener
    {
        private TextView mNameTextView;
        private ImageView mImageView;
        private OnItemClickListener mListener;
        private ImageLoader mImageLoader;
        private DisplayImageOptions mDisplayImageOptions;
        private ImageLoadingListener mImageLoadingListener;


        public interface OnItemClickListener
        {
            void onItemClick(View view, int position, long id, int viewType);
        }


        public RecipeViewHolder(View itemView, OnItemClickListener listener, ImageLoader imageLoader, DisplayImageOptions displayImageOptions, ImageLoadingListener imageLoadingListener)
        {
            super(itemView);
            mListener = listener;
            mImageLoader = imageLoader;
            mDisplayImageOptions = displayImageOptions;
            mImageLoadingListener = imageLoadingListener;

            // set listener
            itemView.setOnClickListener(this);

            // find views
            mNameTextView = itemView.findViewById(R.id.fragment_recipe_list_item_name);
            mImageView = itemView.findViewById(R.id.fragment_recipe_list_item_image);
        }


        @Override
        public void onClick(View view)
        {
            int position = getAdapterPosition();
            if(position != RecyclerView.NO_POSITION)
            {
                mListener.onItemClick(view, position, getItemId(), getItemViewType());
            }
        }


        public void bindData(RecipeModel recipe)
        {
            mNameTextView.setText(recipe.getName());
            mImageLoader.displayImage(recipe.getImage(), mImageView, mDisplayImageOptions, mImageLoadingListener);
        }
    }


    public static final class FooterViewHolder extends RecyclerView.ViewHolder
    {
        public FooterViewHolder(View itemView)
        {
            super(itemView);
        }


        public void bindData(Object object)
        {
            // do nothing
        }
    }
}
