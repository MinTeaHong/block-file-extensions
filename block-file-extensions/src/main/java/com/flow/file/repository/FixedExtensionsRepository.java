package com.flow.file.repository;

import java.util.ArrayList;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import com.flow.file.domain.FixedExtensions;

public interface FixedExtensionsRepository extends JpaRepository<FixedExtensions, Integer> {
	
	List<FixedExtensions> fixedExtensionsList = new ArrayList<>() ;
	
	@Modifying
	@Query("delete from FixedExtensions f where f.name not in :name")
	void deleteNotInInit(@Param("name") List<String> name);

	
}
