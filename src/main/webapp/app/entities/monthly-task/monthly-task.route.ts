import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes } from '@angular/router';
import { JhiPaginationUtil, JhiResolvePagingParams } from 'ng-jhipster';
import { UserRouteAccessService } from 'app/core';
import { of } from 'rxjs';
import { map } from 'rxjs/operators';
import { MonthlyTask } from 'app/shared/model/monthly-task.model';
import { MonthlyTaskService } from './monthly-task.service';
import { MonthlyTaskComponent } from './monthly-task.component';
import { MonthlyTaskDetailComponent } from './monthly-task-detail.component';
import { MonthlyTaskUpdateComponent } from './monthly-task-update.component';
import { MonthlyTaskDeletePopupComponent } from './monthly-task-delete-dialog.component';
import { IMonthlyTask } from 'app/shared/model/monthly-task.model';

@Injectable({ providedIn: 'root' })
export class MonthlyTaskResolve implements Resolve<IMonthlyTask> {
    constructor(private service: MonthlyTaskService) {}

    resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot) {
        const id = route.params['id'] ? route.params['id'] : null;
        if (id) {
            return this.service.find(id).pipe(map((monthlyTask: HttpResponse<MonthlyTask>) => monthlyTask.body));
        }
        return of(new MonthlyTask());
    }
}

export const monthlyTaskRoute: Routes = [
    {
        path: 'monthly-task',
        component: MonthlyTaskComponent,
        resolve: {
            pagingParams: JhiResolvePagingParams
        },
        data: {
            authorities: ['ROLE_USER'],
            defaultSort: 'id,asc',
            pageTitle: 'MonthlyTasks'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'monthly-task/:id/view',
        component: MonthlyTaskDetailComponent,
        resolve: {
            monthlyTask: MonthlyTaskResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'MonthlyTasks'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'monthly-task/new',
        component: MonthlyTaskUpdateComponent,
        resolve: {
            monthlyTask: MonthlyTaskResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'MonthlyTasks'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'monthly-task/:id/edit',
        component: MonthlyTaskUpdateComponent,
        resolve: {
            monthlyTask: MonthlyTaskResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'MonthlyTasks'
        },
        canActivate: [UserRouteAccessService]
    }
];

export const monthlyTaskPopupRoute: Routes = [
    {
        path: 'monthly-task/:id/delete',
        component: MonthlyTaskDeletePopupComponent,
        resolve: {
            monthlyTask: MonthlyTaskResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'MonthlyTasks'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];
