package com.r4z0r.pridesphere.bot;

import com.r4z0r.pridesphere.Util;
import com.r4z0r.pridesphere.bot.data.CallbackMsgRepository;
import com.r4z0r.pridesphere.bot.data.Mensagem;
import com.r4z0r.pridesphere.bot.data.MensagemRepository;
import com.r4z0r.pridesphere.entity.Admin;
import com.r4z0r.pridesphere.entity.Usuario;
import com.r4z0r.pridesphere.repositories.AdminRepository;
import com.r4z0r.pridesphere.repositories.UsuarioRepository;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.validator.routines.EmailValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.ParseMode;
import org.telegram.telegrambots.meta.api.methods.commands.SetMyCommands;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.DeleteMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.commands.BotCommand;
import org.telegram.telegrambots.meta.api.objects.commands.scope.BotCommandScopeDefault;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.security.GeneralSecurityException;
import java.util.*;

import static com.r4z0r.pridesphere.bot.Acoes.*;
import static com.r4z0r.pridesphere.bot.Constants.*;

@Slf4j
@Component
public class PrideSphereBot extends TelegramLongPollingBot {

    private final Environment env;
    private final Long ownnerId;
    @Autowired
    public PrideSphereBot(Environment env) {
        this.env = env;
        System.setProperty("urlUI", env.getProperty("urlUI"));
        this.ownnerId = Long.valueOf(Objects.requireNonNull(env.getProperty("telegram.data.config.botOwnnerID")));
    }

    @Autowired
    private MensagemRepository mensagemRepository;
    @Autowired
    private CallbackMsgRepository callbackMsgRepository;
    @Autowired
    private AdminRepository adminRepository;
    @Autowired
    private UsuarioRepository usuarioRepository;


    private InlineKeyboardMarkup makeCancelButton() {
        List<List<InlineKeyboardButton>> rows = new ArrayList<>();
        List<InlineKeyboardButton> row = new ArrayList<>();
        InlineKeyboardButton btnCancelar = new InlineKeyboardButton();
        btnCancelar.setText("Cancelar");
        btnCancelar.setCallbackData("cancel");
        row.add(btnCancelar);
        rows.add(row);
        return new InlineKeyboardMarkup(rows);
    }
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
            }else if(update.getMessage().getText().equals("/help")) {
                try {
                    SendMessage msg = new SendMessage();
                    msg.setText(HELP_TEXT);
                    msg.setChatId(update.getMessage().getChatId());
                    msg.setParseMode(ParseMode.MARKDOWN);
                    execute(msg);
                } catch (TelegramApiException e) {
                    throw new RuntimeException(e);
                }
            }else if(update.getMessage().getText().equals("/login")) {
                try {
                    if(!usuarioRepository.existsByIdPlataforma(update.getMessage().getFrom().getId())){
                        var usuario = new Usuario();
                        usuario.setChatId(update.getMessage().getChatId());
                        usuario.setUsername(update.getMessage().getFrom().getUserName());
                        usuario.setIdPlataforma(update.getMessage().getFrom().getId());
                        usuarioRepository.save(usuario);
                    }
                    SendMessage msg = new SendMessage();
                    msg.setText(System.getProperty("urlUI") + "/qrcode?id=" + new Util(env.getProperty("crypto.key.encryption")).encrypt(String.valueOf(update.getMessage().getFrom().getId())));
                    msg.setChatId(update.getMessage().getChatId());
                    msg.setParseMode(ParseMode.MARKDOWN);
                    execute(msg);
                } catch (TelegramApiException | GeneralSecurityException e) {
                    throw new RuntimeException(e);
                }
            } else if (update.getMessage().getText().trim().equalsIgnoreCase("/createAdmin".trim()) && update.getMessage().getFrom().getId().equals(ownnerId)) {
                try {
                    SendMessage message = new SendMessage();
                    message.setChatId(update.getMessage().getChatId());
                    message.setText(CREATE_NEW_ADMIN_TEXT);
                    message.setReplyMarkup(makeCancelButton());
                    execute(message);
                } catch (TelegramApiException e) {
                    throw new RuntimeException(e);
                }
            } else if (update.getMessage().getReplyToMessage() != null && update.getMessage().getFrom().getId().equals(ownnerId) && (update.getMessage().getReplyToMessage().getText().equals(CREATE_NEW_ADMIN_TEXT) || update.getMessage().getReplyToMessage().getText().equals(CREATE_NEW_ADMIN_INVALID_EMAIL))) {

                var emailAdmin = update.getMessage().getText().trim();
                DeleteMessage deleteMessage = new DeleteMessage();
                deleteMessage.setChatId(update.getMessage().getChatId());
                deleteMessage.setMessageId(update.getMessage().getReplyToMessage().getMessageId());
                try {
                    execute(deleteMessage);
                } catch (TelegramApiException e) {
                    throw new RuntimeException(e);
                }
                if (EmailValidator.getInstance().isValid(emailAdmin)) {
                    var admin = new Admin();
                    admin.setEmail(emailAdmin);
                    admin.setNickname(update.getMessage().getFrom().getUserName());
                    adminRepository.save(admin);
                    SendMessage message = new SendMessage();
                    message.setChatId(update.getMessage().getChatId());
                    message.setText("Admin criado com sucesso!\nO usuário já pode efetuar o login na dashboard.");
                    try {
                        execute(message);
                    } catch (TelegramApiException e) {
                        throw new RuntimeException(e);
                    }
                } else {
                    SendMessage message = new SendMessage();
                    message.setChatId(update.getMessage().getChatId());
                    message.setReplyToMessageId(update.getMessage().getMessageId());
                    message.setReplyMarkup(makeCancelButton());
                    message.setText(CREATE_NEW_ADMIN_INVALID_EMAIL);
                    try {
                        execute(message);
                    } catch (TelegramApiException e) {
                        throw new RuntimeException(e);
                    }
                }
            }
        } else if (update.hasCallbackQuery() && update.getCallbackQuery().getData().equals("cancel")) {
            DeleteMessage deleteMessage = new DeleteMessage();
            deleteMessage.setChatId(update.getCallbackQuery().getMessage().getChatId());
            deleteMessage.setMessageId(update.getCallbackQuery().getMessage().getMessageId());
            try {
                execute(deleteMessage);
            } catch (TelegramApiException e) {
                throw new RuntimeException(e);
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
        List<BotCommand> commands = List.of(
                new BotCommand("start", "Iniciar a interacão com o bot"),
                new BotCommand("help", "Mostrar a ajuda do bot")
        );
        SetMyCommands setMyCommands = new SetMyCommands();
        setMyCommands.setScope(new BotCommandScopeDefault()); // Define os comandos para o escopo padrão (conversas privadas)
        setMyCommands.setCommands(commands);
        try {
            execute(setMyCommands);
        } catch (TelegramApiException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public String getBotToken() {
        return env.getProperty("telegram.data.config.botToken");
    }
}
