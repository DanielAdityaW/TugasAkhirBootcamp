import { Component } from '@angular/core';
import { Router } from '@angular/router';
import { AuthService } from '../../Services/auth.service';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css'], // jika ada file CSS
})
export class LoginComponent {
  credentials = { username: '', password: '' };
  message: string = '';

  constructor(private authService: AuthService, private router: Router) {}

  onSubmit(): void {
    this.authService.login(this.credentials).subscribe({
      next: () => {
        localStorage.setItem('isLoggedIn', 'true'); // Simpan status login
        this.router.navigate(['/table']); // Arahkan ke halaman navbar
      },
      error: (err) => {
        this.message = 'Invalid username or password.';
      },
    });
  }  
}
