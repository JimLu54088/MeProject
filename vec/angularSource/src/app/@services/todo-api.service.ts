import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Todo } from '../@models/todo.model';

@Injectable({
  providedIn: 'root'
})
export class TodoApiService {
  private url = '/api/todo6_8';

  constructor(private http: HttpClient) { }

  取得資料(id: string) {
    return this.http.get<Todo[]>(this.url + '/' + id);
  }

  新增(value: Todo) {
    return this.http.post<Todo>(this.url, value);
  }

  修改(value: Todo) {
    return this.http.put(`${this.url}/${value.TodoId}`, value);
  }

  刪除(value: Todo) {
    return this.http.delete(`${this.url}/${value.TodoId}`);
  }

  全部狀態統一(value: boolean, id: string) {
    return this.http.put(`${this.url}/Status/${id}/${value}`, null);
  }

  刪除已完成事項(id: string) {
    return this.http.delete(`${this.url}/clearCompleted/${id}`);
  }
}
