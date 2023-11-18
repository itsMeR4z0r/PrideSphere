package com.r4z0r.pridesphere.bot;

import com.r4z0r.pridesphere.bot.data.CallbackMsg;
import com.r4z0r.pridesphere.bot.data.CallbackMsgRepository;
import com.r4z0r.pridesphere.bot.data.Mensagem;
import org.telegram.telegrambots.meta.api.methods.ParseMode;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.MessageEntity;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.util.ArrayList;
import java.util.List;

import static com.r4z0r.pridesphere.bot.Acoes.*;
import static com.r4z0r.pridesphere.bot.Constants.*;

public class Respostas {
    private final Mensagem mensagem;
    private final CallbackMsgRepository callbackMsgRepository;


    public Respostas(Mensagem mensagem, CallbackMsgRepository callbackMsgRepository) {
        this.mensagem = mensagem;
        this.callbackMsgRepository = callbackMsgRepository;
    }

    private String makeRelato(String etapa, String natureza, String relatoId) {
        var relato = new CallbackMsg();
        relato.setMensagem(mensagem);
        if (etapa != null) {
            relato.setEtapa(etapa);
        }
        if (natureza != null) {
            relato.setNatureza(natureza);
        }
        if (relatoId != null) {
            relato.setRelatoId(relatoId);
        }
        CallbackMsg result = callbackMsgRepository.save(relato);
        return String.valueOf(result.getId());
    }

    public SendMessage start() {

        SendMessage message = new SendMessage();
        message.setChatId(mensagem.getChatId());
        message.setText(START_TEXT);
        message.setParseMode(ParseMode.MARKDOWN);
        List<List<InlineKeyboardButton>> rows = new ArrayList<>();
        List<InlineKeyboardButton> row = new ArrayList<>();
        List<InlineKeyboardButton> row2 = new ArrayList<>();
        InlineKeyboardButton btnRelato = new InlineKeyboardButton();
        InlineKeyboardButton btnVerRelatos = new InlineKeyboardButton();
        btnRelato.setText(INICIO_OP_ENVIAR_RECLAMACAO);
        btnRelato.setCallbackData(makeRelato(ENVIAR_RELATO, null, null));
        btnVerRelatos.setText(INICIO_OP_VER_RECLAMACOES);
        btnVerRelatos.setCallbackData(makeRelato(VER_RELATO, null, null));

        row.add(btnRelato);
        row2.add(btnVerRelatos);
        rows.add(row);
        rows.add(row2);
        InlineKeyboardMarkup markupInline = new InlineKeyboardMarkup();
        markupInline.setKeyboard(rows);

        message.setReplyMarkup(markupInline);
        return message;
    }

    public EditMessageText menuInicial() {
        callbackMsgRepository.deleteByMessageId(mensagem.getMessageId());
        EditMessageText new_message = new EditMessageText();
        new_message.setChatId(mensagem.getChatId());
        new_message.setMessageId(mensagem.getMessageId());
        new_message.setText(START_TEXT);
        new_message.setParseMode(ParseMode.MARKDOWN);
        List<List<InlineKeyboardButton>> rows = new ArrayList<>();
        List<InlineKeyboardButton> row = new ArrayList<>();
        List<InlineKeyboardButton> row2 = new ArrayList<>();
        InlineKeyboardButton btnRelato = new InlineKeyboardButton();
        InlineKeyboardButton btnVerRelatos = new InlineKeyboardButton();
        btnRelato.setText(INICIO_OP_ENVIAR_RECLAMACAO);
        btnRelato.setCallbackData(makeRelato(ENVIAR_RELATO, null, null));
        btnVerRelatos.setText(INICIO_OP_VER_RECLAMACOES);
        btnVerRelatos.setCallbackData(makeRelato(VER_RELATO, null, null));

        row.add(btnRelato);
        row2.add(btnVerRelatos);
        rows.add(row);
        rows.add(row2);
        InlineKeyboardMarkup markupInline = new InlineKeyboardMarkup();
        markupInline.setKeyboard(rows);

        new_message.setReplyMarkup(markupInline);
        return new_message;
    }


    public EditMessageText enviarRelato() {
        List<List<InlineKeyboardButton>> rows = new ArrayList<>();
        List<InlineKeyboardButton> voltarParaMenu = new ArrayList<>();
        List<InlineKeyboardButton> row2 = new ArrayList<>();
        InlineKeyboardButton btnPositivo = new InlineKeyboardButton();
        InlineKeyboardButton btnNegativo = new InlineKeyboardButton();
        btnPositivo.setText("Positivo \uD83D\uDE0A");
        btnPositivo.setCallbackData(makeRelato(ENVIAR_RELATO_OP_ENVIAR, "Positivo", null));

        btnNegativo.setText("Negativo \uD83D\uDE14");
        btnNegativo.setCallbackData(makeRelato(ENVIAR_RELATO_OP_ENVIAR, "Negativo", null));

        row2.add(btnPositivo);
        row2.add(btnNegativo);
        rows.add(row2);

        InlineKeyboardButton btnVoltar = new InlineKeyboardButton();
        btnVoltar.setText("Voltar");
        btnVoltar.setCallbackData(makeRelato(MENU_INICIAL, null, null));
        voltarParaMenu.add(btnVoltar);
        rows.add(voltarParaMenu);
        EditMessageText new_message = new EditMessageText();
        new_message.setChatId(mensagem.getChatId());
        new_message.setMessageId(mensagem.getMessageId());
        new_message.setText("Por favor, selecione a natureza do seu relato:");
        new_message.setReplyMarkup(new InlineKeyboardMarkup(rows));
        return new_message;
    }

    public EditMessageText selectOpRelato() {
        List<List<InlineKeyboardButton>> rows = new ArrayList<>();
        List<InlineKeyboardButton> voltarParaMenu = new ArrayList<>();
        List<InlineKeyboardButton> row2 = new ArrayList<>();
        rows.add(row2);

        InlineKeyboardButton btnVoltar = new InlineKeyboardButton();
        btnVoltar.setText("Voltar");
        btnVoltar.setCallbackData(makeRelato(MENU_INICIAL, null, null));
        voltarParaMenu.add(btnVoltar);
        rows.add(voltarParaMenu);
        EditMessageText new_message = new EditMessageText();
        new_message.setChatId(mensagem.getChatId());
        new_message.setMessageId(mensagem.getMessageId());
        new_message.setText("Agora, escolha o tipo de relato que melhor descreve sua experiência na lista abaixo:");
        new_message.setReplyMarkup(new InlineKeyboardMarkup(rows));
        return new_message;
    }

    public EditMessageText verRelatos() {
        List<List<InlineKeyboardButton>> rows = new ArrayList<>();
        List<InlineKeyboardButton> voltarParaMenu = new ArrayList<>();

        InlineKeyboardButton btnVoltar = new InlineKeyboardButton();
        btnVoltar.setText("Voltar");
        btnVoltar.setCallbackData(makeRelato(MENU_INICIAL, null, null));
        voltarParaMenu.add(btnVoltar);
        rows.add(voltarParaMenu);
        EditMessageText new_message = new EditMessageText();
        new_message.setChatId(mensagem.getChatId());
        new_message.setMessageId(mensagem.getMessageId());
        new_message.setText("Para mostrar relatos da sua região, por favor, compartilhe sua localização. Basta tocar no ícone de localização abaixo \uD83D\uDCCD.");
        new_message.setReplyMarkup(new InlineKeyboardMarkup(rows));
        return new_message;
    }
}
