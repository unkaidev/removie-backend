package vn.removie.movies.Payload.Response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class MessageResponse {
    private int EC;
    private String EM;
    private Object DT;

}
