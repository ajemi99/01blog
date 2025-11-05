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
// import { Component, signal } from '@angular/core';
// import { HttpClient, HttpClientModule } from '@angular/common/http';
// import { RouterOutlet } from '@angular/router';

// @Component({
//   selector: 'app-root',
//   standalone: true,
//   imports: [RouterOutlet, HttpClientModule],  // 🔹 هنا سجلنا HttpClientModule
//   templateUrl: './app.html',
//   styleUrls: ['./app.css']
// })
// export class App {
//   protected readonly title = signal('frontend');
//   responseMessage = '';

//   constructor(private http: HttpClient) { }

//   testBackend() {
//     this.http.get('http://localhost:8080/api/test', { responseType: 'text' })
//       .subscribe({
//         next: data => this.responseMessage = data,
//         error: err => this.responseMessage = 'Erreur: ' + err
//       });
//   }
// }
