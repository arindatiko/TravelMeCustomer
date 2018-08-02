package arindatiko.example.com.travelmecustomer.fragment;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import arindatiko.example.com.travelmecustomer.EditProfilActivity;
import arindatiko.example.com.travelmecustomer.LoginActivityBackup;
import arindatiko.example.com.travelmecustomer.MainActivity;
import arindatiko.example.com.travelmecustomer.R;
import arindatiko.example.com.travelmecustomer.TentangActivity;
import arindatiko.example.com.travelmecustomer.util.SessionManager;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class AccountFragment extends Fragment {

    @BindView(R.id.tv_nama)
    TextView tvNama;
    @BindView(R.id.tv_edit)
    TextView tvEdit;
    @BindView(R.id.tv_username)
    TextView tvUsername;
    @BindView(R.id.tv_nomor)
    TextView tvNomor;
    @BindView(R.id.ln_tentang)
    LinearLayout lnTentang;
    @BindView(R.id.ln_logout)
    LinearLayout lnLogout;

    SessionManager sessionManager;

    public AccountFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_account, container, false);
        ButterKnife.bind(this, v);

        sessionManager = new SessionManager(getActivity());

        tvNama.setText(sessionManager.getNamaLengkap());
        tvNomor.setText(sessionManager.getNoTelp());
        tvUsername.setText(sessionManager.getUsername());

        return v;
    }

    @OnClick(R.id.tv_edit)
    public void toEdit(View v){
        Intent i = new Intent(getActivity(), EditProfilActivity.class);
        startActivity(i);
        getActivity().finish();
    }

    @OnClick(R.id.ln_tentang)
    public void toTentang(View v){
        Intent i = new Intent(getActivity(), TentangActivity.class);
        startActivity(i);
        getActivity().finish();
    }

    @OnClick(R.id.ln_logout)
    public void toLogout(View v){
        sessionManager.setLogin(false);
        Intent intent = new Intent(getActivity(),LoginActivityBackup.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }

    public void onResume(){
        super.onResume();
    }
}
