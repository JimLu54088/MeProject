import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable, throwError } from 'rxjs';
import { catchError } from 'rxjs/operators';

@Injectable({
    providedIn: 'root'
})
export class SearchCriteriaService {

    private apiUrl = 'api/getCriteriaList'; // 你的 API URL
    private deleteApiUrl = 'api/deleteSavedCriteria';

    constructor(private http: HttpClient) { }

    private getUserIdFromToken(): string | null {
        const jwt = sessionStorage.getItem('token');
        if (jwt) {
            const payload = JSON.parse(window.atob(jwt.split('.')[1]));
            return payload.sub; // 替换为实际的 userId 属性
        }
        return null;
    }

    getCriteriaList(): Observable<any[]> {
        const userId = this.getUserIdFromToken();
        const url = `${this.apiUrl}?userId=${userId}`; // 假设你需要在 URL 中传递 userId
        return this.http.get<any[]>(url).pipe(
            catchError(error => {
                console.error('Error fetching criteria list', error);
                return throwError(error);
            })
        );
    }

    deleteCriteria(s_c_id: any): Observable<void> {
        const user_id = this.getUserIdFromToken();
        const url = `${this.deleteApiUrl}?user_id=${user_id}&s_c_id=${s_c_id}`; // 假设你需要在 URL 中传递 userId
        return this.http.get<void>(url).pipe(
            catchError(error => {
                console.error('Error deleting criteria', error);
                return throwError(error);
            })
        );
    }
}
