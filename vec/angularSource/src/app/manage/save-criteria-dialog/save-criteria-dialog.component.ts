import { Component, Inject } from '@angular/core';
import { MatDialogRef, MAT_DIALOG_DATA } from '@angular/material/dialog';

@Component({
  selector: 'app-save-criteria-dialog',
  templateUrl: './save-criteria-dialog.component.html',
  styleUrls: ['./save-criteria-dialog.component.scss']
})
export class SaveCriteriaDialogComponent {

  constructor(
    public dialogRef: MatDialogRef<SaveCriteriaDialogComponent>,
    @Inject(MAT_DIALOG_DATA) public data: { defaultName: string }
  ) { }

  onNoClick(): void {
    this.dialogRef.close();  // 点击取消时关闭对话框
  }

  // 添加 saveCriteria 方法
  saveCriteria(): void {
    // 保存条件时，关闭对话框并传递数据
    this.dialogRef.close(this.data.defaultName);
  }
}
