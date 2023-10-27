package com.fdmgroup.MockExam.model;

import lombok.Data;

@Data
public class DataBaseInformation {
	 private final String url = "jdbc:h2:file:~/Documents/h2/mock_exam_v2";
	 private final String user = "sa";
	 private final String password = "";
}
