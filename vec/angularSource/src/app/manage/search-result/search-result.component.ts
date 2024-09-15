import { Component, OnInit } from '@angular/core';
import { SearchResultService } from './search-result.service'; // 确保路径正确
import { Router } from '@angular/router';
import { MatSnackBar } from '@angular/material/snack-bar'; // 引入 MatSnackBar

@Component({
  selector: 'app-search-result',
  templateUrl: './search-result.component.html',
  styleUrls: ['./search-result.component.scss'],
})
export class SearchResultComponent implements OnInit {
  savedSearchResultList: any[] = [];

  constructor(
    private searchResultService: SearchResultService,
    private router: Router
  ) {}

  ngOnInit(): void {
    this.loadSavedCriteria();
  }

  loadSavedCriteria(): void {
    this.searchResultService.getSearchResultList().subscribe(
      (responseSearchResultList) => {
        this.savedSearchResultList = responseSearchResultList;

        console.log('responseSearchResultList:', responseSearchResultList);
      },
      (error) => {
        console.error('Error loading saved search result', error);
      }
    );
  }

  // In SearchCriteriaComponent
  applyCriteria(criteria: any): void {
    const queryParams = {
      kur: criteria.kur,
      project_jya_code: criteria.project_jya_code,
      model_code: criteria.model_code,
      color: criteria.color,
      manufacter_date: criteria.manufacter_date,
    };
    console.log('Navigating with queryParams:', queryParams); // Debug
    this.router.navigate(['/manage/home'], { queryParams });
  }

  deleteSavedSearchResultRecord(s_r_id: any): void {
    if (confirm('Are you sure you want to delete this saved search result?')) {
      this.searchResultService.deleteSavedSearchResultRecord(s_r_id).subscribe(
        () => {
          // 删除成功后在前端更新列表
          this.savedSearchResultList = this.savedSearchResultList.filter(
            (responseSearchResultList) =>
              responseSearchResultList.s_r_id !== s_r_id
          );
        },
        (error) => {
          console.error('Error deleting criteria', error);
        }
      );
    }
  }

  downloadFile(dwn_lnk: any) {
    window.location.href = dwn_lnk; // 觸發文件下載

    // this.snackBar.open('No file available for download.', 'Close', {
    //   duration: 3000, // 顯示3秒
    // });
  }
}
