import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { JhiAlertService } from 'ng-jhipster';

import {IMonthlyTask, MonthlyTask} from 'app/shared/model/monthly-task.model';
import { MonthlyTaskService } from './monthly-task.service';
import { IUser, UserService } from 'app/core';

@Component({
    selector: 'jhi-monthly-task-update',
    templateUrl: './monthly-task-update.component.html'
})
export class MonthlyTaskUpdateComponent implements OnInit {
    private _monthlyTask: IMonthlyTask;
    isSaving: boolean;

    users: IUser[];

    constructor(
        private jhiAlertService: JhiAlertService,
        private monthlyTaskService: MonthlyTaskService,
        private userService: UserService,
        private activatedRoute: ActivatedRoute
    ) {}

    ngOnInit() {
        this.isSaving = false;
        this.activatedRoute.data.subscribe(({ monthlyTask }) => {
            this.monthlyTask = monthlyTask;
            if(this.monthlyTask == new MonthlyTask())
                this.monthlyTask.monthType = this.monthlyTaskService.getMonth(new Date().getMonth());
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
        if (this.monthlyTask.id !== undefined) {
            this.subscribeToSaveResponse(this.monthlyTaskService.update(this.monthlyTask));
        } else {
            this.subscribeToSaveResponse(this.monthlyTaskService.create(this.monthlyTask));
        }
    }

    private subscribeToSaveResponse(result: Observable<HttpResponse<IMonthlyTask>>) {
        result.subscribe((res: HttpResponse<IMonthlyTask>) => this.onSaveSuccess(), (res: HttpErrorResponse) => this.onSaveError());
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
    get monthlyTask() {
        return this._monthlyTask;
    }

    set monthlyTask(monthlyTask: IMonthlyTask) {
        this._monthlyTask = monthlyTask;
    }
}
