// auth.service.ts
import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Observable } from 'rxjs';
import { tap } from 'rxjs/operators';

@Injectable({
  providedIn: 'root'
})
export class AuthService {
  private baseUrl = 'http://localhost:8081/api/auth';

  constructor(private http: HttpClient) {}

  register(user: { username: string, password: string }): Observable<any> {
    return this.http.post<any>(`${this.baseUrl}/register`, user); 
  }

  login(credentials: { username: string; password: string }): Observable<any> {
    return this.http.post(`${this.baseUrl}/login`, credentials, { responseType: 'text' })
      .pipe(
        tap(response => {
          if (response) {
            localStorage.setItem('isLoggedIn', 'true'); // Store login status
            localStorage.setItem('authToken', response); // Store token (if needed)
          }
        })
      );
  }

  logout(): Observable<any> {
    localStorage.removeItem('isLoggedIn'); // Remove login status
    localStorage.removeItem('authToken'); // Remove token
    return this.http.post(`${this.baseUrl}/logout`, {}, { responseType: 'text' });
  }

  isLoggedIn(): boolean {
    return localStorage.getItem('isLoggedIn') === 'true'; // Check if the user is logged in
  }

  getAuthToken(): string | null {
    return localStorage.getItem('authToken'); // Get token from storage
  }
}
