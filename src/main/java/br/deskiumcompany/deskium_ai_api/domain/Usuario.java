package br.deskiumcompany.deskium_ai_api.domain;

import br.deskiumcompany.deskium_ai_api.domain.enums.TipoUsuario;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Usuario extends EntidadeBase implements UserDetails {

    @Column(nullable = false, length = 100)
    private String nomeCompleto;

    @Column(nullable = false)
    private String email;

    @Column(nullable = false)
    private String senha;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TipoUsuario tipoUsuario;

    @Column(nullable = false)
    private boolean ativo = true;

    //Metodos utilizados pelo spring security
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        if(this.tipoUsuario == TipoUsuario.GESTOR_SUPORTE)
            return List.of(
                    new SimpleGrantedAuthority("ROLE_GESTOR_SUPORTE"),
                    new SimpleGrantedAuthority("ROLE_SUPORTE"));

        if (this.tipoUsuario == TipoUsuario.SUPORTE) {
            return List.of(
                    new SimpleGrantedAuthority("ROLE_SUPORTE"));

        }else {
            return List.of(
                    new SimpleGrantedAuthority("ROLE_SOLICITANTE"));
        }
    }

    @Override
    public String getPassword() {
        return senha;
    }

    @Override
    public String getUsername() {
        return email;
    }
}
