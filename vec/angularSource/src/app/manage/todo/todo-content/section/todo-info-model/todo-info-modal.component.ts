
import { Component, Input, OnInit } from '@angular/core';
import { Todo } from 'src/app/@models/todo.model';

@Component({
  selector: 'app-todo-info-modal',
  templateUrl: './todo-info-modal.component.html',
  styleUrls: ['./todo-info-modal.component.scss']
})
export class TodoInfoModalComponent implements OnInit {
  todoInfoModal: any;
  @Input() todo!:Todo

  constructor() { }

  ngOnInit(): void {
  }


  show() {
    console.log('a')
    if (!this.todoInfoModal) {
      this.todoInfoModal = new bootstrap.Modal(document.getElementById('todoInfoModal'), {
        keyboard: false
      });
    }
    this.todoInfoModal.show();
  }
}
