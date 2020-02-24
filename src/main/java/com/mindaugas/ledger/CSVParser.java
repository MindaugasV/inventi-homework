package com.mindaugas.ledger;

import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.springframework.web.multipart.MultipartFile;
import org.supercsv.cellprocessor.ift.CellProcessor;
import org.supercsv.io.CsvListWriter;
import org.supercsv.io.CsvMapReader;
import org.supercsv.io.ICsvListWriter;
import org.supercsv.io.ICsvMapReader;
import org.supercsv.prefs.CsvPreference;

class CSVParser {
    public static void writeTransactionsToResponseAsCSVFile(HttpServletResponse response, List<Transaction> listOfTransactions) throws IOException {
        // uses the Super CSV API to generate CSV data from the model data
        // Chose to us ListWriter, Because BeanWriter was catching exception of Illegal access.
        // It happend because Transaction property was accessed from diferent package and it was considered illegal.
        // Therefore here sticking with ListWriter
        ICsvListWriter csvWriter = new CsvListWriter(response.getWriter(),
                CsvPreference.STANDARD_PREFERENCE);

        String[] header = Transaction.csvHeaderAnnotation(); 
        csvWriter.writeHeader(header);
        for (Transaction aTransaction : listOfTransactions) {
            csvWriter.write(aTransaction.csvParams());
        }
        csvWriter.close();
    }

    public static void parseTransactionsCSVFile(MultipartFile file, TransactionRepository repository) throws IOException {
        ICsvMapReader listReader = new CsvMapReader(new InputStreamReader(file.getInputStream()), CsvPreference.STANDARD_PREFERENCE);      

        //First Column is header names
        final String[] headers = listReader.getHeader(true);
        final CellProcessor[] processors = Transaction.getProcessors();

        Map<String, Object> fieldsInCurrentRow;
        while ((fieldsInCurrentRow = listReader.read(headers, processors)) != null) {
            Transaction newTransaction = new Transaction(fieldsInCurrentRow);
            repository.save(newTransaction);
        }
        listReader.close();
    }
}

