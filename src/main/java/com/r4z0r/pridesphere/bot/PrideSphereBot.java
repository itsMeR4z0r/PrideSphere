package com.r4z0r.pridesphere.bot;

import com.r4z0r.pridesphere.bot.data.CallbackMsgRepository;
import com.r4z0r.pridesphere.bot.data.Mensagem;
import com.r4z0r.pridesphere.bot.data.MensagemRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;

import static com.r4z0r.pridesphere.bot.Acoes.*;

@Slf4j
@Component
public class PrideSphereBot extends TelegramLongPollingBot {

    private final Environment env;

    @Autowired
    public PrideSphereBot(Environment env) {
        this.env = env;
    }

    @Autowired
    private MensagemRepository mensagemRepository;
    @Autowired
    private CallbackMsgRepository callbackMsgRepository;

    private void options(Update update) {
        if (update.hasMessage() && update.getMessage().hasText()) {
            if (update.getMessage().getText().equals("/start")) {
                var mensagem = new Mensagem();
                mensagem.setMessageId(update.getMessage().getMessageId());
                mensagem.setChatId(update.getMessage().getChatId());
                mensagem.setUserId(update.getMessage().getFrom().getId());
                mensagem.setUserName(update.getMessage().getFrom().getUserName());
                var respostas = new Respostas(mensagemRepository.save(mensagem), callbackMsgRepository);
                try {
                    execute(respostas.start());
                } catch (TelegramApiException e) {
                    throw new RuntimeException(e);
                }
            }
        } else if (update.hasCallbackQuery()) {
            var mensagem = new Mensagem();
            mensagem.setMessageId(update.getCallbackQuery().getMessage().getMessageId());
            mensagem.setChatId(update.getCallbackQuery().getMessage().getChatId());
            mensagem.setUserId(update.getCallbackQuery().getFrom().getId());
            mensagem.setUserName(update.getCallbackQuery().getFrom().getUserName());

            if (mensagemRepository.existsById(mensagem.getMessageId())) {
                mensagem = mensagemRepository.findById(mensagem.getMessageId()).get();
            } else {
                mensagem = mensagemRepository.save(mensagem);
            }

            var respostas = new Respostas(mensagem, callbackMsgRepository);
            try {
                if (!callbackMsgRepository.existsById(UUID.fromString(update.getCallbackQuery().getData()))) {
                    throw new NoSuchElementException("Não foi encontrado o callbackMsg com id: " + update.getCallbackQuery().getData());
                }
                var callback = callbackMsgRepository.findById(UUID.fromString(update.getCallbackQuery().getData())).get();
                switch (callback.getEtapa()) {
                    case ENVIAR_RELATO -> execute(respostas.enviarRelato());
                    case VER_RELATO -> execute(respostas.verRelatos());
                    case MENU_INICIAL -> execute(respostas.menuInicial());
                    case ENVIAR_RELATO_OP_ENVIAR -> execute(respostas.selectOpRelato());
                    default ->
                            throw new IllegalStateException("Unexpected value: " + update.getCallbackQuery().getData());
                }
            } catch (Exception e) {
                log.error("Erro ao executar ação: " + e.getMessage(), e);
                EditMessageText msg = new EditMessageText();
                msg.setText("Ocorreu um erro ao executar a ação. Tente novamente.");
                msg.setMessageId(mensagem.getMessageId());
                msg.setChatId(update.getCallbackQuery().getMessage().getChat().getId());
                try {
                    execute(msg);
                } catch (TelegramApiException ex) {
                    throw new RuntimeException(ex);
                }
            }
        }
    }

    @Override
    public void onUpdateReceived(Update update) {
        options(update);
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
