package com.flow.file.service;

import java.util.ArrayList;
import java.util.regex.Pattern;
import javax.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import com.flow.file.ApiMessage;
import com.flow.file.domain.CustomExtensions;
import com.flow.file.repository.CustomExtensionsRepository;
import com.flow.file.repository.FixedExtensionsRepository;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

@Service
public class FileService {
	
	private final FixedExtensionsRepository 	rixedExtensionsRepository;
	private final CustomExtensionsRepository 	customExtensionsRepository;
	private final String CECHK_CUSTOM_PATTERN 	= "^[a-zA-Z0-9]*$";
	private final String WRONG_MSG 				= "wrong request";
	private final String FIXED_CHANGE_FLAG 		= "change";
	private final String CUSTOM_FULL_SIZE 		= "full size already";
	private final String CUSTOM_ITEM_EXIST 		= "already exist";
	private final String CUSTOM_ITEM_NOT_EXIST 	= "not exist";
	private final String CUSTOM_ITEM_ADD 		= "added";
	private final String CUSTOM_ITEM_REMOVE 	= "remove";
	
	
	
	private final int CUSTOM_EXTENSIONS_MAX_SIZE;
	private final int CUSTOM_EXTENSIONS_MAX_LENGTH;
	
	@Autowired
	public FileService( FixedExtensionsRepository rixedExtensionsRepository 
			, CustomExtensionsRepository customExtensionsRepository
			, @Value("${customExtensionsMaxSize}") int customExtensionsMaxSize
			, @Value("${customExtensionsMaxLength}") int customExtensionsMaxLength
			) {
		this.rixedExtensionsRepository 		= rixedExtensionsRepository;
		this.customExtensionsRepository 	= customExtensionsRepository;
		this.CUSTOM_EXTENSIONS_MAX_SIZE 	= customExtensionsMaxSize;
		this.CUSTOM_EXTENSIONS_MAX_LENGTH 	= customExtensionsMaxLength;
	}
	
	private final Object 		fixedExtensionsKey 		= new Object();
	private final Object 		customExtensionsKey 	= new Object();
	private final Gson 			gson 					= new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create();
	
	public int getCustomExtensionsMax() {
		return CUSTOM_EXTENSIONS_MAX_SIZE;
	}
	
	@Transactional
	public ResponseEntity<ApiMessage> changeFixedExtensionsFlagById( String name , boolean use_yn ) {
		
		synchronized (fixedExtensionsKey) {
			FixedExtensionsRepository.fixedExtensionsList.stream()
					.filter(s -> s.getName().equals(name))
					.findAny().ifPresent( x  -> {
						rixedExtensionsRepository.findById(   x.getId()  ).get().changeUse_yn(use_yn);
						x.changeUse_yn(use_yn);
					});
			
			return new ResponseEntity<>(new ApiMessage( 
					FIXED_CHANGE_FLAG
					, gson.toJson( FixedExtensionsRepository.fixedExtensionsList  )
				)
				, HttpStatus.OK);
			
		}
	}
	
	@Transactional
	public ResponseEntity<ApiMessage> addCustomExtensions( String name ) {
		
		if ( !checkCustomExtensions(name) ) return new ResponseEntity<>(new ApiMessage(WRONG_MSG), HttpStatus.BAD_REQUEST);
		
		String message;
		HttpStatus httpStatus;
		
		synchronized ( customExtensionsKey ) {
			
			if ( CustomExtensionsRepository.customExtensions.size() == CUSTOM_EXTENSIONS_MAX_SIZE ) {
				message 		= CUSTOM_FULL_SIZE;
				httpStatus 		= HttpStatus.PAYLOAD_TOO_LARGE;
			} else if ( CustomExtensionsRepository.customExtensions.containsKey( name ) ) {
				message 		= name + " : " + CUSTOM_ITEM_EXIST;
				httpStatus 		= HttpStatus.OK;
			} else {
				CustomExtensions customExtensions = new CustomExtensions( name );
				CustomExtensionsRepository.customExtensions.put( name , customExtensions );
				customExtensionsRepository.save(customExtensions);
				message 		= name + " : " + CUSTOM_ITEM_ADD;
				httpStatus 		= HttpStatus.OK;
			}
			
			return new ResponseEntity<>(new ApiMessage( 
						message 
						, gson.toJson(new ArrayList<CustomExtensions>(  CustomExtensionsRepository.customExtensions.values() ) )
					)
					, httpStatus);
			
		}
	}
	
	@Transactional
	public ResponseEntity<ApiMessage> removeCustomExtensions( String name ) {
		
		String message;
		HttpStatus httpStatus;
		
		synchronized ( customExtensionsKey ) {
			if ( CustomExtensionsRepository.customExtensions.containsKey( name ) ) {
				CustomExtensions customExtensions = CustomExtensionsRepository.customExtensions.remove( name );
				customExtensionsRepository.deleteById( customExtensions.getId() );
				message 		= name + " : " + CUSTOM_ITEM_REMOVE ;
				httpStatus 		= HttpStatus.OK;
			} else {
				message 		= name + " : " + CUSTOM_ITEM_NOT_EXIST ;
				httpStatus 		= HttpStatus.OK;
			}
			return new ResponseEntity<>(new ApiMessage( 
					message
					, gson.toJson(new ArrayList<CustomExtensions>(  CustomExtensionsRepository.customExtensions.values() ) )
				)
				, httpStatus);
			
		}
		
	}
	
	public boolean checkCustomExtensions( String name ) {
		if ( name.length() <= CUSTOM_EXTENSIONS_MAX_LENGTH && Pattern.matches( CECHK_CUSTOM_PATTERN , name)  ) return true;
		else return false;
	}
	
}
