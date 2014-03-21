package mobi.tarantio.qr_codeauth.app;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Base64;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import com.google.gson.JsonSyntaxException;

import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpEntityEnclosingRequestBase;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicHeader;
import org.apache.http.protocol.HTTP;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
import java.lang.reflect.Type;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

/**
 * Created by kolipass on 16.03.14.
 */
public class UrlWorker {
    static String authStr = "kolipass:qweqwe";
    static String authEncoded = Base64.encodeToString(authStr.getBytes(), Base64.URL_SAFE);

    protected Log log = new Log();

    public static Header getHTTPAuthorizationHeader() {
        if(!authEncoded.substring(authEncoded.length()-1).equals("=")){
            authEncoded+="=";
        }

        return new BasicHeader("Authorization", "Basic " + authEncoded);
    }

    /**
     * This Serializer correctty parse 0 1 boolean!
     */
    public static class BooleanSerializer implements JsonSerializer<Boolean>, JsonDeserializer<Boolean> {

        @Override
        public JsonElement serialize(Boolean arg0, Type arg1, JsonSerializationContext arg2) {
            return new JsonPrimitive(arg0 ? 1 : 0);
        }

        @Override
        public Boolean deserialize(JsonElement arg0, Type arg1, JsonDeserializationContext arg2) throws JsonParseException {
            //? true : false
            return arg0.getAsInt() == 1;
        }
    }

    protected <T> T getServerResponseFromInputStream(InputStream inputStream, Class<T> classOfT) throws IOException,
            JsonSyntaxException, IllegalStateException {
        String json = convertStreamToString(inputStream);
        log.d(utf8(json));
        return stringToServerResponse(json, classOfT);
    }

    public static <T> T stringToServerResponse(String json, Class<T> classOfT) throws IOException,
            JsonSyntaxException, IllegalStateException {
        return getCorrectBooleanGson().fromJson(json, classOfT);
    }

    private String utf8(String s) {
        String string = s;
        try {
            byte[] utf8 = string.getBytes(HTTP.UTF_8);
            // Convert from UTF-8 to Unicode
            string = new String(utf8, HTTP.UTF_8);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return s;
        }
        return string;
    }

    private static Gson getCorrectBooleanGson() {
        GsonBuilder b = new GsonBuilder();
        b.registerTypeAdapter(Boolean.class, new BooleanSerializer());
        return b.create();
    }


    protected <T> T getServerResponse(String url, Class<T> classOfT) throws IOException, JsonSyntaxException, IllegalStateException, URISyntaxException {
        InputStream inputStream = getInputStreamFromUrl(url);
        return getServerResponseFromInputStream(inputStream, classOfT);
    }

    public static String convertStreamToString(InputStream is)
            throws IOException {
        //
        // To convert the InputStream to String we use the
        // Reader.read(char[] buffer) method. We iterate until the
        // Reader return -1 which means there's no more flatPreviewList to
        // read. We use the StringWriter class to produce the string.
        //
        if (is != null) {
            Writer writer = new StringWriter();

            char[] buffer = new char[1024];
            try {
                Reader reader = new BufferedReader(new InputStreamReader(is,
                        "UTF-8"));
                int n;
                while ((n = reader.read(buffer)) != -1) {
                    writer.write(buffer, 0, n);
                }
            } finally {
                is.close();
            }
            return writer.toString();
        } else {
            return "";
        }
    }

    public InputStream getInputStreamFromUrl(String url) throws URISyntaxException, IOException {
        log.d("getInputStreamFromUrl: " + url);

        DefaultHttpClient httpClient = new DefaultHttpClient();
        URI uri;
        InputStream data = null;
        uri = new URI(url);
        HttpGet httpGet = new HttpGet(uri);

        HttpResponse response = httpClient.execute(httpGet);
        data = response.getEntity().getContent();

        return data;
    }

    public static boolean isNetworkAvailable(Context ctx) {
        ConnectivityManager connectivityManager = (ConnectivityManager) ctx
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager
                .getActiveNetworkInfo();
        return activeNetworkInfo != null;
    }

    public InputStream putStringData(String requestUrl, List<? extends NameValuePair> parameters) throws IOException, URISyntaxException, JsonSyntaxException {
        DefaultHttpClient httpClient = new DefaultHttpClient();
        URI uri = new URI(requestUrl);
        HttpPut httpPut = new HttpPut(uri);

        setHeaders(httpPut);

//        requestUrl.length()substring(0,1).equals("/")

        setEntity(httpPut, parameters);

        HttpResponse response = httpClient.execute(httpPut);
//        HttpResponse response = httpClient.execute(httpPost, localContext);
        return response.getEntity().getContent();
    }
    protected void setHeaders(HttpRequestBase requestBase) {
            requestBase.addHeader(getHTTPAuthorizationHeader());
    }
    protected void setEntity(HttpEntityEnclosingRequestBase requestBase, List<? extends NameValuePair> parameters) throws IOException {
        if (parameters != null && !parameters.isEmpty()) {
            UrlEncodedFormEntity entity = new UrlEncodedFormEntity(parameters, HTTP.UTF_8);
            log.d("postStringData: " + requestBase.getURI() + " params: " + convertStreamToString(entity.getContent()));
            requestBase.setEntity(entity);
        }
    }


}