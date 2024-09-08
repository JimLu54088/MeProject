import { Component, OnInit } from '@angular/core';
import { SearchCriteriaService } from './search-criteria.service'; // 确保路径正确
import { Router } from '@angular/router';

@Component({
  selector: 'app-search-criteria',
  templateUrl: './search-criteria.component.html',
  styleUrls: ['./search-criteria.component.scss']
})
export class SearchCriteriaComponent implements OnInit {
  savedCriteriaList: any[] = [];

  constructor(private criteriaService: SearchCriteriaService, private router: Router) { }

  ngOnInit(): void {
    this.loadSavedCriteria();
  }

  loadSavedCriteria(): void {
    this.criteriaService.getCriteriaList().subscribe(
      (criteriaList) => {
        this.savedCriteriaList = criteriaList;

        console.log('criteriaList:', criteriaList);
        // this.savedCriteriaList = [
        //   {
        //     name: 'Test Criteria 1',
        //     kur: 'CC',
        //     project_jya_code: 'PC',
        //     model_code: 'MC',
        //     color: 'Red',
        //     manufacter_date: '1942'
        //   },
        //   {
        //     name: 'Test Criteria 2',
        //     kur: 'DD',
        //     project_jya_code: 'PD',
        //     model_code: 'MD',
        //     color: 'Blue',
        //     manufacter_date: '1950'
        //   }
        // ];








      },
      (error) => {
        console.error('Error loading saved criteria', error);
      }
    );
  }

  // In SearchCriteriaComponent
  applyCriteria(criteria: any): void {
    const queryParams = {
      kur: criteria.kur,
      project_jya_code: criteria.project_jya_code,
      model_code: criteria.model_code,
      color: criteria.color,
      manufacter_date: criteria.manufacter_date
    };
    console.log('Navigating with queryParams:', queryParams); // Debug
    this.router.navigate(['/manage/home'], { queryParams });
  }

  deleteCriteria(s_c_id: any): void {
    if (confirm('Are you sure you want to delete this search criteria?')) {
      this.criteriaService.deleteCriteria(s_c_id).subscribe(
        () => {
          // 删除成功后在前端更新列表
          this.savedCriteriaList = this.savedCriteriaList.filter(criteria => criteria.s_c_id !== s_c_id);
        },
        (error) => {
          console.error('Error deleting criteria', error);
        }
      );
    }
  }

}
