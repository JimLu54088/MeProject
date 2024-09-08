import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { SearchByExcelComponent } from './search-by-excel.component';

const routes: Routes = [{ path: '', component: SearchByExcelComponent }];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class SearchByExcelRoutingModule { }
