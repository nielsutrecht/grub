import {Injectable, Injector} from '@angular/core';
import {HttpEvent, HttpInterceptor, HttpHandler, HttpRequest} from '@angular/common/http';

import { Observable }     from 'rxjs/Observable';

import { UserService } from './component/user/user.service';

@Injectable()
export class AuthInterceptor implements HttpInterceptor {
  private userService;

  constructor(inj: Injector) {
      this.userService = inj.get(UserService)
  }

  intercept(req: HttpRequest<any>, next: HttpHandler): Observable<HttpEvent<any>> {
    console.log('AuthInterceptor: intercept');
    if(this.userService.getToken()) {
      const authHeader = 'Bearer '  + this.userService.getToken();

      const authReq = req.clone({headers: req.headers.set('Authorization', authHeader)});

      return next.handle(authReq);
    } else {
      return next.handle(req);
    }
  }
}
