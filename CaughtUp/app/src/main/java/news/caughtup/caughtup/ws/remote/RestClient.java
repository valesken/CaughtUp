package news.caughtup.caughtup.ws.remote;

import android.os.AsyncTask;

import com.google.gson.Gson;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;

import news.caughtup.caughtup.R;

public class RestClient implements IRest {
    private static final String SERVER_URL = "http://" + R.string.server_dns + ":8080/caughtup";
    private enum RequestMethod {
        GET, POST, PUT, DELETE
    }

    @Override
    public Object getCall(String endPoint, Class c) {
        return new RequestTask().execute(endPoint, c, RequestMethod.GET);
    }

    @Override
    public void postCall(String endPoint, Class c) {
        new RequestTask().execute(endPoint, c, RequestMethod.POST);
    }

    @Override
    public void putCall(String endPoint, Class c) {
        new RequestTask().execute(endPoint, c, RequestMethod.PUT);
    }

    @Override
    public void deleteCall(String endPoint, Class c) {
        new RequestTask().execute(endPoint, c, RequestMethod.DELETE);
    }

    private Object readStream(BufferedReader in) {
        StringBuilder sb = new StringBuilder();
        try {
            String line;
            while ((line = in.readLine()) != null) {
                sb.append(line);
            }
            Gson gson = new Gson();
            return gson.fromJson(sb.toString(), Object.class);
        } catch (IOException e) {
            System.err.println("Error while trying to read content of response");
        }
        return null;
    }

    private void writeStream(OutputStream out, Object obj) {
        try {
            String jsonString = new Gson().toJson(obj);
            out.write(jsonString.getBytes(Charset.forName("UTF-8")));
        } catch (IOException e) {
            System.err.println("Error while trying to write to output stream");
        }
    }

    private class RequestTask extends AsyncTask {

        @Override
        protected Object doInBackground(Object[] params) {
            HttpURLConnection urlConnection = null;
            try {
                URL url = new URL(SERVER_URL + params[0]);
                urlConnection = (HttpURLConnection) url.openConnection();
                switch ((RequestMethod) params[2]) {
                    case POST: urlConnection.setRequestMethod("POST");
                    case PUT: urlConnection.setRequestMethod("PUT");
                    case DELETE: urlConnection.setRequestMethod("DELETE");
                    default: urlConnection.setRequestMethod("GET");
                }
                if (params[2] != RequestMethod.GET) {
                    urlConnection.setChunkedStreamingMode(0);
                    urlConnection.setDoOutput(true);
                }
                urlConnection.setDoInput(true);

                OutputStream out = new BufferedOutputStream(urlConnection.getOutputStream());
                writeStream(out, params[1]);
                BufferedReader in = new BufferedReader(
                        new InputStreamReader(urlConnection.getInputStream()));
                return readStream(in);
            } catch (MalformedURLException e) {
                System.err.println("Wrong url format");
            } catch (IOException e) {
                System.err.println("Error while trying to open a stream on the HTTP response");
            } finally {
                urlConnection.disconnect();
            }
            return null;
        }
    }
}
