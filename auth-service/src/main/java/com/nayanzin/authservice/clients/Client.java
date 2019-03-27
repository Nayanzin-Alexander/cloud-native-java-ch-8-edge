package com.nayanzin.authservice.clients;

import lombok.*;
import org.springframework.util.StringUtils;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "clients")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Client {

    @Id
    @GeneratedValue
    @EqualsAndHashCode.Exclude
    private Long id;

    private String clientId;

    private String secret;

    private String scopes = "openid";

    private String authorizedGrantTypes = StringUtils
            .arrayToCommaDelimitedString(new String[]{
                    "authorization_code",
                    "refresh_token",
                    "password"
            });

    private String authorities = StringUtils
            .arrayToCommaDelimitedString(new String[]{
                    "ROLE_USER", "ROLE_ADMIN"});

    private String autoApproveScopes = ".*";

    public Client(String clientId, String clientSecret) {
        this.clientId = clientId;
        this.secret = clientSecret;
    }
}
