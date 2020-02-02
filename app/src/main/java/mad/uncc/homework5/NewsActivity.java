package mad.uncc.homework5;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import org.apache.commons.io.IOUtil;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

public class NewsActivity extends AppCompatActivity {
    ListView news;
    private ProgressDialog pb;
    ArrayList<News> newsItems = new ArrayList<>();
    ListView lv;
    static String webURL;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news);

        lv = findViewById(R.id.listView);
        news = findViewById(R.id.listView);
        pb = new ProgressDialog(NewsActivity.this);
        pb = ProgressDialog.show(NewsActivity.this,"","Loading Sources...",true);
        String url = null;
        if(getIntent()!=null && getIntent().getExtras()!=null){
        Source recieve = (Source) getIntent().getSerializableExtra("NEWS_ID");
        setTitle(recieve.name);
        url = "https://newsapi.org/v2/top-headlines?sources=" + recieve.id + "&apiKey="+MainActivity.APIKEY;
            if(isConnected()){
                new GetNewsData().execute(url);
            }
            else{
                Toast.makeText(this, "No Internet Connectivity", Toast.LENGTH_SHORT).show();
            pb.dismiss();}
        }
        else
            Toast.makeText(NewsActivity.this, "Intent Not Found", Toast.LENGTH_SHORT).show();
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


    public class GetNewsData extends AsyncTask<String,Void,ArrayList<News>>{

        @Override
        protected ArrayList<News> doInBackground(String... strings) {

            HttpURLConnection con = null;
            ArrayList<News> result = new ArrayList<>();
            try {
                URL url = new URL(strings[0]);
                con = (HttpURLConnection)url.openConnection();
                con.connect();
                if(con.getResponseCode() == HttpURLConnection.HTTP_OK){
                    String json = IOUtil.toString(con.getInputStream(),"UTF-8");
                    JSONObject root = new JSONObject(json);
                    JSONArray newsArray = root.getJSONArray("articles");

                    for(int i=0;i< newsArray.length();i++) {
                        JSONObject a = newsArray.getJSONObject(i);
                        News news1 = new News(a.getString("author"),a.getString("title"),a.getString("url")
                        ,a.getString("urlToImage"),a.getString("publishedAt"));
                        result.add(news1);
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
        protected void onPostExecute(ArrayList<News> news) {
            newsItems = news;
            NewsAdapter adapter = new NewsAdapter(NewsActivity.this,R.layout.news_item, newsItems);
            lv.setAdapter(adapter);
            super.onPostExecute(news);
            pb.dismiss();
        }
    }
}
