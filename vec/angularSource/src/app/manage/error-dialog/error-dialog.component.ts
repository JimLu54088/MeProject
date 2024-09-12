import { Component, Inject } from '@angular/core';
import { MAT_DIALOG_DATA, MatDialogRef } from '@angular/material/dialog';

@Component({
    selector: 'app-error-dialog',
    template: `
    <h1 mat-dialog-title>{{ data.poptitle }}</h1>
    <div mat-dialog-content>
      <p>{{ data.message }}</p>
    </div>
    <div mat-dialog-actions>
      <button mat-button (click)="closeDialog()">Close</button>
    </div>
  `
})
export class ErrorDialogComponent {

    constructor(
        public dialogRef: MatDialogRef<ErrorDialogComponent>,  // 注入 MatDialogRef
        @Inject(MAT_DIALOG_DATA) public data: any  // 注入 MAT_DIALOG_DATA
    ) { }
    closeDialog() {
        this.dialogRef.close();  // 調用 close() 方法關閉對話框
    }
}
