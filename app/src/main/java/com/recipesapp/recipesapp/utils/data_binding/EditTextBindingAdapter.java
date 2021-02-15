//package com.recipesapp.recipesapp.utils.data_binding;
//
//import android.text.Editable;
//import android.text.TextWatcher;
//import android.widget.TextView;
//
//import androidx.databinding.BindingAdapter;
//import androidx.databinding.InverseBindingAdapter;
//import androidx.databinding.InverseBindingListener;
//import androidx.databinding.adapters.ListenerUtil;
//import androidx.databinding.adapters.TextViewBindingAdapter;
//
//import com.recipesapp.recipesapp.R;
//
//public class EditTextBindingAdapter {
//    @InverseBindingAdapter(attribute = "android:text", event = "android:textAttrChanged")
//    public static String captureTextValue(TextView view) {
//        CharSequence newValue = view.getText();
//        return newValue.toString();
//    }
//
//    @BindingAdapter(value = {"android:beforeTextChanged", "android:onTextChanged",
//            "android:afterTextChanged", "android:textAttrChanged"},
//            requireAll = false)
//    public static void setTextWatcher(TextView view, final TextViewBindingAdapter.BeforeTextChanged before,
//                                      final TextViewBindingAdapter.OnTextChanged on, final TextViewBindingAdapter.AfterTextChanged after,
//                                      final InverseBindingListener textAttrChanged) {
//        TextWatcher newValue = new TextWatcher() {
//
//            @Override
//            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
//
//            }
//
//            @Override
//            public void onTextChanged(CharSequence s, int start, int before, int count) {
//                if (on != null) {
//                    on.onTextChanged(s, start, before, count);
//                }
//                if (textAttrChanged != null) {
//                    textAttrChanged.onChange();
//                }
//            }
//
//            @Override
//            public void afterTextChanged(Editable s) {
//
//            }
//        };
//
//        TextWatcher oldValue = ListenerUtil.trackListener(view, newValue, R.id.textWatcher);
//        if (oldValue != null) {
//            view.removeTextChangedListener(oldValue);
//        }
//        view.addTextChangedListener(newValue);
//    }
//
//}
