import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class PredictionHistoryService {

  private apiUrl = 'http://localhost:8081/api/history';  // Ganti dengan URL backend Anda

  constructor(private http: HttpClient) { }

  // Method untuk mengambil data riwayat prediksi
  getPredictionHistory(): Observable<any[]> {
    return this.http.get<any[]>(this.apiUrl);
  }
}
