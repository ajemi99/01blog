import { Component, signal } from '@angular/core';
import { RouterOutlet } from '@angular/router';

@Component({
  selector: 'app-root',
  imports: [RouterOutlet],
  templateUrl: './app.html',
  styleUrls: ['./app.css'] // tu avais écrit styleUrl → c'est styleUrls
})
export class App {
  protected readonly title = signal('frontend');
  protected message = signal(''); // signal pour mettre à jour le message

  testBackend() {
    fetch('http://localhost:8080/api/test') // remplace par ton endpoint
      .then(response => response.text())
      .then(data => this.message.set(data)) // on met la réponse dans le signal
      .catch(error => this.message.set('Erreur : ' + error));
  }
}
