package news.caughtup.caughtup.ui.prime;

import android.app.AlertDialog;
import android.app.Fragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import news.caughtup.caughtup.R;
import news.caughtup.caughtup.entities.User;

public class EditProfileFragment extends Fragment {

    private static final int REQUEST_CAMERA = 1;
    private static final int SELECT_FILE = 2;

    private View rootView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_edit_profile, container, false);

        setUpCurrentUserInfo();

        RelativeLayout editProfilePicture = (RelativeLayout) rootView.findViewById(R.id.edit_profile_picture_relative_layout);
        editProfilePicture.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                selectPhoto();
                return false;
            }
        });

        Button changePasswordButton = (Button) rootView.findViewById(R.id.edit_profile_change_password_button);
        changePasswordButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ChangePasswordFragment changePasswordFragment = new ChangePasswordFragment();
                HomeActivity.executeTransaction(changePasswordFragment, "change_password", R.string.change_password_title);
            }
        });

        return rootView;
    }

    private void setUpCurrentUserInfo() {
        User user = HomeActivity.getCurrentUser();

        // Profile Picture
        ImageView profilePicView = (ImageView) rootView.findViewById(R.id.edit_profile_photo_image_view);
        int imageResourceId = user.getProfileImageId();
        if(imageResourceId > 0) {
            profilePicView.setImageDrawable(getActivity().getResources().getDrawable(imageResourceId, null));
        } else {
            profilePicView.setImageDrawable(getActivity().getResources().getDrawable(R.mipmap.profile_pic_2, null));
        }

        // Username
        TextView userNameView = (TextView) rootView.findViewById(R.id.edit_profile_username_text_view);
        userNameView.setText(user.getName());

        // Gender
        EditText genderView = (EditText) rootView.findViewById(R.id.edit_profile_gender_edit_text);
        char gender = user.getGender();
        if(gender != 'x') {
            genderView.setText(Character.toString(gender));
        }

        // Age
        EditText ageView = (EditText) rootView.findViewById(R.id.edit_profile_age_edit_text);
        int age = user.getAge();
        if(age > 1) {
            ageView.setText(String.format("%d", age));
        }

        // Full Name
        EditText fullNameView = (EditText) rootView.findViewById(R.id.edit_profile_full_name_edit_text);
        String fullName = user.getFullName();
        if(fullName != null && !fullName.isEmpty()) {
            fullNameView.setText(user.getFullName());
        }

        // Location
        EditText locationView = (EditText) rootView.findViewById(R.id.edit_profile_location_edit_text);
        String location = user.getLocation();
        if(location != null && !location.isEmpty()) {
            locationView.setText(location);
        }

        // About me
        EditText aboutMeView = (EditText) rootView.findViewById(R.id.edit_profile_about_me_edit_text);
        String aboutMe = user.getAboutMe();
        if(aboutMe != null && !aboutMe.isEmpty()) {
            aboutMeView.setText(aboutMe);
        }
    }

    private void selectPhoto() {
        final CharSequence[] items = { "Take Photo", "Choose from Library", "Cancel" };
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Add Photo!");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                if (items[item].equals("Take Photo")) {
                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(intent, REQUEST_CAMERA);
                } else if (items[item].equals("Choose from Library")) {
                    Intent intent = new Intent(
                            Intent.ACTION_PICK,
                            android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    intent.setType("image/*");
                    startActivityForResult(
                            Intent.createChooser(intent, "Select File"),
                            SELECT_FILE);
                } else if (items[item].equals("Cancel")) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }
}