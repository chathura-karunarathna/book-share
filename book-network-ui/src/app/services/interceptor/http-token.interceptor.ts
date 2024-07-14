import {
  HttpEvent,
  HttpHandler,
  HttpHeaders,
  HttpInterceptor,
  HttpInterceptorFn,
  HttpRequest
} from '@angular/common/http';
import {Observable} from "rxjs";
import {Injectable} from "@angular/core";
import {TokenService} from "../token/token.service";

@Injectable()
export class httpTokenInterceptor implements HttpInterceptor{

  constructor(
    private tokenService: TokenService
  ) {
  }

  intercept(req: HttpRequest<any>, next: HttpHandler): Observable<HttpEvent<unknown>> {
    const token = this.tokenService.token;
    if(token){
      const authReq = req.clone({
        headers: new HttpHeaders({
          Authorization: 'Bearer ' + token
        })
      });
      return next.handle(authReq);
    }
    return next.handle(req);
  }

}
