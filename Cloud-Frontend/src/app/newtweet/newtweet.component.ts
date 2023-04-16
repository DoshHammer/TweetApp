import { Component, ElementRef, OnInit, ViewChild } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';
import { MainService } from '../main.service';
import { TweetForm } from '../models/userInputForm';
import { IncomingResponse, TweetEntity, UserEntity } from '../models/incomingdata.model';
import { ActivatedRoute, Router } from '@angular/router';

@Component({
  selector: 'app-newtweet',
  templateUrl: './newtweet.component.html',
  styleUrls: ['./newtweet.component.css']
})
export class NewtweetComponent implements OnInit {
  tags: string[];
  tagsSize: number = 0;
  value: string;
  tweetLimit = true;
  tweeted = false;
  result = false
  editTweet = false;
  currentTweet: TweetEntity;
  tweetText: string;
  nameUser: string;


  constructor(private service: MainService, private route: ActivatedRoute, private router: Router) {
    this.tags = [];
    this.nameUser = localStorage.getItem("tweetapp-loggeduser");

  }

  ngOnInit(): void {
    let tweetId = this.route.snapshot?.params?.id != null ? this.route.snapshot.params.id : "";
    if (tweetId != null && this.service.selectedTweet != undefined && this.service.selectedTweet != null) {
      this.currentTweet = this.service.selectedTweet;
      this.editTweet = true;
      this.tweetText = this.currentTweet.tweetText;
      this.nameUser = localStorage.getItem("tweetapp-loggeduser");
    }
  }

  updateTweet(message: string) {
    let myTweet = new TweetForm();
    myTweet.username = localStorage.getItem("tweetapp-loggeduser");
    //myTweet.likeCounter = this.currentTweet.likeCounter;
    //myTweet.timeStamp = this.currentTweet.timeStamp;
    myTweet.tweetText = message;
    //myTweet.tags = this.tags;

    this.service.updateTweet(myTweet, this.currentTweet.tweetId).subscribe((res: string) => {
    
        this.tweeted = true;
        this.result = true;
        this.tags = [];
        this.tweetText = "";
        this.service.selectedTweet = null;
        this.editTweet = false;
        setTimeout(() => {
          this.router.navigate(['/tweets']);

        }, 1000)
      
    })

  }

  tweet(message: string) {
    let myTweet = new TweetForm();
    myTweet.username = localStorage.getItem("tweetapp-loggeduser");
    //myTweet.timeStamp = new Date().toDateString();
    //myTweet.likeCounter = 0;

    myTweet.tweetText = message;
    //myTweet.tags = this.tags;
    this.service.addTweetForUser(myTweet).subscribe((res: string) => {
      if (res == "Tweet created") {
        this.tweeted = true;
        this.result = true;
        this.tweetText = "";
        setTimeout(() => {
          this.router.navigate(['/tweets']);

        }, 1000)
      } else {
        this.tweeted = true;
        this.result = false;
      }
    });
  }


  addToList(value: string) {
    console.log(value,"tags")
    let content;
    if (value.indexOf(" ") == -1) {
      content = value;
    } else {
      content = value.split(" ")[0];
    }
    if (this.tagsSize + content.length <= 50) {
      this.tags.push(content);
      this.tagsSize = this.tagsSize + content.length;
    }

  }

  deleteTag(idx) {
    let elem = this.tags.splice(idx, 1);
    this.tagsSize = this.tagsSize - elem.length;
  }

  resetData() {
    this.tweeted = false;
    this.result = false;
  }

}
