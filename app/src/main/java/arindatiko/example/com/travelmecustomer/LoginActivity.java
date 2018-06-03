package arindatiko.example.com.travelmecustomer;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.design.widget.TextInputEditText;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.xwray.passwordview.PasswordView;

import java.sql.Array;
import java.util.ArrayList;
import java.util.List;

import arindatiko.example.com.travelmecustomer.model.ResponseApi;
import arindatiko.example.com.travelmecustomer.model.User;
import arindatiko.example.com.travelmecustomer.model.Wisata;
import arindatiko.example.com.travelmecustomer.util.LocationService;
import arindatiko.example.com.travelmecustomer.util.SessionManager;
import arindatiko.example.com.travelmecustomer.util.Validation;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {

    @BindView(R.id.btnLogin)
    Button btnLogin;
    @BindView(R.id.btnDaftar)
            Button btnDaftar;
    @BindView(R.id.login)
            TextView textLogin;
    @BindView(R.id.signup)
            TextView textSignUp;
    @BindView(R.id.txtTelp)
    TextInputEditText txtTelp;
    @BindView(R.id.txtPassword)
    PasswordView txtPassword;
    @BindView(R.id.txtUsername_daftar)
            EditText txtUsername;
    @BindView(R.id.txtTelp_daftar)
            EditText txtTelp_daftar;
    @BindView(R.id.txtPassword_daftar)
            PasswordView txtPassword_daftar;
    @BindView(R.id.layout_login)
            LinearLayout layoutLogin;
    @BindView(R.id.layout_daftar)
            LinearLayout layoutSignup;

    private ProgressDialog progressDialog;
    private SessionManager sessionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, 1);
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, 1);
        }

        startService(new Intent(LoginActivity.this, LocationService.class));
        sessionManager = new SessionManager(getApplicationContext());
        if (sessionManager.isLogin()) {
            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
            startActivity(intent);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            finish();
        }
    }

    @OnClick(R.id.btnLogin)
    public void toLogin(View view) {
        if (Validation.checkEmpty(txtTelp) && Validation.checkPassword(this, txtPassword)) {
            progressDialog = new ProgressDialog(this);
            progressDialog.setMessage("Please wait...");
            progressDialog.show();

            //String telp = sessionManager.

            API.service_post.login(txtTelp.getText().toString(), txtPassword.getText().toString()).enqueue(new Callback<User>() {
                @Override
                public void onResponse(Call<User> call, Response<User> response) {
                    User user = response.body();

                    if (user != null) {
                        sessionManager.setUid(user.getId_user().toString());
                        sessionManager.setLogin(true);
                        sessionManager.setUsername(user.getUsername());
                        sessionManager.setUsertype(user.getUser_type());

                        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                        startActivity(intent);
                        finish();
                    } else {
                        Toast.makeText(LoginActivity.this, "User not found!", Toast.LENGTH_SHORT).show();
                    }

                    progressDialog.dismiss();
                }

                @Override
                public void onFailure(Call<User> call, Throwable t) {
                    Toast.makeText(LoginActivity.this, "Error login! Check your phone number and password.", Toast.LENGTH_SHORT).show();
                    Log.e("test", String.valueOf(t.fillInStackTrace()));
                    progressDialog.dismiss();
                }
            });
        }
    }

    @OnClick(R.id.btnDaftar)
    public void toRegister(View view){
        if (Validation.checkEmpty(txtTelp_daftar) && Validation.checkPassword(this, txtPassword_daftar) && Validation.checkEmpty(txtUsername)) {
            progressDialog = new ProgressDialog(this);
            progressDialog.setMessage("Please wait...");
            progressDialog.show();

            //String telp = sessionManager.

            API.service_post.register(txtUsername.getText().toString(), txtTelp_daftar.getText().toString(), txtPassword_daftar.getText().toString(), "customer" ).enqueue(new Callback<User>() {
                @Override
                public void onResponse(Call<User> call, Response<User> response) {
                    User user = response.body();
                    if(user != null){
                        sessionManager.setUid(user.getId_user());
                        sessionManager.setUsername(user.getUsername());
                        sessionManager.setUsertype(user.getUser_type());

                        Toast.makeText(LoginActivity.this, "Anda berhasil regiter, silahkan login.", Toast.LENGTH_SHORT).show();
                        progressDialog.dismiss();
                    }
                }

                @Override
                public void onFailure(Call<User> call, Throwable t) {
                    Toast.makeText(LoginActivity.this, "Error login! Check your phone number and password.", Toast.LENGTH_SHORT).show();
                    progressDialog.dismiss();
                }
            });
        }
    }

    @OnClick(R.id.login)
    public void toMasuk(View view){
        layoutLogin.setVisibility(View.VISIBLE);
        layoutSignup.setVisibility(View.GONE);
    }

    @OnClick(R.id.signup)
    public void toDaftar(View view){
        layoutSignup.setVisibility(View.VISIBLE);
        layoutLogin.setVisibility(View.GONE);
    }
}
