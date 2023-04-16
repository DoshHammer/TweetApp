export class RegisterForm {
    firstName: string;
    lastName: string;
    username: string;
    email: string;
    password: string;
    contactNumber: number;
}

export class LoginForm {
    username: string;
    password: string;
}

export class ResetForm {
    newPassword: string;
    confirmNewPassword: string;
}

export class TweetForm {
    username: string;
    tweetText: string;
    tags: string[];
}

export class Reply {
    username: string;
    imageUrl: string;
    comment: string;
}