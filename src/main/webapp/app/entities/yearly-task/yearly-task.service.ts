import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared';
import { IYearlyTask } from 'app/shared/model/yearly-task.model';

type EntityResponseType = HttpResponse<IYearlyTask>;
type EntityArrayResponseType = HttpResponse<IYearlyTask[]>;

@Injectable({ providedIn: 'root' })
export class YearlyTaskService {
    private resourceUrl = SERVER_API_URL + 'api/yearly-tasks';
    private resourceSearchUrl = SERVER_API_URL + 'api/_search/yearly-tasks';

    constructor(private http: HttpClient) {}

    create(yearlyTask: IYearlyTask): Observable<EntityResponseType> {
        return this.http.post<IYearlyTask>(this.resourceUrl, yearlyTask, { observe: 'response' });
    }

    update(yearlyTask: IYearlyTask): Observable<EntityResponseType> {
        return this.http.put<IYearlyTask>(this.resourceUrl, yearlyTask, { observe: 'response' });
    }

    find(id: number): Observable<EntityResponseType> {
        return this.http.get<IYearlyTask>(`${this.resourceUrl}/${id}`, { observe: 'response' });
    }

    query(req?: any): Observable<EntityArrayResponseType> {
        const options = createRequestOption(req);
        return this.http.get<IYearlyTask[]>(this.resourceUrl, { params: options, observe: 'response' });
    }

    delete(id: number): Observable<HttpResponse<any>> {
        return this.http.delete<any>(`${this.resourceUrl}/${id}`, { observe: 'response' });
    }

    search(req?: any): Observable<EntityArrayResponseType> {
        const options = createRequestOption(req);
        return this.http.get<IYearlyTask[]>(this.resourceSearchUrl, { params: options, observe: 'response' });
    }
}
