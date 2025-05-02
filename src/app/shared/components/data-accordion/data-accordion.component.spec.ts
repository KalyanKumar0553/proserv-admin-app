import { ComponentFixture, TestBed } from '@angular/core/testing';

import { DataAccordionComponent } from './data-accordion.component';

describe('DataAccordionComponent', () => {
  let component: DataAccordionComponent;
  let fixture: ComponentFixture<DataAccordionComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [DataAccordionComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(DataAccordionComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
