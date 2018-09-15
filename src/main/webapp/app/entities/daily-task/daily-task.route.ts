import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes } from '@angular/router';
import { JhiPaginationUtil, JhiResolvePagingParams } from 'ng-jhipster';
import { UserRouteAccessService } from 'app/core';
import { of } from 'rxjs';
import { map } from 'rxjs/operators';
import { DailyTask } from 'app/shared/model/daily-task.model';
import { DailyTaskService } from './daily-task.service';
import { DailyTaskComponent } from './daily-task.component';
import { DailyTaskDetailComponent } from './daily-task-detail.component';
import { DailyTaskUpdateComponent } from './daily-task-update.component';
import { DailyTaskDeletePopupComponent } from './daily-task-delete-dialog.component';
import { IDailyTask } from 'app/shared/model/daily-task.model';

@Injectable({ providedIn: 'root' })
export class DailyTaskResolve implements Resolve<IDailyTask> {
    constructor(private service: DailyTaskService) {}

    resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot) {
        const id = route.params['id'] ? route.params['id'] : null;
        if (id) {
            return this.service.find(id).pipe(map((dailyTask: HttpResponse<DailyTask>) => dailyTask.body));
        }
        return of(new DailyTask());
    }
}

export const dailyTaskRoute: Routes = [
    {
        path: 'daily-task',
        component: DailyTaskComponent,
        resolve: {
            pagingParams: JhiResolvePagingParams
        },
        data: {
            authorities: ['ROLE_USER'],
            defaultSort: 'id,asc',
            pageTitle: 'DailyTasks'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'daily-task/:id/view',
        component: DailyTaskDetailComponent,
        resolve: {
            dailyTask: DailyTaskResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'DailyTasks'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'daily-task/new',
        component: DailyTaskUpdateComponent,
        resolve: {
            dailyTask: DailyTaskResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'DailyTasks'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'daily-task/:id/edit',
        component: DailyTaskUpdateComponent,
        resolve: {
            dailyTask: DailyTaskResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'DailyTasks'
        },
        canActivate: [UserRouteAccessService]
    }
];

export const dailyTaskPopupRoute: Routes = [
    {
        path: 'daily-task/:id/delete',
        component: DailyTaskDeletePopupComponent,
        resolve: {
            dailyTask: DailyTaskResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'DailyTasks'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];
