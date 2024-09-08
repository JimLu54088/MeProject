import { TodoService } from './../@services/todo.service';
import { Injectable } from '@angular/core';
import {
  Router, Resolve,
  RouterStateSnapshot,
  ActivatedRouteSnapshot
} from '@angular/router';
import { Observable, of } from 'rxjs';
import { TodoApiService } from '../@services/todo-api.service';
import { Todo } from '../@models/todo.model';
import { Group } from '../@models/group.model';
import { GroupApiService } from '../@services/group-api.service';

@Injectable({
  providedIn: 'root'
})
export class TodoResolver implements Resolve<Todo[]> {

  constructor(private todoApiService: TodoApiService, private todoService: TodoService) { }

  resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot): Observable<Todo[]> {
    const id = route.paramMap.get('id') as string;
    this.todoService.gid = id;
    return this.todoApiService.取得資料(id);
  }
}

@Injectable({
  providedIn: 'root'
})
export class TodoListResolver implements Resolve<Group[]> {

  constructor(private groupApiService: GroupApiService) { }

  resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot): Observable<Group[]> {
    return this.groupApiService.取得資料();
  }
}
