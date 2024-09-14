import { Component, Inject } from '@angular/core';
import { MAT_DIALOG_DATA, MatDialogRef } from '@angular/material/dialog';

@Component({
  selector: 'app-error-dialog',
  templateUrl: './error-dialog.component.html',
  styleUrls: ['./error-dialog.component.scss'], // Add your styles here
})
export class ErrorDialogComponent {
  constructor(
    public dialogRef: MatDialogRef<ErrorDialogComponent>, // 注入 MatDialogRef
    @Inject(MAT_DIALOG_DATA) public data: any // 注入 MAT_DIALOG_DATA
  ) {}
  closeDialog() {
    this.dialogRef.close(); // 調用 close() 方法關閉對話框
  }
}
