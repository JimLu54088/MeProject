import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { HomeRoutingModule } from './home-routing.module';
import { SaveCriteriaDialogComponent } from '../save-criteria-dialog/save-criteria-dialog.component';
import { MatDialogModule } from '@angular/material/dialog';  // 如果需要使用 MatDialog
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { FormsModule } from '@angular/forms';
import { MatButtonModule } from '@angular/material/button';
import { MatSnackBarModule } from '@angular/material/snack-bar';
import { ErrorDialogComponent } from '../error-dialog/error-dialog.component';


@NgModule({
  declarations: [
    SaveCriteriaDialogComponent,
    ErrorDialogComponent
  ],
  imports: [
    CommonModule,
    HomeRoutingModule,
    MatDialogModule,
    MatFormFieldModule,
    MatInputModule,
    FormsModule,
    MatButtonModule,
    MatSnackBarModule
  ],

})
export class HomeModule { }
