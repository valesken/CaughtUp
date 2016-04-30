package news.caughtup.caughtup.ui.prime;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import news.caughtup.caughtup.R;
import news.caughtup.caughtup.util.StringRetriever;

public class ChangePasswordFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_change_password, container, false);
        final HomeActivity activity = (HomeActivity) getActivity();

        Button saveButton = (Button) view.findViewById(R.id.change_password_save_button);
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activity.onBackPressed();
            }
        });

        Button cancelButton = (Button) view.findViewById(R.id.change_password_cancel_button);
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activity.onBackPressed();
            }
        });

        return view;
    }
}
