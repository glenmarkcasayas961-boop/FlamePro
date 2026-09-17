package com.example.flamepro;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.imageview.ShapeableImageView;

public class SetupProfileActivity extends AppCompatActivity {

    private ShapeableImageView ivProfileAvatar;
    private ShapeableImageView ivCameraBadge;
    private EditText etFirstName;
    private EditText etLastName;
    private EditText etMiddleName;
    private EditText etPhoneNumber;
    private EditText etAddress;
    private EditText etBarangay;
    private EditText etCity;
    private EditText etProvince;
    private Button btnSaveProfile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_setup_profile);

        View scrollView = findViewById(R.id.setupProfileScrollView);
        ViewCompat.setOnApplyWindowInsetsListener(scrollView, (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(v.getPaddingLeft(), systemBars.top, v.getPaddingRight(), systemBars.bottom);
            return insets;
        });

        initializeViews();
        preFillExistingData();
        setupClickListeners();
    }

    private void preFillExistingData() {
        UserManager um = UserManager.getInstance();
        etFirstName.setText(um.getFirstName());
        etLastName.setText(um.getLastName());
        etMiddleName.setText(um.getMiddleName());
        etPhoneNumber.setText(um.getPhoneNumber());
        etAddress.setText(um.getAddress());
        etBarangay.setText(um.getBarangay());
        etCity.setText(um.getCity());
        etProvince.setText(um.getProvince());
    }

    private void initializeViews() {
        ivProfileAvatar = findViewById(R.id.ivProfileAvatar);
        ivCameraBadge = findViewById(R.id.ivCameraBadge);
        etFirstName = findViewById(R.id.etFirstName);
        etLastName = findViewById(R.id.etLastName);
        etMiddleName = findViewById(R.id.etMiddleName);
        etPhoneNumber = findViewById(R.id.etPhoneNumber);
        etAddress = findViewById(R.id.etAddress);
        etBarangay = findViewById(R.id.etBarangay);
        etCity = findViewById(R.id.etCity);
        etProvince = findViewById(R.id.etProvince);
        btnSaveProfile = findViewById(R.id.btnSaveProfile);
    }

    private void setupClickListeners() {
        View.OnClickListener avatarListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(SetupProfileActivity.this, "Upload Profile Picture", Toast.LENGTH_SHORT).show();
            }
        };
        ivProfileAvatar.setOnClickListener(avatarListener);
        ivCameraBadge.setOnClickListener(avatarListener);

        btnSaveProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveProfileData();
            }
        });
    }

    private void saveProfileData() {
        String firstName = etFirstName.getText().toString().trim();
        String lastName = etLastName.getText().toString().trim();
        String phoneNumber = etPhoneNumber.getText().toString().trim();
        String address = etAddress.getText().toString().trim();
        String barangay = etBarangay.getText().toString().trim();
        String city = etCity.getText().toString().trim();
        String province = etProvince.getText().toString().trim();

        if (firstName.isEmpty()) {
            etFirstName.setError("First name required");
            etFirstName.requestFocus();
            return;
        }

        if (lastName.isEmpty()) {
            etLastName.setError("Last name required");
            etLastName.requestFocus();
            return;
        }

        if (phoneNumber.isEmpty()) {
            etPhoneNumber.setError("Phone number required");
            etPhoneNumber.requestFocus();
            return;
        }

        if (address.isEmpty()) {
            etAddress.setError("Address required");
            etAddress.requestFocus();
            return;
        }

        if (barangay.isEmpty()) {
            etBarangay.setError("Barangay required");
            etBarangay.requestFocus();
            return;
        }

        if (city.isEmpty()) {
            etCity.setError("City required");
            etCity.requestFocus();
            return;
        }

        if (province.isEmpty()) {
            etProvince.setError("Province required");
            etProvince.requestFocus();
            return;
        }

        // Save fields to central manager storage instance
        UserManager um = UserManager.getInstance();
        um.setFirstName(firstName);
        um.setLastName(lastName);
        um.setMiddleName(etMiddleName.getText().toString().trim());
        um.setPhoneNumber(phoneNumber);
        um.setAddress(address);
        um.setBarangay(barangay);
        um.setCity(city);
        um.setProvince(province);

        Toast.makeText(this, "Profile Saved Successfully", Toast.LENGTH_SHORT).show();

        Intent intent = new Intent(SetupProfileActivity.this, MainActivity.class);
        intent.putExtra("USER_NAME", um.getFullName());
        startActivity(intent);
        finish();
    }
}
