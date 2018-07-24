import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { AlkoholComponent } from './alkohol.component';

describe('AlkoholComponent', () => {
  let component: AlkoholComponent;
  let fixture: ComponentFixture<AlkoholComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ AlkoholComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(AlkoholComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
