package com.beegames.auth;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;

import com.beegames.Models.User;
import com.beegames.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.security.SecureRandom;

public class RegisterActivity extends Activity {
    private static final String TAG = RegisterActivity.class.getSimpleName();
    private Button btnRegister;
    private Button btnLinkToLogin;
    private EditText name;
    private EditText email;
    private EditText password;
    private LinearLayout root;
    private byte[] salt;
    private static SecureRandom random = new SecureRandom();

    DatabaseReference users;
    FirebaseAuth auth;
    FirebaseDatabase db;

    @SuppressLint("SourceLockedOrientationActivity")
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.activity_register);

        name = (EditText) findViewById(R.id.name);
        email = (EditText) findViewById(R.id.email);
        password = (EditText) findViewById(R.id.password);
        btnRegister = (Button) findViewById(R.id.btnRegister);
        btnLinkToLogin = (Button) findViewById(R.id.btnLinkToLoginScreen);
        root = (LinearLayout) findViewById(R.id.register_root);



        auth = FirebaseAuth.getInstance();
        db = FirebaseDatabase.getInstance("https://beegames-4b280-default-rtdb.europe-west1.firebasedatabase.app");

        System.out.println(db);
        System.out.println(auth);
        users = db.getReference("Users");



        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                System.out.println("listner active");
                if (TextUtils.isEmpty(email.getText().toString())) {
                    Snackbar.make(root, "Enter your e-mail", Snackbar.LENGTH_SHORT).show();
                    return;
                }

                if (TextUtils.isEmpty(name.getText().toString())) {
                    Snackbar.make(root, "Enter your name", Snackbar.LENGTH_SHORT).show();
                    return;
                }

                if (password.getText().toString().length() < 5) {
                    Snackbar.make(root, "Enter password with length more than 5", Snackbar.LENGTH_SHORT).show();
                    return;
                }



                auth.createUserWithEmailAndPassword(email.getText().toString(), password.getText().toString())
                        .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                            @Override
                            public void onSuccess(AuthResult authResult) {
                                //System.out.println("succesfull registration");
                                User user = new User(name.getText().toString(),
                                        email.getText().toString());
                                System.out.println(user.getScore_2048());
                                //System.out.println(FirebaseAuth.getInstance().getCurrentUser().getUid());
                                //users.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).setValue(user);
                                users.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).setValue(user)
                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void unused) {
                                                Snackbar.make(root, "User created", Snackbar.LENGTH_SHORT).show();
                                                backOnLogIn();
                                            }
                                        });
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Snackbar.make(root, "Error. " + e.getMessage(), Snackbar.LENGTH_SHORT).show();
                        System.out.println(e.getMessage());
                    }
                });

            }
        });

        btnLinkToLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                backOnLogIn();
            }
        });

    }

    public void backOnLogIn() {
        Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
        startActivity(intent);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        finish();
    }
}
