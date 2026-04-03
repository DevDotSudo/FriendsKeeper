package com.example.magalona_fk;

import android.app.AlertDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.SearchView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.magalona_friendskeeper.R;
import java.util.List;

public class FriendsListActivity extends AppCompatActivity {
    private AppDatabase db;
    private RecyclerView recyclerView;
    private FriendAdapter adapter;
    private int userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friends_list);

        db = AppDatabase.getInstance(this);
        SharedPreferences pref = getSharedPreferences("UserPrefs", MODE_PRIVATE);
        userId = pref.getInt("userId", -1);

        if (userId == -1) {
            finish();
            return;
        }

        recyclerView = findViewById(R.id.recyclerViewFriends);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle("All Friends");
        }
        toolbar.setNavigationOnClickListener(v -> finish());

        SearchView searchView = findViewById(R.id.searchView);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if (adapter != null) {
                    adapter.filter(newText);
                }
                return true;
            }
        });

        refreshList();
    }

    private void refreshList() {
        List<Friend> friends = db.friendDao().getFriendsByUser(userId);
        adapter = new FriendAdapter(friends);
        adapter.setOnFriendActionListener(new FriendAdapter.OnFriendActionListener() {
            @Override
            public void onEdit(Friend friend) {
                Intent intent = new Intent(FriendsListActivity.this, EditFriendActivity.class);
                intent.putExtra("friendId", friend.getId());
                startActivity(intent);
            }

            @Override
            public void onDelete(Friend friend) {
                new AlertDialog.Builder(FriendsListActivity.this)
                        .setTitle("Delete Friend")
                        .setMessage("Are you sure you want to delete " + friend.getName() + "?")
                        .setPositiveButton("Yes", (dialog, which) -> {
                            db.friendDao().delete(friend);
                            refreshList();
                            Toast.makeText(FriendsListActivity.this, "Friend deleted", Toast.LENGTH_SHORT).show();
                        })
                        .setNegativeButton("No", null)
                        .show();
            }
        });
        recyclerView.setAdapter(adapter);
    }
}
