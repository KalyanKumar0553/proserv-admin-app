package com.src.proserv.main.services;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import javax.persistence.Id;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import com.src.proserv.main.model.ServiceCategory;
import com.src.proserv.main.repository.ServiceCategoryRepository;

@Service
public class CsvDataLoaderService {

	private final Map<String, Class<?>> entityMap = Map.of("service_category", ServiceCategory.class);

	private final Map<String, Class<? extends JpaRepository<?, ?>>> repositoryMap = Map.of("service_category",
			ServiceCategoryRepository.class);

	@Autowired
	private ApplicationContext applicationContext;
	
    public void loadAllCsvFiles() {
    	try {
            PathMatchingResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
            Resource[] resources = resolver.getResources("classpath:data/*.csv");

            for (Resource resource : resources) {
                String filename = Objects.requireNonNull(resource.getFilename());
                String key = filename.substring(0, filename.lastIndexOf('.')).toLowerCase();

                if (!repositoryMap.containsKey(key) || !entityMap.containsKey(key)) {
                    System.out.println("No repo/class mapping for file: " + filename);
                    continue;
                }
                
                List<Object> entities = parseCsvToEntities(resource.getInputStream(), entityMap.get(key));
                if (!entities.isEmpty()) {
                	Class<? extends JpaRepository<?, ?>> repoClass = repositoryMap.get(key);
                	JpaRepository repository = applicationContext.getBean(repoClass);
                	repository.deleteAllInBatch();
        			entities.forEach(c->this.injectAuditColumns(c,"SYSTEM"));
        			repository.saveAll(entities);
                    System.out.println("Loaded " + entities.size() + " records into table: " + key);
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Error loading CSV files", e);
        }
    }


    private List<Object> parseCsvToEntities(InputStream inputStream, Class<?> entityClass) throws Exception {
        List<Object> entityList = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream))) {
            String headerLine = reader.readLine(); // skip header
            if (headerLine == null) return entityList;

            // Get declared fields including superclass fields
            List<Field> fields = new ArrayList<>(List.of(entityClass.getDeclaredFields()));
            if (entityClass.getSuperclass() != null) {
                fields.addAll(List.of(entityClass.getSuperclass().getDeclaredFields()));
            }

            List<Field> mappedFields = fields.stream()
                    .filter(f -> !Modifier.isStatic(f.getModifiers()) && !f.isAnnotationPresent(Id.class))
                    .toList();

            String line;
            while ((line = reader.readLine()) != null) {
                String[] values = line.split(",");

                Object instance = entityClass.getDeclaredConstructor().newInstance();

                for (int i = 0; i < mappedFields.size() && i < values.length; i++) {
                    Field field = mappedFields.get(i);
                    field.setAccessible(true);
                    Object typedValue = convertStringToFieldType(values[i].trim(), field.getType());
                    field.set(instance, typedValue);
                }

                entityList.add(instance);
            }
        }

        return entityList;
    }
    
    private Object convertStringToFieldType(String value, Class<?> fieldType) {
        if (fieldType == String.class) return value;
        if (fieldType == int.class || fieldType == Integer.class) return Integer.parseInt(value);
        if (fieldType == long.class || fieldType == Long.class) return Long.parseLong(value);
        if (fieldType == double.class || fieldType == Double.class) return Double.parseDouble(value);
        if (fieldType == boolean.class || fieldType == Boolean.class) return Boolean.parseBoolean(value);
        if (fieldType == LocalDate.class) return LocalDate.parse(value);
        if (fieldType == LocalDateTime.class) return LocalDateTime.parse(value);
        return null;
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
