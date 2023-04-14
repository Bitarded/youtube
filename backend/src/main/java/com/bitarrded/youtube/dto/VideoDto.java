package com.bitarrded.youtube.dto;

import com.bitarrded.youtube.model.VideoStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.stereotype.Component;

import java.util.Set;

@Component
@Data
@AllArgsConstructor
@NoArgsConstructor
public class VideoDto {
    @Id
    private String id;
    private String title;
    private String description;
    private Set<String> tags;
    private String videoUrl;
    private VideoStatus videoStatus;
    private String thumbnailUrl;

}
