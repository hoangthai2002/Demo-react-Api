package vn.demo.jobhunter.util.error;

import java.time.Instant;
import java.time.temporal.ChronoUnit;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jwt.JwsHeader;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.stereotype.Service;

@Service
public class SecurityUtil {
    public final JwtEncoder jwtEncoder;

    public SecurityUtil(JwtEncoder jwtEncoder) {
        this.jwtEncoder = jwtEncoder;

    }

    public static final MacAlgorithm JWT_ALGORITHM = MacAlgorithm.HS512;

    @Value("${test.jwt.base64-secret}")
    private String JwtKey;
    @Value("${test.jwt.token-validity-in-seconds}")
    private Long JwtKeyExpiration;

    public String createToken(Authentication authentication) {
        // @formatter:off
        
        Instant now = Instant.now();
        Instant validity = now.plus(this.JwtKeyExpiration, ChronoUnit.SECONDS);
    

        // @formatter:off
        //tạo ra phần boddy
        JwtClaimsSet claims = JwtClaimsSet.builder()
            .issuedAt(now)
            .expiresAt(validity)
            .subject(authentication.getName())
            .claim("test", authentication)
            .build();
        //tạo ra phần header
        JwsHeader jwsHeader = JwsHeader.with(JWT_ALGORITHM).build();
        return this.jwtEncoder.encode(JwtEncoderParameters.from(jwsHeader, claims)).getTokenValue();


    }

}
