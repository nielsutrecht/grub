import { NgModule }             from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserLoginComponent } from './component/user/login';
import { DiaryDayComponent } from './component/diary/day';

const routes: Routes = [
  { path: '', redirectTo: '/login', pathMatch: 'full' },
  { path: 'login',  component: UserLoginComponent },
  { path: 'diary/:day?', component: DiaryDayComponent },
];

@NgModule({
  imports: [ RouterModule.forRoot(routes) ],
  exports: [ RouterModule ]
})
export class AppRoutingModule {}
