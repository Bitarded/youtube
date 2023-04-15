package com.bitarrded.youtube.service;

import com.bitarrded.youtube.dto.UploadVideoResponse;
import com.bitarrded.youtube.dto.VideoDto;
import com.bitarrded.youtube.model.Video;
import com.bitarrded.youtube.repository.VideoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import reactor.core.publisher.Mono;

import java.io.IOException;

@Service
@RequiredArgsConstructor
public class VideoService {
    private final VideoRepository videoRepository;
    private final FileServiceImpl fileService;
    private final ResourceLoader resourceLoader;
    private static final String FORMAT = "file:///D:/uploads/%s";

    private static final String HOST = "http://localhost:8080/video/player/";


    public UploadVideoResponse uploadVideo(MultipartFile multipartFile) throws IOException {
        String videoID = fileService.uploadFile(multipartFile); //Save in Uploads
        Video video = new Video();
        video.setVideoUUID(videoID);
        video.setVideoUrl(HOST+videoID);
        Video savedVideo = videoRepository.save(video); //Save in DataBase
        return new UploadVideoResponse(savedVideo.getId(),(savedVideo.getVideoUrl()));
    }
    public Mono<Resource> getVideoStream(String videoID) {
        var video = getVideoByUUID(videoID);
//        String x = "file:///D:/uploads/1.mp4";
        return Mono.fromSupplier(()-> resourceLoader.getResource(String.format(FORMAT,video.getVideoUUID())));
    }



    public void uploadThumbnail(MultipartFile file, String videoId) throws IOException {
        Video video = getVideoById(videoId);
        String thumbnailUUID = fileService.uploadFile(file);
        video.setThumbnailID(thumbnailUUID);
        video.setThumbnailUrl(HOST+thumbnailUUID);
        videoRepository.save(video);
    }

    public VideoDto editVideo(VideoDto videoDto) {
        Video savedVideo = getVideoById(videoDto.getId());
        savedVideo.setTitle(videoDto.getTitle());
        savedVideo.setDescription(videoDto.getDescription());
        savedVideo.setTags(videoDto.getTags());
        savedVideo.setThumbnailUrl(videoDto.getThumbnailUrl());
        savedVideo.setVideoStatus(videoDto.getVideoStatus());
        videoRepository.save(savedVideo);
        return videoDto;
    }

    public Video getVideoById(String videoId) {
        return videoRepository.findById(videoId)
                .orElseThrow(() -> new IllegalArgumentException("Cannot find video by id - " + videoId));
    }
    public Video getVideoByUUID(String videoUUID) {
        return videoRepository.findByVideoUUID(videoUUID)
                .orElseThrow(() -> new IllegalArgumentException("Cannot find video by videoUUID - " + videoUUID));

    }
    public VideoDto getVideoDetails(String videoId) {
        Video savedVideo = getVideoById(videoId);
        VideoDto videoDto = new VideoDto();
        videoDto.setVideoUrl(savedVideo.getVideoUrl());
        videoDto.setThumbnailUrl(savedVideo.getThumbnailUrl());
        videoDto.setId(savedVideo.getId());
        videoDto.setTitle(savedVideo.getTitle());
        videoDto.setDescription(savedVideo.getDescription());
        videoDto.setVideoStatus(savedVideo.getVideoStatus());
        videoDto.setTags(savedVideo.getTags());
        return videoDto;
    }
}
