package com.r4z0r.pridesphere;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

@SpringBootApplication
public class PrideSphereApplication {

    public static void main(String[] args) {
        var ctx = SpringApplication.run(PrideSphereApplication.class, args);
        try {
            TelegramBotsApi botsApi = new TelegramBotsApi(DefaultBotSession.class);
            var botSession = botsApi.registerBot(ctx.getBean(TelegramLongPollingBot.class));
        } catch (TelegramApiException e) {
            System.out.println("Erro ao iniciar o bot: " + e.getMessage());
        }
    }

}
