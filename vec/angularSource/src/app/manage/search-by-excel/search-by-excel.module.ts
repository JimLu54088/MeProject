import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';

import { SearchByExcelRoutingModule } from './search-by-excel-routing.module';
import { SearchByExcelComponent } from './search-by-excel.component';


@NgModule({
  declarations: [
    SearchByExcelComponent
  ],
  imports: [
    CommonModule,
    SearchByExcelRoutingModule
  ]
})
export class SearchByExcelModule { }
