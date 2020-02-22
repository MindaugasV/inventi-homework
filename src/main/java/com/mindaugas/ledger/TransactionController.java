package com.mindaugas.ledger;

import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.supercsv.cellprocessor.ift.CellProcessor;
import org.supercsv.io.CsvListWriter;
import org.supercsv.io.CsvMapReader;
import org.supercsv.io.ICsvListWriter;
import org.supercsv.io.ICsvMapReader;
import org.supercsv.prefs.CsvPreference;

@RestController
class TransactionController {

    private final TransactionRepository repository;

    TransactionController(TransactionRepository repository) {
        this.repository = repository;
    }

    @GetMapping("/transactions")
    List<Transaction> all() {
        return repository.findAll();
    }

    @PostMapping("/transactions")
    Transaction newTransaction(@RequestBody Transaction newTransaction) {
        return repository.save(newTransaction);
    }

    @GetMapping("/transactions/export")
    public void exportTransactions(HttpServletResponse response) throws IOException {

        List<Transaction> listOfTransactions = repository.findAll();

        response.setContentType("text/csv");
        // Configure headers
        String csvFileName = "report.csv"; 
        String headerKey = "Content-Disposition";
        String headerValue = String.format("attachment; filename=\"%s\"",
                csvFileName);
        response.setHeader(headerKey, headerValue);
 
        // uses the Super CSV API to generate CSV data from the model data
        ICsvListWriter csvWriter = new CsvListWriter(response.getWriter(),
                CsvPreference.STANDARD_PREFERENCE);

        String[] header = Transaction.csvHeaderAnnotation(); 
        csvWriter.writeHeader(header);
        for (Transaction aTransaction : listOfTransactions) {
            csvWriter.write(aTransaction.csvParams());
        }
        csvWriter.close();
    }
    
    @PostMapping("/transactions/import")
    public void importTransactions(@RequestParam("file") MultipartFile file, RedirectAttributes redirectAttributes) throws IOException {

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
        
        redirectAttributes.addFlashAttribute("message", 
                "You successfully uploaded " + file.getOriginalFilename() + "!");
    }
}
