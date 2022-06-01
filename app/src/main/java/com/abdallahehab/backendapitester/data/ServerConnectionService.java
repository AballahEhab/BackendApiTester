package com.abdallahehab.backendapitester.data;

import android.util.Log;

import androidx.annotation.NonNull;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Map;

public class ServerConnectionService {

     public Response makeRequest(@NonNull  HTTPRequest request) throws IOException {
         String responseBody = null;
         String errorBody = null;

         HttpURLConnection httpURLConnection = (HttpURLConnection) request.getUrl().openConnection();

         httpURLConnection.setRequestMethod(request.getRequestType());

         httpURLConnection.setRequestProperty("Content-Type", "application/json");

         httpURLConnection.setRequestProperty("Accept", "application/json");

         if(request.getRequestHeaderFields()!=null){
             for (HeaderField headerItem : request.getRequestHeaderFields()) {
                 httpURLConnection.setRequestProperty(headerItem.getKey(), headerItem.getValue());
             }
         }

         // to write tha data in our request body
         if(request.getRequestType().equals( HTTPRequest.POST)){

             httpURLConnection.setDoOutput(true);

             httpURLConnection.setChunkedStreamingMode(0);

             OutputStream outputStream = new BufferedOutputStream(httpURLConnection.getOutputStream());

             OutputStreamWriter outputStreamWriter = new OutputStreamWriter(outputStream);

             outputStreamWriter.write(request.getBody());
             outputStreamWriter.flush();
             outputStreamWriter.close();
         }

         //Response
         if (httpURLConnection.getResponseCode() >= HttpURLConnection.HTTP_OK
                 && httpURLConnection.getResponseCode() < HttpURLConnection.HTTP_MULT_CHOICE  ) {
             responseBody = convertResponseInputStreamToString(httpURLConnection.getInputStream());
         }else {
             errorBody = convertResponseInputStreamToString(httpURLConnection.getErrorStream());
         }

          Response response = new  Response(httpURLConnection.getResponseCode(),errorBody,responseBody
                  ,httpURLConnection.getResponseMessage(),httpURLConnection.getHeaderFields());
         httpURLConnection.disconnect();

        return response;
    }


    @NonNull
    private String convertResponseInputStreamToString(InputStream inputStream) throws IOException {
        StringBuffer response;
        BufferedReader rd = new BufferedReader(new InputStreamReader(inputStream));
        String line;
        response = new StringBuffer();
        while ((line = rd.readLine()) != null) {
            response.append(line);
        }
        return response.toString();
    }

}

