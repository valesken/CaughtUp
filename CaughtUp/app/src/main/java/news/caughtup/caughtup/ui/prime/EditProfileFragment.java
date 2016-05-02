package news.caughtup.caughtup.ui.prime;

import android.app.AlertDialog;
import android.app.Fragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import news.caughtup.caughtup.R;
import news.caughtup.caughtup.entities.ResponseObject;
import news.caughtup.caughtup.entities.User;
import news.caughtup.caughtup.ws.remote.Callback;
import news.caughtup.caughtup.ws.remote.RestProxy;

public class EditProfileFragment extends Fragment {

    private static final int REQUEST_CAMERA = 1;
    private static final int SELECT_FILE = 2;
    private User user;
    private ImageView profilePicView;
    private TextView userNameView;
    private Spinner genderSpinner;
    private EditText ageView;
    private EditText fullNameView;
    private EditText emailView;
    private EditText locationView;
    private EditText aboutMeView;


    private View rootView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_edit_profile, container, false);

        user = HomeActivity.getCurrentUser();
        System.out.println("Email: " + user.getEmail());
        System.out.println("Full Name: " + user.getFullName());
        //get references to views region
        profilePicView = (ImageView) rootView.findViewById(R.id.edit_profile_photo_image_view);
        userNameView = (TextView) rootView.findViewById(R.id.edit_profile_username_text_view);
        genderSpinner = (Spinner) rootView.findViewById(R.id.edit_profile_gender_edit_text);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getActivity(),
                R.array.gender_array, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        genderSpinner.setAdapter(adapter);
        ageView = (EditText) rootView.findViewById(R.id.edit_profile_age_edit_text);
        fullNameView = (EditText) rootView.findViewById(R.id.edit_profile_full_name_edit_text);
        emailView = (EditText) rootView.findViewById(R.id.edit_profile_email_edit_text);
        locationView = (EditText) rootView.findViewById(R.id.edit_profile_location_edit_text);
        aboutMeView = (EditText) rootView.findViewById(R.id.edit_profile_about_me_edit_text);
        //end region

        setUpCurrentUserInfo();

        RelativeLayout editProfilePicture = (RelativeLayout) rootView.findViewById(R.id.edit_profile_picture_relative_layout);
        editProfilePicture.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                selectPhoto();
                return false;
            }
        });

        //region setup button listeners
        Button changePasswordButton = (Button) rootView.findViewById(R.id.edit_profile_change_password_button);
        changePasswordButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ChangePasswordFragment changePasswordFragment = new ChangePasswordFragment();
                HomeActivity.executeTransaction(changePasswordFragment, "change_password", R.string.change_password_title);
            }
        });

        Button saveChangesButton = (Button) rootView.findViewById(R.id.edit_profile_save_button);
        saveChangesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RestProxy proxy = RestProxy.getProxy();
                Callback callback = getUpdatedUserCallback();
                JSONObject jsonObject = createJSON();
                if (!validateEmail(user.getEmail())) {
                    Toast.makeText(getActivity(), "The email address is invalid",
                            Toast.LENGTH_SHORT)
                            .show();
                } else {
                    proxy.putCall(String.format("/profile/%s?picture=false", user.getName()),
                            jsonObject.toString(), callback);
                }
            }
        });
        //end region

        return rootView;
    }

    private void setUpCurrentUserInfo() {

        // Profile Picture
        int imageResourceId = user.getProfileImageId();
        if(imageResourceId > 0) {
            profilePicView.setImageDrawable(getActivity().getResources().getDrawable(imageResourceId, null));
        } else {
            profilePicView.setImageDrawable(getActivity().getResources().getDrawable(R.mipmap.profile_pic_2, null));
        }

        // Username
        userNameView.setText(user.getName());

        // Gender
        String gender = user.getGender();
        if(gender != null && !gender.isEmpty()) {
            switch (gender.toLowerCase()) {
                case "male":
                    genderSpinner.setSelection(1);
                    break;
                case "female":
                    genderSpinner.setSelection(2);
                    break;
                default:
                    genderSpinner.setSelection(0);
                    break;
            }
        }

        // Age
        int age = user.getAge();
        if(age > 1) {
            ageView.setText(String.format("%d", age));
        }

        // Full Name
        String fullName = user.getFullName();
        if(fullName != null && !fullName.isEmpty()) {
            fullNameView.setText(user.getFullName());
        }

        // Email
        String email = user.getEmail();
        if(email != null && !email.isEmpty()) {
            emailView.setText(email);
        }

        // Location
        String location = user.getLocation();
        if(location != null && !location.isEmpty()) {
            locationView.setText(location);
        }

        // About me
        String aboutMe = user.getAboutMe();
        if(aboutMe != null && !aboutMe.isEmpty()) {
            aboutMeView.setText(aboutMe);
        }
    }

    private Callback getUpdatedUserCallback() {
        return new Callback() {
            @Override
            public void process(ResponseObject responseObject) {
                if (responseObject.getResponseCode() == 200) {
                    JSONObject jsonObject = responseObject.getJsonObject();
                    try {
                        if (jsonObject.getString("message").equals("Success")) {
                            updateUser();
                        } else {
                            Toast.makeText(getActivity().getApplicationContext(),
                                    getActivity().getResources().getString(R.string.get_user_server_error),
                                    Toast.LENGTH_LONG).show();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } else {
                    Toast.makeText(getActivity().getApplicationContext(),
                            getActivity().getResources().getString(R.string.get_user_server_error),
                            Toast.LENGTH_LONG).show();
                }
            }
        };
    }

    private void updateUser() {
        // Gender
        String gender = genderSpinner.getSelectedItem().toString();
        if(gender != null && !gender.isEmpty()) {
            user.setGender(gender);
        }

        // Age
        int age = Integer.parseInt(ageView.getText().toString());
        if (age > 1) {
            user.setAge(age);
        }

        // Full Name
        String fullName = fullNameView.getText().toString();
        if(fullName != null && !fullName.isEmpty()) {
            user.setFullName(fullName);
        }

        // Email
        String email = emailView.getText().toString();
        if(email != null && !email.isEmpty()) {
            user.setEmail(email);
        }

        // Location
        String location = locationView.getText().toString();
        if(location != null && !location.isEmpty()) {
            user.setLocation(location);
        }

        // About me
        String aboutMe = aboutMeView.getText().toString();
        if(aboutMe != null && !aboutMe.isEmpty()) {
            user.setAboutMe(aboutMe);
        }
    }

    private JSONObject createJSON() {
        JSONObject jsonObject = new JSONObject();
        // Gender
        try {
            String gender = genderSpinner.getSelectedItem().toString();
            if(gender != null && !gender.isEmpty()) {
                jsonObject.put("gender", gender);
            }

            // Age
            int age = Integer.parseInt(ageView.getText().toString());
            if (age > 1) {
                jsonObject.put("age", age);
            }

            // Full Name
            String fullName = fullNameView.getText().toString();
            if(fullName != null && !fullName.isEmpty()) {
                jsonObject.put("fullName", fullName);
            }

            // Email
            String email = emailView.getText().toString();
            if(email != null && !email.isEmpty()) {
                jsonObject.put("email", email);
            }

            // Location
            String location = locationView.getText().toString();
            if(location != null && !location.isEmpty()) {
                jsonObject.put("location", location);
            }

            // About me
            String aboutMe = aboutMeView.getText().toString();
            if(aboutMe != null && !aboutMe.isEmpty()) {
                jsonObject.put("aboutMe", aboutMe);
            }
            return jsonObject;
        } catch (JSONException e) {
            Log.e("JSONException", "Couldn't create JSON with updated user");
            return null;
        }
    }

    /**
     * Validate an input string as an email
     * @param email
     * @return
     */
    private boolean validateEmail(String email) {
        Pattern p = Pattern.compile(".+@.+\\.[a-z]+");
        Matcher m = p.matcher(email);
        return m.matches();
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