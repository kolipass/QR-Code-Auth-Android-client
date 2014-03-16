package mobi.tarantio.qr_codeauth.app.loader;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;

/**
 * Created by kolipass on 16.03.14.
 */
public class ServerLoaderCallbacks implements LoaderManager.LoaderCallbacks<AbstractServerResponse> {
    protected Context context;
    private Listener callbacksListener;
    public static String key = "key";

    public ServerLoaderCallbacks(Context context, Listener callbacksListener) {
        this.context = context;
        this.callbacksListener = callbacksListener;
    }

    @Override
    public Loader<AbstractServerResponse> onCreateLoader(int id, Bundle args) {
        return new SeLoader(context,args.getString(key));
    }

    @Override
    public void onLoadFinished(Loader<AbstractServerResponse> loader, AbstractServerResponse data) {

        callbacksListener.setData(loader, data);
    }


    @Override
    public void onLoaderReset(Loader<AbstractServerResponse> loader) {
        callbacksListener.setData(loader, null);
        callbacksListener.setRefreshing();
    }

    public interface Listener {

        public void setData(Loader<AbstractServerResponse> loader, AbstractServerResponse data);

        public void setRefreshing();
    }
}
