import { Injectable }    from '@angular/core';
import { HttpHeaders, HttpClient} from "@angular/common/http";

import 'rxjs/add/operator/toPromise';

import { User } from './user';

@Injectable()
export class UserService {
  private baseUrl = 'http://localhost:8080';
  private userUrl = 'user/me';
  private loginUrl = 'user/login'
  private headers = new HttpHeaders({'Content-Type': 'application/json'});
  private loginResponse: LoginResponse;

  constructor(private http: HttpClient) { }

  login(email, password): Promise<LoginResponse> {
    const url = `${this.baseUrl}/${this.loginUrl}`;

    return this.http.post(url, JSON.stringify({email: email, password: password}), {headers: this.headers})
      .toPromise()
      .then(response => {
        this.loginResponse = response as LoginResponse;
        return this.loginResponse;
      })
      .catch(this.handleError);
  }

  get(): Promise<User> {
    const url = `${this.baseUrl}/${this.userUrl}`;
    const headers = new HttpHeaders({'Authorization': 'Bearer ' + this.loginResponse.token});
    return this.http.get(url, {headers})
      .toPromise()
      .then(response => response as User)
      .catch(this.handleError);
  }

  getToken() {
    return this.loginResponse ? this.loginResponse.token : null;
  }

  private handleError(error: any): Promise<any> {
    console.error('An error occurred', error); // for demo purposes only
    return Promise.reject(error.message || error);
  }
}

export class LoginResponse {
  token;
  expires;
}
