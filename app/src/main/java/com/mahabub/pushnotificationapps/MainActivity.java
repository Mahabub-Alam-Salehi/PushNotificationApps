package com.mahabub.pushnotificationapps;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Intent;
import android.renderscript.Script;
import android.support.annotation.NonNull;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;

public class MainActivity extends AppCompatActivity {


    // Notification Channel for Oreo Version
    // Notification Builder
    // Notification Manager

    public static final String CHANNEL_ID = "android_coding";
    private static final String CHANNEL_NAME = "android_coding";
    private static final String CHANNEL_DESC = "android_Push_Notifications";

    private EditText editTextEmail, editTextPassword;
    private Button SignUpButton;
    private ProgressBar progressBar;

    private FirebaseAuth mAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_main );

        mAuth = FirebaseAuth.getInstance();

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel( CHANNEL_ID, CHANNEL_NAME, NotificationManager.IMPORTANCE_HIGH );
            channel.setDescription( CHANNEL_DESC );
            NotificationManager manager = getSystemService( NotificationManager.class );
            manager.createNotificationChannel( channel );
        }

        // progressBar.setVisibility( View.INVISIBLE );
        progressBar = findViewById( R.id.progressbar );
        progressBar.setVisibility( View.INVISIBLE );
        editTextEmail = findViewById( R.id.editTextEmail );
        editTextPassword = findViewById( R.id.editTextPassword );

        findViewById( R.id.buttonSignUp ).setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CreateUser();
            }
        } );

    }


    private void CreateUser() {
        final String email = editTextEmail.getText().toString().trim();
        final String password = editTextPassword.getText().toString().trim();

        if (email.isEmpty()) {
            editTextEmail.setError( "Email required" );
            editTextEmail.requestFocus();

            return;
        }
        if ((password.isEmpty())) {
            editTextPassword.setError( "Password required" );
            editTextPassword.requestFocus();
            return;
        }

        if (password.length() < 6) {
            editTextPassword.setError( "password should be atleast 6 char long" );
            editTextPassword.requestFocus();
        }

        progressBar.setVisibility( View.VISIBLE );
        mAuth.createUserWithEmailAndPassword( email, password )
                .addOnCompleteListener( new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        if (task.isSuccessful()) {
                            startProfileActivity();
                        } else {
                            if (task.getException() instanceof FirebaseAuthUserCollisionException) {

                                userLogin( email, password );
                            } else {
                                progressBar.setVisibility( View.INVISIBLE );
                                Toast.makeText( MainActivity.this, task.getException().getMessage(), Toast.LENGTH_LONG ).show();
                            }
                        }


                    }
                } );


    }

    private void userLogin(String email, String password) {

        mAuth.signInWithEmailAndPassword( email, password )
                .addOnCompleteListener( new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            startProfileActivity();
                        } else {
                            progressBar.setVisibility( View.INVISIBLE );
                            Toast.makeText( MainActivity.this, task.getException().getMessage(), Toast.LENGTH_SHORT ).show();
                        }
                    }
                } );

    }

    @Override
    protected void onStart() {
        super.onStart();
        if (mAuth.getCurrentUser() != null) {
            startProfileActivity();

        }
    }

    private void startProfileActivity() {
        Intent intent = new Intent( this, ProfileActivity.class );
        intent.addFlags( Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK );
        startActivity( intent );
    }




}
