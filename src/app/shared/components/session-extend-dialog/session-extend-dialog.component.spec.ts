import { ComponentFixture, TestBed } from '@angular/core/testing';

import { SessionExtendDialogComponent } from './session-extend-dialog.component';

describe('SessionExtendDialogComponent', () => {
  let component: SessionExtendDialogComponent;
  let fixture: ComponentFixture<SessionExtendDialogComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [SessionExtendDialogComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(SessionExtendDialogComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
