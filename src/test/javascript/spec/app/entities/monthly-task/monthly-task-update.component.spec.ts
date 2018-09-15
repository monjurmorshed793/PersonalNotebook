/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { Observable, of } from 'rxjs';

import { PersonalNotebookAppTestModule } from '../../../test.module';
import { MonthlyTaskUpdateComponent } from 'app/entities/monthly-task/monthly-task-update.component';
import { MonthlyTaskService } from 'app/entities/monthly-task/monthly-task.service';
import { MonthlyTask } from 'app/shared/model/monthly-task.model';

describe('Component Tests', () => {
    describe('MonthlyTask Management Update Component', () => {
        let comp: MonthlyTaskUpdateComponent;
        let fixture: ComponentFixture<MonthlyTaskUpdateComponent>;
        let service: MonthlyTaskService;

        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [PersonalNotebookAppTestModule],
                declarations: [MonthlyTaskUpdateComponent]
            })
                .overrideTemplate(MonthlyTaskUpdateComponent, '')
                .compileComponents();

            fixture = TestBed.createComponent(MonthlyTaskUpdateComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(MonthlyTaskService);
        });

        describe('save', () => {
            it(
                'Should call update service on save for existing entity',
                fakeAsync(() => {
                    // GIVEN
                    const entity = new MonthlyTask(123);
                    spyOn(service, 'update').and.returnValue(of(new HttpResponse({ body: entity })));
                    comp.monthlyTask = entity;
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
                    const entity = new MonthlyTask();
                    spyOn(service, 'create').and.returnValue(of(new HttpResponse({ body: entity })));
                    comp.monthlyTask = entity;
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
