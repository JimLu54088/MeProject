import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';

@Component({
  selector: 'app-header2',
  templateUrl: './header2.component.html',
  styleUrls: ['./header2.component.scss']
})
export class Header2Component implements OnInit {

  constructor(private router: Router ) { }

  ngOnInit(): void {
  }

  logOut() {
    localStorage.removeItem('jwt');
    this.router.navigateByUrl('/login');
  }
}
