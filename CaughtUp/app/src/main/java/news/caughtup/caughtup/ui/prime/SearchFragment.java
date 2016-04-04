package news.caughtup.caughtup.ui.prime;

import android.app.AlertDialog;
import android.app.Fragment;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Toast;

import news.caughtup.caughtup.R;
import news.caughtup.caughtup.model.User;
import news.caughtup.caughtup.model.UserList;

public class SearchFragment extends Fragment{

    private Context context;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_search, container, false);
        context = getActivity().getApplicationContext();
        LinearLayout searchLayout = (LinearLayout) rootView.findViewById(R.id.search_layout);
        addAllUsers(searchLayout);
        addTestArticles(searchLayout);
        addTestNewsSources(searchLayout);
        return rootView;
    }

    private void addAllUsers(LinearLayout layout) {
        for(User user : UserList.getUsers()) {
            addUser(user, layout);
        }
    }

    private void addUser(final User user, LinearLayout layout) {
        FollowableTileView newsSource = new FollowableTileView(context);
        newsSource.setThumbnailImage(user.getProfileImageId());
        newsSource.setNameText(user.getUserName());
        newsSource.setDescriptionText(user.getAboutMe());
        newsSource.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                loadUser(user);
                return false;
            }
        });
        setFollowButtonListener(newsSource, user.getUserName());
        layout.addView(newsSource);
    }

    private void loadUser(User user) {
        PublicProfileFragment publicProfile = new PublicProfileFragment();
        Bundle args = new Bundle();
        args.putString("user", user.getUserName());
        publicProfile.setArguments(args);
        HomeActivity.executeTransaction(publicProfile, user.getUserName());
    }

    private void addTestArticles(LinearLayout layout) {
        // Techcrunch article
        Uri techcrunchUri = Uri.parse(getResources().getString(R.string.test_techcrunch_url));
        String techcrunchTitle = getResources().getString(R.string.test_techcrunch_title);
        String techcrunchDescription = getResources().getString(R.string.test_techcrunch_description);
        // BBC article
        Uri bbcUri = Uri.parse(getResources().getString(R.string.test_bbc_url));
        String bbcTitle = getResources().getString(R.string.test_bbc_title);
        String bbcDescription = getResources().getString(R.string.test_bbc_description);
        // Load Articles
        loadArticle(R.mipmap.techcrunch_icon, techcrunchUri, techcrunchTitle, techcrunchDescription, layout);
        loadArticle(R.mipmap.bbc_icon, bbcUri, bbcTitle, bbcDescription, layout);
    }

    private void loadArticle(int thumbnailId, final Uri externalLink, final String name, String description, LinearLayout layout) {
        ArticleTileView article = new ArticleTileView(context);
        article.setThumbnailImage(thumbnailId);
        article.setNameText(name);
        article.setDescriptionText(description);
        if(externalLink != null) {
            article.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    Intent launchBrowser = new Intent(Intent.ACTION_VIEW, externalLink);
                    getActivity().startActivity(launchBrowser);
                    return false;
                }
            });
        }
        ImageButton shareButton = (ImageButton) article.findViewById(R.id.share_article_tile_view);
        if(shareButton != null) {
            shareButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    launchShareDialog(name);
                }
            });
        }
        layout.addView(article);
    }

    private void launchShareDialog(final String articleName) {
        final String[] items = { "Share with Followers", "Share on Facebook", "Share on Twitter", "Cancel"};
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Share Article!");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                switch (items[item]) {
                    case "Share with Followers":
                        Toast.makeText(context,
                                String.format("\"%s\" is now shared with your followers", articleName),
                                Toast.LENGTH_SHORT).show();
                        dialog.dismiss();
                        break;
                    case "Share on Facebook":
                        Toast.makeText(context,
                                String.format("\"%s\" is now shared on Facebook", articleName),
                                Toast.LENGTH_SHORT).show();
                        dialog.dismiss();
                        break;
                    case "Share on Twitter":
                        Toast.makeText(context,
                                String.format("\"%s\" is now shared on Twitter", articleName),
                                Toast.LENGTH_SHORT).show();
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

    private void addTestNewsSources(LinearLayout layout) {
        // Techcrunch
        String techcrunchName = "Techcrunch";
        String techcrunchDescription = "TechCrunch is an online publisher of technology industry news.";
        Uri techcrunchUri = Uri.parse("http://techcrunch.com/");
        // BBC
        String bbcName = "BBC";
        String bbcDescription = "The British Broadcasting Corporation (BBC) is the public service broadcaster of the United Kingdom, headquartered at Broadcasting House in London.";
        Uri bbcUri = Uri.parse("http://www.bbc.com/");
        // Load news sources
        loadNewsSource(R.mipmap.techcrunch_icon, techcrunchUri, techcrunchName, techcrunchDescription, layout);
        loadNewsSource(R.mipmap.bbc_icon, bbcUri, bbcName, bbcDescription, layout);
    }

    private void loadNewsSource(int thumbnailId, final Uri externalLink, String name, String description, LinearLayout layout) {
        FollowableTileView newsSource = new FollowableTileView(context);
        newsSource.setThumbnailImage(thumbnailId);
        newsSource.setNameText(name);
        newsSource.setDescriptionText(description);
        if(externalLink != null) {
            newsSource.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    Intent launchBrowser = new Intent(Intent.ACTION_VIEW, externalLink);
                    getActivity().startActivity(launchBrowser);
                    return false;
                }
            });
        }
        setFollowButtonListener(newsSource, name);
        layout.addView(newsSource);
    }

    private void setFollowButtonListener(FollowableTileView followable, final String name) {
        final ImageButton followButton = (ImageButton) followable.findViewById(R.id.follow_news_source_tile_view);
        followButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String followTag = getResources().getString(R.string.follow_tag);
                String unfollowTag = getResources().getString(R.string.unfollow_tag);
                if (followButton.getTag().equals(followTag)) {
                    Toast.makeText(context,
                            String.format("Now following %s!", name),
                            Toast.LENGTH_SHORT).show();
                    followButton.setImageDrawable(getResources().getDrawable(R.drawable.unfollow_icon, null));
                    followButton.setTag(unfollowTag);
                } else {
                    Toast.makeText(context,
                            String.format("No longer following %s.", name),
                            Toast.LENGTH_SHORT).show();
                    followButton.setImageDrawable(getResources().getDrawable(R.drawable.add_icon, null));
                    followButton.setTag(followTag);
                }
            }
        });
    }
}