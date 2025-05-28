package ua.edu.ukma.cyber.soul.splitfast.security;

import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import ua.edu.ukma.cyber.soul.splitfast.controllers.rest.model.LoginRequestDto;
import ua.edu.ukma.cyber.soul.splitfast.controllers.rest.model.LoginResponseDto;
import ua.edu.ukma.cyber.soul.splitfast.controllers.rest.model.ResetTokenRequestDto;
import ua.edu.ukma.cyber.soul.splitfast.controllers.rest.model.ResetTokenResponseDto;
import ua.edu.ukma.cyber.soul.splitfast.domain.entitites.UserEntity;
import ua.edu.ukma.cyber.soul.splitfast.exceptions.UnauthenticatedException;
import ua.edu.ukma.cyber.soul.splitfast.mappers.EnumsMapper;
import ua.edu.ukma.cyber.soul.splitfast.repositories.UserRepository;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final AuthenticationManager authenticationManager;
    private final JWTService jwtService;
    private final UserRepository userRepository;
    private final EnumsMapper enumsMapper;

    public LoginResponseDto login(LoginRequestDto loginRequestDto) {
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(loginRequestDto.getUsername(), loginRequestDto.getPassword());
        Authentication authentication = authenticationManager.authenticate(authenticationToken);
        String role = authentication.getAuthorities().iterator().next().getAuthority();
        String token = jwtService.generateToken(authentication.getName(), role);
        String refreshToken = jwtService.generateRefreshToken(authentication.getName());
        return new LoginResponseDto(token, refreshToken);
    }

    public ResetTokenResponseDto resetToken(ResetTokenRequestDto resetTokenRequestDto) {
        try {
            JWTService.VerificationResult verified = jwtService.verifyRefreshToken(resetTokenRequestDto.getRefreshToken());
            UserEntity user = userRepository.findByUsername(verified.username()).orElseThrow();
            String token = jwtService.generateToken(user.getUsername(), enumsMapper.toString(user.getRole()));
            return new ResetTokenResponseDto(token);
        } catch (Exception e) {
            throw new UnauthenticatedException();
        }
    }
}
