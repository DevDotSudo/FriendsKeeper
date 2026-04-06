package com.example.magalona_fk;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.magalona_friendskeeper.R;
import java.util.List;

public class FriendAdapter extends RecyclerView.Adapter<FriendAdapter.FriendViewHolder> {
    private List<Friend> friendList;
    private List<Friend> friendListFull;
    private OnFriendActionListener listener;

    public interface OnFriendActionListener {
        void onEdit(Friend friend);
        void onDelete(Friend friend);

        void onClick(Friend friend);
    }

    public FriendAdapter(List<Friend> friendList) {
        this.friendList = friendList;
        this.friendListFull = new java.util.ArrayList<>(friendList);
    }

    public void filter(String text) {
        friendList.clear();
        if (text.isEmpty()) {
            friendList.addAll(friendListFull);
        } else {
            text = text.toLowerCase();
            for (Friend friend : friendListFull) {
                if (friend.getName().toLowerCase().contains(text) || 
                    friend.getEmail().toLowerCase().contains(text)) {
                    friendList.add(friend);
                }
            }
        }
        notifyDataSetChanged();
    }

    public void setOnFriendActionListener(OnFriendActionListener listener) {
        this.listener = listener;
    }

    @NonNull
    @Override
    public FriendViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_friends, parent, false);
        return new FriendViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FriendViewHolder holder, int position) {
        Friend friend = friendList.get(position);
        holder.tvName.setText(friend.getName());
        holder.tvStatus.setText(friend.getStatus());

        if (friend.getImage() != null && friend.getImage().length > 0) {
            Bitmap bitmap = BitmapFactory.decodeByteArray(friend.getImage(), 0, friend.getImage().length);
            holder.ivAvatar.setImageBitmap(bitmap);
        } else {
            holder.ivAvatar.setImageResource(R.drawable.ic_default_avatar);
        }

        holder.itemView.setOnClickListener( v -> {
            if (listener != null) listener.onClick(friend);
        });
    }

    @Override
    public int getItemCount() {
        return friendList.size();
    }

    public static class FriendViewHolder extends RecyclerView.ViewHolder {
        TextView tvName, tvStatus;
        ImageView ivAvatar;

        public FriendViewHolder(@NonNull View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.tvName);
            tvStatus = itemView.findViewById(R.id.tvStatus);
            ivAvatar = itemView.findViewById(R.id.ivFriendItemAvatar);
        }
    }
}
