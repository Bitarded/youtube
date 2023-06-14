package com.bitarrded.youtube.controller;

import com.bitarrded.youtube.dto.CommentDto;
import com.bitarrded.youtube.dto.UploadVideoResponse;
import com.bitarrded.youtube.dto.VideoDto;
import com.bitarrded.youtube.service.VideoService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import reactor.core.publisher.Mono;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/video")
@RequiredArgsConstructor
@Slf4j
@CrossOrigin(origins = "*")
public class VideoController {
    private final VideoService videoService;
    private final VideoDto videoDto;


    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public UploadVideoResponse uploadVideo(@RequestParam("file") MultipartFile file) {
        try {
            log.info("File: {} {} kb uploaded successfully ", file.getOriginalFilename(), file.getSize());
            return videoService.uploadVideo(file);
        } catch (IOException e) {
            log.info("Failed to upload file");
            throw new IllegalArgumentException("Failed to upload file" +e);
        }
    }

    @PostMapping("/thumbnail")
    @ResponseStatus(HttpStatus.CREATED)
    public void uploadThumbnail(@RequestParam("file") MultipartFile file, @RequestParam("videoId") String videoId) {
        try {
            log.info("Video: {} got new thumbnail: {} ", videoService.getVideoById(videoId).getVideoUrl(),
                    file.getOriginalFilename());
            videoService.uploadThumbnail(file, videoId);
        } catch (IOException e) {
            throw new IllegalArgumentException("Failed to upload Thumbnail");
        }
    }

    @PutMapping
    @ResponseStatus(HttpStatus.OK)
    public VideoDto editVideoDetails(@RequestBody VideoDto videoDto) {
        return videoService.editVideo(videoDto);
    }

    @GetMapping("/{videoId}")
    @ResponseStatus(HttpStatus.OK)
    public VideoDto getVideoDetails(@PathVariable String videoId) {
        return videoService.getVideoDetails(videoId);
    }


    @GetMapping(value = "/player/{videoId}")
    public Mono<Resource> getVideoStream(@PathVariable String videoId, String range) {
        log.info("Range in bytes: " + range);
        return videoService.getVideoStream(videoId);
    }

    @PostMapping("/{videoId}/like")
    @ResponseStatus(HttpStatus.OK)
    public VideoDto likeVideo(@PathVariable String videoId) {
        return videoService.likeVideo(videoId);
    }

    @PostMapping("/{videoId}/dislike")
    @ResponseStatus(HttpStatus.OK)
    public VideoDto dislikeVideo(@PathVariable String videoId) {
        return videoService.dislikeVideo(videoId);
    }

    @PostMapping("/{videoId}/comment")
    @ResponseStatus(HttpStatus.OK)
    public void addComment(@PathVariable String videoId, @RequestBody CommentDto commentDto) {
        videoService.addComment(videoId, commentDto);
    }

    @GetMapping("/{videoId}/comment")
    @ResponseStatus(HttpStatus.OK)
    public List<CommentDto> getAllComments(@PathVariable String videoId) {
        return videoService.getAllComments(videoId);
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<VideoDto> getAllVideos() {
        return videoService.getAllVideos();
    }


}

