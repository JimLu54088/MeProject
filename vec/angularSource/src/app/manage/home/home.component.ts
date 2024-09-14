import { Component, OnInit } from '@angular/core';
import { MatDialog } from '@angular/material/dialog';
import { SaveCriteriaDialogComponent } from '../save-criteria-dialog/save-criteria-dialog.component';
import { HttpClient } from '@angular/common/http'; // 导入 HttpClient
import { MatSnackBar } from '@angular/material/snack-bar'; // 引入 MatSnackBar
import { ActivatedRoute } from '@angular/router';
import { ErrorDialogComponent } from '../error-dialog/error-dialog.component';

@Component({
  selector: 'app-home',
  templateUrl: './home.component.html',
  styleUrls: ['./home.component.scss'],
})
export class HomeComponent implements OnInit {
  currentYear: number = new Date().getFullYear();
  years: number[] = [];

  searchData = {
    kur: '',
    project_jya_code: '',
    model_code: '',
    color: '',
    manufacter_date: '',
  };

  constructor(
    private dialog: MatDialog,
    private http: HttpClient,
    private snackBar: MatSnackBar,
    private route: ActivatedRoute
  ) {
    // 注入 HttpClient
    this.generateYears();
  }

  generateYears() {
    for (let year = 1932; year <= this.currentYear; year++) {
      this.years.push(year);
    }
  }

  private getUserIdFromToken(): string | null {
    const jwt = sessionStorage.getItem('token');
    if (jwt) {
      const payload = JSON.parse(window.atob(jwt.split('.')[1]));
      return payload.sub; // 替换为实际的 userId 属性
    }
    return null;
  }

  ngOnInit(): void {
    this.route.queryParams.subscribe((params) => {
      console.log('Query params received:', params); // Debug
      this.searchData = {
        kur: params['kur'] || '',
        project_jya_code: params['project_jya_code'] || '',
        model_code: params['model_code'] || '',
        color: params['color'] || '',
        manufacter_date: params['manufacter_date'] || '',
      };
      console.log('Search data loaded:', this.searchData); // Debug
    });
  }

  onSubmit() {
    // Send searchData to the backend
    console.log(this.searchData);
  }

  openSaveCriteriaDialog(): void {
    const currentDate = new Date();
    const currentDateUTCPlus9 = new Date(
      currentDate.getTime() + 9 * 60 * 60 * 1000
    );
    const defaultName = currentDateUTCPlus9
      .toISOString()
      .split('.')[0]
      .replace('T', ' ');

    const dialogRef = this.dialog.open(SaveCriteriaDialogComponent, {
      width: '250px',
      data: {
        defaultName,
        searchData: this.searchData, // 传递 searchData
      },
    });

    dialogRef.afterClosed().subscribe((result) => {
      if (result) {
        this.saveSearchCriteria(result); // 调用保存方法
      }
    });
  }

  saveSearchCriteria(criteriaName: string) {
    const userId = this.getUserIdFromToken();
    const payload = {
      ...this.searchData, // 展开 searchData
      criteriaName, // 添加 criteriaName 到同一对象
      userId,
    };

    // 发送 POST 请求到后端
    this.http.post('/api/saveSearchCriteria', payload).subscribe(
      (response) => {
        console.log('Criteria saved successfully:', response);
        // 成功后显示 SnackBar
        this.snackBar.open('Saved Criteria Successfully!!', 'Close', {
          duration: 3000, // 显示3秒
        });
      },
      (error) => {
        console.error('Error saving criteria:', error);

        if (error.error && error.error.errorCode) {
          this.snackBar.open(error.error.errorMessage, 'Close', {
            duration: 5000, // 显示3秒
          });
        } else {
          this.snackBar.open('Unexpected Error Occurred.', 'Close', {
            duration: 3000, // 显示3秒
          });
        }
      }
    );
  }

  isLoading = false;
  downloadUrl: string | null = null; // 存儲下載鏈接
  isDownloadEnabled = false;

  callSearchVecAPI() {
    // 開始顯示進度條
    this.isLoading = true;

    const userId = this.getUserIdFromToken();
    const payload = {
      ...this.searchData, // 展开 searchData
      userId,
    };

    // 发送 POST 请求到后端
    this.http.post('/api/searchSingleVec', payload).subscribe(
      (response: any) => {
        console.log('Search completed:', response);
        // 成功后显示 SnackBar
        // this.snackBar.open('Search Successfully!!', 'Close', {
        //   duration: 3000,  // 显示3秒
        // });

        if (response.downloadUrl) {
          this.downloadUrl = response.downloadUrl; // 保存下載鏈接
        }

        // 如果後端返回 warningMessage
        if (response.warningMessage) {
          this.dialog.open(ErrorDialogComponent, {
            data: {
              message: response.warningMessage, // 使用後端返回的 warningMessage
              poptitle: 'WARN',
            },
            panelClass: 'custom-dialog-container',
          });
          this.isDownloadEnabled = false;
        } else {
          //Success Response
          this.dialog.open(ErrorDialogComponent, {
            data: {
              message:
                'The search is complete. You can download the file from the Download button.', // 使用後端返回的 warningMessage
              poptitle: 'SUCCESS',
            },
            panelClass: 'custom-dialog-container',
          });

          this.isDownloadEnabled = true;
        }

        // 請求完成後，隱藏進度條
        this.isLoading = false;
      },
      (error) => {
        console.error('Error Search vecData:', error);

        if (error.error && error.error.warningCode) {
          if (error.error && error.error.warningCode === 'WSW001') {
            // 彈跳視窗顯示 errorMessage
            this.dialog.open(ErrorDialogComponent, {
              data: {
                message: error.error.warningMessage,
                poptitle: 'WARN',
              },
            });
          }
        } else {
          if (error.error && error.error.errorCode) {
            this.snackBar.open(error.error.errorMessage, 'Close', {
              duration: 5000, // 显示3秒
            });
          } else {
            this.snackBar.open('Unexpected Error Occurred.', 'Close', {
              duration: 3000, // 显示3秒
            });
          }
        }

        // 出現錯誤後也隱藏進度條
        this.isLoading = false;
        this.isDownloadEnabled = false;
      }
    );
  }

  downloadFile() {
    if (this.isDownloadEnabled) {
      if (this.downloadUrl) {
        window.location.href = this.downloadUrl; // 觸發文件下載
      } else {
        this.snackBar.open('No file available for download.', 'Close', {
          duration: 3000, // 顯示3秒
        });
      }
    }
  }
}
