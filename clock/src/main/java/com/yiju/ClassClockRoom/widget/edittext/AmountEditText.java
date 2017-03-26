package com.yiju.ClassClockRoom.widget.edittext;

import android.content.Context;
import android.text.Editable;
import android.text.InputFilter;
import android.text.Spanned;
import android.util.AttributeSet;
import android.widget.EditText;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

/**
 * 作者： 葛立平
 * 金额输入框_小数点后两位
 * 2016/5/26 19:09
 */
public class AmountEditText extends EditText {

    public AmountEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        setFilters(new InputFilter[]{lengthfilter});
    }

    public static boolean stringFilter(String str) throws PatternSyntaxException {
        String regEx = "^(([1-9]{1}\\d*)|([0]{1}))(\\.(\\d){0,2})?$";
        Pattern p = Pattern.compile(regEx);
        Matcher m = p.matcher(str);
        return m.matches();
    }

    InputFilter lengthfilter = new InputFilter() {

        @Override
        public CharSequence filter(CharSequence source, int start,
                                   int end, Spanned dest, int dstart, int dend) {
            if (!"".equals(source)) {
                StringBuilder sb = new StringBuilder(dest);
                sb.insert(dstart, source);
                if (!stringFilter(sb.toString())) {
                    return "";
                }
            }
            return source;
        }
    };

    @Override
    public Editable getText() {
        String text = super.getText().toString();
        if (text.endsWith(".")) {
            return Editable.Factory.getInstance().newEditable(text + "00");
        } else if (text.endsWith(".0")) {
            return Editable.Factory.getInstance().newEditable(text + "0");
        }
        return super.getText();
    }

}
