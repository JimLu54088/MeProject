import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';

import { SearchCriteriaRoutingModule } from './search-criteria-routing.module';
import { SearchCriteriaComponent } from './search-criteria.component';
import { SearchCriteriaService } from './search-criteria.service'; // 确保路径正确



@NgModule({
  declarations: [
    SearchCriteriaComponent
  ],
  imports: [
    CommonModule,
    SearchCriteriaRoutingModule
  ],
  providers: [
    SearchCriteriaService // 添加此行
  ]
})
export class SearchCriteriaModule { }
