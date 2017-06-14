package com.example.manan.hygiea;


import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.GeolocationPermissions;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;
import android.widget.TextView;


/**
 * A simple {@link Fragment} subclass.
 */
public class LocateFragent extends Fragment {

    WebView web;
    ProgressBar prog;


    public LocateFragent() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_locate_fragent, container, false);
        web = (WebView)view.findViewById(R.id.webview);
        prog = (ProgressBar)view.findViewById(R.id.progressBar2);
        web.setWebViewClient(new myWebClient());
        web.loadUrl("https://www.google.co.in/maps/search/nearest+hospitals");
        web.setWebChromeClient(new WebChromeClient() {
            public void onGeolocationPermissionsShowPrompt(String origin, GeolocationPermissions.Callback callback) {
                callback.invoke(origin, true, false);
            }
        });
        WebSettings webSettings = web.getSettings();
        webSettings.setJavaScriptEnabled(true);

        return view;
    }

    public class myWebClient extends WebViewClient
    {



        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            prog.setVisibility(View.VISIBLE);
            if (url.startsWith("tel:")) {
                Intent intent = new Intent(Intent.ACTION_DIAL,
                        Uri.parse(url));
                startActivity(intent);
            }else if(url.startsWith("http:") || url.startsWith("https:")) {
                view.loadUrl(url);
            }
            return true;
        }



        @Override
        public void onPageFinished(WebView view, String url) {
            prog.setVisibility(View.GONE);
            super.onPageFinished(view, url);
        }
    }
}
