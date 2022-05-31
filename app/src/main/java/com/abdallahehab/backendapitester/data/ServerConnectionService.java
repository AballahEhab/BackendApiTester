package com.abdallahehab.backendapitester.data;

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


            URL url = new URL(request.getUrlAddress());
            HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();

            // setting the  Request Method Type
            httpURLConnection.setRequestMethod(request.getRequestType());

            // adding the headers for request
            httpURLConnection.setRequestProperty("Content-Type", "application/json");
//
//            // set responseBody format type
            httpURLConnection.setRequestProperty("Accept", "application/json");

            if(request.getRequestHeaderFields()!=null){
                for (HeaderField headerItem : request.getRequestHeaderFields()) {
                    httpURLConnection.setRequestProperty(headerItem.key, headerItem.value);
                }
            }

         // to write tha data in our request body
         if(request.getRequestType().equals( HTTPRequest.POST)){

             // to be able to write content to output stream
             //to tell the connection object that we will be wrting some data on the server and then will fetch the output result
             httpURLConnection.setDoOutput(true);

             // this is used for just in case we don't know about the data size associated with our request
             httpURLConnection.setChunkedStreamingMode(0);

             OutputStream outputStream = new BufferedOutputStream(httpURLConnection.getOutputStream());

             OutputStreamWriter outputStreamWriter = new OutputStreamWriter(outputStream);

             outputStreamWriter.write(request.getBody());
             outputStreamWriter.flush();
             outputStreamWriter.close();
         }

         //Response
         if (httpURLConnection.getResponseCode() >= HttpURLConnection.HTTP_OK && httpURLConnection.getResponseCode() < HttpURLConnection.HTTP_MULT_CHOICE  ) {
             responseBody = convertResponseInputStreamToString(httpURLConnection.getInputStream());
         }else {
             errorBody = convertResponseInputStreamToString(httpURLConnection.getErrorStream());
         }

          Response response = new  Response(httpURLConnection.getResponseCode(),errorBody,responseBody,httpURLConnection.getResponseMessage(),httpURLConnection.getHeaderFields());
        // this is done so that there are no open connections left when this task is going to complete
         httpURLConnection.disconnect();

        return response;
    }


    @NonNull
    private String convertResponseInputStreamToString(InputStream inputStream) throws IOException {
        StringBuffer response;
        BufferedReader rd = new BufferedReader(new InputStreamReader(inputStream));
        String line;
        response = new StringBuffer();
        //Expecting answer of type JSON single line {"json_items":[{"status":"OK","message":"<Message>"}]}
        while ((line = rd.readLine()) != null) {
            response.append(line);
        }
        return response.toString();
    }

}

