import { Component } from '@angular/core';
import { InputManualService } from '../../Services/input-manual.service';

@Component({
  selector: 'app-input-manual',
  templateUrl: './input-manual.component.html',
  styleUrls: ['./input-manual.component.css']
})
export class InputManualComponent {
  inputData = {
    "Credit Score": null,
    "Age": null,
    "Tenure": null,
    "Balance": null,
    "Estimated Salary": null,
    "Vehicle Type": '',
    "Installment Amount": null,
    "Payment History": null
  };
  result: any;
  errorMessage: string | null = null;

  constructor(private inputManualService: InputManualService) {}

  onSubmit() {
    this.inputManualService.predictManual(this.inputData).subscribe(
      response => {
        this.result = response;
        this.errorMessage = null;
      },
      error => {
        this.errorMessage = 'Failed to get prediction. Please try again.';
        console.error(error);
      }
    );
  }

  // Function to determine the appropriate class based on predictedRisk
  getRiskClass(risk: string): string {
    if (risk === 'Low Risk') {
      return 'low-risk';
    } else if (risk === 'Medium Risk') {
      return 'medium-risk';
    } else if (risk === 'High Risk') {
      return 'high-risk';
    }
    return ''; // Default class if no match
  }
}
