import {Component, OnInit} from '@angular/core';
import {PageResponseBorrowedBookHistoryResponseDto} from "../../../../services/models/page-response-borrowed-book-history-response-dto";
import {FeedbackRequestDto} from "../../../../services/models/feedback-request-dto";
import {BorrowedBookHistoryResponseDto} from "../../../../services/models/borrowed-book-history-response-dto";
import {BookService} from "../../../../services/services/book.service";
import {FeedbackService} from "../../../../services/services/feedback.service";

@Component({
  selector: 'app-return-books',
  templateUrl: './return-books.component.html',
  styleUrl: './return-books.component.scss'
})
export class ReturnBooksComponent implements OnInit{
  returnedBooks: PageResponseBorrowedBookHistoryResponseDto = {};
  page = 0;
  size = 10;
  message = '';
  level = 'success';

  constructor(
    private bookService: BookService
  ) {
  }

  ngOnInit(): void {
    this.findAllReturnedBooks();
  }

  private findAllReturnedBooks() {
    this.bookService.findAllreturnedBooks({
      page: this.page,
      size: this.size
    }).subscribe({
      next: (resp)=> {
        this.returnedBooks = resp;
      }
    })
  }

  goToFirstPage() {
    this.page = 0;
    this.findAllReturnedBooks();
  }

  goToPreviousPage() {
    this.page--;
    this.findAllReturnedBooks();
  }

  goToPage(page: number) {
    this.page = page;
    this.findAllReturnedBooks();
  }

  goToNextPage() {
    this.page++;
    this.findAllReturnedBooks();
  }

  goToLastPage() {
    this.page = this.returnedBooks.totalPages as number -1;
    this.findAllReturnedBooks();
  }

  get isLastPage(): boolean{
    return this.page == this.returnedBooks.totalPages as number -1;
  }

  approveBookReturn(book : BorrowedBookHistoryResponseDto) {
    if(!book.returned){
      this.level = 'error';
      this.message = 'The Book is not yet returned';
      return;
    }
    this.bookService.approveReturnBorrowBook({
      'book-id' : book.id as number
    }).subscribe({
      next: ()=> {
        this.level = 'success';
        this.message = 'Book return approved';
        this.findAllReturnedBooks();
      }
    })
  }
}
