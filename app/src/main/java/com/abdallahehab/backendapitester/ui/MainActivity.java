package com.abdallahehab.backendapitester.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkCapabilities;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.RadioGroup;
import android.widget.Toast;
import com.abdallahehab.backendapitester.R;
import com.abdallahehab.backendapitester.data.HTTPRequest;
import com.abdallahehab.backendapitester.data.HeaderField;
import com.abdallahehab.backendapitester.data.Response;
import com.abdallahehab.backendapitester.data.ServerConnectionService;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.MalformedURLException;
import java.net.Socket;
import java.net.SocketAddress;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    EditText urlEditText,requestBodyEditText,headerKeyEditText,headerValueEditText;
    RadioGroup requestTypeRadioGroup;
    ImageButton addHeaderButton;
    ListView headerFieldListView;
    Button sendRequestBtn;
    ArrayList<HeaderField> headerFieldsList;
    HeaderFieldsListAdapter adapter;
    String requestType = null;
    public static final String RESPONSE_CODE = "response code";
    public static final String ERROR_MESSAGE = "error message";
    public static final String REQUEST_BODY = "request body";
    public static final String REQUEST_HEADERS = "request headers";


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        initialize();

        setAdapterForHeaderFieldsList();

        setClickListenersForButtons();

        requestTypeRadioGroup.setOnCheckedChangeListener((radioGroup, checked) -> {

            switch(checked){
                case R.id.post_radio_btn :
                    requestBodyEditText.setVisibility(View.VISIBLE);
                    requestType = HTTPRequest.POST;
                    break;
                case R.id.get_radio_btn :
                        requestBodyEditText.setVisibility(View.GONE);
                        requestType = HTTPRequest.GET;
                        break;
            }

        });

    }

    private void initialize() {
        urlEditText = findViewById(R.id.url_edt_txt);
        requestBodyEditText = findViewById(R.id.request_body_edt_txt);
        headerKeyEditText = findViewById(R.id.header_key_edt_txt);
        headerValueEditText = findViewById(R.id.header_value_edt_txt);
        requestTypeRadioGroup = findViewById(R.id.request_type_rad_gr);
        addHeaderButton = findViewById(R.id.add_header_field_btn);
        headerFieldListView = findViewById(R.id.headers_fields_list);
        sendRequestBtn = findViewById(R.id.send_request_btn);
        headerFieldsList = new ArrayList<>();

    }

    private void showMessageAsToast(String message){
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }


    private void setClickListenersForButtons() {

        addHeaderButton.setOnClickListener(view -> {
            String key = headerKeyEditText.getText().toString();
            String value = headerValueEditText.getText().toString();
            if (key.isEmpty() || value.isEmpty()){
                showMessageAsToast("key or value can't be empty");
                return;
            } ;

            if(isHeaderKeyIsRedundant(key)) {
                showMessageAsToast("header key can't be redundant");
                return;
            }
            headerFieldsList.add(new HeaderField(key,value));
            setAdapterForHeaderFieldsList();
             headerKeyEditText.setText(null);
             headerValueEditText.setText(null);

        });

        sendRequestBtn.setOnClickListener(view -> {

            String urlAdress = urlEditText.getText().toString();

            String body = requestBodyEditText.getText().toString();

            if(urlAdress.isEmpty()) {
                showMessageAsToast("url can't be empty");
                return;
            }

            if(requestType== null) {
                showMessageAsToast("you must specify the request type");
                return;
            }

            try {

                if(isConnectedToNetwork()){

                    HTTPRequest request = new HTTPRequest(urlAdress, requestType, body, headerFieldsList);
                    NetWorkConnectionAsyncTask asyncTask = new NetWorkConnectionAsyncTask();
                    asyncTask.execute(request);

                }else{
                    showMessageAsToast("notwork not connected please connect to network");
                }

            } catch (MalformedURLException e) {
                e.printStackTrace();
                showMessageAsToast(e.getMessage());
            }

        });

    }

    private void setAdapterForHeaderFieldsList() {
        adapter = new HeaderFieldsListAdapter(this, headerFieldsList);
        headerFieldListView.setAdapter(adapter);

    }

    private void navigateToResponseActivity(Response response) {
        Intent intent =  new Intent(this,ResponseActivity.class);
        intent.putExtra(RESPONSE_CODE,response.getResponseCode());
        intent.putExtra(ERROR_MESSAGE,response.getErrorMessage());
        intent.putExtra(REQUEST_BODY,response.getResponseBody());
        intent.putExtra(REQUEST_HEADERS,response.getResponseHeaderFields().toString());
        startActivity(intent);
    }

    private boolean isHeaderKeyIsRedundant(String key) {
        for (HeaderField item:headerFieldsList){
            if (key.equals(item.getKey())) return true;
        }
        return false;
    }

    public void editHeaderItem(HeaderField currentHeaderField) {
        headerKeyEditText.setText(currentHeaderField.getKey());
        headerValueEditText.setText(currentHeaderField.getValue());
        headerFieldsList.remove(currentHeaderField);
        setAdapterForHeaderFieldsList();
    }

    public void removeHeaderItem(HeaderField currentHeaderField) {
        headerFieldsList.remove(currentHeaderField);
        setAdapterForHeaderFieldsList();
    }

    public boolean isConnectedToNetwork() {
        ConnectivityManager connectivityManager = (ConnectivityManager)getSystemService(CONNECTIVITY_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            Network network = connectivityManager.getActiveNetwork();
            NetworkCapabilities networkCapabilities = connectivityManager.getNetworkCapabilities(network);
            if (networkCapabilities == null ) return false;
             if(networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) ||
                    networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) ||
                    networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) ||
                    networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_BLUETOOTH) ) return true;
                    else return false;
        } else {
            NetworkInfo nwInfo = connectivityManager.getActiveNetworkInfo();
            if(nwInfo == null)
            return nwInfo.isConnected();
        }
        return false;
    }

    public boolean isOnline() {
        try {
            int timeoutMs = 1500;
            Socket sock = new Socket();
            SocketAddress sockaddr = new InetSocketAddress("8.8.8.8", 53);

            sock.connect(sockaddr, timeoutMs);
            sock.close();

            return true;
        } catch (IOException e) {
            return false;
        }
    }

    public class NetWorkConnectionAsyncTask extends AsyncTask<HTTPRequest, Integer, Response> {
        private ProgressDialog dialog;

        public NetWorkConnectionAsyncTask() {
            dialog = new ProgressDialog(MainActivity.this);
            dialog.setCancelable(false);
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog.setMessage("loading");
            dialog.show();
        }

        @Override
        protected Response doInBackground(HTTPRequest... requests) {

            try {

                if(isOnline()){
                    ServerConnectionService connection = new ServerConnectionService();
                    return connection.makeRequest(requests[0]);
                }else{
                    return null;
                }
            } catch (IOException e) {
                e.printStackTrace();
            }catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }
        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
        }

        @Override
        protected void onPostExecute(Response result) {
            super.onPostExecute(result);
            if(dialog.isShowing()) dialog.dismiss();
            if(result == null){
                showMessageAsToast("error please check you connectivity");
            }else {
                navigateToResponseActivity(result);
            }
        }


    }


}