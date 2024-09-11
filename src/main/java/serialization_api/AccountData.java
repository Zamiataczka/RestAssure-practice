package serialization_api;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class AccountData {
    private String email;
    private String password;
}
