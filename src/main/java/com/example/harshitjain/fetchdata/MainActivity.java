package com.example.harshitjain.fetchdata;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

public class MainActivity extends AppCompatActivity implements View.OnClickListener,GoogleApiClient.OnConnectionFailedListener{

    private static String url = "http://www.jainharshit.16mb.com/detailsjson.json";
    String TAG = MainActivity.class.getSimpleName();
    private ProgressDialog pDialog;
    private SignInButton login;
    ListView lv;
    EditText search;
    //TextView t1;
    String nm;
    ArrayList<HashMap<String, String>> details;
    String msg="bh";
    int ln=0;

    private GoogleApiClient gac;
    private  static  final int req=9001;
    ListAdapter adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        details = new ArrayList<>();
        lv = (ListView) findViewById(R.id.listView);
        search=(EditText)findViewById(R.id.editText);
        lv.setVisibility(View.GONE);
        login=(SignInButton)findViewById(R.id.login);
        search.setVisibility(View.GONE);
        login.setOnClickListener(this);
        GoogleSignInOptions signInOptions= new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestEmail().build();
        gac =new GoogleApiClient.Builder(this).enableAutoManage(this,this).addApi(Auth.GOOGLE_SIGN_IN_API,signInOptions).build();
        new GetDetails().execute();
        search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
              /*  HashMap<String, String> dt = new HashMap<String, String>();
                ArrayList<HashMap<String, String>> tempArr=new ArrayList<HashMap<String, String>>(details);

                for(int i=0;i<100;i++){
                    dt=tempArr.get(i);


                    String gs=dt.get("id");
                    //Toast.makeText(MainActivity.this,gs,Toast.LENGTH_LONG).show();
                    if(gs.equals(s)){
                        //Toast.makeText(MainActivity.this,s,Toast.LENGTH_LONG).show();
                    }
                    else
                    {
                        tempArr.remove(i);
                        tempArr.notifyAll();
                    }
                }*/

            }
        });
    }

    @Override
    public void onClick(View v) {
        Intent intent=Auth.GoogleSignInApi.getSignInIntent(gac);
        startActivityForResult(intent,req);
    }


    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    private void handleResult(GoogleSignInResult result){
        if(result.isSuccess())
        {
            GoogleSignInAccount account=result.getSignInAccount();
            nm=account.getDisplayName();
            //t1.setText("Welcome "+nm);
            updateUI(true);
        }
        else
            updateUI(false);
    }
    private void updateUI(boolean isLogin){
        if(isLogin)
        {
            lv.setVisibility(View.VISIBLE);
            //t1.setVisibility(View.VISIBLE);
            login.setVisibility(View.GONE);
            Toast.makeText(MainActivity.this,"Welcome "+nm, Toast.LENGTH_LONG).show();
            new GetDetails().execute();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==req)
        {
            GoogleSignInResult result=Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            //Toast.makeText(MainActivity.this,"Welcome "+nm, Toast.LENGTH_LONG).show();
            handleResult(result);
        }
    }
    private class GetDetails extends AsyncTask<Void, Void, Void> {


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // Showing progress dialog
            pDialog = new ProgressDialog(MainActivity.this);
            pDialog.setMessage("Please wait...");
            pDialog.setCancelable(false);
            pDialog.show();

        }

        @Override
        protected Void doInBackground(Void... params) {
            HttpHandler sh=new HttpHandler();
            // Making a request to url and getting response
            String jsonStr = sh.makeServiceCall(url);

           // Log.e(TAG, "Response from url: " + jsonStr);

            if (jsonStr != null) {
                try {

                    JSONObject jsonObj = new JSONObject(jsonStr);

                    // Getting JSON Array node
                   JSONArray dt =jsonObj.getJSONArray("details");
                        for (int i = 0; i < dt.length(); i++) {
                            ln++;
                            JSONObject c = dt.getJSONObject(i);

                            String uid = c.getString("userId");
                            String id = c.getString("id");
                            String title = c.getString("title");
                            String body = c.getString("body");

                            // tmp hash map for single contact
                            HashMap<String, String> contact = new HashMap<String, String>();

                            // adding each child node to HashMap key => value

                                contact.put("userId", uid);
                                contact.put("id", id);
                                contact.put("title", title);
                                contact.put("body", body);

                            // adding contact to contact list
                            details.add(contact);
                        }
                    } catch (final Exception e) {
                        Log.e(TAG, "Harshit1" + e.getMessage());
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(getApplicationContext(),
                                        "2Json parsing error: " + e.getMessage(),
                                        Toast.LENGTH_LONG)
                                        .show();


                            }
                        });

                    }
                }

            else {
                Log.e(TAG, "Couldn't get json from server.");
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getApplicationContext(),
                                "Couldn't get json from server. Check LogCat for possible errors!",
                                Toast.LENGTH_LONG)
                                .show();
                    }
                });

            }
            return null;
        }
        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            // Dismiss the progress dialog
            if (pDialog.isShowing())
                pDialog.dismiss();
            /**
             * Updating parsed JSON data into ListView
             * */
            //new Sorting();
            Collections.sort(details,new Sorting());
            adapter = new SimpleAdapter(
                    MainActivity.this, details,
                    R.layout.list_item,
                    new String[]{"userId", "id","title","body"},
                    new int[]{R.id.uid, R.id.id, R.id.title,R.id.body});

            lv.setAdapter(adapter);
           //Toast.makeText(getApplicationContext(), msg,Toast.LENGTH_LONG).show();
        }

    }

}