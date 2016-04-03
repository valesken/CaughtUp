package news.caughtup.caughtup.ui.login;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import news.caughtup.caughtup.R;
import news.caughtup.caughtup.ui.prime.HomeActivity;

public class LoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // FOR TESTING ONLY
        //Intent intent = new Intent(this, HomeActivity.class);
        //startActivity(intent);
    }
}
