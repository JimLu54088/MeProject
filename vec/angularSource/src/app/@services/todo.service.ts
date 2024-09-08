import { Injectable } from '@angular/core';
import { Todo, TodoClass, TodoStatusType } from '../@models/todo.model';
import { TodoApiService } from './todo-api.service';

@Injectable({
  providedIn: 'root'
})
export class TodoService {
  toggleAllBtn = false;
  nowTodoStatusType = TodoStatusType.All;
  todoDataList: Todo[] = [];
  gid = '';

  get nowTodoList() {
    let list: Todo[] = [];

    switch (this.nowTodoStatusType) {
      case TodoStatusType.Active:
        list = this.todoActive;
        break;
      case TodoStatusType.Completed:
        list = this.todoCompleted;
        break;
      default:
        list = this.todoDataList;
        break;
    }

    return list;
  }

  get todoActive(): Todo[] {
    return this.todoDataList.filter(data => !data.Status);
  }

  get todoCompleted(): Todo[] {
    return this.todoDataList.filter(data => data.Status);
  }

  constructor(private todoApiService: TodoApiService) { }

  getData() {
    this.todoApiService.取得資料(this.gid).subscribe(data => {
      this.todoDataList = data;
      this.ready();
    });
  }

  ready() {
    this.todoDataList.forEach(data2 => {
      data2.CanEdit = true;
      data2.Editing = false;
    });
    this.checkToggleAllBtn();
  }

  add(value: string) {
    const seqno = new Date().getTime();
    const todo: Todo = new TodoClass(value, false, seqno);
    todo.GroupId = this.gid;
    this.todoDataList.push(todo);
    this.todoApiService.新增(todo).subscribe(data => {
      this.todoDataList.forEach(data2 => {
        if (data2.Seqno === seqno) {
          data2.TodoId = data.TodoId;
          data2.CanEdit = true;
        }
      })
    });
  }

  update(item: Todo) {
    this.todoApiService.修改(item).subscribe();
    item.Editing = false;
  }

  clickCheck(item: Todo) {
    item.Status = !item.Status;

    this.todoApiService.修改(item).subscribe();
    this.checkToggleAllBtn();
  }

  delete(item: Todo) {
    this.todoApiService.刪除(item).subscribe();
    this.todoDataList = this.todoDataList.filter(data => data !== item);
  }

  toggleAll() {
    this.toggleAllBtn = !this.toggleAllBtn;
    this.todoDataList.forEach(data => {
      data.Status = this.toggleAllBtn;
    });

    this.todoApiService.全部狀態統一(this.toggleAllBtn, this.gid).subscribe();
  }

  clearCompleted() {
    this.todoApiService.刪除已完成事項(this.gid).subscribe();
    this.todoDataList = this.todoActive;
  }

  checkToggleAllBtn() {
    if (this.todoCompleted.length === this.todoDataList.length) {
      this.toggleAllBtn = true;
    } else {
      this.toggleAllBtn = false;
    }
  }

  setTodoStatusType(type: number) {
    this.nowTodoStatusType = type;
  }

}
