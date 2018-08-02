package arindatiko.example.com.travelmecustomer;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Toast;

import arindatiko.example.com.travelmecustomer.model.User;
import arindatiko.example.com.travelmecustomer.util.SessionManager;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EditProfilActivity extends AppCompatActivity {

    @BindView(R.id.et_nama)
    EditText etNama;
    @BindView(R.id.et_username)
    EditText etUsername;
    @BindView(R.id.et_notelp)
    EditText etNotelp;
    @BindView(R.id.rv_simpan)
    RelativeLayout rvSimpan;

    SessionManager sessionManager;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profil);

        ButterKnife.bind(this);
        sessionManager = new SessionManager(this);

        etNama.setText(sessionManager.getNamaLengkap());
        etUsername.setText(sessionManager.getUsername());
        etNotelp.setText(sessionManager.getNoTelp());

        Toast.makeText(this, sessionManager.getPassword(), Toast.LENGTH_SHORT).show();
    }

    @OnClick(R.id.rv_simpan)
    public void toSimpan(View view){
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Please wait...");
        progressDialog.show();

        API.service_post.edit_profil(sessionManager.getId(),
                etNama.getText().toString(),
                etUsername.getText().toString(),
                sessionManager.getPassword(),
                "customer",
                etNotelp.getText().toString(),
                sessionManager.getStatus()).enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                User user = response.body();

                Toast.makeText(EditProfilActivity.this, "Data berhasil disimpan", Toast.LENGTH_SHORT).show();

                etNama.setText(user.getNama_lengkap());
                etUsername.setText(user.getUsername());
                etNotelp.setText(user.getNo_telp());

                progressDialog.dismiss();
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {

            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void onBackPressed(){
        Intent i = new Intent(EditProfilActivity.this, MainActivity.class);
        startActivity(i);
        finish();
    }
}
