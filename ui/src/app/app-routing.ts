import { NgModule }             from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserLoginComponent } from './component/user/login';
import { UserLoginComponent } from './component/user/login';

const routes: Routes = [
  { path: '', redirectTo: '/login', pathMatch: 'full' },
  { path: 'login',  component: UserLoginComponent }
];

@NgModule({
  imports: [ RouterModule.forRoot(routes) ],
  exports: [ RouterModule ]
})
export class AppRoutingModule {}
