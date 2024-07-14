/* tslint:disable */
/* eslint-disable */
import { BorrowedBookHistoryResponseDto } from '../models/borrowed-book-history-response-dto';
export interface PageResponseBorrowedBookHistoryResponseDto {
  content?: Array<BorrowedBookHistoryResponseDto>;
  first?: boolean;
  last?: boolean;
  pageNumber?: number;
  size?: number;
  totalElements?: number;
  totalPages?: number;
}
