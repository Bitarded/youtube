import { Injectable } from '@angular/core';
import {HttpClient} from "@angular/common/http";
import {FileSystemFileEntry} from "ngx-file-drop";
import {Observable} from "rxjs";
import {UploadVideoResponse} from "./UploadVideoResponse";
import {VideoDto} from "./video-dto";

@Injectable({
  providedIn: 'root'
})
export class VideoService {

  constructor(private httpClient:HttpClient) { }
  uploadVideo(fileEntry:File):Observable<UploadVideoResponse>{
    const formData = new FormData()
    formData.append("file",fileEntry, fileEntry.name)

    //HTTP Post Call to upload the video
    return this.httpClient.post<UploadVideoResponse>("http://localhost:8080/video",formData);

  }
  uploadThumbnail(fileEntry:File,videoId:string):Observable<string>{
    const formData = new FormData()
    formData.append("file",fileEntry, fileEntry.name);
    formData.append("videoId",videoId)
    //HTTP Post Call to upload the Thumbnail
    return this.httpClient.post("http://localhost:8080/video/thumbnail",formData,{
      responseType:'text'
    });

  }
  getVideo(videoId:string):Observable<VideoDto>{
   return  this.httpClient.get<VideoDto>("http://localhost:8080/video/" + videoId)
  }

  saveVideo(videoMetaData: VideoDto): Observable<VideoDto>  {
    return this.httpClient.put<VideoDto>("http://localhost:8080/videos",videoMetaData)
  }
}
