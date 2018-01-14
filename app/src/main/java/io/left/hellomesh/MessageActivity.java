package io.left.hellomesh;

import android.content.Context;
import android.graphics.Color;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.app.Activity;
import android.os.Handler;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.github.bassaer.chatmessageview.view.*;
import com.github.bassaer.chatmessageview.model.Message;

import io.left.rightmesh.android.AndroidMeshManager;
import io.left.rightmesh.id.MeshID;
import io.left.rightmesh.mesh.MeshManager;
import io.left.rightmesh.mesh.MeshStateListener;
import io.left.rightmesh.util.MeshUtility;
import io.left.rightmesh.util.RightMeshException;
import io.reactivex.functions.Consumer;

import static io.left.rightmesh.mesh.MeshManager.DATA_RECEIVED;


public class MessageActivity extends Activity implements MeshStateListener {
    private ChatView mChatView;
    AndroidMeshManager mm = null;

    private static final int HELLO_PORT = 9876;
    private MeshID uuid = null;
    private String username = null;
    private String myName = null;
    private String receivedMessageText = null;
    private User you;
    String data;

    DataHolder dh;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);

       loadActivity();
    }


    public void sendOne(View v, String message, MeshID recpMshId) throws RightMeshException {
        String msg = "Hello to: " + username + "\n Message: " + message + "\nfrom " + myName;
        MeshUtility.Log(this.getClass().getCanonicalName(), "MSG: " + msg);
        byte[] testData = msg.getBytes();
        mm.sendDataReliable(recpMshId, HELLO_PORT, testData);

    }


    private void handleDataReceived(MeshManager.RightMeshEvent e) {
        final MeshManager.DataReceivedEvent event = (MeshManager.DataReceivedEvent) e;

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                // Toast data contents.
                receivedMessageText = new String(event.data);
                Toast.makeText(MessageActivity.this, receivedMessageText, Toast.LENGTH_LONG).show();

                // Play a notification.
                Uri notification = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
                Ringtone r = RingtoneManager.getRingtone(MessageActivity.this, notification);
                r.play();

            }
        });
    }

    @Override
    public void meshStateChanged(MeshID meshID, int state) {
        if (state == MeshStateListener.SUCCESS) {
            try {
                // Binds this app to MESH_PORT.
                // This app will now receive all events generated on that port.
                mm.bind(HELLO_PORT);

                // Subscribes handlers to receive events from the mesh.
                mm.on(DATA_RECEIVED, new Consumer() {
                    @Override
                    public void accept(Object o) throws Exception {
                        handleDataReceived((MeshManager.RightMeshEvent) o);
                    }
                });
            } catch (RightMeshException e) {
                String status = "Error initializing the library" + e.toString();
                Toast.makeText(getApplicationContext(), status, Toast.LENGTH_SHORT).show();
                TextView txtStatus = (TextView) findViewById(R.id.txtStatus);
                txtStatus.setText(status);
                return;
            }
        }

    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {


    }

    private void loadActivity() {
        // Do all of your work here
        mm = AndroidMeshManager.getInstance(MessageActivity.this, MessageActivity.this);

        username = this.getIntent().getExtras().getString("title"); //
        uuid = (MeshID) this.getIntent().getExtras().get("uuid"); //
        myName = this.getIntent().getExtras().getString("myName");

        int myId = 0;

        int yourId = 1;
        String yourName = username;

        final User me = new User(myId, myName);
        you = new User(yourId, yourName);

        mChatView = (ChatView)findViewById(R.id.chat_view);

        DataHolder.getInstance().setData(mChatView);
//        DataHolder.getInstance().setUserData(you);
        //Set UI parameters if you need
        mChatView.setAutoScroll(true);
        mChatView.setRightBubbleColor(ContextCompat.getColor(this, R.color.green500));
        mChatView.setLeftBubbleColor(Color.WHITE);
        mChatView.setBackgroundColor(ContextCompat.getColor(this, R.color.blueGray500));
        mChatView.setSendButtonColor(ContextCompat.getColor(this, R.color.cyan900));
        mChatView.setSendIcon(R.drawable.ic_action_send);
        mChatView.setRightMessageTextColor(Color.WHITE);
        mChatView.setLeftMessageTextColor(Color.BLACK);
        mChatView.setUsernameTextColor(Color.WHITE);
        mChatView.setSendTimeTextColor(Color.WHITE);
        mChatView.setDateSeparatorColor(Color.WHITE);
        mChatView.setInputTextHint("new message...");
        mChatView.setMessageMarginTop(5);
        mChatView.setMessageMarginBottom(5);

        //Click Send Button
        mChatView.setOnClickSendButtonListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //new message

                Message message = new Message.Builder()
                        .setUser(me)
                        .setRightMessage(true)
                        .setMessageText(mChatView.getInputText())
                        .build();
                //Set to chat view
                mChatView.send(message);
                //Reset edit text
                mChatView.setInputText("");

                try {
                    sendOne(view, message.getMessageText(), uuid);
                } catch (RightMeshException e) {
                    e.printStackTrace();
                }


//                //Receive message
//                final Message receivedMessage = new Message.Builder()
//                        .setUser(you)
//                        .setRightMessage(false)
//                        .setMessageText(receivedMessageText)
//                        .build();
//
//                // This is a demo bot
//                // Return within 3 seconds
//                int sendDelay = (new Random().nextInt(4) + 1) * 1000;
//                new Handler().postDelayed(new Runnable() {
//                    @Override
//                    public void run() {
//                        mChatView.receive(receivedMessage);
//                    }
//                }, sendDelay);
            }
        });

        setTitle(username);

    }
}
