export class JwtToken{
    private jwt : string = "";
    constructor(
    ){}

    public get Jwt() : string{
        return this.jwt;
    }
    
    public set Jwt(jwt : string){
        this.jwt = jwt;
    }
}