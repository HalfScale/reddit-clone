import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { GlobalVariable } from '../globals';
import { PostModel } from './post-model';

@Injectable({
  providedIn: 'root'
})
export class PostService {

  apiUrl = GlobalVariable.BASE_API_URL;

  constructor(private http: HttpClient) { }

  getAllPosts(): Observable<Array<PostModel>> {
    return this.http.get<Array<PostModel>>(this.apiUrl + '/api/posts');
  }
}
