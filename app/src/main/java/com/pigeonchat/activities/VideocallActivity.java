package com.pigeonchat.activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.webkit.PermissionRequest;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.pigeonchat.R;
import com.pigeonchat.Utility.InterfaceJava;
import com.pigeonchat.databinding.ActivityVideocallBinding;

public class VideocallActivity extends AppCompatActivity {
    ActivityVideocallBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        binding = ActivityVideocallBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });


        String connId = getIntent().getStringExtra("connId");
        String receiver = getIntent().getStringExtra("receiver");
        String sender = getIntent().getStringExtra("sender");


        Log.d("videocall", "onCreate: from videocall connectin id "+connId+ " sender : " +sender + "receiver :"+ receiver);




        if(connId!=null && connId.length()>0)
        {
            setupWebViewForconnectionForHim(connId,sender,receiver);
        }else
        {
            setupWebViewForconnectionForMe(sender,receiver);
        }




    }

//    @Override
//    protected void onDestroy() {
//        super.onDestroy();
//        super.onDestroy();
//        binding.webView.destroy();
//        new Handler().postDelayed(() -> removeRequest(),3000);
//
//        getOnBackPressedDispatcher().onBackPressed();
//    }

    void setupWebViewForconnectionForHim(String connId, String sender , String receiver) {
        binding.webView.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onPermissionRequest(PermissionRequest request) {
                request.grant(request.getResources());

            }
        });

        binding.webView.getSettings().setJavaScriptEnabled(true);
        binding.webView.getSettings().setMediaPlaybackRequiresUserGesture(false);
        binding.webView.addJavascriptInterface(new InterfaceJava(this, sender,receiver, connId), "Android");


        loadVideoCall();
    }
    void setupWebViewForconnectionForMe( String sender , String receiver) {
        binding.webView.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onPermissionRequest(PermissionRequest request) {
                request.grant(request.getResources());

            }
        });

        binding.webView.getSettings().setJavaScriptEnabled(true);
        binding.webView.getSettings().setMediaPlaybackRequiresUserGesture(false);
        binding.webView.addJavascriptInterface(new InterfaceJava(this, sender,receiver, null), "Android");
        loadVideoCall();





    }
    public void loadVideoCall() {
        String filePath = "https://da24-2402-8100-31bc-1f08-4d93-ccbf-354d-6cac.ngrok-free.app";
        binding.webView.loadUrl(filePath);

        binding.webView.setWebViewClient(new WebViewClient() {


            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
            }

        });



    }





    private void removeRequest() {

        FirebaseAuth auth = FirebaseAuth.getInstance();
        FirebaseUser user = auth.getCurrentUser();
        if(user!=null)
        {
            String uid =user.getUid();
            DatabaseReference dbref = FirebaseDatabase.getInstance().getReference().child("callRequest");

            dbref.child(uid).removeValue()
                    .addOnSuccessListener(aVoid -> Log.d("Firebase", "Room deleted successfully."))
                    .addOnFailureListener(e -> Log.e("Firebase", "Error deleting room", e));


        }

    }
}