import { Component, OnInit } from '@angular/core';
import { MatDialog } from '@angular/material/dialog';
import { HttpClient } from '@angular/common/http'; // 导入 HttpClient
import { MatSnackBar } from '@angular/material/snack-bar'; // 引入 MatSnackBar
import { ActivatedRoute } from '@angular/router';
import { ErrorDialogComponent } from '../error-dialog/error-dialog.component';

@Component({
  selector: 'app-search-by-excel',
  templateUrl: './search-by-excel.component.html',
  styleUrls: ['./search-by-excel.component.scss'],
})
export class SearchByExcelComponent implements OnInit {
  constructor(
    private dialog: MatDialog,
    private http: HttpClient,
    private snackBar: MatSnackBar
  ) {}

  ngOnInit(): void {}

  searchTitle: string = '';
  file: File | null = null;
  isFileChecked: boolean = false;
  isResultAvailable: boolean = false;

  private getUserIdFromToken(): string | null {
    const jwt = sessionStorage.getItem('token');
    if (jwt) {
      const payload = JSON.parse(window.atob(jwt.split('.')[1]));
      return payload.sub; // 替换为实际的 userId 属性
    }
    return null;
  }

  onFileSelected(event: any) {
    const selectedFile = event.target.files[0];

    this.file = selectedFile;
  }

  onCheckFile() {
    if (this.file) {
      this.isFileChecked = true;

      const uploadedFileName = this.file.name.toLowerCase(); // 取得檔案名稱並轉換為小寫
      const isExcelFile =
        uploadedFileName.endsWith('.xlsx') || uploadedFileName.endsWith('.xls');

      const userId = this.getUserIdFromToken();
      const payload = {
        ...this.file,
        userId,
      };

      if (isExcelFile) {
        // 发送 POST 请求到后端
        // this.http.post('/api/searchSingleVec', payload).subscribe(
        //   (response: any) => {
        //     console.log('Search completed:', response);
        //     // 成功后显示 SnackBar
        //     // this.snackBar.open('Search Successfully!!', 'Close', {
        //     //   duration: 3000,  // 显示3秒
        //     // });
        //     if (response.downloadUrl) {
        //     }
        //     // 如果後端返回 warningMessage
        //     if (response.warningMessage) {
        //       this.dialog.open(ErrorDialogComponent, {
        //         data: {
        //           message: response.warningMessage, // 使用後端返回的 warningMessage
        //           poptitle: 'WARN',
        //         },
        //         panelClass: 'custom-dialog-container',
        //       });
        //     } else {
        //       //Success Response
        //       this.dialog.open(ErrorDialogComponent, {
        //         data: {
        //           message:
        //             'The search is complete. You can download the file from the Download button.', // 使用後端返回的 warningMessage
        //           poptitle: 'SUCCESS',
        //         },
        //         panelClass: 'custom-dialog-container',
        //       });
        //     }
        //   },
        //   (error) => {
        //     console.error('Error Search vecData:', error);
        //     if (error.error.errorCode) {
        //       console.log('errror code : ' + error.error.errorCode);
        //       console.log('error message : ' + error.error.errorMessage);
        //       this.dialog.open(ErrorDialogComponent, {
        //         data: {
        //           message: error.error.errorMessage, // 使用後端返回的 errorMessage
        //           poptitle: 'ERROR',
        //         },
        //         panelClass: 'custom-dialog-container',
        //       });
        //     } else {
        //       //Unexpeted error
        //       this.dialog.open(ErrorDialogComponent, {
        //         data: {
        //           message: 'Unexpected error.',
        //           poptitle: 'ERROR',
        //         },
        //         panelClass: 'custom-dialog-container',
        //       });
        //     }
        //   }
        // );

        this.dialog.open(ErrorDialogComponent, {
          data: {
            message:
              'Excel check was successful. Please click the search button.', // 使用後端返回的 warningMessage
            poptitle: 'SUCG',
          },
          panelClass: 'custom-dialog-container',
        });
      } else {
        this.dialog.open(ErrorDialogComponent, {
          data: {
            message: 'Uploaded File is not excel file.', // 使用後端返回的 warningMessage
            poptitle: 'WARN',
          },
          panelClass: 'custom-dialog-container',
        });
      }
    } else {
      this.dialog.open(ErrorDialogComponent, {
        data: {
          message: 'No file selected, please upload a file.', // 使用後端返回的 warningMessage
          poptitle: 'WARN',
        },
        panelClass: 'custom-dialog-container',
      });
    }
  }

  onSearch() {
    if (this.isFileChecked) {
      // Perform search logic here, and when done:
      this.isResultAvailable = true;
    }
  }

  onDownload() {
    if (this.isResultAvailable) {
      // Logic to download the result set
    }
  }
}
