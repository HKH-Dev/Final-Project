package co.edu.uniquindio.reservasapp.plataforma.alojamiento;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Review {
    private String userId;
    private int rating; // 1 to 5
    private String comment;

    // Constructor
    public Review(String userId, int rating, String comment) {
        this.userId = userId;
        this.rating = rating;
        this.comment = comment;
    }

    // Getters and Setters
    public String getUserId() {
        return userId;
    }

    public int getRating() {
        return rating;
    }

    public String getComment() {
        return comment;
    }

    // Optional: toString method for easier debugging
    @Override
    public String toString() {
        return "Review{" +
                "userId='" + userId + '\'' +
                ", rating=" + rating +
                ", comment='" + comment + '\'' +
                '}';
    }
}