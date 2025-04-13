package com.src.proserv.main.services;

import java.io.InputStream;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.persistence.Id;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.src.proserv.main.model.State;
import com.src.proserv.main.repository.StateRepository;

@Service
public class ExcelImportService {

	@Autowired
	private ApplicationContext applicationContext;

	private final Map<String, Class<?>> entityMap = Map.of("stateinfo", State.class);

	private final Map<String, Class<? extends JpaRepository<?, ?>>> repositoryMap = Map.of("stateinfo",
			StateRepository.class);

	public void importExcel(String userUUID,MultipartFile file, String tableName, boolean override) throws Exception {

		Class<?> entityClass = entityMap.get(tableName.toLowerCase());
		Class<? extends JpaRepository<?, ?>> repoClass = repositoryMap.get(tableName.toLowerCase());

		if (entityClass == null || repoClass == null) {
			throw new IllegalArgumentException("Unsupported table name: " + tableName);
		}
		List<String> failedLogs = new ArrayList<>();

		List<Object> entities = parseExcelRows(file, entityClass);

		JpaRepository repository = applicationContext.getBean(repoClass);

		if (override) {
			repository.deleteAllInBatch();
			entities.forEach(c->this.injectAuditColumns(c,userUUID));
			repository.saveAll(entities);
		} else {
			entities.forEach(entity -> {
				injectLastModified(entity,userUUID);
			});
			for (int i = 0; i < entities.size(); i++) {
				Object entity = entities.get(i);
				try {
					repository.save(entity); // save one by one
				} catch (Exception e) {
					failedLogs.add("Row " + (i + 2) + " failed: " + e.getMessage());
				}
			}
		}
		if (!failedLogs.isEmpty()) {
			throw new RuntimeException("Partial failure:\n" + String.join("\n", failedLogs));
		}
	}

	private List<Object> parseExcelRows(MultipartFile file, Class<?> entityClass) throws Exception {
		List<Object> entityList = new ArrayList<>();

		try (InputStream is = file.getInputStream(); Workbook workbook = new XSSFWorkbook(is)) {

			Sheet sheet = workbook.getSheetAt(0);

			List<Field> fields = new ArrayList<>();
			fields.addAll(List.of(entityClass.getDeclaredFields()));
			if (entityClass.getSuperclass() != null) {
				fields.addAll(List.of(entityClass.getSuperclass().getDeclaredFields()));
			}

			List<Field> mappedFields = fields.stream()
					.filter(f -> !Modifier.isStatic(f.getModifiers()) && !f.isAnnotationPresent(Id.class)).toList();

			for (int i = 1; i <= sheet.getLastRowNum(); i++) { // skip header
				Row row = sheet.getRow(i);
				if (row == null)
					continue;

				Object instance = entityClass.getDeclaredConstructor().newInstance();

				for (int j = 0; j < mappedFields.size() && j < row.getPhysicalNumberOfCells(); j++) {
					Field field = mappedFields.get(j);
					field.setAccessible(true);
					Cell cell = row.getCell(j);
					Object value = getCellValueAsType(cell, field.getType());
					field.set(instance, value);
				}

				entityList.add(instance);
			}
		}

		return entityList;
	}

	private void injectLastModified(Object instance,String userUUID) {
		LocalDateTime now = LocalDateTime.now();

		try {
			for (Field field : instance.getClass().getSuperclass().getDeclaredFields()) {
				field.setAccessible(true);
				if ("lastModifiedBy".equals(field.getName())) {
					field.set(instance, userUUID);
				} else if ("lastModifiedOn".equals(field.getName())) {
					field.set(instance, now);
				}
			}
		} catch (IllegalAccessException e) {
			throw new RuntimeException("Failed to set last modified fields");
		}
	}

	private Object getCellValueAsType(Cell cell, Class<?> type) {
	    if (cell == null) return null;

	    if (cell.getCellType() == CellType.STRING) {
	        String text = cell.getStringCellValue();
	        return switch (type.getSimpleName()) {
	            case "String" -> text;
	            case "LocalDate" -> LocalDate.parse(text);
	            case "LocalDateTime" -> LocalDateTime.parse(text);
	            case "Integer" -> Integer.parseInt(text);
	            case "Long" -> Long.parseLong(text);
	            case "Double" -> Double.parseDouble(text);
	            case "Boolean" -> Boolean.parseBoolean(text);
	            default -> null;
	        };
	    }

	    return switch (type.getSimpleName()) {
	        case "String" -> cell.getStringCellValue();
	        case "Integer" -> (int) cell.getNumericCellValue();
	        case "Long" -> (long) cell.getNumericCellValue();
	        case "Double" -> cell.getNumericCellValue();
	        case "Boolean" -> cell.getBooleanCellValue();
	        case "LocalDate" -> cell.getLocalDateTimeCellValue().toLocalDate();
	        case "LocalDateTime" -> cell.getLocalDateTimeCellValue();
	        default -> null;
	    };
	}

	private void injectAuditColumns(Object instance, String userUUID) {
		LocalDateTime now = LocalDateTime.now();
		try {
			for (Field field : instance.getClass().getSuperclass().getDeclaredFields()) {
				field.setAccessible(true);
				switch (field.getName()) {
				case "createdBy", "lastModifiedBy" -> field.set(instance, userUUID);
				case "createdOn", "lastModifiedOn" -> field.set(instance, now);
				}
			}
		} catch (IllegalAccessException e) {
			throw new RuntimeException("Failed to set audit fields: " + e.getMessage());
		}
	}
}
