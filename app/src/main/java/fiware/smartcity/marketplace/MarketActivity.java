package fiware.smartcity.marketplace;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.transition.Scene;
import android.transition.TransitionManager;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.RelativeLayout;

import fiware.smartcity.Application;
import fiware.smartcity.MainActivity;
import fiware.smartcity.R;

/**
 * Created by francisco on 29/03/16.
 */
public class MarketActivity {
    private Context context;

    private static Activity activity;

    private Drawable x, y;
    private WebView webView;

    public MarketActivity(Context ctx) {
        context = ctx;
        activity = Application.mainActivity;

        x = activity.getResources().getDrawable(R.drawable.clear);
        x.setBounds(0, 0, 50, 50);

        y = activity.getResources().getDrawable(R.drawable.search);
        y.setBounds(0, 0, 50, 50);
    }

    public void start() {
        ViewGroup rootContainer = (ViewGroup) activity.findViewById(R.id.mainFrame);
        activity.getLayoutInflater().inflate(R.layout.route, rootContainer);

        ViewGroup routeContainer = (ViewGroup) activity.findViewById(R.id.frameRoute);
        Scene scene1 = Scene.getSceneForLayout(routeContainer, R.layout.activity_market, activity);

        TransitionManager.go(scene1);

        // Create a web view containing the Business API Ecosystem web page
        webView = (WebView) activity.findViewById(R.id.market);
        webView.setWebChromeClient(new WebChromeClient() {
            // Handle window.open requests in order to support PayPal redirection
            @Override
            public boolean onCreateWindow(WebView view, boolean isDialog, boolean isUserGesture, Message resultMsg) {
                webView.removeAllViews();
                webView.scrollTo(0, 0);

                WebView newView = new WebView(context);
                newView.getSettings().setJavaScriptEnabled(true);
                newView.setWebViewClient(new WebViewClient());

                // Create dynamically a new view
                newView.setLayoutParams(new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));

                webView.addView(newView);

                WebView.WebViewTransport transport = (WebView.WebViewTransport) resultMsg.obj;
                transport.setWebView(newView);
                resultMsg.sendToTarget();
                return true;
            }
        });

        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
        webView.getSettings().setSupportMultipleWindows(true);

        webView.loadUrl("https://demo-mwc.conwet.com/");

        setupHeader();
    }

    public void back() {
        ((MainActivity)activity).onMarketplaceClosed();
    }

    private void setupHeader() {
        Toolbar myToolbar = (Toolbar) activity.findViewById(R.id.my_toolbar);
        ((AppCompatActivity)activity).setSupportActionBar(myToolbar);

        ((AppCompatActivity)activity).getSupportActionBar().setTitle("Marketplace");
        ((AppCompatActivity)activity).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }
}
