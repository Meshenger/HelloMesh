package io.left.hellomesh;


import android.graphics.Bitmap;

import com.github.bassaer.chatmessageview.model.IChatUser;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class User implements IChatUser {
    Integer id;
    String name;

    public User(int id, String name) {
        this.id = id;
        this.name = name;
    }

    @NotNull
    @Override
    public String getId() {
        return this.id.toString();
    }

    @Nullable
    @Override
    public String getName() {
        return this.name;
    }

    @Nullable
    @Override
    public Bitmap getIcon() {
        return null;
    }

    @Override
    public void setIcon(Bitmap bitmap) {

    }
}
