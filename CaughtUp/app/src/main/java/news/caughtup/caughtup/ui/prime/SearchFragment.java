package news.caughtup.caughtup.ui.prime;

import android.app.AlertDialog;
import android.app.Fragment;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Random;

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
        RelativeLayout userResult = new RelativeLayout(context);
        userResult.setPadding(5, 5, 5, 5);
        Random random = new Random();

        //region profile picture
        ImageView profilePic = new ImageView(context);
        profilePic.setId(random.nextInt(Integer.MAX_VALUE));
        profilePic.setImageDrawable(getResources().getDrawable(user.getProfileImageId(), null));
        profilePic.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                loadUser(user);
                return false;
            }
        });
        RelativeLayout.LayoutParams profilePicParams = new RelativeLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        profilePicParams.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
        profilePicParams.addRule(RelativeLayout.ALIGN_PARENT_TOP);
        userResult.addView(profilePic, profilePicParams);
        //endregion

        //region follow/unfollow button
        final ImageButton imageButton = new ImageButton(context);
        imageButton.setId(random.nextInt(Integer.MAX_VALUE));
        imageButton.setImageDrawable(getResources().getDrawable(R.drawable.add_icon, null));
        imageButton.setBackgroundColor(Color.TRANSPARENT);
        imageButton.setElevation(3);
        imageButton.setScaleType(ImageView.ScaleType.FIT_END);
        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Drawable add = getResources().getDrawable(R.drawable.add_icon, null);
                if (imageButton.getDrawable().getConstantState().equals(add.getConstantState())) {
                    Toast.makeText(context,
                            String.format("Now following %s!", user.getUserName()),
                            Toast.LENGTH_SHORT).show();
                    imageButton.setImageDrawable(getResources().getDrawable(R.drawable.unfollow_icon, null));
                } else {
                    Toast.makeText(context,
                            String.format("No longer following %s.", user.getUserName()),
                            Toast.LENGTH_SHORT).show();
                    imageButton.setImageDrawable(getResources().getDrawable(R.drawable.add_icon, null));
                }
            }
        });
        RelativeLayout.LayoutParams imageButtonParams = new RelativeLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        imageButtonParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
        userResult.addView(imageButton, imageButtonParams);
        //endregion

        //region user info
        LinearLayout userInfo = new LinearLayout(context);
        userInfo.setOrientation(LinearLayout.VERTICAL);
        userInfo.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                loadUser(user);
                return false;
            }
        });

        TextView userName = new TextView(context);
        userName.setText(user.getUserName());
        userName.setTypeface(null, Typeface.BOLD);
        userName.setTextColor(Color.BLACK);
        userInfo.addView(userName,
                new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));

        TextView aboutMe = new TextView(context);
        aboutMe.setTextColor(Color.BLACK);
        String aboutMeString = user.getAboutMe();
        if(aboutMeString.length() > 140) {
            aboutMeString = aboutMeString.substring(0, 140) + "...";
        }
        aboutMe.setText(aboutMeString);
        userInfo.addView(aboutMe,
                new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));

        RelativeLayout.LayoutParams userInfoParams = new RelativeLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        userInfoParams.addRule(RelativeLayout.RIGHT_OF, profilePic.getId());
        userInfoParams.addRule(RelativeLayout.LEFT_OF, imageButton.getId());

        userResult.addView(userInfo, userInfoParams);
        //endregion

        // Add User result to overall layout
        layout.addView(userResult,
                new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
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

    private void loadNewsSource(int thumbnailId, final Uri externalLink, final String name, String description, LinearLayout layout) {
        NewsSourceTileView newsSource = new NewsSourceTileView(context);
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
        final ImageButton followButton = (ImageButton) newsSource.findViewById(R.id.follow_news_source_tile_view);
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
        layout.addView(newsSource);
    }
}
