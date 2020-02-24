package com.mindaugas.ledger;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.util.NestedServletException;

@WebMvcTest(TransactionController.class)
class TransactionControllerTests {

	@Autowired
	private MockMvc mockMvc;

	@MockBean
	private TransactionRepository repository;

	private Date dateFromString(String dateString) throws Exception {
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
		return formatter.parse(dateString);
	}

	@Test
	void rootShouldReturnNotFound() throws Exception {
		this.mockMvc.perform(get("/")).andDo(print()).andExpect(status().isNotFound());
	}

	@Test
	void transactionImportShouldSaveTransactionsToRepository() throws Exception {
		String fileString = "accountNumber,date,beneficiary,comment,amount,currency\nLT12312,2020-02-20 02:12:54.934,LT1231,transfer,2.12,LTL";
		MockMultipartFile file = new MockMultipartFile("file", "orig", null, fileString.getBytes());

		Transaction expectedTransaction = new Transaction("LT12312", dateFromString("2020-02-20 02:12:54.934"), "LT1231", "transfer", new BigDecimal("2.12"), "LTL");

		this.mockMvc.perform(multipart("/transactions/import").file(file))
			.andExpect(status().isOk());
		Mockito.verify(repository).save(expectedTransaction);

	}

	@Test
	void transactionImportWithEmptyCommentShouldSaveTransactionsToRepository() throws Exception {
		String fileString = "accountNumber,date,beneficiary,comment,amount,currency\nLT12312,2020-02-20 02:12:54.934,LT1231,,2.00,LTL";
		MockMultipartFile file = new MockMultipartFile("file", "orig", null, fileString.getBytes());

		this.mockMvc.perform(multipart("/transactions/import").file(file))
			.andExpect(status().isOk());
		Mockito.verify(repository).save(any());
	}

	@Test
	void transactionImportWithThreeRowsShouldSaveThreeTransactions() throws Exception {
		String fileString = "accountNumber,date,beneficiary,comment,amount,currency\nLT12312,2020-02-20 02:12:54.934,LT1231,comment,2.00,LTL\nLT12312,2020-02-20 02:12:54.934,LT1231,comment,2.00,LTL\nLT12312,2020-02-20 02:12:54.934,LT1231,comment,2.00,LTL";
		MockMultipartFile file = new MockMultipartFile("file", "orig", null, fileString.getBytes());

		this.mockMvc.perform(multipart("/transactions/import").file(file))
			.andExpect(status().isOk());
		Mockito.verify(repository, times(3)).save(any());
	}

	@Test
	void transactionImportWithNotEnoughCollumnsShouldThrowException() throws Exception {
		String fileString = "accountNumber,date,beneficiary,comment,amount,currency\nLT12312,2020-02-20 02:12:54.934,LT1231,,2.00";
		MockMultipartFile file = new MockMultipartFile("file", "orig", null, fileString.getBytes());

		assertThrows(NestedServletException.class, () -> {
			this.mockMvc.perform(multipart("/transactions/import").file(file));
		});
	}

	@Test
	void transactionExportShouldReturnDataFromDatabase() throws Exception {
		String expectedFileString = "accountNumber,date,beneficiary,comment,amount,currency\r\nLT12312,2020-02-20 02:12:54.934,LT1231,,2.00,LTL\r\n";

		Transaction expectedTransaction = new Transaction("LT12312", dateFromString("2020-02-20 02:12:54.934"), "LT1231", "", new BigDecimal("2.00"), "LTL");
		List<Transaction> expectedTransactions = new ArrayList<Transaction>();
		Collections.addAll(expectedTransactions, expectedTransaction);
		when(repository.findAll()).thenReturn(expectedTransactions);

		this.mockMvc.perform(get("/transactions/export"))
					.andExpect(status().isOk())
					.andExpect(content().contentType("text/csv"))
					.andExpect(content().string(expectedFileString));
	}
}
