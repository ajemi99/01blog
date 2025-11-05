import { Component, signal } from '@angular/core';
import { RouterOutlet } from '@angular/router';

@Component({
  selector: 'app-root',
  standalone: true,
  imports: [RouterOutlet],
  templateUrl: './app.html',
  styleUrls: ['./app.css'] // tu avais écrit styleUrl → c'est styleUrls
})
export class App {
  protected readonly title = signal('frontend');
}

