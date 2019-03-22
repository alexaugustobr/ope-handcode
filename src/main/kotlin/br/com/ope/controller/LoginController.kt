package br.com.ope.controller

import br.com.ope.model.Usuario
import br.com.ope.vo.MensagemVO
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import java.util.*
import javax.servlet.http.HttpServletRequest

@Controller
class LoginController {

    @RequestMapping("/login")
    fun login(model: Model, @RequestParam(value = "error", required = false) error: Optional<String>, request : HttpServletRequest): String {

        if (error.isPresent) {
            val attr = Optional.ofNullable(request.session.getAttribute("SPRING_SECURITY_LAST_EXCEPTION"))
            if (attr.isPresent) {
                val exception = attr.get() as Exception
                model.addAttribute("mensagem", MensagemVO(conteudo = "Usuário ou senha incorretos!", tipo = MensagemVO.TipoMensagem.danger))
            }
        }

        return "login"
    }

}