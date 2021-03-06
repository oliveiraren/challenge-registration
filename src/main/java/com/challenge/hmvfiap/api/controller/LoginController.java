package com.challenge.hmvfiap.api.controller;

import com.challenge.hmvfiap.api.dto.JwtTokenDTO;
import com.challenge.hmvfiap.api.dto.LoginInputDTO;
import com.challenge.hmvfiap.domain.entity.AppUser;
import com.challenge.hmvfiap.domain.service.JwtTokenService;
import com.challenge.hmvfiap.domain.service.LoginService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "api/login")
@AllArgsConstructor
public class LoginController {

    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private JwtTokenService jwtTokenService;
    @Autowired
    private LoginService loginService;

    @PostMapping
    public ResponseEntity<JwtTokenDTO> autentica(@RequestBody LoginInputDTO loginRequest){

        Authentication authentication = new UsernamePasswordAuthenticationToken(loginRequest.getUserName(),loginRequest.getPassword());
        Authentication authenticate = authenticationManager.authenticate(authentication);
        String token = jwtTokenService.geraToken(authenticate);
        Long id = jwtTokenService.pegarIdUsuario(token);
        AppUser appUser = loginService.buscarPorId(id);
        String firstName = appUser.getFullName().split(" ")[0];
        return ResponseEntity.ok(new JwtTokenDTO(token, "Bearer", appUser.getId(), firstName, appUser.getEmail()));
    }
}