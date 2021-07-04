package in.hocg.sso.server2.sample.config.security;

import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.token.TokenEnhancer;
import org.springframework.stereotype.Component;

@Component
public class BootTokenEnhancer implements TokenEnhancer {

    private final static String CLIENT_CREDENTIALS = "client_credentials";


    @Override
    public OAuth2AccessToken enhance(OAuth2AccessToken accessToken, OAuth2Authentication authentication) {
        if (CLIENT_CREDENTIALS.equals(authentication.getOAuth2Request().getGrantType())) {
            return accessToken;
        }

//        final Map<String, Object> additionalInfo = new HashMap<>(8);
//        Map<String, Object> info = new LinkedHashMap<>();
//        info.put("user", SecurityContextHolder.getContext().getAuthentication().getPrincipal());
//        additionalInfo.put("info", info);
//        ((DefaultOAuth2AccessToken) accessToken).setAdditionalInformation(additionalInfo);
        return accessToken;
    }
}
