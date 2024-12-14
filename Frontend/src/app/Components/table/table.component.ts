import { Component, OnInit } from '@angular/core';
import { TableService } from '../../Services/table.service';

@Component({
  selector: 'app-table',
  templateUrl: './table.component.html',
  styleUrls: ['./table.component.css']
})
export class TableComponent implements OnInit {
  customers: any[] = [];
  currentPage: number = 0;
  totalPages: number = 0;
  pageNumbers: number[] = [];
  pageSize: number = 100; // Adjust this based on how many customers per page
  sortOrder: string = 'desc';
  vehicleType: string = 'car'; // Default sort order is descending
  searchQuery: string = ''; // To store the search query

  constructor(private tableService: TableService) {}

  ngOnInit(): void {
    this.loadCustomers();
  }

  // Fetch customers for a specific page with filtering and sorting
  loadCustomers(page: number = 0): void {
    this.tableService.getCustomers(page, this.pageSize, this.sortOrder, this.vehicleType, this.searchQuery).subscribe(data => {
      this.customers = data.content;
      this.currentPage = data.number;
      this.totalPages = data.totalPages;
      this.setPageNumbers();
    });
  }

  // Change to the selected page
  changePage(page: number): void {
    if (page >= 0 && page < this.totalPages) {
      this.loadCustomers(page);
    }
  }

  // Logic for displaying the page numbers
  setPageNumbers(): void {
    const pages = 5; // Number of page buttons to display
    const half = Math.floor(pages / 2);
    let start = this.currentPage - half;
    let end = this.currentPage + half;

    // Adjust for edge cases where page numbers are close to the beginning or end
    if (start < 0) {
      start = 0;
      end = Math.min(pages - 1, this.totalPages - 1);
    }

    if (end >= this.totalPages) {
      end = this.totalPages - 1;
      start = Math.max(this.totalPages - pages, 0);
    }

    // Set the array of page numbers to display
    this.pageNumbers = [];
    for (let i = start; i <= end; i++) {
      this.pageNumbers.push(i);
    }
  }

  // Quick jump to the first page
  goToFirstPage(): void {
    this.loadCustomers(0);
  }

  // Quick jump to the last page
  goToLastPage(): void {
    this.loadCustomers(this.totalPages - 1);
  }

  // Handle sorting change from dropdown
  onSortChange(event: any): void {
    this.sortOrder = event.target.value;
    this.loadCustomers();  // Reload customers with the new sort order
  }

  // Handle vehicle type filter change
  onVehicleTypeChange(event: any): void {
    this.vehicleType = event.target.value;
    this.loadCustomers();  // Reload customers with the selected vehicle type
  }

  // Handle search input change
  onSearchChange(): void {
    this.loadCustomers(); // Reload customers with the search query applied
  }
}
