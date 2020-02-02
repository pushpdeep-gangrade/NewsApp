package mad.uncc.homework5;


/*

a. Assignment : InClass 06
b. File Name : MainActivity.java
c. Full name : Adwait Suryakant More, Pushpdeep Gangrade
d. Group No : 1 31

 */
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import org.apache.commons.io.IOUtil;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    ListView listView ;
    private ProgressDialog pb;
    final static String APIKEY = "83ab3d833945445481cd292a12e1f82f";
    ArrayList<Source> sourceObj = new ArrayList<>();
    ArrayList<String> sourceNameList = new ArrayList<String>();
    ArrayAdapter<String> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setTitle(R.string.title);

        listView = findViewById(R.id.sourcesList);
        pb = new ProgressDialog(MainActivity.this);
        pb = ProgressDialog.show(MainActivity.this,"","Loading Sources...",true);

        if(isConnected()) {
            String url = "https://newsapi.org/v2/sources?apiKey=" + APIKEY;
            new GetSources().execute(url);
        }
        else{
            Toast.makeText(this, "No Internet Connectivity", Toast.LENGTH_SHORT).show();
            pb.dismiss();
        }

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Source s = sourceObj.get(i);
                Intent intent = new Intent(MainActivity.this,NewsActivity.class);
                intent.putExtra("NEWS_ID",s);
                startActivity(intent);
            }
        });

    }

    public Boolean isConnected(){
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        if(networkInfo ==null || !networkInfo.isConnected() || (networkInfo.getType() !=ConnectivityManager.TYPE_WIFI
                && networkInfo.getType() !=  connectivityManager.TYPE_MOBILE)) {
            return false;
        }
        return true;
    }

    public class GetSources extends AsyncTask<String, Void, ArrayList<Source>>{

        @Override
        protected ArrayList<Source> doInBackground(String... strings) {

            HttpURLConnection con = null;
            ArrayList<Source> result = new ArrayList<>();
            try {
                URL url = new URL(strings[0]);
                con = (HttpURLConnection)url.openConnection();
                con.connect();
                if(con.getResponseCode() == HttpURLConnection.HTTP_OK){
                    String json = IOUtil.toString(con.getInputStream(),"UTF-8");
                    JSONObject root = new JSONObject(json);
                    JSONArray sourceArray = root.getJSONArray("sources");
                    for(int i=0;i< sourceArray.length();i++) {
                        JSONObject a = sourceArray.getJSONObject(i);
                        Source s1 = new Source(a.getString("id"),a.getString("name"));
                        sourceNameList.add(s1.name);
                        result.add(s1);
                    }
                }
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return result;
        }

        @Override
        protected void onPostExecute(ArrayList<Source> sources) {
            if(sources.size() > 0){
            sourceObj = sources;
            pb.dismiss();
            adapter = new ArrayAdapter<String>(MainActivity.this, android.R.layout.simple_list_item_1, android.R.id.text1, sourceNameList);
            listView.setAdapter(adapter);}
            else
                Toast.makeText(MainActivity.this, "No Source Found", Toast.LENGTH_SHORT).show();
        }
    }
}
