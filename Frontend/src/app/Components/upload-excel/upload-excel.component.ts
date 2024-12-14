import { Component } from '@angular/core';
import { HttpClient, HttpHeaders, HttpEventType, HttpEvent } from '@angular/common/http';
import { Observable } from 'rxjs';

@Component({
  selector: 'app-upload-excel',
  templateUrl: './upload-excel.component.html',
  styleUrls: ['./upload-excel.component.css']
})
export class UploadExcelComponent {
  selectedFile: File | null = null;
  loading = false;
  downloadUrl: string | null = null;
  errorMessage: string = '';
  uploadProgress: number = 0;

  constructor(private http: HttpClient) {}

  onFileSelected(event: any) {
    this.selectedFile = event.target.files[0];
    this.errorMessage = '';  // Clear previous error message
  }

  onUpload() {
    if (!this.selectedFile) {
      this.errorMessage = 'Please select a file first.';
      return;
    }

    this.loading = true;
    this.errorMessage = '';
    this.uploadProgress = 0;

    const formData = new FormData();
    formData.append('file', this.selectedFile, this.selectedFile.name);

    // Here we need to handle progress
    this.uploadFile(formData).subscribe({
      next: (event: HttpEvent<any>) => {
        switch (event.type) {
          case HttpEventType.UploadProgress:
            if (event.total) {
              this.uploadProgress = Math.round((100 * event.loaded) / event.total);
            }
            break;
          case HttpEventType.Response:
            this.loading = false;
            if (event.body.status === 'success') {
              this.downloadUrl = event.body.downloadUrl;
            } else {
              this.errorMessage = event.body.message || 'Error processing file.';
            }
            break;
        }
      },
      error: (error) => {
        this.loading = false;
        this.errorMessage = `An error occurred while uploading the file: ${error.message}`;
      }
    });
  }

  // Method to upload the file with progress monitoring
  uploadFile(formData: FormData): Observable<HttpEvent<any>> {
    const headers = new HttpHeaders();
    return this.http.post<any>('/api/upload-excel', formData, {
      headers: headers,
      observe: 'events',
      reportProgress: true
    });
  }

  downloadFile() {
    if (this.downloadUrl) {
      window.location.href = this.downloadUrl;
    }
  }
}
