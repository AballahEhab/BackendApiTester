package com.abdallahehab.backendapitester.ui;

import static com.abdallahehab.backendapitester.ui.MainActivity.ERROR_MESSAGE;
import static com.abdallahehab.backendapitester.ui.MainActivity.REQUEST_BODY;
import static com.abdallahehab.backendapitester.ui.MainActivity.REQUEST_HEADERS;
import static com.abdallahehab.backendapitester.ui.MainActivity.RESPONSE_CODE;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import com.abdallahehab.backendapitester.R;

public class ResponseActivity extends AppCompatActivity {

    Intent outIntent;
    TextView responseCodeTxtV, errorMessageTxtV, errorMessageLabelTxtV,responseBodyTxtV,responseBodyLabelTxtV, responseHeadersTxtV;
    int requestCode ;
    String errorMessage , responseBody, requestHeader;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_response);

        initialize();

        setTextViewsValues();

        setVisibilityForTextViewsErrorAndBody();


    }

    private void setVisibilityForTextViewsErrorAndBody() {
        if(errorMessage == null || errorMessage.isEmpty()) {
            errorMessageTxtV.setVisibility(View.GONE);
            errorMessageLabelTxtV.setVisibility(View.GONE);
        }
        if(responseBody == null || responseBody.isEmpty()) {
            responseBodyTxtV.setVisibility(View.GONE);
            responseBodyLabelTxtV.setVisibility(View.GONE);

        }
    }

    private void setTextViewsValues() {

        responseCodeTxtV.setText(Integer.toString(requestCode));
        errorMessageTxtV.setText(errorMessage);
        responseBodyTxtV.setText(responseBody);
        responseHeadersTxtV.setText(requestHeader);

    }

    private void initialize() {

         outIntent = getIntent();

         requestCode = outIntent.getIntExtra(RESPONSE_CODE,0);
         errorMessage = outIntent.getStringExtra(ERROR_MESSAGE);
         responseBody = outIntent.getStringExtra(REQUEST_BODY);
         requestHeader = outIntent.getStringExtra(REQUEST_HEADERS);

         responseCodeTxtV = findViewById(R.id.response_code_value);
         errorMessageTxtV = findViewById(R.id.error_message_value);
         errorMessageLabelTxtV = findViewById(R.id.error_message_label_txt);
         responseBodyTxtV = findViewById(R.id.response_body);
         responseBodyLabelTxtV = findViewById(R.id.response_body_label_txt);
         responseHeadersTxtV = findViewById(R.id.response_headers_value);

    }

}