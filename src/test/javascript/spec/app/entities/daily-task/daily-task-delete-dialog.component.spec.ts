/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, inject, fakeAsync, tick } from '@angular/core/testing';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { Observable, of } from 'rxjs';
import { JhiEventManager } from 'ng-jhipster';

import { PersonalNotebookAppTestModule } from '../../../test.module';
import { DailyTaskDeleteDialogComponent } from 'app/entities/daily-task/daily-task-delete-dialog.component';
import { DailyTaskService } from 'app/entities/daily-task/daily-task.service';

describe('Component Tests', () => {
    describe('DailyTask Management Delete Component', () => {
        let comp: DailyTaskDeleteDialogComponent;
        let fixture: ComponentFixture<DailyTaskDeleteDialogComponent>;
        let service: DailyTaskService;
        let mockEventManager: any;
        let mockActiveModal: any;

        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [PersonalNotebookAppTestModule],
                declarations: [DailyTaskDeleteDialogComponent]
            })
                .overrideTemplate(DailyTaskDeleteDialogComponent, '')
                .compileComponents();
            fixture = TestBed.createComponent(DailyTaskDeleteDialogComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(DailyTaskService);
            mockEventManager = fixture.debugElement.injector.get(JhiEventManager);
            mockActiveModal = fixture.debugElement.injector.get(NgbActiveModal);
        });

        describe('confirmDelete', () => {
            it('Should call delete service on confirmDelete', inject(
                [],
                fakeAsync(() => {
                    // GIVEN
                    spyOn(service, 'delete').and.returnValue(of({}));

                    // WHEN
                    comp.confirmDelete(123);
                    tick();

                    // THEN
                    expect(service.delete).toHaveBeenCalledWith(123);
                    expect(mockActiveModal.dismissSpy).toHaveBeenCalled();
                    expect(mockEventManager.broadcastSpy).toHaveBeenCalled();
                })
            ));
        });
    });
});
