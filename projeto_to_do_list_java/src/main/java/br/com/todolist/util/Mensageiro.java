package br.com.todolist.util;

public class Mensageiro {

    public boolean enviarEmail(String emailDestino, String assunto, String corpoMensagem, String anexo) {
        // Lógica de envio de email com anexo
        return true;
    }

    public boolean enviarEmail(String emailDestino, String assunto, String corpoMensagem) {
        // Reutiliza o método existente passando null para o caminho do arquivo
        return enviarEmail(emailDestino, assunto, corpoMensagem, null);
    }
}