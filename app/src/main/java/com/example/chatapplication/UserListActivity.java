package com.example.chatapplication;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.Query;
import com.firebase.client.ValueEventListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;

public class UserListActivity extends AppCompatActivity {

    ListView listView;
    ArrayList<ListViewDetails> storeUsers = new ArrayList<ListViewDetails>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_list);

        Firebase.setAndroidContext(this);
        setTitle(User.name + "'s" + " Messages");

        listView = findViewById(R.id.listView);

        final ProgressDialog progressDialog = new ProgressDialog(UserListActivity.this);
        progressDialog.setMessage("Loading...");
        progressDialog.show();

        //Now, populate the listview
        listView.setVisibility(View.VISIBLE);
        final ListAdapter adapter = new ListAdapter(this, R.layout.custom_list_view, storeUsers);
        listView.setAdapter(adapter);

        adapter.notifyDataSetChanged();

        RequestQueue queue = Volley.newRequestQueue(this);
        String url = "https://chat-application-55873.firebaseio.com/users.json";
        //Retrieves each users full name, username and last message to populate list
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);

                            //Returns iterator of the String names in object
                            Iterator i = jsonObject.keys();
                            String username = "";
                            String fullName = "";
                            String lastMessage = "";

                            while (i.hasNext()) {

                                username = i.next().toString();
                                fullName = jsonObject.getJSONObject(username).getString("name").toString();
                                lastMessage = jsonObject.getJSONObject(username).getString("lastMessage").toString();

                                //For all the users excluding self
                                if (!username.equals(User.user)) {
                                    Log.i("Key", username);
                                    Log.i("Value", fullName);
                                    Log.i("Message", lastMessage);

                                    ListViewDetails details = new ListViewDetails(username, fullName, lastMessage);
                                    storeUsers.add(details);
                                }
                                adapter.notifyDataSetChanged();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.i("VolleyError", "" + error);

            }
        });

        queue.add(stringRequest);
        progressDialog.dismiss();

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                User.to = storeUsers.get(position).getContactUsername();
                Intent intent = new Intent(UserListActivity.this, ChatActivity.class);
                startActivity(intent);
            }
        });
    }


}
