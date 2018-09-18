import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { JhiAlertService } from 'ng-jhipster';

import {IYearlyTask, YearlyTask} from 'app/shared/model/yearly-task.model';
import { YearlyTaskService } from './yearly-task.service';
import { IUser, UserService } from 'app/core';

@Component({
    selector: 'jhi-yearly-task-update',
    templateUrl: './yearly-task-update.component.html'
})
export class YearlyTaskUpdateComponent implements OnInit {
    private _yearlyTask: IYearlyTask;
    isSaving: boolean;

    users: IUser[];

    constructor(
        private jhiAlertService: JhiAlertService,
        private yearlyTaskService: YearlyTaskService,
        private userService: UserService,
        private activatedRoute: ActivatedRoute
    ) {}

    ngOnInit() {
        this.isSaving = false;
        this.activatedRoute.data.subscribe(({ yearlyTask }) => {
            this.yearlyTask = yearlyTask;
            if(this.yearlyTask = new YearlyTask())
                this.yearlyTask.year = new Date().getFullYear();
        });
        this.userService.query().subscribe(
            (res: HttpResponse<IUser[]>) => {
                this.users = res.body;
            },
            (res: HttpErrorResponse) => this.onError(res.message)
        );
    }

    previousState() {
        window.history.back();
    }

    save() {
        this.isSaving = true;
        if (this.yearlyTask.id !== undefined) {
            this.subscribeToSaveResponse(this.yearlyTaskService.update(this.yearlyTask));
        } else {
            this.subscribeToSaveResponse(this.yearlyTaskService.create(this.yearlyTask));
        }
    }

    private subscribeToSaveResponse(result: Observable<HttpResponse<IYearlyTask>>) {
        result.subscribe((res: HttpResponse<IYearlyTask>) => this.onSaveSuccess(), (res: HttpErrorResponse) => this.onSaveError());
    }

    private onSaveSuccess() {
        this.isSaving = false;
        this.previousState();
    }

    private onSaveError() {
        this.isSaving = false;
    }

    private onError(errorMessage: string) {
        this.jhiAlertService.error(errorMessage, null, null);
    }

    trackUserById(index: number, item: IUser) {
        return item.id;
    }
    get yearlyTask() {
        return this._yearlyTask;
    }

    set yearlyTask(yearlyTask: IYearlyTask) {
        this._yearlyTask = yearlyTask;
    }
}
