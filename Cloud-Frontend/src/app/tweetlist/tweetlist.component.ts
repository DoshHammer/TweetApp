import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { MainService } from '../main.service';
import { IncomingResponse, TweetEntity } from '../models/incomingdata.model';

@Component({
  selector: 'app-tweetlist',
  templateUrl: './tweetlist.component.html',
  styleUrls: ['./tweetlist.component.css']
})
export class TweetlistComponent implements OnInit {
  tweetsList: TweetEntity[];
  tweetSelected = false;
  tweet: TweetEntity;
  userNameTweets: string;

  constructor(private service: MainService, private route: ActivatedRoute, private router: Router) { }

  ngOnInit(): void {

    let username = this.route.snapshot?.params?.id != null ? this.route.snapshot.params.id : "";
    if(username!=""){
      console.log("tweet by user")
      this.service.getUserTweets(username).subscribe((res:any)=>{
        this.tweetsList = res;
        if(this.tweetsList.length>0){
            this.service.searchByUsername(username).subscribe((res: TweetEntity)=>{
              this.tweetsList.map((item,index)=>{
                  this.tweetsList[index].imageUrl = res.imageUrl;
              })
            })
          }
      })
    }
    else{
    console.log("all tweet",this.service.isUserLoggedIn)
    this.service.getAllTweets().subscribe((res: any) => {
       this.tweetsList = res;
      if(this.tweetsList.length>0){
          this.tweetsList.map((item,index)=>this.service.searchByUsername(item.username).subscribe((res:TweetEntity)=>{
            this.tweetsList[index].imageUrl = res.imageUrl;
          }))
      }
    });
  
  }

  }

  viewTweet(idx) {
    
    this.tweet = this.tweetsList[idx];
    this.tweetSelected = true;
  }

  closeTweet(){
    this.tweetSelected = false;
  }

  deleteTweet(idx) {
    let idsList = this.tweetsList.map((tweet) => tweet.tweetId);
    let currentIdx = idsList.indexOf(idx);
    this.tweetsList.splice(currentIdx, 1);
    this.tweetSelected = false;
    this.tweet = null;
  }

}
