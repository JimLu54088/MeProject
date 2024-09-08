import { ActivatedRoute } from '@angular/router';
import { Component, OnInit } from '@angular/core';
import { Group } from 'src/app/@models/group.model';
import { GroupApiService } from 'src/app/@services/group-api.service';

@Component({
  selector: 'app-todo-list',
  templateUrl: './todo-list.component.html',
  styleUrls: ['./todo-list.component.scss']
})
export class TodoListComponent implements OnInit {
  dataList: Group[] = [];


  constructor(private route: ActivatedRoute) { }

  ngOnInit(): void {
    this.route.data.subscribe(data => {
      this.dataList = data['dataList'];
    });
  }

}
