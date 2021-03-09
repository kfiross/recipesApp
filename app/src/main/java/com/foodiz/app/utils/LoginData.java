package com.foodiz.app.utils;

import androidx.databinding.BaseObservable;
import androidx.databinding.Bindable;

import com.foodiz.app.BR;




public class LoginData extends BaseObservable {

    private final int MIN_LEN_PASS = 6;
    private final int MIN_LEN_EMAIL = 0;
    //------------DATA MEMBERS-------------------
    private String name;
    private String mail;
    private String password;
    private boolean correct;
    //--------------------------------------------


    public LoginData(String mail, String password, String name) {
        this.name = name;
        this.mail = mail;
        this.password = password;
        this.correct = false;
    }

    @Bindable
    public String getEmail() {
        return mail;
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
        this.mail = email;
        this.correct = valid();
        notifyPropertyChanged(BR.email);
        notifyPropertyChanged(BR.password);
        notifyPropertyChanged(BR.correct);
    }

    public void setPassword(String password) {
        this.password = password;
        this.correct = valid();
        notifyPropertyChanged(BR.email);
        notifyPropertyChanged(BR.password);
        notifyPropertyChanged(BR.correct);
    }

    public void setName(String name) {
        this.name = name;
        this.correct = valid();
        notifyPropertyChanged(BR.name);
        notifyPropertyChanged(BR.correct);
    }

    public void setCorrect(boolean correct) {
        this.correct = correct;
        notifyPropertyChanged(BR.correct);
    }

    private boolean valid()
    {
        boolean res1 = getEmail().length() !=MIN_LEN_EMAIL;
        boolean res2 = getPassword().length() >= MIN_LEN_PASS;
        return (res1 && res2);
    }

}