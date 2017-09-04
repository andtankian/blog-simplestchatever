package br.com.andtankian.blog.websockets1.impl;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;
import javax.websocket.OnClose;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;

/**
 *
 * @author Andrew Ribeiro
 */
@ServerEndpoint(value = "/talk")
public class SimplestChatEverEndpoint {

    /*É necessário salvar todas as sessões em algo que possa ser enxergado
      por toda a aplicação.
      Como este é um exemplo minimalista, vamos usar uma static global variable.*/
    public final static Set sessions = new HashSet();

    /**
     * Este método é executado quando um novo usuário abre uma conexão websocket
     * com este endpoint ("/talk").
     *
     * @param session
     * @param config
     */
    @OnOpen
    public void onOpen(Session session) {
        /*Adicionando a nova sessão o conjunto de sessões.*/
        sessions.add(session);
    }

    /**
     * Este método é executado quando o usuário fecha a conexão com o websocket
     * neste endpoint ("/talk") Isso pode ocorrer por diversas formas: Usuário
     * fechou ou atualizou a página contendo o script conectado no endpoint;
     * Usuário fechou explicitamente a conexão com o método JS close().
     *
     * @param session
     */
    @OnClose
    public void onClose(Session session) {
        /*Já que o usuário está fechando conexão,
          vamos retira-lo do conjunto de sessões ativas.*/
        sessions.remove(session);
    }

    /**
     * Este método é executado quando o endpoint recebe uma mensagem vindo do
     * cliente; O método então replica a mensagem para todas as demais sessões.
     *
     * @param message
     * @param session
     * @throws IOException
     */
    @OnMessage
    public void onMessage(String message, Session session) throws IOException {
        /*Isso é uma mensagem vindo diretamente do
          cliente através de um canal TCP.
          Não há nenhuma http request comum e sim
          uma completa mensagem através do socket.
            
          Vamos replicar essa mensagem para todas
          as sessões ativas.*/
        for (Object s : sessions) {
            Session sn = (Session) s;
            sn.getBasicRemote().sendText(message);
        }
    }
}
