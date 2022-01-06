package com.devarshi.buzoclone;

import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;

import com.google.android.ads.nativetemplates.TemplateView;
import com.google.android.gms.ads.nativead.NativeAd;

public class ExitDialog extends Dialog {

    Activity activity;
    NativeAd ad;

    public ExitDialog(Activity activity, NativeAd ad){

        super(activity);
        this.activity = activity;
        this.ad = ad;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.exit_dialog);

        Button btnYes = findViewById(R.id.btn_yes);
        Button btnNo = findViewById(R.id.btn_no);

        btnYes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activity.finish();
            }
        });

        btnNo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        TemplateView templateView = findViewById(R.id.my_template);

        if (this.ad == null){
            templateView.setVisibility(View.GONE);
        }
        else {
            templateView.setVisibility(View.VISIBLE);
            templateView.setNativeAd(this.ad);
        }
    }
}
