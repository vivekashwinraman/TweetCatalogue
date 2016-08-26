package com.tweetcatalogue.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.tweetcatalogue.R;
import com.tweetcatalogue.modelobjects.TweetObject;
import com.tweetcatalogue.uicontrols.CustomDialog;

import java.util.List;

/**
 * Created by vraman on 8/22/16.
 */
public class TweetAdapter extends RecyclerView.Adapter<TweetAdapter.ViewHolder> {
    private List<TweetObject> tweetObjectList;
    private Context context;

    public TweetAdapter(List<TweetObject> tweetObjectList, Context context) {
        this.tweetObjectList = tweetObjectList;
        this.context = context;
    }

    @Override
    public TweetAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                      int viewType) {
        View itemLayoutView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.tweet_item, null);

        ViewHolder viewHolder = new ViewHolder(itemLayoutView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int position) {
        TweetObject tweetObject = tweetObjectList.get(position);
        if (tweetObject != null) {
            viewHolder.tweetText.setText(tweetObject.getStatusText());
            viewHolder.userName.setText(tweetObject.getUserName());
            viewHolder.profileImageView.setTag(tweetObject.getMiniProfileImageURL());
            viewHolder.userURL.setText(tweetObject.getUserUrl());
            Picasso.with(context).load(tweetObject.getMiniProfileImageURL()).into(viewHolder.profileImageView);
        }
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public TextView userName;
        public TextView tweetText;
        public TextView userURL;
        public ImageView profileImageView;


        public ViewHolder(View itemLayoutView) {
            super(itemLayoutView);
            tweetText = (TextView) itemLayoutView.findViewById(R.id.tweetText);
            userName = (TextView) itemLayoutView.findViewById(R.id.tweetUsername);
            profileImageView = (ImageView) itemLayoutView.findViewById(R.id.profileImage);
            userURL = (TextView)itemLayoutView.findViewById(R.id.userUrl);
            itemLayoutView.setOnClickListener(this);

        }

        @Override
        public void onClick(View view) {
            final CustomDialog customDialog = new CustomDialog(context, R.layout.dialog_tweet_detail, context.getString(R.string.app_name));
            TextView userNameText = (TextView)customDialog.findViewById(R.id.tweetUsername);
            TextView tweetStatus = (TextView)customDialog.findViewById(R.id.tweetText);
            ImageView profileImage = (ImageView)customDialog.findViewById(R.id.profileImage);
            String userUrl = "<a href="+userURL.getText()+">";
            userNameText.setText(Html.fromHtml(userUrl + userName.getText().toString()));
            userNameText.setMovementMethod(LinkMovementMethod.getInstance());
            tweetStatus.setText(tweetText.getText());
            Picasso.with(context).load((String) profileImageView.getTag()).into(profileImage);
            Button closeButton = (Button) customDialog.findViewById(R.id.close);
            closeButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    customDialog.dismiss();
                }
            });
            customDialog.show();
        }
    }

    @Override
    public int getItemCount() {
        return tweetObjectList.size();
    }
}