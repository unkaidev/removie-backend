package vn.removie.movies.Entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;
@Document(collection = "credits")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Credit {
    @Id
    private ObjectId id;
    @DBRef
    private Movie movie;
    @DBRef
    private Set<Cast> cast_ids = new HashSet<Cast>();
    @DBRef
    private Set<Crew> crew_ids = new HashSet<Crew>();

    private String title;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date createAt;
    private boolean active;
}
