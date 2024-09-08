import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';

import { SearchSingleRoutingModule } from './search-single-routing.module';
import { SearchSingleComponent } from './search-single.component';


@NgModule({
  declarations: [
    SearchSingleComponent
  ],
  imports: [
    CommonModule,
    SearchSingleRoutingModule
  ]
})
export class SearchSingleModule { }
