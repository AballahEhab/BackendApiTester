package com.abdallahehab.backendapitester.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.os.AsyncTask;
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
    String requestType;

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
        });

        sendRequestBtn.setOnClickListener(view -> {
            String urlAdress = urlEditText.getText().toString();
            String body = requestBodyEditText.getText().toString();

            if(urlAdress.isEmpty()) {
                showMessageAsToast("url can't be empty");
                return;
            }

            HTTPRequest request = new HTTPRequest(urlAdress,requestType,body,headerFieldsList);
            NetWorkConnectionAsyncTask asyncTask = new NetWorkConnectionAsyncTask();
            asyncTask.execute(request);
        });
    }

    private boolean isHeaderKeyIsRedundant(String key) {
        for (HeaderField item:headerFieldsList){
            if (key.equals(item.key)) return true;
        }
        return false;
    }

    private void setAdapterForHeaderFieldsList() {
        adapter = new HeaderFieldsListAdapter(this, headerFieldsList);
        headerFieldListView.setAdapter(adapter);

    }

    private void initialize() {
        urlEditText = findViewById(R.id.url_edt_txt);
        requestBodyEditText = findViewById(R.id.request_body_edt_txt);
        headerKeyEditText = findViewById(R.id.header_key_edt_txt);
        headerValueEditText = findViewById(R.id.header_value_edt_txt);
        requestTypeRadioGroup = findViewById(R.id.request_type_rad_gr);
        addHeaderButton = findViewById(R.id.add_header_feild_btn);
        headerFieldListView = findViewById(R.id.headers_feilds_list);
        sendRequestBtn = findViewById(R.id.send_request_btn);
        headerFieldsList = new ArrayList<>();

    }

    public class NetWorkConnectionAsyncTask extends AsyncTask<HTTPRequest, Integer, Response> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }

        @Override
        protected Response doInBackground(HTTPRequest... requests) {

            try {
                ServerConnectionService connection = new ServerConnectionService();

                return connection.makeRequest(requests[0]);
            } catch (IOException e) {
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
            showMessageAsToast(result.toString());

        }


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
}