import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { SearchSingleComponent } from './search-single.component';

const routes: Routes = [{ path: '', component: SearchSingleComponent }];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class SearchSingleRoutingModule { }
