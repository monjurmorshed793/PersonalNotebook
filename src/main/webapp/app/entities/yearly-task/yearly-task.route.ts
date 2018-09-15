import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes } from '@angular/router';
import { JhiPaginationUtil, JhiResolvePagingParams } from 'ng-jhipster';
import { UserRouteAccessService } from 'app/core';
import { of } from 'rxjs';
import { map } from 'rxjs/operators';
import { YearlyTask } from 'app/shared/model/yearly-task.model';
import { YearlyTaskService } from './yearly-task.service';
import { YearlyTaskComponent } from './yearly-task.component';
import { YearlyTaskDetailComponent } from './yearly-task-detail.component';
import { YearlyTaskUpdateComponent } from './yearly-task-update.component';
import { YearlyTaskDeletePopupComponent } from './yearly-task-delete-dialog.component';
import { IYearlyTask } from 'app/shared/model/yearly-task.model';

@Injectable({ providedIn: 'root' })
export class YearlyTaskResolve implements Resolve<IYearlyTask> {
    constructor(private service: YearlyTaskService) {}

    resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot) {
        const id = route.params['id'] ? route.params['id'] : null;
        if (id) {
            return this.service.find(id).pipe(map((yearlyTask: HttpResponse<YearlyTask>) => yearlyTask.body));
        }
        return of(new YearlyTask());
    }
}

export const yearlyTaskRoute: Routes = [
    {
        path: 'yearly-task',
        component: YearlyTaskComponent,
        resolve: {
            pagingParams: JhiResolvePagingParams
        },
        data: {
            authorities: ['ROLE_USER'],
            defaultSort: 'id,asc',
            pageTitle: 'YearlyTasks'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'yearly-task/:id/view',
        component: YearlyTaskDetailComponent,
        resolve: {
            yearlyTask: YearlyTaskResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'YearlyTasks'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'yearly-task/new',
        component: YearlyTaskUpdateComponent,
        resolve: {
            yearlyTask: YearlyTaskResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'YearlyTasks'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'yearly-task/:id/edit',
        component: YearlyTaskUpdateComponent,
        resolve: {
            yearlyTask: YearlyTaskResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'YearlyTasks'
        },
        canActivate: [UserRouteAccessService]
    }
];

export const yearlyTaskPopupRoute: Routes = [
    {
        path: 'yearly-task/:id/delete',
        component: YearlyTaskDeletePopupComponent,
        resolve: {
            yearlyTask: YearlyTaskResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'YearlyTasks'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];
