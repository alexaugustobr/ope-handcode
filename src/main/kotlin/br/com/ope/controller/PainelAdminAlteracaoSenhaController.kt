package br.com.ope.controller

import br.com.ope.model.Usuario
import br.com.ope.repository.UsuarioRepository
import br.com.ope.vo.MensagemVO
import br.com.ope.vo.UsuarioAlteracaoSenhaVO
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.validation.BindingResult
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.servlet.mvc.support.RedirectAttributes
import javax.validation.Valid

@Controller
@RequestMapping(value = "/painel/admin/alterar-senha")
class PainelAdminAlteracaoSenhaController(val usuarioRepository: UsuarioRepository) {

    @GetMapping()
    fun indexAlterarSenha(model: Model, @AuthenticationPrincipal usuarioAutenticado: Usuario?, usuarioAlteracaoSenhaVO: UsuarioAlteracaoSenhaVO) : String {
        model.addAttribute("usuarioAlteracaoSenhaVO", usuarioAlteracaoSenhaVO)
        return "/painel/admin/alterar-senha/index"
    }

    @PostMapping()
    fun alterarSenhaSalvar(model: Model,
                           @AuthenticationPrincipal usuarioAutenticado: Usuario?,
                           @Valid usuarioAlteracaoSenhaVO : UsuarioAlteracaoSenhaVO,
                           bindingResult : BindingResult,
                           redirectAttributes: RedirectAttributes) : String {
        if (bindingResult.hasErrors()) {
            return(indexAlterarSenha(model,usuarioAutenticado,usuarioAlteracaoSenhaVO))
        }

        val usuarioOptional = usuarioRepository.findById(usuarioAutenticado!!.id!!)

        if (!usuarioOptional.isPresent) {
            redirectAttributes.addFlashAttribute("mensagem", MensagemVO("Usuário não encontrado!","Erro!", MensagemVO.TipoMensagem.danger ))
            return "redirect:/painel/admin/alterar-senha"
        }

        val usuario = usuarioOptional.get()

        usuario.senha = BCryptPasswordEncoder().encode(usuarioAlteracaoSenhaVO.novaSenha)

        usuarioRepository.save(usuario)

        redirectAttributes.addFlashAttribute("mensagem", MensagemVO("Senha alterada!","Sucesso!", MensagemVO.TipoMensagem.success ))
        return "redirect:/painel/admin/alterar-senha"
    }

}