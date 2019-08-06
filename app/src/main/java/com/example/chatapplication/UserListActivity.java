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

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;

public class UserListActivity extends AppCompatActivity {

    ListView listView;
    ArrayList<String> storeUsers = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_list);

        setTitle(User.name + " Messages");

        listView = findViewById(R.id.listView);

        final ProgressDialog progressDialog = new ProgressDialog(UserListActivity.this);
        progressDialog.setMessage("Loading...");
        progressDialog.show();

        //Now, populate the listview
        listView.setVisibility(View.VISIBLE);
        final ArrayAdapter adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, storeUsers);
        listView.setAdapter(adapter);

        RequestQueue queue = Volley.newRequestQueue(this);
        String url ="https://chat-application-55873.firebaseio.com/users.json";
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            //Returns iterator of the String names in object
                            Log.i("Response", response);
                            Iterator i = jsonObject.keys();
                            String key = "";

                            while(i.hasNext()) {
                                key = i.next().toString();

                                //For all the users excluding self
                                if (!key.equals(User.user)) {
                                    Log.i("Key", key);
                                    storeUsers.add(key);
                                    adapter.notifyDataSetChanged();
                                }
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
                User.to = storeUsers.get(position);
                Intent intent = new Intent(UserListActivity.this, ChatActivity.class);
                startActivity(intent);
            }
        });
    }
}
