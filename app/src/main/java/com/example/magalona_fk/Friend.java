package com.example.magalona_fk;

import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;
import androidx.room.PrimaryKey;

@Entity(tableName = "friends",
        foreignKeys = @ForeignKey(entity = User.class,
                parentColumns = "id",
                childColumns = "userId",
                onDelete = ForeignKey.CASCADE),
        indices = {@Index("userId")})
public class Friend {
    @PrimaryKey(autoGenerate = true)
    private int id;
    private int userId; // Link to the user who added this friend
    private String name;
    private String email;
    private String phone;
    private String birthday;
    private String status;
    private byte[] image;

    public Friend() {}

    public Friend(int userId, String name, String email, String phone, String birthday, String status, byte[] image) {
        this.userId = userId;
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.birthday = birthday;
        this.status = status;
        this.image = image;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public int getUserId() { return userId; }
    public void setUserId(int userId) { this.userId = userId; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }

    public String getBirthday() { return birthday; }
    public void setBirthday(String birthday) { this.birthday = birthday; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public byte[] getImage() { return image; }
    public void setImage(byte[] image) { this.image = image; }
}
