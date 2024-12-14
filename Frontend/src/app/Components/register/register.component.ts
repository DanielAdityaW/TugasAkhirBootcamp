import { Component } from '@angular/core';
import { AuthService } from '../../Services/auth.service';

@Component({
  selector: 'app-register',
  templateUrl: './register.component.html',
  styleUrls: ['./register.component.css'],
})
export class RegisterComponent {
  user = { username: '', password: '' };
  message: string = '';

  constructor(private authService: AuthService) {}

  onSubmit() {
    this.authService.register(this.user).subscribe({
      next: (response) => {
        // Memastikan bahwa response.message ada
        console.log('Response from server:', response);  // Debugging respons
        if (response && response.message) {
          this.message = response.message;  // Menampilkan pesan sukses dari backend
        } else {
          this.message = 'Registration successful!';
        }
      },
      error: (error) => {
        console.log('Error response:', error);  // Debugging error
        this.message = error.error?.message || 'An error occurred';  // Menampilkan pesan error
      }
    });
  }
}
