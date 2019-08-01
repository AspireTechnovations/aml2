package in.aspiretechnovations.aml.fragments;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import in.aspiretechnovations.aml.R;
import in.aspiretechnovations.aml.activities.OtpLoginActivity;
import in.aspiretechnovations.aml.activities.WishlistActivity;
import in.aspiretechnovations.aml.utils.AppPref;

public class MyAccountFragment extends Fragment {

    private View view;
    private TextView tv_username, tv_mobile_no, tv_email_id;

    private CardView cv_logout, cv_wishlist;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
         super.onCreateView(inflater, container, savedInstanceState);

         view = inflater.inflate(R.layout.fragment_account, container, false);
        tv_username = view.findViewById(R.id.tv_username);
        tv_mobile_no = view.findViewById(R.id.tv_mobile_no);
        tv_email_id = view.findViewById(R.id.tv_email_id);
        cv_logout = view.findViewById(R.id.cv_logout);
        cv_wishlist = view.findViewById(R.id.cv_wishlist);

        cv_wishlist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getContext(), WishlistActivity.class));
            }
        });
        cv_logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AppPref.setLogout(getContext());
                startActivity(new Intent(getContext(), OtpLoginActivity.class));
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                    getActivity().finishAffinity();
                }else
                    getActivity().finish();
            }
        });
        if (AppPref.getUsername(getContext()) != null)
            tv_username.setText(AppPref.getUsername(getContext()));

        if (AppPref.getKeyMobileNo(getContext()) != null)
            tv_mobile_no.setText(AppPref.getKeyMobileNo(getContext()));


        if (AppPref.getEmailId(getContext()) != null)
            tv_email_id.setText(AppPref.getEmailId(getContext()));

        return view;
    }
}
