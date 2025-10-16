package br.deskiumcompany.deskium_ai_api.infra.security;

import br.deskiumcompany.deskium_ai_api.service.UsuarioService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
@Component //Componente utilizado para filtrar todas as requisições validar se existe token no header
public class SecurityFilter extends OncePerRequestFilter {

    @Autowired
    JwtService jwtService;

    @Autowired
    UsuarioService usuarioService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        var token = this.recoverToken(request);

        if(token != null){
           String login = jwtService.validateToken(token);
           UserDetails user = usuarioService.findByLogin(login);

           //Validando as informações do ROLE
           var authentication = new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities());

           //Salvo a autenticação no contexto do security, para os próximos filtros e requisições
           SecurityContextHolder.getContext().setAuthentication(authentication);
        }
        //Vai para o proximo filtro que ira validar a necessidade do token ou não
        filterChain.doFilter(request, response);
    }

    private String recoverToken(HttpServletRequest request) {
        var authHeader = request.getHeader("Authorization");
        if(authHeader == null) return null;

        return authHeader.replace("Bearer ", "");
    }
}
