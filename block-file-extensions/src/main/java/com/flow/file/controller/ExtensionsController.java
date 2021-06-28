package com.flow.file.controller;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import java.util.ArrayList;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import com.flow.file.ApiMessage;
import com.flow.file.domain.CustomExtensions;
import com.flow.file.repository.CustomExtensionsRepository;
import com.flow.file.repository.FixedExtensionsRepository;
import com.flow.file.service.FileService;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

@Controller
@RequestMapping("extensions")
public class ExtensionsController {

	private final FileService fileService;
	
	@Autowired
	public ExtensionsController(FileService fileService) {
		this.fileService = fileService;
	}
	
	@GetMapping("setting")
	public String showMain(Model model) {
		
		Gson gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create();
		model.addAttribute("fixedExtensions", gson.toJson( FixedExtensionsRepository.fixedExtensionsList  ) );
		model.addAttribute("customExtensions", gson.toJson( new ArrayList<CustomExtensions>(  CustomExtensionsRepository.customExtensions.values() ) ) );
		model.addAttribute("customExtensionsMax", fileService.getCustomExtensionsMax()  );
		
		return "main";
		
	}
	
	@PatchMapping("fixedExtensions/{name}")
	@ResponseBody
	public ResponseEntity<ApiMessage> patchFixedExtensions( @PathVariable("name") String name, @RequestBody Map<String, Object> data  ) {
		return fileService.changeFixedExtensionsFlagById(name, (boolean)data.get("use_yn"));
	}
	
	@PostMapping("customExtensions/{name}")
	@ResponseBody
	public ResponseEntity<ApiMessage> addCustomExtensions( @PathVariable("name") String name ) {
		return fileService.addCustomExtensions( name );
	}
	
	@DeleteMapping("customExtensions/{name}")
	@ResponseBody
	public ResponseEntity<ApiMessage> removeCustomExtensions( @PathVariable("name") String name ) {
		return fileService.removeCustomExtensions( name );
	}
	
	
}
