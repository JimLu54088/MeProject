import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { SearchCriteriaComponent } from './search-criteria.component';


const routes: Routes = [{ path: '', component: SearchCriteriaComponent }];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class SearchCriteriaRoutingModule { }
