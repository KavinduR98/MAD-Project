package unity.example.shoppingpro;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

public class SellerHome extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seller_home);

        getSupportActionBar().hide();
    }
}