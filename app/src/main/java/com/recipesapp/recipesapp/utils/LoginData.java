package com.recipesapp.recipesapp.utils;

import androidx.databinding.BaseObservable;
import androidx.databinding.Bindable;

import com.recipesapp.recipesapp.BR;

public class LoginData extends BaseObservable {
    private String email;
    private String password;
    private boolean correct;


    public LoginData(String email, String password) {
        this.email = email;
        this.password = password;
        this.correct = false;
    }

    @Bindable
    public String getEmail() {
        return email;
    }

    @Bindable
    public boolean getCorrect() {
        return correct;
    }

    @Bindable
    public String getPassword() {
        return password;
    }

    public void setEmail(String email){
        this.email = email;
        this.correct = isEnabled();
        notifyPropertyChanged(BR.email);
        notifyPropertyChanged(BR.password);
        notifyPropertyChanged(BR.correct);
    }

    public void setPassword(String password) {
        this.password = password;
        this.correct = isEnabled();
        notifyPropertyChanged(BR.email);
        notifyPropertyChanged(BR.password);
        notifyPropertyChanged(BR.correct);
    }

    public void setCorrect(boolean correct) {
        this.correct = correct;
        notifyPropertyChanged(BR.correct);
    }

    private boolean isEnabled() {
        return getEmail().length() !=0  && getPassword().length() >= 6;
    }

}