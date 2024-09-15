import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ManageRoutingModule } from './manage-routing.module';
import { MenuComponent } from './shared/menu/menu.component';
import { Header2Component } from './shared/header2/header2.component';
import { HomeComponent } from './home/home.component';
import { ManageComponent } from './manage.component';
import { FormsModule } from '@angular/forms';
import { ErrorDialogComponent } from './error-dialog/error-dialog.component';

@NgModule({
  declarations: [
    MenuComponent,
    Header2Component,
    HomeComponent,
    ManageComponent,
    ErrorDialogComponent,
  ],
  imports: [CommonModule, ManageRoutingModule, FormsModule],
})
export class ManageModule {}
