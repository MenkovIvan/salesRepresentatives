package org.iamenko1.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserDto {
    @NonNull
    @JsonProperty("user_id")
    private Long userId;
    private String login;
    private String password;
    private String type;
    private String fio;
    @JsonProperty("telephone_number")
    private String telephoneNumber;
}
