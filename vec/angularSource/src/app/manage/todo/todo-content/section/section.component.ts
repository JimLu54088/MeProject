import { Component, OnInit, ViewChild } from '@angular/core';
import { Todo } from 'src/app/@models/todo.model';
import { TodoService } from 'src/app/@services/todo.service';
import { TodoInfoModalComponent } from './todo-info-model/todo-info-modal.component';

@Component({
  selector: 'app-section',
  templateUrl: './section.component.html',
  styleUrls: ['./section.component.scss']
})
export class SectionComponent implements OnInit {
  nowSelectTodo!: Todo;  
  @ViewChild(TodoInfoModalComponent)
  private todoInfoModalComponent!: TodoInfoModalComponent;


  get toggleAllBtn() {
    return this.todoService.toggleAllBtn;
  }
  get nowTodoList() {
    return this.todoService.nowTodoList;
  }

  constructor(private todoService: TodoService) { }

  ngOnInit(): void {
  }

  toggleAll() {
    this.todoService.toggleAll();
  }
  update(item: Todo) {
    this.todoService.update(item);
  }

  clickCheck(item: Todo) {
    this.todoService.clickCheck(item);
  }

  delete(item: Todo) {
    this.todoService.delete(item);
  }
  edit(item: Todo) {
    if (item.CanEdit) {
      item.Editing = true;
    }
  }

  modalShow(item: Todo) {
    this.nowSelectTodo = item;
    this.todoInfoModalComponent.show();
  }
}
