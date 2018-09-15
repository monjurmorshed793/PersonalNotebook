/* tslint:disable max-line-length */
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { PersonalNotebookAppTestModule } from '../../../test.module';
import { DailyTaskDetailComponent } from 'app/entities/daily-task/daily-task-detail.component';
import { DailyTask } from 'app/shared/model/daily-task.model';

describe('Component Tests', () => {
    describe('DailyTask Management Detail Component', () => {
        let comp: DailyTaskDetailComponent;
        let fixture: ComponentFixture<DailyTaskDetailComponent>;
        const route = ({ data: of({ dailyTask: new DailyTask(123) }) } as any) as ActivatedRoute;

        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [PersonalNotebookAppTestModule],
                declarations: [DailyTaskDetailComponent],
                providers: [{ provide: ActivatedRoute, useValue: route }]
            })
                .overrideTemplate(DailyTaskDetailComponent, '')
                .compileComponents();
            fixture = TestBed.createComponent(DailyTaskDetailComponent);
            comp = fixture.componentInstance;
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
                // GIVEN

                // WHEN
                comp.ngOnInit();

                // THEN
                expect(comp.dailyTask).toEqual(jasmine.objectContaining({ id: 123 }));
            });
        });
    });
});
