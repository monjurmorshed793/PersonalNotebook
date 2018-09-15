/* tslint:disable max-line-length */
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { PersonalNotebookAppTestModule } from '../../../test.module';
import { MonthlyTaskDetailComponent } from 'app/entities/monthly-task/monthly-task-detail.component';
import { MonthlyTask } from 'app/shared/model/monthly-task.model';

describe('Component Tests', () => {
    describe('MonthlyTask Management Detail Component', () => {
        let comp: MonthlyTaskDetailComponent;
        let fixture: ComponentFixture<MonthlyTaskDetailComponent>;
        const route = ({ data: of({ monthlyTask: new MonthlyTask(123) }) } as any) as ActivatedRoute;

        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [PersonalNotebookAppTestModule],
                declarations: [MonthlyTaskDetailComponent],
                providers: [{ provide: ActivatedRoute, useValue: route }]
            })
                .overrideTemplate(MonthlyTaskDetailComponent, '')
                .compileComponents();
            fixture = TestBed.createComponent(MonthlyTaskDetailComponent);
            comp = fixture.componentInstance;
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
                // GIVEN

                // WHEN
                comp.ngOnInit();

                // THEN
                expect(comp.monthlyTask).toEqual(jasmine.objectContaining({ id: 123 }));
            });
        });
    });
});
