import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import * as moment from 'moment';
import { DATE_FORMAT } from 'app/shared/constants/input.constants';
import { map } from 'rxjs/operators';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared';
import { IDailyTask } from 'app/shared/model/daily-task.model';

type EntityResponseType = HttpResponse<IDailyTask>;
type EntityArrayResponseType = HttpResponse<IDailyTask[]>;

@Injectable({ providedIn: 'root' })
export class DailyTaskService {
    private resourceUrl = SERVER_API_URL + 'api/daily-tasks';
    private resourceSearchUrl = SERVER_API_URL + 'api/_search/daily-tasks';

    constructor(private http: HttpClient) {}

    create(dailyTask: IDailyTask): Observable<EntityResponseType> {
        const copy = this.convertDateFromClient(dailyTask);
        return this.http
            .post<IDailyTask>(this.resourceUrl, copy, { observe: 'response' })
            .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
    }

    update(dailyTask: IDailyTask): Observable<EntityResponseType> {
        const copy = this.convertDateFromClient(dailyTask);
        return this.http
            .put<IDailyTask>(this.resourceUrl, copy, { observe: 'response' })
            .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
    }

    find(id: number): Observable<EntityResponseType> {
        return this.http
            .get<IDailyTask>(`${this.resourceUrl}/${id}`, { observe: 'response' })
            .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
    }

    query(req?: any): Observable<EntityArrayResponseType> {
        const options = createRequestOption(req);
        return this.http
            .get<IDailyTask[]>(this.resourceUrl, { params: options, observe: 'response' })
            .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
    }

    delete(id: number): Observable<HttpResponse<any>> {
        return this.http.delete<any>(`${this.resourceUrl}/${id}`, { observe: 'response' });
    }

    search(req?: any): Observable<EntityArrayResponseType> {
        const options = createRequestOption(req);
        return this.http
            .get<IDailyTask[]>(this.resourceSearchUrl, { params: options, observe: 'response' })
            .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
    }

    private convertDateFromClient(dailyTask: IDailyTask): IDailyTask {
        const copy: IDailyTask = Object.assign({}, dailyTask, {
            date: dailyTask.date != null && dailyTask.date.isValid() ? dailyTask.date.format(DATE_FORMAT) : null
        });
        return copy;
    }

    private convertDateFromServer(res: EntityResponseType): EntityResponseType {
        res.body.date = res.body.date != null ? moment(res.body.date) : null;
        return res;
    }

    private convertDateArrayFromServer(res: EntityArrayResponseType): EntityArrayResponseType {
        res.body.forEach((dailyTask: IDailyTask) => {
            dailyTask.date = dailyTask.date != null ? moment(dailyTask.date) : null;
        });
        return res;
    }
}
