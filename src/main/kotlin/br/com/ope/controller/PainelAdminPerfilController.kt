package br.com.ope.controller

import br.com.ope.model.Administrador
import br.com.ope.model.Usuario
import br.com.ope.repository.UsuarioRepository
import br.com.ope.vo.MensagemVO
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.validation.BindingResult
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.servlet.mvc.support.RedirectAttributes
import javax.validation.Valid

@Controller
@RequestMapping("/painel/admin/perfil")
class PainelAdminPerfilController(val usuarioRepository: UsuarioRepository) {

    @GetMapping()
    fun indexPerfil(model: Model, @AuthenticationPrincipal usuarioAutenticado: Usuario?) : String {
        model.addAttribute("usuario", usuarioAutenticado)
        return "/painel/admin/perfil/index"
    }

    @PostMapping()
    fun perfilSalvar(model: Model, @AuthenticationPrincipal usuarioAutenticado: Usuario?, @Valid usuario : Administrador,
                     bindingResult : BindingResult,
                     redirectAttributes: RedirectAttributes) : String {

        if (bindingResult.hasErrors()) {
            return indexPerfil(model, usuario)
        }

        usuarioRepository.save(usuarioAutenticado!!.parse(usuario))

        redirectAttributes.addFlashAttribute("mensagem", MensagemVO("Usuário alterado!","Sucesso!", MensagemVO.TipoMensagem.success ))
        return "redirect:/painel/admin/perfil"
    }

}