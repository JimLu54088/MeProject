import { Component, OnInit } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Router } from '@angular/router';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.scss']
})
export class LoginComponent implements OnInit {

  username: string = '';
  password: string = '';

  constructor(private http: HttpClient, private router: Router) { }

  ngOnInit(): void {
  }


  todoDataList: String[] = [];

  token: any;

  login() {
    const loginData = {
      username: this.username,
      password: this.password
    };

    //debug
    // this.router.navigateByUrl('/manage/home');

    this.http.post('/api/Login', loginData).subscribe({
      next: (response: any) => {
        // 成功时处理，比如跳转页面
        console.log('登录成功', response);
        // 确保 response 中有 token 属性
        if (response && response.token) {
          sessionStorage.setItem('token', response.token);
          this.router.navigateByUrl('/manage/home');
        } else {
          console.error('登录响应中没有 token');
          alert('登录失败，请稍后再试');
        }
      },
      error: (error) => {
        // 处理错误，比如显示错误信息
        console.error('登录失败', error);
        alert('登录失败，请检查账号或密码');
      }
    });
  }



}
