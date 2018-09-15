import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IYearlyTask } from 'app/shared/model/yearly-task.model';

@Component({
    selector: 'jhi-yearly-task-detail',
    templateUrl: './yearly-task-detail.component.html'
})
export class YearlyTaskDetailComponent implements OnInit {
    yearlyTask: IYearlyTask;

    constructor(private activatedRoute: ActivatedRoute) {}

    ngOnInit() {
        this.activatedRoute.data.subscribe(({ yearlyTask }) => {
            this.yearlyTask = yearlyTask;
        });
    }

    previousState() {
        window.history.back();
    }
}
