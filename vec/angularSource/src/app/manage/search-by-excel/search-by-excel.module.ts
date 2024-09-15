import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms'; // 匯入 FormsModule

import { SearchByExcelRoutingModule } from './search-by-excel-routing.module';
import { SearchByExcelComponent } from './search-by-excel.component';

import { MatDialogModule } from '@angular/material/dialog'; // 如果需要使用 MatDialog
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatButtonModule } from '@angular/material/button';
import { MatSnackBarModule } from '@angular/material/snack-bar';

@NgModule({
  declarations: [SearchByExcelComponent],
  imports: [
    CommonModule,
    SearchByExcelRoutingModule,
    FormsModule,
    MatDialogModule,
    MatFormFieldModule,
    MatInputModule,
    MatButtonModule,
    MatSnackBarModule,
  ],
})
export class SearchByExcelModule {}
