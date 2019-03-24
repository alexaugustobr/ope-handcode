package br.com.ope.controller

import br.com.ope.model.Usuario
import br.com.ope.repository.UsuarioRepository
import br.com.ope.vo.MensagemVO
import br.com.ope.vo.UsuarioEdicaoVO
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
@RequestMapping("/painel/aluno/perfil")
class PainelAlunoPerfilController(val usuarioRepository: UsuarioRepository) {

    @GetMapping()
    fun index(model: Model, @AuthenticationPrincipal usuarioAutenticado: Usuario?) : String {
        return form(model, UsuarioEdicaoVO(nome = usuarioAutenticado!!.nome, email = usuarioAutenticado!!.email))
    }

    fun form(model: Model, usuario : UsuarioEdicaoVO) : String {
        model.addAttribute("usuario", usuario)
        return "/painel/aluno/perfil/index"
    }

    @PostMapping()
    fun perfilSalvar(model: Model, @AuthenticationPrincipal usuarioAutenticado: Usuario?, @Valid usuario : UsuarioEdicaoVO,
                     bindingResult : BindingResult,
                     redirectAttributes: RedirectAttributes) : String {
        try {
            if (bindingResult.hasErrors()) {
                return form(model, usuario)
            }

            usuarioRepository.save(usuarioAutenticado!!.parse(usuario))

            redirectAttributes.addFlashAttribute("mensagem", MensagemVO("Usuário alterado!","Sucesso!", MensagemVO.TipoMensagem.success ))
            return "redirect:/painel/aluno/perfil"
        } catch (e : Exception) {
            e.printStackTrace()
            redirectAttributes.addFlashAttribute("mensagem", MensagemVO("Ocorreu um erro ao tentar salvar o usuário!","Erro!", MensagemVO.TipoMensagem.danger ))
            return "redirect:/painel/aluno/perfil"
        }

    }

}