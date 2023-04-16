import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { TweetappComponent } from './tweetapp.component';

describe('TweetappComponent', () => {
  let component: TweetappComponent;
  let fixture: ComponentFixture<TweetappComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ TweetappComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(TweetappComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
