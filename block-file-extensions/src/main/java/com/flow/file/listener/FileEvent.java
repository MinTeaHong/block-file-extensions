package com.flow.file.listener;

import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.event.ApplicationStartedEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.data.domain.Sort;

import com.flow.file.domain.CustomExtensions;
import com.flow.file.domain.FixedExtensions;
import com.flow.file.repository.CustomExtensionsRepository;
import com.flow.file.repository.FixedExtensionsRepository;

@Configuration
@PropertySource("classpath:start.properties")
public class FileEvent implements  ApplicationListener<ApplicationStartedEvent> {

	private final String fixedExtensionsName;
	private final boolean fixedExtensionsCheck;
	private final FixedExtensionsRepository rixedExtensionsRepository;
	private final CustomExtensionsRepository customExtensionsRepository;
	
	@Autowired
	public FileEvent( FixedExtensionsRepository rixedExtensionsRepository 
			, CustomExtensionsRepository customExtensionsRepository
			, @Value("${fixedExtensionsName}") String fixedExtensionsName
			, @Value("${fixedExtensionsCheck}") boolean fixedExtensionsCheck) {
		this.rixedExtensionsRepository 		= rixedExtensionsRepository;
		this.customExtensionsRepository 	= customExtensionsRepository;
		this.fixedExtensionsName 			= fixedExtensionsName;
		this.fixedExtensionsCheck 			= fixedExtensionsCheck;
	}
	
	@Override
	@Transactional
	public void onApplicationEvent(ApplicationStartedEvent event) {
		startFixedExtensionsInit();
		startCustomExtensionsInit();
	}
	
	private void startFixedExtensionsInit() {
		List<String> nameList =  Arrays.asList( fixedExtensionsName.split(",") ) ;
		rixedExtensionsRepository.deleteNotInInit( nameList );
		
		//조회
		List< FixedExtensions > fixedExtensionsList  = rixedExtensionsRepository.findAll(  Sort.by(Sort.Direction.ASC, "id")  );
		
		Set<String> dbSet = fixedExtensionsList.stream()
			.map(x -> {   
				FixedExtensionsRepository.fixedExtensionsList.add( x );
				return x.getName();
			}).collect( Collectors.toSet() );
		
		//없는 값 넣기.
		for (String name : nameList) {
			if (!dbSet.contains(name)) {
				FixedExtensions fe = new FixedExtensions( name , fixedExtensionsCheck ) ;
				rixedExtensionsRepository.save(fe);
				FixedExtensionsRepository.fixedExtensionsList.add( fe );
			}
		}
	}
	
	
	private void startCustomExtensionsInit() {
//		for (int i = 1; i <= 200; i++) {
//			CustomExtensions startTest = new CustomExtensions("muze"+i);
//			customExtensionsRepository.save(startTest);
//		}
		List< CustomExtensions > customsExtensionsList  = customExtensionsRepository.findAll( Sort.by(Sort.Direction.ASC, "id") );
		for (CustomExtensions customExtensions : customsExtensionsList) {
			CustomExtensionsRepository.customExtensions.put( customExtensions.getName() ,  customExtensions );  
		}
		
	}
}
