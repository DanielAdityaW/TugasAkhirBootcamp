from flask import Flask, request, jsonify, send_file
from flask_cors import CORS
import pickle
import pandas as pd
import numpy as np
from sklearn.preprocessing import StandardScaler
from sklearn.decomposition import PCA
from sklearn.cluster import KMeans
import io

app = Flask(__name__)
CORS(app, origins=["http://localhost:4200"])

# Load trained models and transformers
with open('kmeans_model.pkl', 'rb') as f:
    kmeans = pickle.load(f)
with open('scaler_model.pkl', 'rb') as f:
    scaler = pickle.load(f)
with open('pca_model.pkl', 'rb') as f:
    pca = pickle.load(f)

# Define risk mapping
risk_mapping = {
    0: ('Low Risk', 0.1, 0.4),
    1: ('Medium Risk', 0.4, 0.7),
    2: ('High Risk', 0.7, 1.0)
}

def process_data(df):
    # Drop 'Vehicle Type' since it is no longer used
    df = df.drop(['Vehicle Type'], axis=1)

    # Reorder columns to match model expectations, now include 'Payment History'
    relevant_features = ['Credit Score', 'Tenure', 'Age', 'Balance', 'Installment Amount', 'Payment History']
    df = df[relevant_features]

    # Fill missing values for numerical features
    numeric_columns = df.select_dtypes(include=[np.number]).columns
    df[numeric_columns] = df[numeric_columns].fillna(df[numeric_columns].mean())

    # Scale data
    df_scaled = scaler.transform(df)

    # Apply PCA transformation
    df_pca = pca.transform(df_scaled)

    # Predict clusters
    cluster_labels = kmeans.predict(df_pca)

    # Map clusters to risk categories and scores
    predicted_risk = []
    predicted_risk_score = []
    for label in cluster_labels:
        if label in risk_mapping:
            risk_category, lower_bound, upper_bound = risk_mapping[label]
            # Generate risk score and round to 3 decimal places
            risk_score = np.random.uniform(lower_bound, upper_bound)
            predicted_risk.append(risk_category)
            predicted_risk_score.append(round(risk_score, 5))  # Round to 3 decimal places
        else:
            predicted_risk.append("Unknown")
            predicted_risk_score.append(None)

    # Add predicted columns
    df['Risk Cluster'] = predicted_risk
    df['Risk Score'] = predicted_risk_score
    return df

@app.route('/predict', methods=['POST'])
def predict_json():
    try:
        # Receive JSON data
        data = request.json
        if not isinstance(data, list):
            return jsonify({"error": "Invalid input format, expected a list of records"}), 400

        df = pd.DataFrame(data)

        # Verify required columns
        required_columns = ['Vehicle Type', 'Payment History', 'Credit Score', 'Tenure', 'Age', 'Balance', 'Installment Amount']
        for col in required_columns:
            if col not in df.columns:
                return jsonify({"error": f"Missing required column: {col}"}), 400

        # Process data and get predictions
        result_df = process_data(df)

        # Prepare response
        response = {
            'predictedRisk': result_df['Risk Cluster'].tolist(),
            'predictedRiskScore': result_df['Risk Score'].tolist()
        }
        return jsonify(response)

    except Exception as e:
        return jsonify({"error": f"Internal server error: {str(e)}"}), 500

@app.route('/predict-excel', methods=['POST'])
def predict_excel():
    try:
        # Receive the uploaded Excel file
        file = request.files['file']
        df = pd.read_excel(file)

        # Ensure required columns are in the DataFrame
        required_columns = ['Customer ID', 'Credit Score', 'Age', 'Tenure', 'Balance', 'Vehicle Type', 'Installment Amount', 'Payment History']
        for col in required_columns:
            if col not in df.columns:
                return jsonify({"error": f"Missing required column: {col}"}), 400

        # Save 'Customer ID' and 'Vehicle Type' separately to ensure they are not dropped during processing
        customer_id_column = df['Customer ID']
        vehicle_type_column = df['Vehicle Type']

        # Process data and get predictions
        result_df = process_data(df)

        # Ensure 'Customer ID' and 'Vehicle Type' are added back after processing
        result_df.insert(0, 'Customer ID', customer_id_column)
        result_df['Vehicle Type'] = vehicle_type_column  # Add 'Vehicle Type' back at the end

        # Now, 'Risk Cluster' and 'Risk Score' should be present in the result DataFrame
        # Reorder columns to ensure 'Customer ID' is first
        result_df = result_df[['Customer ID', 'Credit Score', 'Age', 'Tenure', 'Balance', 'Vehicle Type', 
                               'Installment Amount', 'Payment History', 'Risk Score', 'Risk Cluster']]

        # Save the final results to an in-memory Excel file
        output = io.BytesIO()
        with pd.ExcelWriter(output, engine='xlsxwriter') as writer:
            result_df.to_excel(writer, index=False)
        output.seek(0)

        # Return the updated Excel file
        return send_file(output, as_attachment=True, download_name="predicted_results.xlsx", mimetype='application/vnd.openxmlformats-officedocument.spreadsheetml.sheet')

    except Exception as e:
        return jsonify({"error": f"Internal server error: {str(e)}"}), 500


@app.route('/manual-predict', methods=['POST'])
def manual_predict():
    try:
        # Extract data from JSON request body
        data = request.json
        # Verify that required keys are present in the JSON input
        required_fields = ['Credit Score', 'Age', 'Tenure', 'Balance', 'Vehicle Type', 'Installment Amount', 'Payment History']
        for field in required_fields:
            if field not in data:
                return jsonify({"error": f"Missing required field: {field}"}), 400

        # Create a DataFrame for a single entry
        df = pd.DataFrame([data])

        # Process the data and get predictions
        result_df = process_data(df)

        # Prepare the response
        response = {
            'predictedRisk': result_df['Risk Cluster'].iloc[0],
            'predictedRiskScore': result_df['Risk Score'].iloc[0]
        }
        return jsonify(response)

    except Exception as e:
        return jsonify({"error": f"Internal server error: {str(e)}"}), 500

if __name__ == '__main__':
    app.run(debug=True, port=5000)
