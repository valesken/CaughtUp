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
import news.caughtup.caughtup.entities.ResponseObject;

public class RestClient implements IRest {
    private static final String SERVER_URL = "http://" + R.string.server_dns + ":8080/caughtup";

    @Override
    public void getCall(String endPoint, String jsonData, Callback callback) {
        new RequestTask(callback).execute(endPoint, jsonData, "GET");
    }

    @Override
    public void postCall(String endPoint, String jsonData, Callback callback) {
        new RequestTask(callback).execute(endPoint, "POST");
    }

    @Override
    public void putCall(String endPoint, String jsonData, Callback callback) {
        new RequestTask(callback).execute(endPoint, "PUT");
    }

    @Override
    public void deleteCall(String endPoint, String jsonData, Callback callback) {
        new RequestTask(callback).execute(endPoint, "DELETE");
    }

    private class RequestTask extends AsyncTask<String, Void, ResponseObject> {
        private Callback callback;

        public RequestTask(Callback callback) {
            this.callback = callback;
        }

        @Override
        protected ResponseObject doInBackground(String... params) {
            HttpURLConnection urlConnection = null;
            try {
                URL url = new URL(SERVER_URL + params[0]);
                urlConnection = (HttpURLConnection) url.openConnection();
                switch (params[2]) {
                    case "POST": urlConnection.setRequestMethod("POST");
                    case "PUT": urlConnection.setRequestMethod("PUT");
                    case "DELETE": urlConnection.setRequestMethod("DELETE");
                    default: urlConnection.setRequestMethod("GET");
                }
                if (!params[2].equals("GET")) {
                    urlConnection.setChunkedStreamingMode(0);
                    urlConnection.setDoOutput(true);
                    urlConnection.setRequestProperty("Content-Type", "application/json");
                }
                urlConnection.setDoInput(true);
                OutputStream out = new BufferedOutputStream(urlConnection.getOutputStream());
                writeStream(out, params[1]);
                BufferedReader in = new BufferedReader(
                        new InputStreamReader(urlConnection.getInputStream()));
                return new ResponseObject(urlConnection.getResponseCode(), readStream(in));
            } catch (MalformedURLException e) {
                System.err.println("Wrong url format");
            } catch (IOException e) {
                System.err.println("Error while trying to open a stream on the HTTP response");
            } finally {
                urlConnection.disconnect();
            }
            return null;
        }

        @Override
        protected void onPostExecute(ResponseObject o) {
            callback.process(o);
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

        private void writeStream(OutputStream out, String jsonData) {
            try {
                out.write(jsonData.getBytes(Charset.forName("UTF-8")));
                out.flush();
                out.close();
            } catch (IOException e) {
                System.err.println("Error while trying to write to output stream");
            }
        }
    }
}
