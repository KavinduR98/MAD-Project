package unity.example.shoppingpro;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity extends AppCompatActivity {

    EditText login_email_input,login_password_input;

    Button btnLogin;

    private FirebaseAuth mAuth;
    ProgressDialog loadingBar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        // Hide action bar
        getSupportActionBar().hide();

        mAuth = FirebaseAuth.getInstance();
        loadingBar = new ProgressDialog(this);

        login_email_input = findViewById(R.id.login_email_input);
        login_password_input = findViewById(R.id.login_password_input);

        btnLogin = findViewById(R.id.login_btn);

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String email = login_email_input.getText().toString();
                String password = login_password_input.getText().toString();

                Signin(email, password);
                ;
            }
        });
    }
    private void Signin(String email, String password) {

        if (TextUtils.isEmpty(email)){
            Toast.makeText(this,"Enter Your Email!",Toast.LENGTH_SHORT).show();
        }

        if (TextUtils.isEmpty(password)){
            Toast.makeText(this,"Enter Your Password!",Toast.LENGTH_SHORT).show();
        }

        else {
            loadingBar.setTitle("Sign in");
            loadingBar.setMessage("Please wait, while we are checking...");
            loadingBar.show();

            mAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {

                    if (task.isSuccessful()){

                        loadingBar.dismiss();
                        Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
                        startActivity(intent);
                        Toast.makeText(LoginActivity.this, "Successfully Sign in!!!", Toast.LENGTH_SHORT).show();
                        finish();
                    }
                    else {
                        Toast.makeText(LoginActivity.this,"Sing in failed!!!",Toast.LENGTH_SHORT).show();
                    }

                }
            });
        }
    }
}