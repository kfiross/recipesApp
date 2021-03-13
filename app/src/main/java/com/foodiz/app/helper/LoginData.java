package com.foodiz.app.helper;

import androidx.databinding.BaseObservable;
import androidx.databinding.Bindable;

import com.foodiz.app.BR;

public class LoginData extends BaseObservable {
    private String name;
    private String email;
    private String password;
    private boolean correct;


    public LoginData(String email, String password, String name) {
        this.name = name;
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

    @Bindable
    public String getName() {
        return name;
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

    public void setName(String name) {
        this.name = name;
        this.correct = isEnabled();
        notifyPropertyChanged(BR.name);
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