package com.example.transactionapi.services.utils;

import com.example.transactionapi.models.enums.Currency;
import com.example.transactionapi.models.utils.Rate;
import com.example.transactionapi.repository.utils.RateRepository;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.time.LocalDateTime;

@Service
public class CurrencyConverter {
    private Logger logger = LoggerFactory.getLogger(CurrencyConverter.class);
    private RateRepository rateRepository;

    @Autowired
    CurrencyConverter(RateRepository rateRepository){
        this.rateRepository = rateRepository;
    }

    public boolean setCurrency() throws IOException {
        String url_str = "https://v6.exchangerate-api.com/v6/15b3371edba6a419a915b2c5/latest/USD";

        URL url = new URL(url_str);
        HttpURLConnection request = (HttpURLConnection) url.openConnection();
        request.connect();

        JsonParser jp = new JsonParser();
        JsonElement root = jp.parse(new InputStreamReader((InputStream) request.getContent()));
        JsonObject jsonobj = root.getAsJsonObject();

        String req_result = jsonobj.get("result").getAsString();
        if (req_result.equals("success")){
            Rate rate = new Rate();
            JsonObject currency = jsonobj.getAsJsonObject("conversion_rates");
            rate.setUsd(currency.get("USD").getAsFloat());
            rate.setAmd(currency.get("AMD").getAsFloat());
            rate.setEur(currency.get("EUR").getAsFloat());
            rateRepository.save(rate);
            logger.info("Currency Updated");
            return true;
        }
        return false;
    }

    public Rate getCurrency() throws IOException {
        Rate rate = rateRepository.findTopByOrderByIdDesc();
        LocalDateTime lastDate = rate.getUpdatedtime();

        if (LocalDateTime.now().getDayOfMonth()!=lastDate.getDayOfMonth()){
            if (setCurrency()){
                rate = rateRepository.findTopByOrderByIdDesc();
            }
        }
        return rate;
    }
    public double convertByValue(Currency from, Currency to, Double balance, LocalDateTime time){
        Rate rate = rateRepository.findTopByOrderByIdDesc();
        if (time!=null){
            rate = rateRepository.findByUpdatedtime(time);
            if (rate==null){
                rate = rateRepository.findTopByOrderByIdDesc();
            }
        }
        double senderBalance = 0;
        double receiverBalance = 0;
        if (from==to){
            receiverBalance = balance;
        }else{
            if (from != Currency.USD){
                if (from== com.example.transactionapi.models.enums.Currency.AMD){
                    senderBalance = balance/rate.getAmd();
                }else {
                    senderBalance = balance/rate.getEur();
                }
            }else {
                senderBalance = balance;
            }
            if (to != com.example.transactionapi.models.enums.Currency.USD){
                if (to== com.example.transactionapi.models.enums.Currency.AMD){
                    receiverBalance = senderBalance*rate.getAmd();
                }else {
                    receiverBalance = senderBalance*rate.getEur();
                }
            }else{
                receiverBalance = senderBalance;
            }
        }
        return receiverBalance;
    }
}
