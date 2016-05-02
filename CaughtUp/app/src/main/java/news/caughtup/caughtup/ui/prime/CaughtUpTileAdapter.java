package news.caughtup.caughtup.ui.prime;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import news.caughtup.caughtup.R;
import news.caughtup.caughtup.entities.Article;
import news.caughtup.caughtup.entities.ICaughtUpItem;
import news.caughtup.caughtup.entities.NewsSource;
import news.caughtup.caughtup.entities.Resource;
import news.caughtup.caughtup.entities.ResponseObject;
import news.caughtup.caughtup.entities.User;
import news.caughtup.caughtup.util.Constants;
import news.caughtup.caughtup.util.StringRetriever;
import news.caughtup.caughtup.ws.remote.Callback;
import news.caughtup.caughtup.ws.remote.FacebookManager;
import news.caughtup.caughtup.ws.remote.ISocialMediaManager;
import news.caughtup.caughtup.ws.remote.RestProxy;
import news.caughtup.caughtup.ws.remote.TwitterManager;

public class CaughtUpTileAdapter extends ArrayAdapter<ICaughtUpItem> {

    private Activity activity;
    private StringRetriever retriever;
    public CaughtUpTileAdapter(List<ICaughtUpItem> dataArray, Activity activity) {
        super(activity, android.R.layout.simple_list_item_1, dataArray);
        this.activity = activity;
        retriever = StringRetriever.getInstance();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ICaughtUpItem item = getItem(position);
        if(item instanceof Article) {
            return getArticleView(convertView, item);
        }
        if(item instanceof User) {
            return getUserView(convertView, item);
        }
        return getNewsSourceView(convertView, item);
    }

