package com.pagasys.welcome.pegasysattendence;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.webkit.JavascriptInterface;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.lang.annotation.Annotation;

public class WebviewActivity extends AppCompatActivity implements Annotation {
    WebView webView_google;
    EditText text_search;
    Button button_search;
    String input = "radhan@gokaldasexports.com";
    String password = "radhan@123";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_webview);
        webView_google = (WebView) findViewById(R.id.wv_google);
        webView_google.getSettings().setJavaScriptEnabled(true);
        text_search = (EditText) findViewById(R.id.et_search);
        button_search = (Button) findViewById(R.id.bt_search);

        webView_google.loadUrl("file:///android_asset/html_one.html");
//        webView_google.loadUrl("https://www.google.co.in/?gfe_rd=cr&dcr=0&ei=7ObIWeaLFMSL8Qe7u4Ro");
//        webView_google.loadUrl("http://107.180.69.138/gokaldas_exports/Authentication");


        webView_google.getSettings().setDomStorageEnabled(true);

        webView_google.addJavascriptInterface(new Object() {
            Context context;

            @JavascriptInterface
            public void performClick(Context context, String Str) {
                this.context = context;
                Toast.makeText(context, Str + "HTML Buttons onClick", Toast.LENGTH_SHORT).show();

            }
        }, "Ok");
        webView_google.setWebViewClient(new MyWebViewClient());

    }

    @Override
    public Class<? extends Annotation> annotationType() {
        return null;
    }

    private class MyWebViewClient extends WebViewClient {

        @Override
        public void onPageFinished(WebView view, String url) {
            view.loadUrl("javascript:" + "document.getElementBy('ok').click();");
//            view.loadUrl("javascript:" + "var a =document.getElementById(\"user_email\").value='" + input + "'; " + "var a =document.getElementById(\"user_password\").value='" + password + "'; " + "document.getElementsByClassName('btn btn-success btn-submit')[0].click();");
            /*view.loadUrl("javascript:" + "document.getElementById('lst-ib').value='apple';"
                    + "document.getElementById('tsf').submit();");*/
            Toast.makeText(getApplicationContext(), "HTML onClick", Toast.LENGTH_SHORT).show();
            super.onPageFinished(view, url);


        }

    }
}