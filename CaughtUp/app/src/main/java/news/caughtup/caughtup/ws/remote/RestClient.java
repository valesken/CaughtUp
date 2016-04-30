package news.caughtup.caughtup.ws.remote;

import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;

import news.caughtup.caughtup.entities.ResponseObject;
import news.caughtup.caughtup.util.StringRetriever;

public class RestClient implements IRest {
    @Override
    public void getCall(String endPoint, String jsonData, Callback callback) {
        new RequestTask(callback).execute(endPoint, jsonData, "GET");
    }

    @Override
    public void postCall(String endPoint, String jsonData, Callback callback) {
        new RequestTask(callback).execute(endPoint, jsonData, "POST");
    }

    @Override
    public void putCall(String endPoint, String jsonData, Callback callback) {
        new RequestTask(callback).execute(endPoint, jsonData, "PUT");
    }

    @Override
    public void deleteCall(String endPoint, String jsonData, Callback callback) {
        new RequestTask(callback).execute(endPoint, jsonData, "DELETE");
    }

    private class RequestTask extends AsyncTask<String, Void, ResponseObject> {
        private Callback callback;

        public RequestTask(Callback callback) {
            this.callback = callback;
        }

        @Override
        protected ResponseObject doInBackground(String... params) {
            String server_url = StringRetriever.getInstance().getServerConnectionString();
            HttpURLConnection urlConnection = null;
            try {
                URL url = new URL(server_url + params[0]);
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
                    OutputStream out = new BufferedOutputStream(urlConnection.getOutputStream());
                    writeStream(out, params[1]);
                }
                int responseCode = urlConnection.getResponseCode();
                BufferedReader in;
                if(responseCode < 400) {
                    in = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
                } else {
                    in = new BufferedReader(new InputStreamReader(urlConnection.getErrorStream()));
                }
                return new ResponseObject(responseCode, readStream(in));
            } catch (MalformedURLException e) {
                System.err.println("Wrong url format");
            } catch (IOException e) {
                System.err.println("Error while trying to open a stream on the HTTP response");
                e.printStackTrace();
            } finally {
                urlConnection.disconnect();
            }
            return null;
        }

        @Override
        protected void onPostExecute(ResponseObject o) {
            // Will be null if the server is down or ip address is out of date
            if(o != null) {
                callback.process(o);
            }
        }

        private JSONObject readStream(BufferedReader in) {
            StringBuilder sb = new StringBuilder();
            try {
                String line;
                while ((line = in.readLine()) != null) {
                    sb.append(line);
                }
                JSONObject myObject = new JSONObject(sb.toString());
                return myObject;
            } catch (IOException e) {
                System.err.println("Error while trying to read content of response");
            } catch (JSONException e) {
                return null;
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
