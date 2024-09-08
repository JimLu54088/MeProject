import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { ManageComponent } from './manage.component';

const routes: Routes = [
  {
    path: '',
    component: ManageComponent,
    children: [
      { path: 'home', loadChildren: () => import('./home/home.module').then(m => m.HomeModule) },
      { path: 'todo', loadChildren: () => import('./todo/todo.module').then(m => m.TodoModule) },
      { path: 'account', loadChildren: () => import('./account/account.module').then(m => m.AccountModule) },
      { path: 'search_single', loadChildren: () => import('./search-single/search-single.module').then(m => m.SearchSingleModule) },
      { path: 'search_criteria', loadChildren: () => import('./search-criteria/search-criteria.module').then(m => m.SearchCriteriaModule) },
      { path: 'search_by_excel', loadChildren: () => import('./search-by-excel/search-by-excel.module').then(m => m.SearchByExcelModule) },
      { path: 'search_result', loadChildren: () => import('./search-result/search-result.module').then(m => m.SearchResultModule) },
      { path: '', redirectTo: 'home', pathMatch: 'full' }
    ]
  },

];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class ManageRoutingModule { }
