import { Reply } from "./userInputForm";
export class TweetEntity {
    tweetId: string;
    username: string;
    tweetText: string;
    tweetDate: string;
    tweetUpdateDate:string;
    comments: Reply[];
    likes: string[];
    imageUrl:string;
}

export class UserEntity {
    firstName: string;
    lastName: string;
    username: string;
    email: string;
}

export interface IncomingResponse {
    msg: any;
    token:any
}
