import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable, throwError } from 'rxjs';
import { catchError } from 'rxjs/operators';
import { MatSnackBar } from '@angular/material/snack-bar'; // 引入 MatSnackBar

@Injectable({
  providedIn: 'root',
})
export class SearchResultService {
  private apiUrl = 'api/getSearchResultList'; // 你的 API URL
  private deleteApiUrl = 'api/deleteSavedsearchResult';

  constructor(private http: HttpClient) {}

  private getUserIdFromToken(): string | null {
    const jwt = sessionStorage.getItem('token');
    if (jwt) {
      const payload = JSON.parse(window.atob(jwt.split('.')[1]));
      return payload.sub; // 替换为实际的 userId 属性
    }
    return null;
  }

  getSearchResultList(): Observable<any[]> {
    const userId = this.getUserIdFromToken();
    const url = `${this.apiUrl}?userId=${userId}`; // 假设你需要在 URL 中传递 userId
    return this.http.get<any[]>(url).pipe(
      catchError((error) => {
        console.error('Error fetching criteria list', error);
        return throwError(error);
      })
    );
  }

  // deleteSavedSearchResultRecord(s_r_id: any, ins_dt: any): Observable<void> {
  //   const user_id = this.getUserIdFromToken();
  //   const url = `${this.deleteApiUrl}?user_id=${user_id}&s_r_id=${s_r_id}&ins_dt=${ins_dt}`; // 假设你需要在 URL 中传递 userId
  //   console.log('deleteUrl : ' + url);
  //   return this.http.get<void>(url).pipe(
  //     catchError((error) => {
  //       console.error('Error deleting saved search result record', error);
  //       return throwError(error);
  //     })
  //   );
  // }

  deleteSavedSearchResultRecord(s_r_id: any, ins_dt: any): Observable<void> {
    const user_id = this.getUserIdFromToken();
    const url = this.deleteApiUrl; // 使用 POST，不需要將參數附加到 URL
    const body = { user_id, s_r_id, ins_dt }; // 將參數放到 body 中

    console.log('delete body :', body);
    return this.http.post<void>(url, body).pipe(
      catchError((error) => {
        console.error('Error deleting saved search result record', error);
        return throwError(error);
      })
    );
  }
}
