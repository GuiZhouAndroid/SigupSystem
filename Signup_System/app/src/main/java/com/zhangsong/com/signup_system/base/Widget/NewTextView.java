package com.zhangsong.com.signup_system.base.Widget;


import android.content.Context;
import android.util.AttributeSet;

public class NewTextView extends android.support.v7.widget.AppCompatEditText {
    public NewTextView(Context context) {
        super(context);
        // TODO Auto-generated constructor stub
    }

    public NewTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        // TODO Auto-generated constructor stub
    }

    public NewTextView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        // TODO Auto-generated constructor stub
    }

    @Override
    protected boolean getDefaultEditable() {//禁止EditText被编辑
        return false;
    }
}

