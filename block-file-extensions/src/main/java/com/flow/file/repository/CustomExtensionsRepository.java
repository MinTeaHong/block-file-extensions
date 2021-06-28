package com.flow.file.repository;

import java.util.LinkedHashMap;
import java.util.Map;
import org.springframework.data.jpa.repository.JpaRepository;
import com.flow.file.domain.CustomExtensions;

public interface CustomExtensionsRepository extends JpaRepository<CustomExtensions, Long> {
	
	Map<String, CustomExtensions> customExtensions = new LinkedHashMap<>();
	
}
