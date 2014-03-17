package mobi.tarantio.qr_codeauth.app;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.Loader;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import mobi.tarantio.qr_codeauth.app.loader.AbstractServerResponse;
import mobi.tarantio.qr_codeauth.app.loader.ServerLoaderCallbacks;


public class MainActivity extends ActionBarActivity implements ServerLoaderCallbacks.Listener {
    private int LOADER_ID = 1;
    private ServerLoaderCallbacks loaderCallbacks;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//        Loader<Object> seLoader = getSupportLoaderManager().getLoader(LOADER_ID);
//        if (seLoader == null) {
//            IntentIntegrator.initiateScan(this);
//        }

        initLoader("http://qrcodeauth.ifacesoft.ru/auth/?c=3424234&s=wqe");

    }

    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        IntentResult scanResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, intent);
        if (scanResult != null) {
            String text = getString(R.string.qr_result_init);
            TextView textView = (TextView) findViewById(R.id.qr_result);

            android.util.Log.d("----", scanResult.getContents());

            if (IntentIntegrator.QR_CODE_TYPES.equals(scanResult.getFormatName())) {
                textView.setText(String.format(text, scanResult.getContents()));

                initLoader(scanResult.getContents());

            } else {
                Toast.makeText(this, R.string.bad_bar_code, Toast.LENGTH_SHORT).show();
                IntentIntegrator.initiateScan(this);
            }

        }
    }

    //     http://qrcodeauth.ifacesoft.ru/auth/?c=3424234&s=wqe
    private void initLoader(String s) {
        if (loaderCallbacks == null) {
            loaderCallbacks = new ServerLoaderCallbacks(this, this);
        }
        Bundle bundle = new Bundle();
        bundle.putString(ServerLoaderCallbacks.key, s);
        getSupportLoaderManager().restartLoader(LOADER_ID, bundle, loaderCallbacks);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void setData(Loader<AbstractServerResponse> loader, AbstractServerResponse data) {

        findViewById(R.id.progressBar).setVisibility(View.GONE);
        ((TextView) findViewById(R.id.qr_result)).setText(data.toString());
    }

    @Override
    public void setRefreshing() {
        ((TextView) findViewById(R.id.qr_result)).setText(R.string.processing);
        findViewById(R.id.progressBar).setVisibility(View.VISIBLE);
    }
}
