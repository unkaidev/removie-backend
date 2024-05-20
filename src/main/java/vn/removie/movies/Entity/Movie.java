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
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Document(collection = "movies")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Movie {
    @Id
    private ObjectId id;
    private String imdbId;

    private boolean adult;
    @DBRef
    private Set<Genre> genre_ids = new HashSet<Genre>();
    private String original_language;
    private String original_title;

    private String tagline;
    private String overview;
    private long popularity;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date releaseDate;
    private String title;
    private long vote_count;
    private float vote_average;

    private List<String> backdrops;
    private String poster;

    @DocumentReference
    private List<Review> reviewIds;

    private String trailerLink;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date createAt;
    private boolean active;
    private int viewToday;
    private int viewThisWeek;
}
