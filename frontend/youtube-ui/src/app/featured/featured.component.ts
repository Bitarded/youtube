import { Component,OnInit } from '@angular/core';
import {VideoService} from "../services/video.service";
import {VideoDto} from "../upload-video/video-dto";

@Component({
  selector: 'app-featured',
  templateUrl: './featured.component.html',
  styleUrls: ['./featured.component.css']
})
export class FeaturedComponent implements OnInit{
  featuredVideos: Array<VideoDto> = [];
  constructor(private videoService:VideoService) {
  }

  ngOnInit(): void {
    this.videoService.getAllVideos().subscribe(response=> {
      this.featuredVideos = response;
    });
  }

}
