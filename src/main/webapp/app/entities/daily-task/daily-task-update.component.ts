import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import * as moment from 'moment';
import { DATE_TIME_FORMAT } from 'app/shared/constants/input.constants';
import { JhiAlertService } from 'ng-jhipster';

import { IDailyTask } from 'app/shared/model/daily-task.model';
import { DailyTaskService } from './daily-task.service';
import { IUser, UserService } from 'app/core';

@Component({
    selector: 'jhi-daily-task-update',
    templateUrl: './daily-task-update.component.html'
})
export class DailyTaskUpdateComponent implements OnInit {
    private _dailyTask: IDailyTask;
    isSaving: boolean;

    users: IUser[];
    dateDp: any;

    constructor(
        private jhiAlertService: JhiAlertService,
        private dailyTaskService: DailyTaskService,
        private userService: UserService,
        private activatedRoute: ActivatedRoute
    ) {}

    ngOnInit() {
        this.isSaving = false;
        this.activatedRoute.data.subscribe(({ dailyTask }) => {
            this.dailyTask = dailyTask;
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
        if (this.dailyTask.id !== undefined) {
            this.subscribeToSaveResponse(this.dailyTaskService.update(this.dailyTask));
        } else {
            this.subscribeToSaveResponse(this.dailyTaskService.create(this.dailyTask));
        }
    }

    private subscribeToSaveResponse(result: Observable<HttpResponse<IDailyTask>>) {
        result.subscribe((res: HttpResponse<IDailyTask>) => this.onSaveSuccess(), (res: HttpErrorResponse) => this.onSaveError());
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
    get dailyTask() {
        return this._dailyTask;
    }

    set dailyTask(dailyTask: IDailyTask) {
        this._dailyTask = dailyTask;
    }
}
