package com.example.magalona_fk;

import android.content.Intent;
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

public class EditFriendActivity extends AppCompatActivity {
    private AppDatabase db;
    private ImageView ivAvatar;
    private Friend friend;
    private boolean imageChanged = false;

    private final ActivityResultLauncher<String> getContent = registerForActivityResult(
            new ActivityResultContracts.GetContent(),
            uri -> {
                if (uri != null) {
                    ivAvatar.setImageURI(uri);
                    imageChanged = true;
                }
            }
    );

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_friend); // Reusing the layout since they are identical

        db = AppDatabase.getInstance(this);
        int friendId = getIntent().getIntExtra("friendId", -1);

        if (friendId == -1) {
            finish();
            return;
        }

        friend = db.friendDao().getFriendById(friendId);
        if (friend == null) {
            finish();
            return;
        }

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle("Edit Friend");
        }
        toolbar.setNavigationOnClickListener(v -> finish());

        ivAvatar = findViewById(R.id.ivAvatar);
        TextView tvAddPhoto = findViewById(R.id.tvAddPhoto);
        TextInputEditText etName = findViewById(R.id.etFriendName);
        TextInputEditText etEmail = findViewById(R.id.etFriendEmail);
        TextInputEditText etPhone = findViewById(R.id.etFriendPhone);
        TextInputEditText etBirthday = findViewById(R.id.etFriendBirthday);
        MaterialButton btnSave = findViewById(R.id.btnSaveFriend);

        // Pre-fill data
        etName.setText(friend.getName());
        etEmail.setText(friend.getEmail());
        etPhone.setText(friend.getPhone());
        etBirthday.setText(friend.getBirthday());
        btnSave.setText("Update Friend");

        if (friend.getImage() != null) {
            Bitmap bitmap = BitmapFactory.decodeByteArray(friend.getImage(), 0, friend.getImage().length);
            ivAvatar.setImageBitmap(bitmap);
        }

        ivAvatar.setOnClickListener(v -> getContent.launch("image/*"));
        tvAddPhoto.setOnClickListener(v -> getContent.launch("image/*"));

        btnSave.setOnClickListener(v -> {
            String name = etName.getText().toString();
            String email = etEmail.getText().toString();
            String phone = etPhone.getText().toString();
            String birthday = etBirthday.getText().toString();

            if (name.isEmpty() || email.isEmpty()) {
                Toast.makeText(this, "Please fill at least name and email", Toast.LENGTH_SHORT).show();
                return;
            }

            friend.setName(name);
            friend.setEmail(email);
            friend.setPhone(phone);
            friend.setBirthday(birthday);

            if (imageChanged) {
                friend.setImage(convertImageViewToByteArray(ivAvatar));
            }

            db.friendDao().update(friend);
            Toast.makeText(this, "Friend updated successfully", Toast.LENGTH_SHORT).show();
            finish();
        });
    }

    private byte[] convertImageViewToByteArray(ImageView imageView) {
        Bitmap bitmap = ((BitmapDrawable) imageView.getDrawable()).getBitmap();
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 50, stream);
        return stream.toByteArray();
    }
}
