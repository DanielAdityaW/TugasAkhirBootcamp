import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class InputManualService {
  private apiUrl = 'http://localhost:8081/api/predict-manual';

  constructor(private http: HttpClient) {}

  predictManual(inputData: any): Observable<any> {
    const headers = new HttpHeaders({ 'Content-Type': 'application/json' });
    return this.http.post<any>(this.apiUrl, inputData, { headers });
  }
}
  