    @SuppressLint("InflateParams")
    private View getArticleView(View convertView, ICaughtUpItem item) {

        final Article article = (Article) item;
        // if we weren't given a view, inflate one
        if (convertView == null) {
            convertView = this.activity.getLayoutInflater().inflate(R.layout.tile_view, null);
        }

        // Set text
        TextView titleTextView = (TextView)convertView.findViewById(R.id.name_tile_view);
        String title = article.getTitle();
        titleTextView.setText(title.length() > 80 ? title.substring(0, 77) + "..." : title);

        // Set description
        TextView descriptionTextView = (TextView)convertView.findViewById(R.id.description_tile_view);
        String description = article.getSummary();
        String visibleText = (description.length() > 80) ? description.substring(0, 77) + "..." : description;
        descriptionTextView.setText(visibleText);

        // Set thumbnail
        ImageView thumbnailImageView = (ImageView) convertView.findViewById(R.id.thumbnail_tile_view);
        thumbnailImageView.setImageDrawable(activity.getResources().getDrawable(article.getThumbnailID(), null));

        // Load article in browser when tile is touched
        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent launchBrowser = new Intent(Intent.ACTION_VIEW, article.getArticleURI());
                activity.startActivity(launchBrowser);
            }
        });

        // Launch dialog to share the article when share button is clicked
        ImageButton shareButton = (ImageButton) convertView.findViewById(R.id.button_tile_view);
        if(shareButton != null) {
            shareButton.setImageDrawable(activity.getResources().getDrawable(R.drawable.share_icon, null));
            shareButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    launchShareDialog(article);
                }
            });
        }

        return convertView;
    }

    @SuppressLint("InflateParams")
    private View getUserView(View convertView, ICaughtUpItem item) {
        final User user = (User) item;
        // if we weren't given a view, inflate one
        if (convertView == null) {
            convertView = this.activity.getLayoutInflater().inflate(R.layout.tile_view, null);
        }

        // Set userName as title
        TextView titleTextView = (TextView) convertView.findViewById(R.id.name_tile_view);
        titleTextView.setText(user.getName());

        // Set user's aboutMe as description text (up to 80 characters)
        TextView descriptionTextView = (TextView) convertView.findViewById(R.id.description_tile_view);
        String aboutMeText = user.getAboutMe();
        String visibleText = (aboutMeText.length() > 80) ? aboutMeText.substring(0, 77) + "..." : aboutMeText;
        descriptionTextView.setText(visibleText);

        // Set user picture
        ImageView thumbnail = (ImageView) convertView.findViewById(R.id.thumbnail_tile_view);
        thumbnail.setImageDrawable(activity.getResources().getDrawable(user.getProfileImageId(), null));

        // Set onTouchListener to load User's public profile on touch
        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PublicProfileFragment publicProfile = new PublicProfileFragment();
                Bundle args = new Bundle();
                args.putString("username", user.getName());
                publicProfile.setArguments(args);
                HomeActivity.executeTransaction(publicProfile, user.getName(), user.getName());
            }
        });

        // Set up follow button
        ImageButton followButton = (ImageButton) convertView.findViewById(R.id.button_tile_view);
        User currentUser = HomeActivity.getCurrentUser();
        if(currentUser.isFollowing(user.getResourceId())) {
            followButton.setImageDrawable(activity.getResources().getDrawable(R.drawable.unfollow_icon, null));
            followButton.setTag(retriever.getStringById(Constants.UNFOLLOW_TAG)); // Meaning next click will unfollow
        } else {
            followButton.setImageDrawable(activity.getResources().getDrawable(R.drawable.add_icon, null));
            followButton.setTag(retriever.getStringById(Constants.FOLLOW_TAG)); // Meaning next click will follow
        }
        setFollowButtonListener(followButton, user);

        return convertView;
    }

    @SuppressLint("InflateParams")
    private View getNewsSourceView(View convertView, ICaughtUpItem item) {
        NewsSource newsSource = (NewsSource) item;
        // if we weren't given a view, inflate one
        if (convertView == null) {
            convertView = this.activity.getLayoutInflater().inflate(R.layout.tile_view, null);
        }

        // Set newsSource name as title
        TextView titleTextView = (TextView) convertView.findViewById(R.id.name_tile_view);
        titleTextView.setText(newsSource.getName());

        // Set newsSource description as description text (up to 80 characters)
        TextView descriptionTextView = (TextView) convertView.findViewById(R.id.description_tile_view);
        String description = newsSource.getDescription();
        String visibleText = (description.length() > 80) ? description.substring(0, 77) + "..." : description;
        descriptionTextView.setText(visibleText);

        // Set newsSource thumbnail
        ImageView thumbnail = (ImageView) convertView.findViewById(R.id.thumbnail_tile_view);
        thumbnail.setImageDrawable(activity.getResources().getDrawable(newsSource.getThumbnailId(), null));

        // Set onTouchListener to load NewsSource's site
        final Uri externalLink = newsSource.getBaseUrl();
        if(externalLink != null) {
            convertView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent launchBrowser = new Intent(Intent.ACTION_VIEW, externalLink);
                    activity.startActivity(launchBrowser);
                }
            });
        }

        // Set up follow button
        ImageButton followButton = (ImageButton) convertView.findViewById(R.id.button_tile_view);
        User currentUser = HomeActivity.getCurrentUser();
        if(currentUser.isFollowing(newsSource.getResourceId())) {
            followButton.setImageDrawable(activity.getResources().getDrawable(R.drawable.unfollow_icon, null));
            followButton.setTag(retriever.getStringById(Constants.UNFOLLOW_TAG)); // Meaning next click will unfollow
        } else {
            followButton.setImageDrawable(activity.getResources().getDrawable(R.drawable.add_icon, null));
            followButton.setTag(retriever.getStringById(Constants.FOLLOW_TAG)); // Meaning next click will follow
        }
        setFollowButtonListener(followButton, newsSource);

        return convertView;
    }

    private void launchShareDialog(final Article article) {
        final String[] items = { "Share with Followers", "Share on Facebook", "Share on Twitter", "Cancel"};
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setTitle("Share Article!");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                switch (items[item]) {
                    case "Share with Followers":
                        User user = HomeActivity.getCurrentUser();
                        Callback callback = getShareCallback(article);
                        String endpoint = String.format("/share?user_id=%d&article_id=%d", user.getUserId(),
                                article.getArticleId());
                        RestProxy.getProxy().postCall(endpoint, "", callback);
                        dialog.dismiss();
                        break;
                    case "Share on Facebook":
                        String fbMessage = String.format("\"%s\" is now shared on Facebook", article.getTitle());
                        ISocialMediaManager fbAccessManager = new FacebookManager();
                        fbAccessManager.share(fbMessage, article, activity);
                        dialog.dismiss();
                        break;
                    case "Share on Twitter":
                        String tweet = String.format("Checkout \"%s\"!", article.getTitle());
                        ISocialMediaManager twitterAccessManager = new TwitterManager();
                        twitterAccessManager.share(tweet, article, activity);
                        dialog.dismiss();
                        break;
                    case "Cancel":
                    default:
                        dialog.dismiss();
                        break;
                }
            }
        });
        builder.show();
    }

    private void setFollowButtonListener(final ImageButton followButton, final Resource resource) {
        followButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                User currentUser = HomeActivity.getCurrentUser();
                Callback callback = getFollowCallback(followButton, resource);
                RestProxy proxy = RestProxy.getProxy();
                if (followButton.getTag().equals(retriever.getStringById(Constants.FOLLOW_TAG))) {
                    proxy.postCall(String.format("/follow?user_id=%d&resource_id=%d", currentUser.getUserId(),
                            resource.getResourceId()), "", callback);
                } else {
                    proxy.deleteCall(String.format("/follow?user_id=%d&resource_id=%d", currentUser.getUserId(),
                            resource.getResourceId()), "", callback);
                }
            }
        });
    }

    private Callback getFollowCallback(final ImageButton followButton, final Resource resource) {
        return new Callback() {
            @Override
            public void process(ResponseObject responseObject) {
                if (responseObject.getResponseCode() == 200) {
                    User currentUser = HomeActivity.getCurrentUser();
                    String followTag = retriever.getStringById(Constants.FOLLOW_TAG);
                    String unfollowTag = retriever.getStringById(Constants.UNFOLLOW_TAG);
                    if (followButton.getTag().equals(followTag)) {
                        currentUser.follow(resource);
                        Toast.makeText(activity.getApplicationContext(),
                                String.format("Now following %s!", resource.getName()),
                                Toast.LENGTH_SHORT).show();
                        followButton.setImageDrawable(activity.getResources().getDrawable(R.drawable.unfollow_icon, null));
                        followButton.setTag(unfollowTag); // Meaning next click will cause it to unfollow
                    } else {
                        currentUser.unfollow(resource.getResourceId());
                        Toast.makeText(activity.getApplicationContext(),
                                String.format("No longer following %s.", resource.getName()),
                                Toast.LENGTH_SHORT).show();
                        followButton.setImageDrawable(activity.getResources().getDrawable(R.drawable.add_icon, null));
                        followButton.setTag(followTag); // Meaning next click will cause it to follow
                    }
                } else {
                    Toast.makeText(activity,
                            activity.getResources().getString(R.string.follow_server_error),
                            Toast.LENGTH_LONG).show();
                }
            }
        };
    }

    private Callback getShareCallback(final Article article) {
        return new Callback() {
            @Override
            public void process(ResponseObject responseObject) {
                if (responseObject.getResponseCode() == 200) {
                    String toastMsg = String.format("\"%s\" is now shared with your followers", article.getTitle());
                    Toast.makeText(activity, toastMsg, Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(activity,
                            activity.getResources().getString(R.string.follow_server_error),
                            Toast.LENGTH_LONG).show();
                }
            }
        };
    }
}
