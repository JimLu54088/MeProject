import { ActivatedRoute } from '@angular/router';
import { Component, OnInit } from '@angular/core';
import { TodoService } from 'src/app/@services/todo.service';

@Component({
  selector: 'app-todo-content',
  templateUrl: './todo-content.component.html',
  styleUrls: ['./todo-content.component.scss']
})
export class TodoContentComponent implements OnInit {
  title = 'OneTodo';
  constructor(private todoService: TodoService, private route: ActivatedRoute) { }

  ngOnInit() {
    this.todoService.todoDataList = [];

    this.route.data.subscribe(data => {
      this.todoService.todoDataList = data['todoList'];
      this.todoService.ready();
    });
  }


}
