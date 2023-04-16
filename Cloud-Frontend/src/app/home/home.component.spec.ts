import { CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { ComponentFixture, TestBed } from "@angular/core/testing";
import { HomeComponent } from "./home.component";

describe("HomeComponent", () => {
  let component: HomeComponent;
  let fixture: ComponentFixture<HomeComponent>;
  let myService: MyService;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [HomeComponent],
      schemas: [CUSTOM_ELEMENTS_SCHEMA],
      providers: [{ provide: MyService, useValue: {} }],
      imports: []
    }).compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(HomeComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();

    myService = TestBed.inject(MyService);
  });

  describe('method1', () => {
    it('should ...', () => {
      expect(component).toBeTruthy();
    });
  });
})