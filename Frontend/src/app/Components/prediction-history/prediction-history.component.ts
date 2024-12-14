import { Component, OnInit } from '@angular/core';
import { PredictionHistoryService } from '../../Services/prediction-history.service';

@Component({
  selector: 'app-prediction-history',
  templateUrl: './prediction-history.component.html',
  styleUrl: './prediction-history.component.css'
})
export class PredictionHistoryComponent implements OnInit {
  historyLogs: any[] = [];

  constructor(private historyService: PredictionHistoryService) {}

  ngOnInit(): void {
    this.getPredictionHistory();
  }

  getPredictionHistory(): void {
    this.historyService.getPredictionHistory().subscribe(
      (data) => {
        this.historyLogs = data; // Menyimpan data dari API
      },
      (error) => {
        console.error('Error fetching prediction history:', error);
      }
    );
  }
}

