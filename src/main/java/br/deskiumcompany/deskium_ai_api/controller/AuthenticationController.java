package br.deskiumcompany.deskium_ai_api.controller;

import br.deskiumcompany.deskium_ai_api.domain.Usuario;
import br.deskiumcompany.deskium_ai_api.dto.UsuarioDTO;
import br.deskiumcompany.deskium_ai_api.dto.auth.AuthenticationDTO;
import br.deskiumcompany.deskium_ai_api.dto.auth.LoginResponseDto;
import br.deskiumcompany.deskium_ai_api.infra.security.JwtService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("auth")
public class AuthenticationController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtService jwtService;

    @PostMapping("/login")
    public ResponseEntity<LoginResponseDto> login(@RequestBody @Valid AuthenticationDTO dto){

        var emailSenhaToken = new UsernamePasswordAuthenticationToken(dto.getEmail(), dto.getSenha());
        var auth = authenticationManager.authenticate(emailSenhaToken);
        var token = jwtService.generateToken((Usuario) auth.getPrincipal());

        return ResponseEntity.ok(new LoginResponseDto(token));
    }

    @GetMapping()
    public ResponseEntity<UsuarioDTO> getAuthenticatedUser(Authentication auth){
        var usuario = (Usuario) auth.getPrincipal();

        return ResponseEntity.ok().body(new UsuarioDTO(usuario));
    }
}
