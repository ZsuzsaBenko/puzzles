import { HttpEvent, HttpHandler, HttpInterceptor, HttpRequest } from '@angular/common/http';
import { Observable } from 'rxjs';

export class AuthInterceptorService implements HttpInterceptor {
  intercept(req: HttpRequest<any>, next: HttpHandler): Observable<HttpEvent<any>> {
    if (!req.url.endsWith('registration') && !req.url.endsWith('login')) {
      const modifiedReq = req.clone({
        headers: req.headers.append('Authorization', `Bearer ${localStorage.getItem('token')}`)
      });
      return next.handle(modifiedReq);
    }
    return next.handle(req);
  }
}
