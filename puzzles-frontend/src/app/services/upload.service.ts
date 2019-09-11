import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';

@Injectable({
  providedIn: 'root'
})
export class UploadService {
  url = 'http://localhost:8080/upload';

  constructor(private http: HttpClient) { }

  uploadImage(image: File) {

    const formData: FormData = new FormData();
    formData.append('file', image);

    return this.http.post<any>(this.url, formData);
  }
}
