package com.bitarrded.youtube.repository;

import com.bitarrded.youtube.model.Video;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface VideoRepository extends MongoRepository<Video,String> {

    Optional<Video> findByUuid(String videoUuid);
}
