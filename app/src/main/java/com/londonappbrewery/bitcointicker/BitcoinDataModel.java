package com.londonappbrewery.bitcointicker;

import org.json.JSONException;
import org.json.JSONObject;

public class BitcoinDataModel {

    double mBitCoinAskValue;

    public static BitcoinDataModel fromJson(JSONObject jsonObject) {

        BitcoinDataModel bitcoinDataModel = new BitcoinDataModel();
        try {
            bitcoinDataModel.mBitCoinAskValue = jsonObject.getDouble("ask");

            return bitcoinDataModel;

        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }

    }

}
