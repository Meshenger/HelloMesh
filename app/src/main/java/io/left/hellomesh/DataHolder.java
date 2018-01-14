package io.left.hellomesh;

import com.github.bassaer.chatmessageview.view.ChatView;

/**
 * Created by davidjulien on 2018-01-14.
 */

public class DataHolder {
    private ChatView data;
    private User u;
    public ChatView getData() {return data;}
    public void setData(ChatView data) {this.data = data;}


    public User getUserData() {return u;}
    public void setUserData(User data) {this.u = data;}

    private static final DataHolder holder = new DataHolder();
    public static DataHolder getInstance() {return holder;}
}
