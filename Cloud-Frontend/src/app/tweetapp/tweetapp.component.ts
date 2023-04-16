import { Component, EventEmitter, Input, OnInit, Output } from '@angular/core';
import { Router } from '@angular/router';
import { MainService } from '../main.service';
import { IncomingResponse, TweetEntity} from '../models/incomingdata.model';
import { Reply } from '../models/userInputForm';

@Component({
  selector: 'app-tweetapp',
  templateUrl: './tweetapp.component.html',
  styleUrls: ['./tweetapp.component.css']
})
export class TweetappComponent implements OnInit {

  @Input("selectedTweet")
  tweet: TweetEntity;
  @Output("delTweet")
  deleteCurrentTweet: EventEmitter<string> = new EventEmitter();
  @Output("closeTweet")
  closeCurrentTweet: EventEmitter<boolean> = new EventEmitter();

  currentComment = "";
  currentUser: string;

  constructor(private service: MainService, private route: Router) {
    this.currentUser = localStorage.getItem("tweetapp-loggeduser");
  }

  ngOnInit(): void {

  }

  replyToTweet(reply) {

    let currentReply = new Reply();
    currentReply.username = this.currentUser;
    //currentReply.tweetDate = new Date().toDateString();
    currentReply.comment = reply;
    this.tweet.comments.push(currentReply);
    this.service.replyTweet(currentReply, this.tweet.tweetId).subscribe((res: IncomingResponse) => {
      
      this.service.getTweetById(this.tweet.tweetId).subscribe((res: any)=>{
        this.tweet = res
        this.currentComment = "";
     });
    })
  }

  likeTweet(tweet) {

    this.service.likeTweet(localStorage.getItem("tweetapp-loggeduser"),tweet.tweetId).subscribe((res: string) => {
       console.log(res); 
       this.service.getTweetById(tweet.tweetId).subscribe((res: any)=>{
        this.tweet = res
     });
    })
  }

  deleteTweet() {
    this.service.deleteTweet(this.tweet.username,this.tweet.tweetId).subscribe((res: string) => {
      console.log(res,this.tweet.tweetId);
      this.deleteCurrentTweet.emit(this.tweet.tweetId);
      this.tweet = null;
    },
    err=>{
      console.log(err);
    }
    );
  }

  editTweet() {
    this.service.selectedTweet = this.tweet;
    this.route.navigate(['/edit/' + this.tweet.tweetId]);
  }

  close(){
    this.closeCurrentTweet.emit(true);

  }

}
