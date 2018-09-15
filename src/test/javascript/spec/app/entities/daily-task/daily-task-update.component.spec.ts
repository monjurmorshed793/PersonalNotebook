/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { Observable, of } from 'rxjs';

import { PersonalNotebookAppTestModule } from '../../../test.module';
import { DailyTaskUpdateComponent } from 'app/entities/daily-task/daily-task-update.component';
import { DailyTaskService } from 'app/entities/daily-task/daily-task.service';
import { DailyTask } from 'app/shared/model/daily-task.model';

describe('Component Tests', () => {
    describe('DailyTask Management Update Component', () => {
        let comp: DailyTaskUpdateComponent;
        let fixture: ComponentFixture<DailyTaskUpdateComponent>;
        let service: DailyTaskService;

        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [PersonalNotebookAppTestModule],
                declarations: [DailyTaskUpdateComponent]
            })
                .overrideTemplate(DailyTaskUpdateComponent, '')
                .compileComponents();

            fixture = TestBed.createComponent(DailyTaskUpdateComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(DailyTaskService);
        });

        describe('save', () => {
            it(
                'Should call update service on save for existing entity',
                fakeAsync(() => {
                    // GIVEN
                    const entity = new DailyTask(123);
                    spyOn(service, 'update').and.returnValue(of(new HttpResponse({ body: entity })));
                    comp.dailyTask = entity;
                    // WHEN
                    comp.save();
                    tick(); // simulate async

                    // THEN
                    expect(service.update).toHaveBeenCalledWith(entity);
                    expect(comp.isSaving).toEqual(false);
                })
            );

            it(
                'Should call create service on save for new entity',
                fakeAsync(() => {
                    // GIVEN
                    const entity = new DailyTask();
                    spyOn(service, 'create').and.returnValue(of(new HttpResponse({ body: entity })));
                    comp.dailyTask = entity;
                    // WHEN
                    comp.save();
                    tick(); // simulate async

                    // THEN
                    expect(service.create).toHaveBeenCalledWith(entity);
                    expect(comp.isSaving).toEqual(false);
                })
            );
        });
    });
});
