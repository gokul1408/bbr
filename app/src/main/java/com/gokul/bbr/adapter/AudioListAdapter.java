package com.gokul.bbr.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.gokul.bbr.databinding.AudioListItemBinding;
import com.gokul.bbr.interfaces.PlayListClickListener;
import com.gokul.bbr.model.Music;

import java.util.List;

public class AudioListAdapter extends RecyclerView.Adapter<AudioListAdapter.ViewHolder> {
    private List<Music> mList;
    private Context mContext;
    private PlayListClickListener playListClickListener;

    public AudioListAdapter(Context context, PlayListClickListener prd) {
        this.mContext = context;
        this.playListClickListener = prd;
    }

    @Override
    public AudioListAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        AudioListItemBinding itemBlogBinding = AudioListItemBinding.inflate(LayoutInflater.from(mContext), parent, false);
        return new AudioListAdapter.ViewHolder(itemBlogBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull AudioListAdapter.ViewHolder holder, int position) {
        Music data = mList.get(position);
        holder.db.audioName.setText(data.getTitle());
        holder.db.play.setOnClickListener(v -> {
            playListClickListener.onClick(position);
        });
    }

    @Override
    public int getItemCount() {
        return (null != mList ? mList.size() : 0);
    }

    public List<Music> getmList() {
        return mList;
    }

    public void setmList(List<Music> mList) {
        this.mList = mList;
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private AudioListItemBinding db;

        public ViewHolder(AudioListItemBinding viewDataBinding) {
            super(viewDataBinding.getRoot());
            this.db = viewDataBinding;
        }
    }
}

