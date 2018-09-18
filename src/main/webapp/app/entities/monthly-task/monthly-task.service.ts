import {Injectable} from '@angular/core';
import {HttpClient, HttpResponse} from '@angular/common/http';
import {Observable} from 'rxjs';

import {SERVER_API_URL} from 'app/app.constants';
import {createRequestOption} from 'app/shared';
import {IMonthlyTask, MonthType} from 'app/shared/model/monthly-task.model';

type EntityResponseType = HttpResponse<IMonthlyTask>;
type EntityArrayResponseType = HttpResponse<IMonthlyTask[]>;

@Injectable({ providedIn: 'root' })
export class MonthlyTaskService {
    private resourceUrl = SERVER_API_URL + 'api/monthly-tasks';
    private resourceSearchUrl = SERVER_API_URL + 'api/_search/monthly-tasks';

    constructor(private http: HttpClient) {}

    create(monthlyTask: IMonthlyTask): Observable<EntityResponseType> {
        return this.http.post<IMonthlyTask>(this.resourceUrl, monthlyTask, { observe: 'response' });
    }

    update(monthlyTask: IMonthlyTask): Observable<EntityResponseType> {
        return this.http.put<IMonthlyTask>(this.resourceUrl, monthlyTask, { observe: 'response' });
    }

    find(id: number): Observable<EntityResponseType> {
        return this.http.get<IMonthlyTask>(`${this.resourceUrl}/${id}`, { observe: 'response' });
    }

    findByMonthType(monthType: MonthType, req?:any): Observable<EntityArrayResponseType>{
        const options = createRequestOption(req);
        return this.http.get<IMonthlyTask[]>(`${this.resourceUrl}/month-type/${monthType}`, { params: options, observe: 'response' });
    }

    query(req?: any): Observable<EntityArrayResponseType> {
        const options = createRequestOption(req);
        return this.http.get<IMonthlyTask[]>(this.resourceUrl, { params: options, observe: 'response' });
    }

    delete(id: number): Observable<HttpResponse<any>> {
        return this.http.delete<any>(`${this.resourceUrl}/${id}`, { observe: 'response' });
    }

    search(req?: any): Observable<EntityArrayResponseType> {
        const options = createRequestOption(req);
        return this.http.get<IMonthlyTask[]>(this.resourceSearchUrl, { params: options, observe: 'response' });
    }

    getMonth(monthNumber:number):MonthType{
        if(monthNumber==0)
            return MonthType.JANUARY;
        else if(monthNumber==1)
            return MonthType.FEBRUARY;
        else if(monthNumber==2)
            return MonthType.MARCH;
        else if(monthNumber==3)
            return MonthType.APRIL;
        else if(monthNumber==4)
            return MonthType.MAY;
        else if(monthNumber==5)
            return MonthType.JUNE;
        else if(monthNumber==6)
            return MonthType.JULY;
        else if(monthNumber==7)
            return MonthType.AUGUST;
        else if(monthNumber==8)
            return MonthType.SEPTEMBER;
        else if(monthNumber==9)
            return MonthType.OCTOBER;
        else if(monthNumber==10)
            return MonthType.NOVEMBER;
        else
            return MonthType.DECEMBER;
    }
}
