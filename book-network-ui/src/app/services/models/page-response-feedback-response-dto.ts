/* tslint:disable */
/* eslint-disable */
import { FeedbackResponseDto } from '../models/feedback-response-dto';
export interface PageResponseFeedbackResponseDto {
  content?: Array<FeedbackResponseDto>;
  first?: boolean;
  last?: boolean;
  pageNumber?: number;
  size?: number;
  totalElements?: number;
  totalPages?: number;
}
