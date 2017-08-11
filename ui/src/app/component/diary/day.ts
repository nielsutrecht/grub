import { Component } from '@angular/core';

import { DiaryService } from './diary.service';

@Component({
  selector: 'diary-day',
  templateUrl: './day.html',
  styleUrls: ['./day.css']
})

export class DiaryDayComponent {
  constructor(
    private diaryService: DiaryService,
  ) {}

  get(day): void {
    this.diaryService.get('2017-01-01')
        .then(diary => {
          console.info(diary);
        });
  }
}
