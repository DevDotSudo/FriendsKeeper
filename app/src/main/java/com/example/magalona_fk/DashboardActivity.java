package com.example.magalona_fk;

import android.app.AlertDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.magalona_friendskeeper.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import java.util.List;

public class DashboardActivity extends AppCompatActivity {
    private AppDatabase db;
    private RecyclerView recyclerView;
    private FriendAdapter adapter;
    private TextView tvFriendCount;
    private TextView tvUserName;
    private int userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        db = AppDatabase.getInstance(this);
        SharedPreferences pref = getSharedPreferences("UserPrefs", MODE_PRIVATE);
        userId = pref.getInt("userId", -1);
        String fullName = pref.getString("fullName", "Friend");

        if (userId == -1) {
            startActivity(new Intent(this, LoginActivity.class));
            finish();
            return;
        }

        tvFriendCount = findViewById(R.id.tvFriendCount);
        tvUserName = findViewById(R.id.tvUserName);
        tvUserName.setText(fullName + "!");

        recyclerView = findViewById(R.id.recyclerViewRecentFriends);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        FloatingActionButton fab = findViewById(R.id.fabAddFriend);
        fab.setOnClickListener(v -> {
            startActivity(new Intent(DashboardActivity.this, AddFriendActivity.class));
        });

        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setOnMenuItemClickListener(item -> {
            int itemId = item.getItemId();
            if (itemId == R.id.action_logout) {
                // Clear preferences on logout
                getSharedPreferences("UserPrefs", MODE_PRIVATE).edit().clear().apply();
                startActivity(new Intent(DashboardActivity.this, LoginActivity.class));
                finish();
                return true;
            } else if (itemId == R.id.action_all_friends) {
                startActivity(new Intent(DashboardActivity.this, FriendsListActivity.class));
                return true;
            }
            return false;
        });

        updateUI();
    }

    @Override
    protected void onResume() {
        super.onResume();
        updateUI();
    }

    private void updateUI() {
        List<Friend> friends = db.friendDao().getFriendsByUser(userId);
        tvFriendCount.setText(friends.size() + " friends");
        adapter = new FriendAdapter(friends);
        adapter.setOnFriendActionListener(new FriendAdapter.OnFriendActionListener() {
            @Override
            public void onEdit(Friend friend) {
                Intent intent = new Intent(DashboardActivity.this, EditFriendActivity.class);
                intent.putExtra("friendId", friend.getId());
                startActivity(intent);
            }

            @Override
            public void onDelete(Friend friend) {
                new AlertDialog.Builder(DashboardActivity.this)
                        .setTitle("Delete Friend")
                        .setMessage("Are you sure you want to delete " + friend.getName() + "?")
                        .setPositiveButton("Yes", (dialog, which) -> {
                            db.friendDao().delete(friend);
                            updateUI();
                            Toast.makeText(DashboardActivity.this, "Friend deleted", Toast.LENGTH_SHORT).show();
                        })
                        .setNegativeButton("No", null)
                        .show();
            }

            @Override
            public void onClick(Friend friend) {
                Intent intent = new Intent(DashboardActivity.this, FriendInfoActivity.class);
                intent.putExtra("friendId", friend.getId());
                startActivity(intent);
            }
        });
        recyclerView.setAdapter(adapter);
    }
}
