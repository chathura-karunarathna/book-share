import {Component, EventEmitter, Input, Output} from '@angular/core';
import {BookResponseDto} from "../../../../services/models/book-response-dto";

@Component({
  selector: 'app-book-card',
  templateUrl: './book-card.component.html',
  styleUrl: './book-card.component.scss'
})
export class BookCardComponent {
  private _book: BookResponseDto = {};
  private _manage:boolean = false;
  private _bookCover: string | undefined;

  get book(): BookResponseDto {
    return this._book;
  }

  @Input()
  set book(value: BookResponseDto) {
    this._book = value;
  }

  get bookCover(): string | undefined {
    if(this._book.cover){
      return 'data:image/jpg;base64,'+ this._book.cover
    }
    return 'https://picsum.photos/1900/800';
  }

  @Input()
  set manage(value: boolean) {
    this._manage = value;
  }

  get manage(): boolean {
    return this._manage;
  }

  @Output() private share: EventEmitter<BookResponseDto> = new EventEmitter<BookResponseDto>();
  @Output() private archive: EventEmitter<BookResponseDto> = new EventEmitter<BookResponseDto>();
  @Output() private addToWaitingList: EventEmitter<BookResponseDto> = new EventEmitter<BookResponseDto>();
  @Output() private borrow: EventEmitter<BookResponseDto> = new EventEmitter<BookResponseDto>();
  @Output() private edit: EventEmitter<BookResponseDto> = new EventEmitter<BookResponseDto>();
  @Output() private details: EventEmitter<BookResponseDto> = new EventEmitter<BookResponseDto>();

  onshowDetails() {
    this.details.emit(this.book);
  }

  onBorrow() {
    this.borrow.emit(this.book);
  }

  onAddToWaitingList() {
    this.addToWaitingList.emit(this.book);
  }

  onEdit() {
    this.edit.emit(this.book);
  }

  onShare() {
    this.share.emit(this.book);
  }

  onArchive() {
    this.archive.emit(this.book);
  }
}
