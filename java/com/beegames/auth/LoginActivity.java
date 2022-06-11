package com.beegames.auth;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;

import com.beegames.R;
import com.beegames.menus.MainActivity;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class LoginActivity extends Activity {
    private static final String TAG = RegisterActivity.class.getSimpleName();
    private Button btnLogin;
    private Button btnLinkToRegister;
    private EditText email;
    private EditText password;
    private LinearLayout root;
    public static final String IS_LOGGED_IN = "Is logged in";
    public static final boolean LOG_IN = false;
    private SharedPreferences myPreferences;

    DatabaseReference users;
    FirebaseAuth auth;
    FirebaseDatabase db;

    SQLtHelper sqLtHelper;
    GoogleSignInClient mGoogleSignInClient;

    @SuppressLint("SourceLockedOrientationActivity")
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        myPreferences = getSharedPreferences(IS_LOGGED_IN, LoginActivity.MODE_PRIVATE);
        if (myPreferences.contains("LOG_IN") && myPreferences.getBoolean("LOG_IN", false)) {
            openMainMenu();
        }

        setContentView(R.layout.activity_login);

        btnLogin = (Button) findViewById(R.id.btnLogin);
        btnLinkToRegister = (Button) findViewById(R.id.btnLinkToRegisterScreen);
        email = (EditText) findViewById(R.id.email);
        password = (EditText) findViewById(R.id.password);
        root = (LinearLayout) findViewById(R.id.root_login);

        sqLtHelper = new SQLtHelper(this);
        SQLiteDatabase database = sqLtHelper.getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        auth = FirebaseAuth.getInstance();
        db = FirebaseDatabase.getInstance("https://beegames-4b280-default-rtdb.europe-west1.firebasedatabase.app");
        users = db.getReference("Users");

        btnLinkToRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showRegisterWindow();
            }
        });

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (TextUtils.isEmpty(email.getText().toString())) {
                    Snackbar.make(root, "Enter your e-mail", Snackbar.LENGTH_SHORT).show();
                    return;
                }

                if (TextUtils.isEmpty(password.getText().toString())) {
                    Snackbar.make(root, "Enter your name", Snackbar.LENGTH_SHORT).show();
                    return;
                }

                auth.signInWithEmailAndPassword(email.getText().toString(), password.getText().toString())
                        .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                            @Override
                            public void onSuccess(AuthResult authResult) {

                                contentValues.put(SQLtHelper.KEY_EMAIL, email.getText().toString());
                                contentValues.put(SQLtHelper.KEY_PASSWORD, password.getText().toString());

                                database.insert(SQLtHelper.TABLE_USER, null, contentValues);

                                SharedPreferences.Editor editor = myPreferences.edit();
                                editor.putBoolean("LOG_IN", true);
                                editor.apply();

                                openMainMenu();
                            }

                        }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Snackbar.make(root, "Sign in error " + e.getMessage(), Snackbar.LENGTH_SHORT).show();
                    }
                });
            }
        });

    }

    private void showRegisterWindow() {
        Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
        startActivity(intent);
    }

    private void openMainMenu() {
        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
        startActivity(intent);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        finish();
    }
}
