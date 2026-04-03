package com.example.magalona_fk;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import com.example.magalona_friendskeeper.R;

public class FriendInfoActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friend_info);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        toolbar.setNavigationOnClickListener(v -> finish());

        TextView tvName = findViewById(R.id.tvFriendName);
        TextView tvEmail = findViewById(R.id.tvFriendEmail);
        TextView tvPhone = findViewById(R.id.tvFriendPhone);
        TextView tvBirthday = findViewById(R.id.tvFriendBirthday);
        ImageView ivAvatar = findViewById(R.id.ivFriendAvatar);
        AppDatabase db = AppDatabase.getInstance(this);

        int friendId = getIntent().getIntExtra("friendId", -1);
        if (friendId != -1) {
            Friend friend = db.friendDao().getFriendById(friendId);
            if (friend != null) {
                tvName.setText(friend.getName());
                tvEmail.setText(friend.getEmail());
                tvPhone.setText(friend.getPhone());
                tvBirthday.setText("Birthday: " + friend.getBirthday());
                if (friend.getImage() != null) {
                    android.graphics.Bitmap bitmap = android.graphics.BitmapFactory.decodeByteArray(friend.getImage(), 0, friend.getImage().length);
                    ivAvatar.setImageBitmap(bitmap);
                }

                findViewById(R.id.btnEditFriend).setOnClickListener(v -> {
                    android.content.Intent intent = new android.content.Intent(this, EditFriendActivity.class);
                    intent.putExtra("friendId", friend.getId());
                    startActivity(intent);
                    finish();
                });

                findViewById(R.id.btnDeleteFriend).setOnClickListener(v -> {
                    new android.app.AlertDialog.Builder(this)
                            .setTitle("Delete Friend")
                            .setMessage("Are you sure you want to delete " + friend.getName() + "?")
                            .setPositiveButton("Yes", (dialog, which) -> {
                                db.friendDao().delete(friend);
                                finish();
                            })
                            .setNegativeButton("No", null)
                            .show();
                });
            }
        }
    }
}
