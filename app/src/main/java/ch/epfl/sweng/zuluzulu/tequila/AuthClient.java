package ch.epfl.sweng.zuluzulu.tequila;


/**
 * Client code for Tequila authentication.
 *
 * @author Solal Pirelli
 */
public final class AuthClient {
    public static String createCodeRequestUrl(OAuth2Config config) {
        return "https://tequila.epfl.ch/cgi-bin/OAuth2IdP/auth" +
                "?response_type=code" +
                "&client_id=" + HttpUtils.urlEncode(config.clientId) +
                "&redirect_uri=" + HttpUtils.urlEncode(config.redirectUri) +
                "&scope=" + config.scopes[0];
    }


    public static String extractCode(String redirectUri) {
        String marker = "code=";
        return redirectUri.substring(redirectUri.indexOf(marker) + marker.length());
    }
}