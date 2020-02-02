package mad.uncc.homework5;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class NewsAdapter extends ArrayAdapter<News> {
    public NewsAdapter(@NonNull Context context, int resource, @NonNull List<News> objects) {
        super(context, resource, objects);

    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            final News news = getItem(position);
            ViewHolder viewHolder = new ViewHolder();

            if(convertView == null){
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.news_item,parent,false);
            viewHolder.author = convertView.findViewById(R.id.author);
            viewHolder.title = convertView.findViewById(R.id.title);
            viewHolder.date = convertView.findViewById(R.id.date);
            viewHolder.iv = convertView.findViewById(R.id.image);
                convertView.setTag(viewHolder);
            }
            else{
                viewHolder = (ViewHolder) convertView.getTag();
            }
        if(news.title == "null"){
            viewHolder.title.setVisibility(View.INVISIBLE); }
        else{
            viewHolder.title.setText(news.title);
            viewHolder.title.setVisibility(View.VISIBLE);}

        if(news.author == "null"){
            viewHolder.author.setVisibility(View.INVISIBLE); }
        else{
            viewHolder.author.setText(news.author);
            viewHolder.author.setVisibility(View.VISIBLE); }

        if(news.publishedAt == "null"){
            viewHolder.date.setVisibility(View.INVISIBLE); }
        else{
            viewHolder.date.setText(news.publishedAt);
            viewHolder.date.setVisibility(View.VISIBLE);}

        if(news.imageUrl == "null"){
            viewHolder.iv.setVisibility(View.INVISIBLE); }
        else{
            Picasso.get().load(news.imageUrl).error(R.drawable.placeholder).into(viewHolder.iv);
            viewHolder.iv.setVisibility(View.VISIBLE);}

            convertView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(getContext(),WebViewActivity.class);
                    intent.putExtra("URL",news.url);
                    getContext().startActivity(intent);
                }
            });

        return convertView;
    }


    static class ViewHolder{
        ImageView iv;
        TextView title;
        TextView author;
        TextView date;
    }

}
