package com.example.chatapplication;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.firebase.client.ChildEventListener;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;

import java.util.HashMap;
import java.util.Map;

public class ChatActivity extends AppCompatActivity {

    LinearLayout chatLayout;
    ScrollView scrollView;
    Button sendBtn;
    EditText textField;
    Firebase referenceUser, referenceChatWith, referenceMsg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        scrollView = findViewById(R.id.scrollView);
        chatLayout = findViewById(R.id.linearChatLayout);
        sendBtn = findViewById(R.id.sendBtn);
        textField = findViewById(R.id.textInput);

        setTitle(User.to);

        Firebase.setAndroidContext(this);
        //reference to create json keys in firebase
        //Keeps track of the chat for both users
        referenceUser = new Firebase("https://chat-application-55873.firebaseio.com/messages/"+User.user+"_"+User.to);
        referenceChatWith = new Firebase("https://chat-application-55873.firebaseio.com/messages/"+User.to+"_"+User.user);
        referenceMsg = new Firebase("https://chat-application-55873.firebaseio.com/users");

        //Click listener for send button
        sendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               String message = textField.getText().toString();
               if(!message.equals("")){
                   //Firebase reference can push Maps among other data structures
                    Map<String, String> chatHist = new HashMap<String, String>();

                    //Creates objects called messages and user
                    chatHist.put("message", message);
                    chatHist.put("user", User.user);

                    // pushes with values with unique reference ids
                    referenceUser.push().setValue(chatHist);
                    referenceChatWith.push().setValue(chatHist);

                    //Keep track of the last users
                    referenceMsg.child(User.user).child("lastMessage").setValue("You:"+message);
                    referenceMsg.child(User.to).child("lastMessage").setValue(User.user+":"+message);

               }
               textField.setText("");
            }
        });

        //Receive events about changes in the child locations - listener to get chat data
        referenceUser.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                //DataSnapshot contains data from Firebase. Anytime data is read,
                //it will arrive as a DataSnapshot
                Map map = dataSnapshot.getValue(Map.class);
                //Save name for reference to be used later
                String message = map.get("message").toString();
                String userName = map.get("user").toString();

                //Users messages will be type 1
                if(userName.equals(User.user)){
                    addMessageBox("You:\n" + message, 1);
                }
                else{
                    addMessageBox(User.to + ":\n" + message, 2);
                }
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) { }
            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) { }
            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) { }
            @Override
            public void onCancelled(FirebaseError firebaseError) { }
        });
    }



    public void addMessageBox(String message, int type){
        //Create a new textview to imitate a chat bubble
        TextView textView = new TextView(ChatActivity.this);
        textView.setText(message);

        //Child layout information
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        layoutParams.setMargins(5, 10, 5, 10);
        //Spacing of the textview bubble
        textView.setLayoutParams(layoutParams);

        //bubble texture for textviews
        if(type == 1) {
            textView.setBackgroundResource(R.drawable.bubble_in);
            layoutParams.gravity = Gravity.RIGHT;

        }
        else{
            textView.setBackgroundResource(R.drawable.bubble_out);
            layoutParams.gravity = Gravity.LEFT;

        }

        //Add the text bubbles to the linear layout
        chatLayout.addView(textView);
        //Force scroll to go down
        scrollView.fullScroll(View.FOCUS_DOWN);
    }
}
