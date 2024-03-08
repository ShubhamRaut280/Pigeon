package com.pigeonchat.Utility;

import android.content.Context;
import android.webkit.JavascriptInterface;
import android.widget.Toast;

public class InterfaceJava {
    private Context mContext;
    private String sender;
    private String receiver;
    private String connId;

    public InterfaceJava(Context context, String sender,String receiver, String connId) {
        this.mContext = context;
        this.sender = sender;
        this.receiver = receiver;
        this.connId = connId;
    }


    @JavascriptInterface
    public String getReceiverId() {
        return receiver;
    }



    @JavascriptInterface
    public String getSenderId() {
        return sender;
    }

    @JavascriptInterface
    public String getConnId() {
        return connId;
    }

    @JavascriptInterface
    public void showToast(String message) {
        Toast.makeText(mContext, message, Toast.LENGTH_SHORT).show();
    }

    private WebViewCallback callback;

    public InterfaceJava(WebViewCallback callback) {
        this.callback = callback;
    }


    public interface WebViewCallback {
        void onHangup();
    }
}

