import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IDailyTask } from 'app/shared/model/daily-task.model';

@Component({
    selector: 'jhi-daily-task-detail',
    templateUrl: './daily-task-detail.component.html'
})
export class DailyTaskDetailComponent implements OnInit {
    dailyTask: IDailyTask;

    constructor(private activatedRoute: ActivatedRoute) {}

    ngOnInit() {
        this.activatedRoute.data.subscribe(({ dailyTask }) => {
            this.dailyTask = dailyTask;
        });
    }

    previousState() {
        window.history.back();
    }
}
