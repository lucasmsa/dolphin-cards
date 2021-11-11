package br.com.dolphinCards.utils;

import org.json.JSONObject;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class FetchKeyFromJsonString {
    public String run(String jsonString, String key) {
        JSONObject jsonObject = new JSONObject(jsonString);
        
        String value = (String) jsonObject.get(key);

        return value;
    }
}
