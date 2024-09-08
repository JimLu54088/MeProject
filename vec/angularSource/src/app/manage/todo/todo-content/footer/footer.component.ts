import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Todo, TodoStatusType } from 'src/app/@models/todo.model';
import { TodoService } from 'src/app/@services/todo.service';

@Component({
  selector: 'app-footer',
  templateUrl: './footer.component.html',
  styleUrls: ['./footer.component.scss']
})
export class FooterComponent implements OnInit {
  TodoStatusType = TodoStatusType;

  get todoActive(): Todo[] {
    return this.todoService.todoActive;
  }

  get todoCompleted(): Todo[] {
    return this.todoService.todoCompleted;
  }

  get nowTodoStatusType() {
    return this.todoService.nowTodoStatusType;
  }

  constructor(private todoService: TodoService, private route: ActivatedRoute) { }

  ngOnInit(): void {
    this.route.paramMap.subscribe(data => {
      const action = data.get('action');
      if (action === 'All') {
        this.todoService.setTodoStatusType(TodoStatusType.All);
      } else if (action === 'Active') {
        this.todoService.setTodoStatusType(TodoStatusType.Active);
      } else if (action === 'Completed') {
        this.todoService.setTodoStatusType(TodoStatusType.Completed);
      }
    })
  }

  clearCompleted() {
    this.todoService.clearCompleted();
  }

  setTodoStatusType(type: number) {
    this.todoService.setTodoStatusType(type);
  }
}
