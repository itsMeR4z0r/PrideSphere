package com.r4z0r.pridesphere.bot;

import com.google.gson.Gson;
import com.r4z0r.pridesphere.bot.data.Relato;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.List;

import static com.r4z0r.pridesphere.bot.Acoes.*;

@Component
public class PrideSphereBot extends TelegramLongPollingBot {

    private final Environment env;

    @Autowired
    public PrideSphereBot(Environment env) {
        this.env = env;
    }

    @Override
    public void onUpdateReceived(Update update) {
        if (update.hasMessage() && update.getMessage().hasText()) {
            var respostas = new Respostas(update.getMessage().getChatId());
            if (update.getMessage().getText().equals("/start")) {
                try {
                    execute(respostas.start());
                } catch (TelegramApiException e) {
                    throw new RuntimeException(e);
                }
            }
        } else if (update.hasCallbackQuery()) {
            var respostas = new Respostas(update.getCallbackQuery().getMessage().getChatId());
            try {
                var relate = new Gson().fromJson(update.getCallbackQuery().getData(), Relato.class);
                System.out.println(update.getCallbackQuery().getData());
                if (relate.getEtapa().equals(ENVIAR_RELATO)) {
                    execute(respostas.enviarRelato(update.getCallbackQuery().getMessage().getMessageId()));
                } else if (relate.getEtapa().equals(VER_RELATO)) {
                    execute(respostas.verRelatos(update.getCallbackQuery().getMessage().getMessageId()));
                } else if (relate.getEtapa().equals(MENU_INICIAL)) {
                    execute(respostas.menuInicial(update.getCallbackQuery().getMessage().getMessageId()));
                } else if (relate.getEtapa().equals(ENVIAR_RELATO_OP_ENVIAR)) {
                    execute(respostas.selectOpRelato(update.getCallbackQuery().getMessage().getMessageId()));
                } else {
                    throw new IllegalStateException("Unexpected value: " + update.getCallbackQuery().getData());
                }
            } catch (TelegramApiException e) {
                throw new RuntimeException(e);
            }
        }
    }

    @Override
    public void onUpdatesReceived(List<Update> updates) {
        super.onUpdatesReceived(updates);
    }

    @Override
    public String getBotUsername() {
        return env.getProperty("telegram.data.config.botUsername");
    }

    @Override
    public void onRegister() {
        super.onRegister();
    }

    @Override
    public String getBotToken() {
        return env.getProperty("telegram.data.config.botToken");
    }
}
