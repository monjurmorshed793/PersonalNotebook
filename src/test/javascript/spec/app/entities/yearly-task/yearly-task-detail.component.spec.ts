/* tslint:disable max-line-length */
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { PersonalNotebookAppTestModule } from '../../../test.module';
import { YearlyTaskDetailComponent } from 'app/entities/yearly-task/yearly-task-detail.component';
import { YearlyTask } from 'app/shared/model/yearly-task.model';

describe('Component Tests', () => {
    describe('YearlyTask Management Detail Component', () => {
        let comp: YearlyTaskDetailComponent;
        let fixture: ComponentFixture<YearlyTaskDetailComponent>;
        const route = ({ data: of({ yearlyTask: new YearlyTask(123) }) } as any) as ActivatedRoute;

        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [PersonalNotebookAppTestModule],
                declarations: [YearlyTaskDetailComponent],
                providers: [{ provide: ActivatedRoute, useValue: route }]
            })
                .overrideTemplate(YearlyTaskDetailComponent, '')
                .compileComponents();
            fixture = TestBed.createComponent(YearlyTaskDetailComponent);
            comp = fixture.componentInstance;
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
                // GIVEN

                // WHEN
                comp.ngOnInit();

                // THEN
                expect(comp.yearlyTask).toEqual(jasmine.objectContaining({ id: 123 }));
            });
        });
    });
});
