import { Component } from '@angular/core';
import { AuthService } from '../../Services/auth.service';
import { Router } from '@angular/router';

@Component({
  selector: 'app-navbar',
  templateUrl: './navbar.component.html',
  styleUrls: ['./navbar.component.css'],
})
export class NavbarComponent {
  constructor(
    private authService: AuthService,
    private router: Router
  ) {}

  logout() {
    // Show confirmation dialog
    const confirmed = window.confirm('Are you sure you want to logout?');
    
    if (confirmed) {
      // If confirmed, log out and redirect to login
      this.authService.logout().subscribe({
        next: (response) => {
          // Optional: Clear session data (like localStorage) here if needed
          localStorage.removeItem('isLoggedIn');
          localStorage.removeItem('authToken');

          // Force Angular router to navigate to the login page
          this.router.navigate(['/login']).then(() => {
            window.location.reload();  // Force a page reload to clear any application state
          });
        },
        error: (error) => {
          console.error('Logout error:', error);
        }
      });
    }
    // If not confirmed, do nothing (stay on the page)
  }
}
