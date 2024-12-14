import { HttpClient, HttpParams } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class TableService {
  private apiUrl = 'http://localhost:8081/api/customers';

  constructor(private http: HttpClient) {}

  // Fetch customers with search query, pagination, and sorting
  getCustomers(page: number = 0, size: number = 100, sortOrder: string = 'desc', vehicleType: string, searchQuery: string): Observable<any> {
    let params = new HttpParams()
      .set('page', page.toString())
      .set('size', size.toString())
      .set('sortOrder', sortOrder)
      .set('vehicleType', vehicleType);

    // Add the search query if it's not empty
    if (searchQuery) {
      params = params.set('customerId', searchQuery);
    }

    return this.http.get<any>(this.apiUrl, { params });
  }
}
