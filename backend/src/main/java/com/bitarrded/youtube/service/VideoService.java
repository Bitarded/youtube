package com.bitarrded.youtube.service;

import com.bitarrded.youtube.dto.CommentDto;
import com.bitarrded.youtube.dto.UploadVideoResponse;
import com.bitarrded.youtube.dto.VideoDto;
import com.bitarrded.youtube.model.Comment;
import com.bitarrded.youtube.model.Video;
import com.bitarrded.youtube.repository.VideoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import reactor.core.publisher.Mono;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class VideoService {
    private final VideoRepository videoRepository;
    private final FileServiceImpl fileService;
    private final ResourceLoader resourceLoader;
//    private static final String FORMAT = "file:///D:/uploads/%s";
    private static final String FORMAT = "classpath:uploads1\\%s";
    private static final String HOST = "http://localhost:8080/video/player/";
    private final UserService userService;


    public UploadVideoResponse uploadVideo(MultipartFile multipartFile) throws IOException {
        String videoID = fileService.uploadFile(multipartFile); //Save in Uploads
        Video video = new Video();
        video.setVideoUUID(videoID);
        video.setVideoUrl(HOST + videoID);
        Video savedVideo = videoRepository.save(video); //Save in DataBase
        return new UploadVideoResponse(savedVideo.getId(), (savedVideo.getVideoUrl()));
    }

    public Mono<Resource> getVideoStream(String videoID) {
        var video = getVideoByUUID(videoID);
        return Mono.fromSupplier(() -> resourceLoader.getResource(String.format(FORMAT, video.getVideoUUID())));
    }


    public void uploadThumbnail(MultipartFile file, String videoId) throws IOException {
        Video video = getVideoById(videoId);
        String thumbnailUUID = fileService.uploadFile(file);
        video.setThumbnailID(thumbnailUUID);
        video.setThumbnailUrl(HOST + thumbnailUUID);
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

        increaseVideoCount(savedVideo);
//        userService.addVideoToHistory(videoId);

        return mapToVideoDto(savedVideo);
    }



    private void increaseVideoCount(Video savedVideo) {
        savedVideo.incrementViewCount();
        videoRepository.save(savedVideo);
    }

    /**
     * @param videoId
     * @logic Like or Remove Like
     */
    public VideoDto likeVideo(String videoId) {
        Video videoById = getVideoById(videoId);
        if (userService.ifLikedVideo(videoId)) {  // Liked
            videoById.incrementLikes();
            userService.removeFromLikedVideos(videoId);
        } else if (userService.ifDisLikedVideo(videoId)) { //Disliked
            videoById.decrementDisLikes();
            userService.removeFromDisLikedVideos(videoId);
            videoById.incrementLikes();
            userService.addToLikedVideos(videoId);
        } else {
            videoById.incrementLikes();     //None
            userService.addToLikedVideos(videoId);
        }
        videoRepository.save(videoById);
        return mapToVideoDto(videoById);
    }

    /**
     * @param videoId
     * @logic Dislike or remove Dislike
     */
    public VideoDto dislikeVideo(String videoId) {
        Video videoById = getVideoById(videoId);
        if (userService.ifDisLikedVideo(videoId)) {        // Disliked
            videoById.decrementDisLikes();
            userService.removeFromDisLikedVideos(videoId);
        } else if (userService.ifLikedVideo(videoId)) {    // Liked
            videoById.decrementLikes();
            userService.removeFromLikedVideos(videoId);
            videoById.incrementDisLikes();
            userService.addToDisLikedVideos(videoId);
        } else {                                           // None
            videoById.incrementDisLikes();
            userService.addToDisLikedVideos(videoId);
        }
        videoRepository.save(videoById);
        return mapToVideoDto(videoById);
    }

    private static VideoDto mapToVideoDto(Video savedVideo) {
        VideoDto videoDto = new VideoDto();
        videoDto.setVideoUrl(savedVideo.getVideoUrl());
        videoDto.setThumbnailUrl(savedVideo.getThumbnailUrl());
        videoDto.setId(savedVideo.getId());
        videoDto.setTitle(savedVideo.getTitle());
        videoDto.setDescription(savedVideo.getDescription());
        videoDto.setVideoStatus(savedVideo.getVideoStatus());
        videoDto.setTags(savedVideo.getTags());
        videoDto.setLikeCount(savedVideo.getLikes().get());
        videoDto.setDislikeCount(savedVideo.getDislikes().get());
        videoDto.setViewCount(savedVideo.getViewCount().get());
        return videoDto;
    }

    public void addComment(String videoId, CommentDto commentDto) {
        Video video = getVideoById(videoId);
        Comment comment = new Comment();
        comment.setText(commentDto.getText());
        comment.setAuthorId(commentDto.getAuthorId());
        video.addComment(comment);

        videoRepository.save(video);
    }

    public List<CommentDto> getAllComments(String videoId) {
        Video video = getVideoById(videoId);
        List<Comment> commentList = video.getCommentList();

       return commentList.stream().map(this::mapToCommentDto).collect(Collectors.toList());
    }

    private CommentDto mapToCommentDto(Comment comment) {
        CommentDto commentDto = new CommentDto();
        commentDto.setText(comment.getText());
        commentDto.setAuthorId(comment.getAuthorId());
        return commentDto;
    }

    public List<VideoDto> getAllVideos() {
        return videoRepository.findAll()
                .stream()
                .map(VideoService::mapToVideoDto).toList();

    }
}
