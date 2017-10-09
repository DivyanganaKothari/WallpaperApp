package trainedge.social_wallpaper_app;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText etname;
    private EditText etemail;
    private EditText etpass;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private EditText etrepass;
    private static final String PASSWORD_PATTERN =
            "((?=.*\\d)(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%]).{6,20})";
    private Pattern pattern;
    private Matcher matcher;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        etname = (EditText) findViewById(R.id.etname);
        etemail = (EditText) findViewById(R.id.etemail);
        etpass = (EditText) findViewById(R.id.etpass);
        etrepass = (EditText) findViewById(R.id.etrepass);
        Button btnin = (Button) findViewById(R.id.btnin);
        Button btnup = (Button) findViewById(R.id.btnup);
        btnin.setOnClickListener(this);
        btnup.setOnClickListener(this);
        mAuth = FirebaseAuth.getInstance();
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    Intent home = new Intent(MainActivity.this, HomeActivity.class);
                    startActivity(home);
                    finish();

                    // User is signed in

                } else {
                    // User is signed out
                    Log.d("ERROR!", "onAuthStateChanged:signed_out");
                }
                // ...
            }
        };

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnin:
                SignIn();
                break;
            case R.id.btnup:
                SignUp();
                break;
        }

    }

    private void SignUp() {

        String name = etname.getText().toString();
        String email = etemail.getText().toString();
        String pass = etpass.getText().toString();
        String repass = etrepass.getText().toString();

        if (name.isEmpty()) {
            etname.setError("Required to fill NAME");
            return;
        }
        if (email.isEmpty()) {
            etemail.setError("Required to fill Email");
            return;
        }
        if (pass.isEmpty()) {
            etpass.setError("Fill Passwword");
            return;
        }
        if (!pass.equals(repass)) {
            etrepass.setError("Password doesn't match");
            return;
        }
        matcher = Pattern.compile(PASSWORD_PATTERN).matcher(pass);


        if (!matcher.matches()) {
            etpass.setError(
                    "    must contains one digit from 0-9\n" +
                            "     must contains one lowercase characters\n" +
                            "     must contains one uppercase characters\n" +
                            "     must contains one special symbols in the list \"@#$%\"\n" +
                            "     match anything with previous condition checking\n" +
                            "    length at least 6 characters and maximum of 20");
            return;
        }


        mAuth.createUserWithEmailAndPassword(email, pass)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        Log.d("SignUp successfull", "createUserWithEmail:onComplete:" + task.isSuccessful());

                        // If sign in fails, display a message to the user. If sign in succeeds
                        // the auth state listener will be notified and logic to handle the
                        // signed in user can be handled in the listener.
                        if (!task.isSuccessful()) {

                            Toast.makeText(MainActivity.this, "SignUp Failed!Retry" + task.getException(),
                                    Toast.LENGTH_SHORT).show();
                        } else {


                        }

                        // ...
                    }
                });

    }

    private void SignIn() {

        String email = etemail.getText().toString();
        String pass = etpass.getText().toString();
        mAuth.signInWithEmailAndPassword(email, pass)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        Log.d("successfully logged in", "signInWithEmail:onComplete:" + task.isSuccessful());

                        // If sign in fails, display a message to the user. If sign in succeeds
                        // the auth state listener will be notified and logic to handle the
                        // signed in user can be handled in the listener.
                        if (!task.isSuccessful()) {
                            Log.w("unable to LogIn", "signInWithEmail:failed", task.getException());
                            Toast.makeText(MainActivity.this, "Failed to LogIn",
                                    Toast.LENGTH_SHORT).show();
                        }

                        // ...
                    }
                });

    }


    @Override
    public void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mAuthListener != null) {
            mAuth.removeAuthStateListener(mAuthListener);
        }
    }
}
