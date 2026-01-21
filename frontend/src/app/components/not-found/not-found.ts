import { Component, ElementRef, ViewChild } from '@angular/core';
import { RouterLink } from '@angular/router';

@Component({
  selector: 'app-not-found',
  imports: [RouterLink],
  templateUrl: './not-found.html',
  styleUrl: './not-found.css',
})
export class NotFound {
  @ViewChild('bgVideo') videoRef!: ElementRef<HTMLVideoElement>;

  ngAfterViewInit() {
    const video = this.videoRef.nativeElement;

    // Force play mn b3d ma t-chargat l-page
    video.muted = true; // Darori bach l-browser may-blokich
    video.play().catch(error => {
      console.log("Autoplay blocked, retrying...", error);
      // Ila t-blokat, n-jrbouha b-tariqa khra
      video.load();
      video.play();
    });
  }
}
