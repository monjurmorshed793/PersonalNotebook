import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IMonthlyTask } from 'app/shared/model/monthly-task.model';

@Component({
    selector: 'jhi-monthly-task-detail',
    templateUrl: './monthly-task-detail.component.html'
})
export class MonthlyTaskDetailComponent implements OnInit {
    monthlyTask: IMonthlyTask;

    constructor(private activatedRoute: ActivatedRoute) {}

    ngOnInit() {
        this.activatedRoute.data.subscribe(({ monthlyTask }) => {
            this.monthlyTask = monthlyTask;
        });
    }

    previousState() {
        window.history.back();
    }
}
