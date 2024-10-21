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
  userId: any;

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

  isSearchEnabled: boolean = false;

  onCheckFile() {
    if (this.file) {
      this.isFileChecked = true;

      const uploadedFileName = this.file.name.toLowerCase(); // 取得檔案名稱並轉換為小寫
      const isExcelFile =
        uploadedFileName.endsWith('.xlsx') || uploadedFileName.endsWith('.xls');

      this.userId = this.getUserIdFromToken();
      const formData = new FormData();
      formData.append('file', this.file); // this.file 是你要上傳的檔案
      formData.append('userId', this.userId); // 添加 userId 到 FormData
      formData.append('searchTitle', this.searchTitle); // 添加 searchTitle 到 FormData

      if (isExcelFile) {
        // 发送 POST 请求到后端
        this.http.post('/api/uploadAndCheckExcel', formData).subscribe(
          (response: any) => {
            console.log('Search completed:', response);
            // 如果後端返回 warningMessage
            if (response.warningMessage) {
              this.dialog.open(ErrorDialogComponent, {
                data: {
                  message: response.warningMessage, // 使用後端返回的 warningMessage
                  poptitle: 'WARN',
                },
                panelClass: 'custom-dialog-container',
              });
            } else {
              //Success Response
              this.dialog.open(ErrorDialogComponent, {
                data: {
                  message:
                    'Excel check was successful. Please click the search button.', // 使用後端返回的 warningMessage
                  poptitle: 'SUCG',
                },
                panelClass: 'custom-dialog-container',
              });
              this.isSearchEnabled = true;
            }
          },
          (error) => {
            console.error('Error Search vecData:', error);
            if (error.error.errorCode) {
              console.log('errror code : ' + error.error.errorCode);
              console.log('error message : ' + error.error.errorMessage);
              this.dialog.open(ErrorDialogComponent, {
                data: {
                  message: error.error.errorMessage, // 使用後端返回的 errorMessage
                  poptitle: 'ERROR',
                },
                panelClass: 'custom-dialog-container',
              });
            } else {
              //Unexpeted error
              this.dialog.open(ErrorDialogComponent, {
                data: {
                  message: 'Unexpected error.',
                  poptitle: 'ERROR',
                },
                panelClass: 'custom-dialog-container',
              });
            }

            this.isSearchEnabled = false;
          }
        );
      } else {
        this.dialog.open(ErrorDialogComponent, {
          data: {
            message: 'It is not an Excel file.', // 使用後端返回的 warningMessage
            poptitle: 'ERROR',
          },
          panelClass: 'custom-dialog-container',
        });
        this.isSearchEnabled = false;
      }
    } else {
      this.dialog.open(ErrorDialogComponent, {
        data: {
          message: 'No file specified.',
          poptitle: 'ERROR',
        },
        panelClass: 'custom-dialog-container',
      });
      this.isSearchEnabled = false;
    }
  }

  isLoading = false;

  onSearch() {
    if (this.isSearchEnabled) {
      // 開始顯示進度條
      this.isLoading = true;
      if (this.isFileChecked) {
        if (this.file) {
          const userId = this.getUserIdFromToken();

          const payload = {
            searchTitle: this.searchTitle,
            userId,
          };

          // 发送 POST 请求到后端
          this.http.post('/api/searchByExcel', payload).subscribe(
            (response: any) => {
              console.log('Search completed:', response);
              // 如果後端返回 warningMessage
              if (response.warningMessage) {
                this.dialog.open(ErrorDialogComponent, {
                  data: {
                    message: response.warningMessage, // 使用後端返回的 warningMessage
                    poptitle: 'WARN',
                  },
                  panelClass: 'custom-dialog-container',
                });

                this.isLoading = false;
              } else {
                //Success Response
                this.dialog.open(ErrorDialogComponent, {
                  data: {
                    message:
                      'This search is complete. You can download the file from the result page.', // 使用後端返回的 warningMessage
                    poptitle: 'SUCG',
                  },
                  panelClass: 'custom-dialog-container',
                });
                this.isSearchEnabled = true;
                // 請求完成後，隱藏進度條
                this.isLoading = false;
              }
            },
            (error) => {
              console.error('Error Search By excel:', error);
              if (error.error.errorCode) {
                console.log('errror code : ' + error.error.errorCode);
                console.log('error message : ' + error.error.errorMessage);
                this.dialog.open(ErrorDialogComponent, {
                  data: {
                    message: error.error.errorMessage, // 使用後端返回的 errorMessage
                    poptitle: 'ERROR',
                  },
                  panelClass: 'custom-dialog-container',
                });
              } else {
                //Unexpeted error
                this.dialog.open(ErrorDialogComponent, {
                  data: {
                    message: 'Unexpected error.',
                    poptitle: 'ERROR',
                  },
                  panelClass: 'custom-dialog-container',
                });
              }
              // 出現錯誤後也隱藏進度條
              this.isLoading = false;
              this.isSearchEnabled = false;
            }
          );
        } else {
          this.dialog.open(ErrorDialogComponent, {
            data: {
              message: 'No file specified.',
              poptitle: 'ERROR',
            },
            panelClass: 'custom-dialog-container',
          });
          this.isSearchEnabled = false;
        }
      }
    }
  }
}
