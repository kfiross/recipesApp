package com.foodiz.app.views.dialogs;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.RadioButton;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.DialogFragment;
import androidx.navigation.NavController;
import androidx.navigation.NavOptions;
import androidx.navigation.Navigation;

import com.foodiz.app.MainActivity;
import com.foodiz.app.R;
import com.foodiz.app.databinding.DialogLanguageFragmentBinding;
import com.foodiz.app.helper.MediaQueryHelper;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class LanguageDialogFragment extends DialogFragment {
    private DialogLanguageFragmentBinding mBinding;

    private String mCheckedLang;

    public LanguageDialogFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mBinding = DataBindingUtil.inflate(
                inflater, R.layout.dialog_language_fragment, container, false);

        // Inflate the layout for this fragment
        return mBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mBinding.radioBtnEn.setOnClickListener(this::onRadioButtonClicked);
        mBinding.radioBtnHe.setOnClickListener(this::onRadioButtonClicked);
        mBinding.btnConfirmLang.setOnClickListener(v -> changeLang());
    }

    public void onRadioButtonClicked(View view) {
        // Is the button now checked?
        boolean checked = ((RadioButton) view).isChecked();

        // Check which radio button was clicked
        switch (view.getId()) {
            case R.id.radio_btn_he:
                if (checked)
                    mCheckedLang = "he";
                break;
            case R.id.radio_btn_en:
                if (checked)
                    mCheckedLang = "en";
                break;
        }
    }

    private void refreshScreen(){
        NavController navController = Navigation.findNavController(getActivity(), R.id.nav_host_fragment);

        int currentFragmentID = navController.getCurrentDestination().getId();

        NavOptions navOptions = new NavOptions.Builder()
                .setLaunchSingleTop(true)
                .build();

        navController.navigate(currentFragmentID, null, navOptions);

        BottomNavigationView navigationView = getActivity().findViewById(R.id.bttmNav);
        // refresh bottom navigation menu
        navigationView.getMenu().clear();
        navigationView.inflateMenu(R.menu.navigation_menu);
        navigationView.setSelectedItemId(R.id.nav_my_recipes);
    }

    private void changeLang() {
        String locale = "";
        switch (mCheckedLang) {
            case "he":
                MainActivity.preferencesConfig.writeLangStatus("he");
                locale = "iw";
                break;


            case "en":
                MainActivity.preferencesConfig.writeLangStatus("en");
                locale = "en";
                break;
        }


        MainActivity.preferencesConfig.setLocal(locale, getContext());

        refreshScreen();

        dismiss();
    }

    @Override
    public void onResume() {
        super.onResume();
        WindowManager.LayoutParams params = getDialog().getWindow().getAttributes();

        params.width = MediaQueryHelper.wp(getContext(), 90);
        params.height = MediaQueryHelper.hp(getContext(), 37);

        getDialog().getWindow().setAttributes(params);
    }
}
