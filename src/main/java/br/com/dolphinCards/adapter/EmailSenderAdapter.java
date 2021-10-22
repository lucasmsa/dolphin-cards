package br.com.dolphinCards.adapter;

import com.google.gson.Gson;
import com.google.gson.JsonParser;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import br.com.dolphinCards.constants.ExternalApiConstants;
import br.com.dolphinCards.errors.Exceptions;
import br.com.dolphinCards.model.MailParameters;
import br.com.dolphinCards.model.MailResponse;

public class EmailSenderAdapter {
    public ResponseEntity<?> forward(MailParameters mailParameters) {
        try {
            String uri = new ExternalApiConstants().getUri();
            RestTemplate restTemplate = new RestTemplate();
            restTemplate.postForObject(uri, mailParameters, MailResponse.class);            
        } catch(Exception e) {
            if (e.getMessage().contains("(Connection refused)")) {
                return new Exceptions("Mail sender API is not available", 503).throwException();
            }
            MailResponse mailResponse = handleErrorResponse(e.getMessage());
            return new Exceptions(mailResponse.getMessage(), mailResponse.getStatus()).throwException();
        }

        return ResponseEntity.ok().body("sent");
    }

    private MailResponse handleErrorResponse(String errorMessage) {
        errorMessage = errorMessage.substring(errorMessage.indexOf("{"));
        errorMessage = errorMessage.substring(0, errorMessage.indexOf("}") + 1);
        Gson gson = new Gson(); 
        MailResponse mailResponse = gson.fromJson(new JsonParser().parse(errorMessage), MailResponse.class);
        return mailResponse;
    }
}