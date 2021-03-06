package ch.epfl.sweng.zuluzulu.tequila;

import com.google.gson.annotations.SerializedName;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import ch.epfl.sweng.zuluzulu.structure.user.User;

/**
 * Interface for authentication on a server.
 *
 * @author Solal Pirelli
 */
public final class AuthServer {
    public static Map<String, String> fetchTokens(OAuth2Config config, String code) throws IOException {
        Map<String, String> result = new HashMap<>();
        for (String scope : config.scopes) {
            String url = "https://tequila.epfl.ch/cgi-bin/OAuth2IdP/token" +
                    "?client_id=" + HttpUtils.urlEncode(config.clientId) +
                    "&client_secret=" + HttpUtils.urlEncode(config.clientSecret) +
                    "&redirect_uri=" + HttpUtils.urlEncode(config.redirectUri) +
                    "&grant_type=authorization_code" +
                    "&code=" + code +
                    "&scope=" + scope;
            JsonTokenContainer container = HttpUtils.fetch(url, JsonTokenContainer.class);

            if (container.error != null) {
                throw new IOException("Error from Tequila: " + container.error);
            }

            result.put(scope, container.token);
        }

        return result;
    }

    public static User fetchUser(String token) throws IOException {
        String url = "https://tequila.epfl.ch/cgi-bin/OAuth2IdP/userinfo" +
                "?access_token=" + HttpUtils.urlEncode(token);
        JsonProfile profile = HttpUtils.fetch(url, JsonProfile.class);

        if (profile.error != null) {
            throw new IOException("Error from Tequila:" + profile.error);
        }

        return createUser(profile);
    }

    public static User createUser(JsonProfile profile){

        User.UserBuilder builder = new User.UserBuilder();
        builder.setEmail(profile.email);
        if (profile.section != null) {
            builder.setSection(profile.section.substring(0, profile.section.indexOf("-")));
            builder.setSemester(profile.section.substring(profile.section.indexOf("-") + 1));
        } else {
            builder.setSection("");
            builder.setSemester("");
        }
        builder.setSciper(profile.sciper);
        builder.setGaspar(profile.gaspar);
        builder.setFirst_names(profile.firstNames);
        builder.setLast_names(profile.lastNames);
        builder.setFollowedChannels(new ArrayList<>());
        builder.setFollowedAssociations(new ArrayList<>());
        builder.setFollowedEvents(new ArrayList<>());

        return builder.buildAuthenticatedUser();
    }


    private static final class JsonTokenContainer {
        @SerializedName("error")
        public String error;

        @SerializedName("access_token")
        public String token;
    }

    public static final class JsonProfile {
        @SerializedName("error")
        public String error;

        @SerializedName("Firstname")
        public String firstNames;

        @SerializedName("Name")
        public String lastNames;

        @SerializedName("Email")
        public String email;

        @SerializedName("Sciper")
        public String sciper;

        @SerializedName("Username")
        public String gaspar;

        @SerializedName("Unit")
        public String section;
    }
}