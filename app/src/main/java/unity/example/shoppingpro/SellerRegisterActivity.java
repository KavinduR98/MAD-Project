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
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import unity.example.shoppingpro.Model.Seller;
import unity.example.shoppingpro.Model.User;

public class SellerRegisterActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    FirebaseDatabase db;
    DatabaseReference sellerRef;

    EditText register_sellername_input,register_selleremail_input,register_sellerphone_input,register_sellerpassword_input;
    Button Register_btn;

    ProgressDialog loadingBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seller_register);

        getSupportActionBar().hide();

        TextView sellerLogin = (TextView) findViewById(R.id.seller_login_view) ;

        mAuth = FirebaseAuth.getInstance();
        db = FirebaseDatabase.getInstance();
        sellerRef = db.getReference("Seller");

        loadingBar = new ProgressDialog(this);

        register_sellername_input =  findViewById(R.id.register_sellername_input);
        register_selleremail_input =  findViewById(R.id.register_selleremail_input);
        register_sellerphone_input =  findViewById(R.id.register_sellerphone_input);
        register_sellerpassword_input =  findViewById(R.id.register_sellerpassword_input);

        Register_btn =  findViewById(R.id.sellerRegister_btn1);

        Register_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String email = register_selleremail_input.getText().toString();
                String pass = register_sellerpassword_input.getText().toString();

                RegisterSeller(email,pass);

                Intent intent = new Intent(SellerRegisterActivity.this, SellerHome.class);
                startActivity(intent);
            }
        });

        //Navigate to the Seller Login Activity

        sellerLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SellerRegisterActivity.this, SellerLoginActivity.class);
                startActivity(intent);
            }
        });
    }

    private void RegisterSeller(String email, String pass) {

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

                        Seller seller = new Seller();
                        seller.setName(register_sellername_input.getText().toString());
                        seller.setEmail(register_selleremail_input.getText().toString());
                        seller.setPhone(register_sellerphone_input.getText().toString());
                        seller.setPassword(register_sellerpassword_input.getText().toString());

                        sellerRef.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).setValue(seller)
                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {

                                        Toast.makeText(SellerRegisterActivity.this,"Seller Registered Successfully!!!", Toast.LENGTH_SHORT).show();

                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(SellerRegisterActivity.this,"Failed "+e.getMessage(),Toast.LENGTH_SHORT).show();
                            }
                        });

                    }
                    else {
                        Toast.makeText(SellerRegisterActivity.this, "Registration Failed !!!", Toast.LENGTH_SHORT).show();
                    }
                    loadingBar.dismiss();
                }
            });
        }
    }
}