package com.example.translate;


import com.google.gson.JsonObject;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;

import java.io.IOException;
import java.net.URI;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;

import okhttp3.*;
import org.json.JSONObject;


public class MainController {
    @FXML
    private TextArea enteredText;

    @FXML
    private SplitMenuButton selectLangFromMenu;

    @FXML
    private SplitMenuButton selectLangToMenu;

    @FXML
    private Button submitButton;

    @FXML
    private TextArea translatedText;

    @FXML
    void initialize(){

        String []langs = {"ru", "es", "en", "de"};
        String []langsFN  = {"Russian", "Spanish", "English", "German"};
        StringBuffer translateTo = new StringBuffer("it");
        StringBuffer translateFrom = new StringBuffer("en");

        MakeMenu(langs, langsFN, selectLangFromMenu, translateFrom);
        MakeMenu(langs, langsFN, selectLangToMenu, translateTo);

        submitButton.setText("Translate");

        submitButton.setOnAction(actionEvent -> {

            String trText = MakeRequest(translateFrom.toString(), translateTo.toString());
            translatedText.setText(trText);

        });
    }

    String MakeRequest(String translateFrom, String translateTo){
        StringBuffer translatedText = new StringBuffer("");
        OkHttpClient client = new OkHttpClient();

        Request request = new Request.Builder()

                .url("https://translated-mymemory---translation-memory.p.rapidapi.com/get?langpair="+translateFrom+"%7C"+translateTo+"&q="+enteredText.getText()+"&mt=1&onlyprivate=0&de=a%40b.c")
                .get()
                .addHeader("X-RapidAPI-Key", "f01e7e224amshff96456cb61fe8bp17b7d6jsn78bd3c8b6ad9")
                .addHeader("X-RapidAPI-Host", "translated-mymemory---translation-memory.p.rapidapi.com")
                .build();

        try {
            Response response = client.newCall(request).execute();

            String translatedUnparsedJSON = org.apache.commons.lang3.StringEscapeUtils.unescapeJava(response.body().string());
            System.out.println(translatedUnparsedJSON);
            JSONObject obj = new JSONObject(translatedUnparsedJSON);

            translatedText.append(obj.getJSONObject("responseData").getString("translatedText"));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return  translatedText.toString();
    }

    void MakeMenu(String []langs, String []langsFN, SplitMenuButton splitMenu, StringBuffer lang){
        splitMenu.setText("Select language");
        for(int i = 0; i < langs.length; i++) {

            MenuItem item = new MenuItem(langsFN[i]);
            splitMenu.getItems().addAll(item);

            int finalI = i;

            item.setOnAction(event -> {

                lang.replace(0, lang.length(), langs[finalI]);
                splitMenu.setText(langsFN[finalI]);

            });
        }
    }
}


