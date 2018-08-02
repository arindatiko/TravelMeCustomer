package arindatiko.example.com.travelmecustomer;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
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

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.xwray.passwordview.PasswordView;

import java.sql.Array;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

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

    /*@BindView(R.id.btnLogin)
    Button btnLogin;
    @BindView(R.id.btnDaftar)
            Button btnDaftar;
    @BindView(R.id.btnVerify)
            Button btnVerify;
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
    @BindView(R.id.txtVerify)
            EditText txtVerify;
    @BindView(R.id.layout_login)
            LinearLayout layoutLogin;
    @BindView(R.id.layout_daftar)
            LinearLayout layoutSignup;*/

    private Button btnVerify, btnLogin;
    private TextInputEditText txtTelp_daftar, txtUsername, txtVerify;

    private ProgressDialog progressDialog;
    private SessionManager sessionManager;
    private FirebaseAuth mAuth;
    private String codeSent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        //ButterKnife.bind(this);

        /*btnVerify = (Button) findViewById(R.id.btnVerify);
        btnLogin = (Button) findViewById(R.id.btnLogin);

        txtTelp_daftar = (TextInputEditText) findViewById(R.id.txtTelp_daftar);
        txtVerify = (TextInputEditText) findViewById(R.id.txtVerify);
        txtUsername = (TextInputEditText) findViewById(R.id.txtUsername_daftar);

        mAuth = FirebaseAuth.getInstance();

        btnVerify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendVerificationCode();
            }
        });

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                verifySignInCode();
            }
        });*/
    }

    /*private void verifySignInCode() {
        String code = txtVerify.getText().toString();
        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(codeSent, code);
        signInWithPhoneAuthCredential(credential);
    }

    private void sendVerificationCode() {
        String phone = txtTelp_daftar.getText().toString();
        txtVerify.setVisibility(View.VISIBLE);

        if(phone.isEmpty()){
            txtTelp_daftar.setError("Harap Isi Nomor Telepon Anda");
            txtTelp_daftar.requestFocus();
            return;
        }

        if(phone.length()<20){
            txtTelp_daftar.setError("Harap Memasukkan Nomor Telepon dengan Benar");
            txtTelp_daftar.requestFocus();
            return;
        }

        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                phone,
                60,
                TimeUnit.SECONDS,
                this,
                mCallbacks
        );
    }*/

    /*@OnClick(R.id.btnVerify)
    public void toVerify(View view){


    }*/

    /*PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
        @Override
        public void onVerificationCompleted(PhoneAuthCredential phoneAuthCredential) {

        }

        @Override
        public void onVerificationFailed(FirebaseException e) {

        }

        @Override
        public void onCodeSent(String s, PhoneAuthProvider.ForceResendingToken forceResendingToken) {
            super.onCodeSent(s, forceResendingToken);

            codeSent = s;
        }
    };

   *//* @OnClick(R.id.btnLogin)
    public void toLogin(View view) {


    }*//*

    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential){
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                            startActivity(intent);
                            finish();
                        }else{
                            if(task.getException() instanceof FirebaseAuthInvalidCredentialsException){
                                Toast.makeText(LoginActivity.this, "Harap memasukkan kode dengan benar", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                });
    }*/

    /*@OnClick(R.id.btnDaftar)
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
                        sessionManager.setUid(String.valueOf(user.getId_user()));
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
    }*/
}
