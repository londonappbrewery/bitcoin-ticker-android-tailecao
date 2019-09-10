package com.londonappbrewery.bitcointicker;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.Toast;


import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONException;
import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;


public class MainActivity extends AppCompatActivity {

    // Constants:
    // TODO: Create the base URL
    private final String BASE_URL = "https://apiv2.bitcoinaverage.com";

    // Member Variables:
    TextView mPriceTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mPriceTextView = (TextView) findViewById(R.id.priceLabel);
        Spinner spinner = (Spinner) findViewById(R.id.currency_spinner);

        // Create an ArrayAdapter using the String array and a spinner layout
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.currency_array, R.layout.spinner_item);

        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(R.layout.spinner_dropdown_item);

        // Apply the adapter to the spinner
        spinner.setAdapter(adapter);

        // TODO: Set an OnItemSelected listener on the spinner
        spinner.setOnItemSelectedListener(new OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                String[] currency_arr = getApplicationContext().getResources().getStringArray(R.array.currency_array);
                Log.d("BTC", "Spinner position: " + position);
                Log.d("BTC", "Spinner Currency string: " + currency_arr[position]);

//                RequestParams params = new RequestParams();
//                params.put("indices", "global");
//                params.put("ticker", "BTC" + url);

                letsDoSomeNetworking(currency_arr[position]);

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }

    // TODO: complete the letsDoSomeNetworking() method
    private void letsDoSomeNetworking(String currency) {

        currency = "BTC" + currency;

        // https://apiv2.bitcoinaverage.com/indices/global/ticker/BTCAUD
        String urlAPI = BASE_URL + "/indices/global/ticker/" + currency ;

        AsyncHttpClient client = new AsyncHttpClient();
        client.get(urlAPI, new AsyncHttpResponseHandler() {

            @Override
            public void onStart() {
                // called before request is started
            }



            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] response) {
                // called when response HTTP status is "200 OK"
                try {
//                    String response_str =new JSONObject(new String(response)).toString();
//                    Log.d("BTC", "JSON: " + response_str);
                    JSONObject jsonObject = new JSONObject(new String(response));

                    BitcoinDataModel bitcoinDataModel = BitcoinDataModel.fromJson(jsonObject);

                    Log.d("BTC", "bitcoinDataModel: " + bitcoinDataModel.mBitCoinAskValue);

                    updateUI(String.valueOf(bitcoinDataModel.mBitCoinAskValue));

                } catch (JSONException e) {
                    e.printStackTrace();
                    Log.d("BTC", "Failed on getting JSON");
                }

//                WeatherDataModel weatherData = WeatherDataModel.fromJson(response);
//                updateUI(weatherData);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] errorResponse, Throwable e) {
                // called when response HTTP status is "4XX" (eg. 401, 403, 404)
                // called when response HTTP status is "4XX" (eg. 401, 403, 404)
                Log.d("Clima", "Request fail! Status code: " + statusCode);
                Log.d("Clima", "Fail response: " + errorResponse);
                Log.e("ERROR", e.toString());
                Toast.makeText(MainActivity.this, "Request Failed", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onRetry(int retryNo) {
                // called when request is retried
            }
        });

    }

    private void updateUI(String price_str) {
        mPriceTextView.setText(price_str);
    }


}
