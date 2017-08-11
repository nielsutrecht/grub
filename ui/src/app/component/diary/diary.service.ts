import { Injectable }    from '@angular/core';
import { HttpHeaders, HttpClient} from "@angular/common/http";

import 'rxjs/add/operator/toPromise';

@Injectable()
export class DiaryService {
  private baseUrl = 'http://localhost:8080';
  private diaryUrl = 'diary/me';

  private headers = new HttpHeaders({'Content-Type': 'application/json'});

  constructor(private http: HttpClient) { }

  get(day): Promise<DayDiary> {
    const url = `${this.baseUrl}/${this.diaryUrl}`;
    //const headers = new HttpHeaders({'Authorization': 'Bearer ' + this.loginResponse.token});
    return this.http.get(url)
      .toPromise()
      .then(response => response as DayDiary)
      .catch(this.handleError);
  }

  private handleError(error: any): Promise<any> {
    console.error('An error occurred', error); // for demo purposes only
    return Promise.reject(error.message || error);
  }
}

export class DayDiary {

}

