/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { Observable, of } from 'rxjs';

import { PersonalNotebookAppTestModule } from '../../../test.module';
import { YearlyTaskUpdateComponent } from 'app/entities/yearly-task/yearly-task-update.component';
import { YearlyTaskService } from 'app/entities/yearly-task/yearly-task.service';
import { YearlyTask } from 'app/shared/model/yearly-task.model';

describe('Component Tests', () => {
    describe('YearlyTask Management Update Component', () => {
        let comp: YearlyTaskUpdateComponent;
        let fixture: ComponentFixture<YearlyTaskUpdateComponent>;
        let service: YearlyTaskService;

        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [PersonalNotebookAppTestModule],
                declarations: [YearlyTaskUpdateComponent]
            })
                .overrideTemplate(YearlyTaskUpdateComponent, '')
                .compileComponents();

            fixture = TestBed.createComponent(YearlyTaskUpdateComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(YearlyTaskService);
        });

        describe('save', () => {
            it(
                'Should call update service on save for existing entity',
                fakeAsync(() => {
                    // GIVEN
                    const entity = new YearlyTask(123);
                    spyOn(service, 'update').and.returnValue(of(new HttpResponse({ body: entity })));
                    comp.yearlyTask = entity;
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
                    const entity = new YearlyTask();
                    spyOn(service, 'create').and.returnValue(of(new HttpResponse({ body: entity })));
                    comp.yearlyTask = entity;
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
