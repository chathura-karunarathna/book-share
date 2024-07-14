/* tslint:disable */
/* eslint-disable */
import { HttpClient, HttpContext, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import { StrictHttpResponse } from '../../strict-http-response';
import { RequestBuilder } from '../../request-builder';

import { PageResponseBorrowedBookHistoryResponseDto } from '../../models/page-response-borrowed-book-history-response-dto';

export interface FindAllreturnedBooks$Params {
  page?: number;
  size?: number;
}

export function findAllreturnedBooks(http: HttpClient, rootUrl: string, params?: FindAllreturnedBooks$Params, context?: HttpContext): Observable<StrictHttpResponse<PageResponseBorrowedBookHistoryResponseDto>> {
  const rb = new RequestBuilder(rootUrl, findAllreturnedBooks.PATH, 'get');
  if (params) {
    rb.query('page', params.page, {});
    rb.query('size', params.size, {});
  }

  return http.request(
    rb.build({ responseType: 'json', accept: 'application/json', context })
  ).pipe(
    filter((r: any): r is HttpResponse<any> => r instanceof HttpResponse),
    map((r: HttpResponse<any>) => {
      return r as StrictHttpResponse<PageResponseBorrowedBookHistoryResponseDto>;
    })
  );
}

findAllreturnedBooks.PATH = '/books/returned';
