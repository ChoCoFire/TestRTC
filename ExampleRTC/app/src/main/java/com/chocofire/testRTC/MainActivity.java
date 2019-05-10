package com.chocofire.testRTC;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.net.http.SslError;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.webkit.PermissionRequest;
import android.webkit.SslErrorHandler;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import java.nio.charset.MalformedInputException;

public class MainActivity extends AppCompatActivity {

    private WebView activity_main_webview;
    private boolean loadingFinished;
    private boolean redirect;
    private ProgressDialog dialogLoading;
    private final int requestCode_camera = 1001;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.CAMERA)
                == PackageManager.PERMISSION_DENIED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    requestCode_camera); //number request code
        }

        dialogLoading = ProgressDialog.show(MainActivity.this, "", "Loading. Please wait...", true);

        activity_main_webview = findViewById(R.id.activity_main_webview);

        if (Build.VERSION.SDK_INT >= 21) {
            activity_main_webview.getSettings().setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
        }
        activity_main_webview.getSettings().setJavaScriptEnabled(true);
        activity_main_webview.getSettings().setAppCacheEnabled(true);
        activity_main_webview.getSettings().setAllowContentAccess(true);
        activity_main_webview.getSettings().setDomStorageEnabled(true);
        activity_main_webview.getSettings().setBuiltInZoomControls(true);

        activity_main_webview.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
        activity_main_webview.getSettings().setPluginState(WebSettings.PluginState.ON);
        activity_main_webview.getSettings().setMediaPlaybackRequiresUserGesture(false);

        activity_main_webview.setInitialScale(100);
        activity_main_webview.getSettings().setUseWideViewPort(true);
        activity_main_webview.setWebViewClient(new CustomWebViewClient());
        activity_main_webview.setWebChromeClient(webChromeClient);
        activity_main_webview.loadUrl("https://warm-basin-66481.herokuapp.com");
    }

    public class CustomWebViewClient extends WebViewClient {
        @Override
        public void onReceivedSslError(WebView view, final SslErrorHandler handler, SslError error) { //ดัก ssl
            String message = getSslErrorMessage(error);
            String proceed = "Proceed";
            String cancel = "Cancel";
            AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext()).setCancelable(false);
            builder.setMessage(message)
                    .setPositiveButton(proceed, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            handler.proceed();
                        }
                    })
                    .setNegativeButton(cancel, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            handler.cancel();
                        }
                    });
            builder.create().show();
        }

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String urlNewString) {
            if (!loadingFinished) {
                redirect = true;
            }

            loadingFinished = false;
            view.loadUrl(urlNewString);
            return true;
        }

        @Override
        public void onPageStarted(WebView view, String url, Bitmap facIcon) {
            loadingFinished = false;
            //SHOW LOADING IF IT ISNT ALREADY VISIBLE
            if(!dialogLoading.isShowing())
            dialogLoading.show();
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            if (!redirect) {
                loadingFinished = true;
            }

            if (loadingFinished && !redirect) {
                //HIDE LOADING IT HAS FINISHED
                if(dialogLoading.isShowing())
                dialogLoading.dismiss();
            } else {
                redirect = false;
            }

        }
    }

    private String getSslErrorMessage(SslError error) {
        switch (error.getPrimaryError()) {
            case SslError.SSL_DATE_INVALID:
                return "The certificate date is invalid.";
            case SslError.SSL_EXPIRED:
                return "The certificate has expired.";
            case SslError.SSL_IDMISMATCH:
                return "The certificate hostname mismatch.";
            case SslError.SSL_INVALID:
                return "The certificate is invalid.";
            case SslError.SSL_NOTYETVALID:
                return "The certificate is not yet valid";
            case SslError.SSL_UNTRUSTED:
                return "The certificate is untrusted.";
            default:
                return "SSL Certificate error.";
        }
    }

    public WebChromeClient webChromeClient = new WebChromeClient() {

        @Override
        public void onPermissionRequest(final PermissionRequest request) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                request.grant(request.getResources());
            }
        }

        //The undocumented magic method override
        //Eclipse will swear at you if you try to put @Override here
        // For Android 3.0+
        public void openFileChooser(ValueCallback<Uri> uploadMsg) {

            mUploadMessage = uploadMsg;
            Intent i = new Intent(Intent.ACTION_GET_CONTENT);
            i.addCategory(Intent.CATEGORY_OPENABLE);
            i.setType("image/*");
            MainActivity.this.startActivityForResult(Intent.createChooser(i, "File Chooser"), FILECHOOSER_RESULTCODE);

        }

        // For Android 3.0+
        public void openFileChooser(ValueCallback uploadMsg, String acceptType) {
            mUploadMessage = uploadMsg;
            Intent i = new Intent(Intent.ACTION_GET_CONTENT);
            i.addCategory(Intent.CATEGORY_OPENABLE);
            i.setType("*/*");
            MainActivity.this.startActivityForResult(
                    Intent.createChooser(i, "File Browser"),
                    FILECHOOSER_RESULTCODE);
        }

        //For Android 4.1
        public void openFileChooser(ValueCallback<Uri> uploadMsg, String acceptType, String capture) {
            mUploadMessage = uploadMsg;
            Intent i = new Intent(Intent.ACTION_GET_CONTENT);
            i.addCategory(Intent.CATEGORY_OPENABLE);
            i.setType("image/*");
            MainActivity.this.startActivityForResult(Intent.createChooser(i, "File Chooser"), MainActivity.FILECHOOSER_RESULTCODE);

        }

        // For Lollipop 5.0+ Devices
        public boolean onShowFileChooser(WebView mWebView, ValueCallback<Uri[]> filePathCallback, WebChromeClient.FileChooserParams fileChooserParams) {
            if (mUploadMessage2 != null) {
                mUploadMessage2.onReceiveValue(null);
                mUploadMessage2 = null;
            }

            mUploadMessage2 = filePathCallback;
            Intent intent = null;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                intent = fileChooserParams.createIntent();
            }
            try {
                startActivityForResult(intent, FILECHOOSER_RESULTCODE);
            } catch (ActivityNotFoundException e) {
                mUploadMessage2 = null;
                Toast.makeText(getApplicationContext(), "Cannot Open File Chooser", Toast.LENGTH_LONG).show();
                return false;
            }
            return true;
        }

    };

    private ValueCallback<Uri[]> mUploadMessage2;
    private ValueCallback<Uri> mUploadMessage;
    private final static int FILECHOOSER_RESULTCODE = 1;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {

        if (android.os.Build.VERSION.SDK_INT < android.os.Build.VERSION_CODES.LOLLIPOP) {
            if (requestCode == FILECHOOSER_RESULTCODE) {
                if (null == mUploadMessage) return;
                Uri result = intent == null || resultCode != RESULT_OK ? null : intent.getData();
                mUploadMessage.onReceiveValue(result);
                mUploadMessage = null;

            }
        } else {
            if (null == mUploadMessage2)
                return;
            Uri result;

            if (intent == null || resultCode != Activity.RESULT_OK) {
                result = null;
            } else {
                result = intent.getData();
            }

            if (result != null) {
//                Log.v(TAG, TAG + " # result.getPath()=" + result.getPath());
                mUploadMessage2.onReceiveValue(new Uri[]{result});
            } else {
                mUploadMessage2.onReceiveValue(new Uri[]{});
            }
            mUploadMessage2 = null;
        }
    }

    @Override
    public void onBackPressed() {
        if (activity_main_webview.canGoBack()) {
            activity_main_webview.goBack();
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == requestCode_camera) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "camera permission granted", Toast.LENGTH_LONG).show();
                activity_main_webview.loadUrl("https://warm-basin-66481.herokuapp.com"); //load again
            } else {
                Toast.makeText(this, "camera permission denied", Toast.LENGTH_LONG).show();
                finish();
            }
        }
    }//end onRequestPermissionsResult
}