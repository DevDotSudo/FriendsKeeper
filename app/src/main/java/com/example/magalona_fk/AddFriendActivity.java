package com.example.magalona_fk;

import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import com.example.magalona_friendskeeper.R;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;

public class AddFriendActivity extends AppCompatActivity {
    private AppDatabase db;
    private ImageView ivAvatar;
    private boolean imageSelected = false;

    private final ActivityResultLauncher<String> getContent = registerForActivityResult(
            new ActivityResultContracts.GetContent(),
            uri -> {
                if (uri != null) {
                    ivAvatar.setImageURI(uri);
                    imageSelected = true;
                }
            }
    );

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_friend);

        db = AppDatabase.getInstance(this);
        SharedPreferences pref = getSharedPreferences("UserPrefs", MODE_PRIVATE);
        int userId = pref.getInt("userId", -1);

        if (userId == -1) {
            finish();
            return;
        }

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle("Add New Friend");
        }
        toolbar.setNavigationOnClickListener(v -> finish());

        ivAvatar = findViewById(R.id.ivAvatar);
        TextView tvAddPhoto = findViewById(R.id.tvAddPhoto);

        ivAvatar.setOnClickListener(v -> getContent.launch("image/*"));
        tvAddPhoto.setOnClickListener(v -> getContent.launch("image/*"));

        TextInputEditText etName = findViewById(R.id.etFriendName);
        TextInputEditText etEmail = findViewById(R.id.etFriendEmail);
        TextInputEditText etPhone = findViewById(R.id.etFriendPhone);
        TextInputEditText etBirthday = findViewById(R.id.etFriendBirthday);
        MaterialButton btnSave = findViewById(R.id.btnSaveFriend);

        btnSave.setOnClickListener(v -> {
            String name = etName.getText().toString();
            String email = etEmail.getText().toString();
            String phone = etPhone.getText().toString();
            String birthday = etBirthday.getText().toString();

            if (name.isEmpty() || email.isEmpty()) {
                Toast.makeText(this, "Please fill at least name and email", Toast.LENGTH_SHORT).show();
            } else {
                byte[] imageBytes = null;
                if (imageSelected) {
                    imageBytes = convertImageViewToByteArray(ivAvatar);
                }

                Friend friend = new Friend(userId, name, email, phone, birthday, "New Friend", imageBytes);
                db.friendDao().insert(friend);
                Toast.makeText(this, "Friend added successfully", Toast.LENGTH_SHORT).show();
                finish();
            }
        });
    }

    private byte[] convertImageViewToByteArray(ImageView imageView) {
        try {
            Bitmap bitmap = ((BitmapDrawable) imageView.getDrawable()).getBitmap();
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 50, stream);
            return stream.toByteArray();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
