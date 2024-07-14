import {Component, OnInit} from '@angular/core';
import {PageResponseBookResponseDto} from "../../../../services/models/page-response-book-response-dto";
import {BookService} from "../../../../services/services/book.service";
import {Router} from "@angular/router";
import {BookResponseDto} from "../../../../services/models/book-response-dto";

@Component({
  selector: 'app-my-books',
  templateUrl: './my-books.component.html',
  styleUrl: './my-books.component.scss'
})
export class MyBooksComponent implements OnInit{
  bookResponse: PageResponseBookResponseDto = {};

  page = 0;
  size = 5;

  constructor(
    private bookService: BookService,
    private router: Router
  ) {

  }

  ngOnInit(): void {
    this.findAllBooks();
  }

  private findAllBooks() {
    this.bookService.findAllBooksByOwner({
      page: this.page,
      size: this.size
    }).subscribe({
      next: (books)=>{
        this.bookResponse = books;
      }
    })
  }

  goToFirstPage() {
    this.page = 0;
    this.findAllBooks();
  }

  goToPreviousPage() {
    this.page--;
    this.findAllBooks();
  }

  goToPage(page: number) {
    this.page = page;
    this.findAllBooks();
  }

  goToNextPage() {
    this.page++;
    this.findAllBooks();
  }

  goToLastPage() {
    this.page = this.bookResponse.totalPages as number -1;
    this.findAllBooks();
  }

  get isLastPage(): boolean{
    return this.page == this.bookResponse.totalPages as number -1;
  }

  archiveBook(book: BookResponseDto) {
    this.bookService.updateArchivedStatus({
      'book-id': book.id as number
    }).subscribe({
      next: ()=>{
        book.archived = !book.archived;
      }
    });
  }

  shareBook(book: BookResponseDto) {
    this.bookService.updateShareableStatus({
      'book-id': book.id as number
    }).subscribe({
      next:()=>{
        book.shareable = !book.shareable;
      }
    });
  }

  editBook(book: BookResponseDto) {
    this.router.navigate(['books','manage',book.id])
  }
}
