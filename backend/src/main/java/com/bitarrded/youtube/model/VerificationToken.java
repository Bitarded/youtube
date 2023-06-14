package com.bitarrded.youtube.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;


@Data
@AllArgsConstructor
@NoArgsConstructor
@Document(value = "token")
public class VerificationToken {

    @Id
    private String id;
    private String token;
    private User user;
    private LocalDateTime expires;

    public void expiresIn(long min){
      setExpires(this.expires=LocalDateTime.now().plusMinutes(min));
    }
    public boolean isExpired(){
      return getExpires().isBefore(LocalDateTime.now());
    }
}