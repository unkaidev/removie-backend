package vn.removie.movies.Entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.DocumentReference;
@Document(collection = "casts")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Cast {
    @Id
    private ObjectId id;
    private String name;
    private String known_for_department;
    private String character;
    private String profile;
}
