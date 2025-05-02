import { ComponentFixture, TestBed } from '@angular/core/testing';

import { AddUpdateCategoryTaskComponent } from './add-update-category-task.component';

describe('AddUpdateCategoryTaskComponent', () => {
  let component: AddUpdateCategoryTaskComponent;
  let fixture: ComponentFixture<AddUpdateCategoryTaskComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [AddUpdateCategoryTaskComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(AddUpdateCategoryTaskComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
