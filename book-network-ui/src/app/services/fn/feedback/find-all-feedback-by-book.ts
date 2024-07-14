/* tslint:disable */
/* eslint-disable */
import { HttpClient, HttpContext, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import { StrictHttpResponse } from '../../strict-http-response';
import { RequestBuilder } from '../../request-builder';

import { PageResponseFeedbackResponseDto } from '../../models/page-response-feedback-response-dto';

export interface FindAllFeedbackByBook$Params {
  'book-id': number;
  page?: number;
  size?: number;
}

export function findAllFeedbackByBook(http: HttpClient, rootUrl: string, params: FindAllFeedbackByBook$Params, context?: HttpContext): Observable<StrictHttpResponse<PageResponseFeedbackResponseDto>> {
  const rb = new RequestBuilder(rootUrl, findAllFeedbackByBook.PATH, 'get');
  if (params) {
    rb.path('book-id', params['book-id'], {});
    rb.query('page', params.page, {});
    rb.query('size', params.size, {});
  }

  return http.request(
    rb.build({ responseType: 'json', accept: 'application/json', context })
  ).pipe(
    filter((r: any): r is HttpResponse<any> => r instanceof HttpResponse),
    map((r: HttpResponse<any>) => {
      return r as StrictHttpResponse<PageResponseFeedbackResponseDto>;
    })
  );
}

findAllFeedbackByBook.PATH = '/feedbacks/book/{book-id}';
