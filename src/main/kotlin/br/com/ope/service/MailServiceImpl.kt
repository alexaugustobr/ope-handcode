package br.com.ope.service

import br.com.ope.model.Grupo
import org.apache.commons.mail.DefaultAuthenticator
import org.apache.commons.mail.HtmlEmail
import org.apache.commons.mail.SimpleEmail
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service

@Service
class MailServiceImpl : MailService {

    @Value("mail.pwd")
    val YOUR_PASSWORD = ""
    val YOUR_EMAIL = "handcode@amazestudio.com.br"
    val MAIL_HOST = "br362.hostgator.com.br"
    val MAIL_SENDER = "handcode@amazestudio.com.br"

    fun enviarEmailGrupoAprovado(grupo: Grupo) {

        val destinatarios = mutableListOf<String>()

        for (aluno in grupo.alunos) {
            destinatarios.add(aluno.email)
        }

        val corpo = "<br> O seu grupo ${grupo.nome} foi aprovado! <br> Sua senha para acessar a plataforma é:<br>senha<br>Link: <a href='http://opehandcode.herokuapp.com/'>OPE<a><br>"

        for (destinatario in destinatarios) {
            this.enviarEmail(destinatario, "Grupo aprovado!", corpo)
        }
    }

    fun enviarEmailGrupoRecusado(grupo: Grupo) {

        val destinatarios = mutableListOf<String>()

        for (aluno in grupo.alunos) {
            destinatarios.add(aluno.email)
        }

        val corpo = "<br> O seu grupo ${grupo.nome} foi recusado! <br> Crie um novo grupo seguindo as regras informadas pelo professor!<br>Link: <a href='http://opehandcode.herokuapp.com/grupos'>OPE<a><br>"

        for (destinatario in destinatarios) {
            this.enviarEmail(destinatario, "Grupo recusado!", corpo)
        }

    }

    override fun enviarEmail(destinatario: String, assunto: String, corpo: String) {
        try {
           Thread{
               HtmlEmail().apply {
                   setHostName(MAIL_HOST)
                   setSmtpPort(587)
                   setAuthenticator(DefaultAuthenticator(YOUR_EMAIL, YOUR_PASSWORD))
                   setSSLOnConnect(true)
                   setFrom(MAIL_SENDER)
                   setSubject(assunto)
                   setHtmlMsg(corpo.trimIndent())
                   addTo(destinatario)
               }.send()
           }.start()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override fun enviarEmail(destinatarios: MutableList<String>, assunto: String, corpo: String) {
        for (destinatario in destinatarios) {
            try {
                SimpleEmail().apply {
                    setHostName(MAIL_HOST)
                    setSmtpPort(587)
                    setAuthenticator(DefaultAuthenticator(YOUR_EMAIL, YOUR_PASSWORD))
                    setSSLOnConnect(true)
                    setFrom(MAIL_SENDER)
                    setSubject(assunto)
                    setMsg(corpo.trimIndent())
                    addTo(destinatario)
                }.send()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}

