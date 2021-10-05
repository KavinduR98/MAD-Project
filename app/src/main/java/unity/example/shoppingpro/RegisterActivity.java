package unity.example.shoppingpro;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import unity.example.shoppingpro.Model.User;

public class RegisterActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    FirebaseDatabase db;
    DatabaseReference userRef;

    EditText register_username_input,register_email_input,register_password_input;
    Button register_btn;

    ProgressDialog loadingBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        getSupportActionBar().hide();

        mAuth = FirebaseAuth.getInstance();
        db = FirebaseDatabase.getInstance();
        userRef = db.getReference("User");

        loadingBar = new ProgressDialog(this);

        register_username_input =  findViewById(R.id.register_username_input);
        register_email_input =  findViewById(R.id.register_email_input);
        register_password_input =  findViewById(R.id.register_password_input);

        register_btn =  findViewById(R.id.register_btn);

        register_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = register_email_input.getText().toString();
                String pass = register_password_input.getText().toString();

                RegisterUser(email,pass);

                Intent intent = new Intent(RegisterActivity.this, HomeActivity.class);
                startActivity(intent);
            }
        });
    }

    private void RegisterUser(String email, String pass) {

        if (TextUtils.isEmpty(email)){
            Toast.makeText(this,"Enter Your Email!",Toast.LENGTH_SHORT).show();
        }

        if (TextUtils.isEmpty(pass)){
            Toast.makeText(this,"Enter Your Password!",Toast.LENGTH_SHORT).show();
        }

        else{
            loadingBar.setTitle("Registration");
            loadingBar.setMessage("Please wait, while we are register...");
            loadingBar.show();

            mAuth.createUserWithEmailAndPassword(email,pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {

                    if(task.isSuccessful()){
                       // save data to the Database

                        User user = new User();
                        user.setName(register_username_input.getText().toString());
                        user.setEmail(register_email_input.getText().toString());
                        user.setPassword(register_password_input.getText().toString());

                        userRef.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).setValue(user)
                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {

                                        Toast.makeText(RegisterActivity.this,"User Registered Successfully!!!", Toast.LENGTH_SHORT).show();

                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(RegisterActivity.this,"Failed "+e.getMessage(),Toast.LENGTH_SHORT).show();
                            }
                        });

                    }
                    else {
                        Toast.makeText(RegisterActivity.this, "Registration Failed !!!", Toast.LENGTH_SHORT).show();
                    }
                    loadingBar.dismiss();
                }
            });
        }
    }
}