package vn.removie.movies.Entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.DocumentReference;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Document(collection = "wishlist")
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
public class Wishlist {
    @Id
    private ObjectId id;
    @DBRef
    private Set<Movie> movies = new HashSet<Movie>();
}
