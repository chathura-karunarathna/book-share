import {Component, OnInit} from '@angular/core';
import {PageResponseBorrowedBookHistoryResponseDto} from "../../../../services/models/page-response-borrowed-book-history-response-dto";
import {BorrowedBookHistoryResponseDto} from "../../../../services/models/borrowed-book-history-response-dto";
import {BookService} from "../../../../services/services/book.service";
import {FeedbackRequestDto} from "../../../../services/models/feedback-request-dto";
import {FeedbackService} from "../../../../services/services/feedback.service";

@Component({
  selector: 'app-borrowed-book-list',
  templateUrl: './borrowed-book-list.component.html',
  styleUrl: './borrowed-book-list.component.scss'
})
export class BorrowedBookListComponent implements OnInit{
  borrowedBooks: PageResponseBorrowedBookHistoryResponseDto = {};
  feedBackRequest: FeedbackRequestDto = {bookId: 0, comment: '', note: 0};
  page = 0;
  size = 10;
  selectedBook: BorrowedBookHistoryResponseDto | undefined = undefined;

  constructor(
    private bookService: BookService,
    private feedbackService: FeedbackService
  ) {
  }

  returnBorrowedBook(book : BorrowedBookHistoryResponseDto) {
    this.selectedBook = book;
    this.feedBackRequest.bookId = book.id as number;
  }

  ngOnInit(): void {
    this.findAllBorrowedBooks();
  }

  private findAllBorrowedBooks() {
    this.bookService.findAllBorrowedBooks({
      page: this.page,
      size: this.size
    }).subscribe({
      next: (resp)=> {
        this.borrowedBooks = resp;
      }
    })
  }

  goToFirstPage() {
    this.page = 0;
    this.findAllBorrowedBooks();
  }

  goToPreviousPage() {
    this.page--;
    this.findAllBorrowedBooks();
  }

  goToPage(page: number) {
    this.page = page;
    this.findAllBorrowedBooks();
  }

  goToNextPage() {
    this.page++;
    this.findAllBorrowedBooks();
  }

  goToLastPage() {
    this.page = this.borrowedBooks.totalPages as number -1;
    this.findAllBorrowedBooks();
  }

  get isLastPage(): boolean{
    return this.page == this.borrowedBooks.totalPages as number -1;
  }

  returnBook(withFeedback: boolean) {
    this.bookService.returnBorrowBook({
      'book-id': this.selectedBook?.id as number
    }).subscribe({
      next: () => {
        if(withFeedback){
          this.giveFeedBack();
        }
        this.selectedBook = undefined;
        this.findAllBorrowedBooks();
      }
    });
  }

  private giveFeedBack() {
    this.feedbackService.saveFeedback({
      body: this.feedBackRequest
    }).subscribe({
      next: () => {
      }
    });
  }
}
