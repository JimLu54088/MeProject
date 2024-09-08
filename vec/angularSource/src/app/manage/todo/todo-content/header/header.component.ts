import { Component, Input, OnInit } from '@angular/core';
import { TodoService } from 'src/app/@services/todo.service';

@Component({
  selector: 'app-header',
  templateUrl: './header.component.html',
  styleUrls: ['./header.component.scss']
})
export class HeaderComponent implements OnInit {
  @Input()
  title!: string;
  todoInputModel = '';

  constructor(private todoService: TodoService) { }

  ngOnInit(): void {
  }
  
  add() {
    this.todoService.add(this.todoInputModel);
    this.todoInputModel = '';
  }

}
