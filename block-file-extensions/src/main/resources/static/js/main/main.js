function alertError(e) {
	if(e.status==0){
		alert('네트워크 문제가 있습니다.');
		return;
    } else {
    	alert(e.responseJSON.message);
    }
}

function getContextPath() {
    var hostIndex = location.href.indexOf( location.host ) + location.host.length;
    return location.href.substring( hostIndex, location.href.indexOf('/', hostIndex + 1) );
}



//고정확장자관련
var fixExtension = {
		
	
	"changeFlag" : function( list ) {
		for (var i = 0; i < list.length; i++) {
    		
    		if ( $("[name='"+ list[i].name +"']").is(":checked")  != list[i].use_yn) {
    			$("[name='"+ list[i].name +"']").prop('checked',  list[i].use_yn );
			}
			
		}
	},
		
	"makeList" : function( list ) {
		
		var width = 100;
		if ( list.length != 0 ) width = width / list.length;
		for (var i = 0; i < list.length; i++) {
			
			var name = list[i].name;
			$div = $('<div class="fix-check-div" style="width : '+width+'%;" >'
					  +'<input class="fixed-extension-checkbox" type="checkbox" name="'+name+'" value = "true"  >'
					  +'<label style="margin-left: 10px; vertical-align: top;"> ' + name + ' </label>'
					+'</div>');
			
			$("#extensions-setting-choose-select").append($div);
			$div.find("input").prop('checked',  list[i].use_yn );
			
			$div.find("input").change(function(){ 
				fixExtension.changeInput(this.name,this.checked);
			}); 
		}
	},
	"changeInput" : function( name , use_yn ) {
		
		$.ajax({
	        url: getContextPath() + "/extensions/fixedExtensions/"+name,
	        contentType: "application/json",
	        data: JSON.stringify({
	        	"use_yn" : use_yn
	        }),
	        type:"PATCH",
	        success:function (result) {
	        	var fixedList 	= JSON.parse( result.data );
	        	fixExtension.changeFlag( fixedList );
	        },
	        error:function (e) {
	        	alertError(e);
	        }
	    })
		
	}	
}

//커스텀확장자관련
var customExtension = {
	
	"makeList" : function( list ) {
		$("#custom-info-span").text( list.length + "/" +  customExtensionsMax  );
		$("#custom-selected-div").empty();
		var list = list.reverse();
		for (var i = 0; i < list.length; i++) {
			var name = list[i].name;
			$button = $('<button type="button" class="custom-btn">'
					+'<span class="custom-name-span">' + name + '</span>'
					+'<span class="custom-name-span-remove" onclick="customExtension.removeName(\''+name+'\');" >x</span>'
					+'</button>');
			$("#custom-selected-div").append($button);
		}
	},
		
	"addName" : function( name ) {
		$.ajax({
	        url: getContextPath() + "/extensions/customExtensions/"+name,
	        contentType: "application/json",
	        type:"POST",
	        success:function (result) {
	        	customExtension.makeList( JSON.parse( result.data ) );
	        },
	        //200개 넘어가도 에러로 주자.
	        error:function (e) {
	        	alertError(e);
	        	if ( e.responseJSON != null && e.responseJSON.data != null ) {
	        		customExtension.makeList( JSON.parse( e.responseJSON.data ) );
	        	}
	        }
	    })
	},
	
	"removeName" : function( name ) {
		$.ajax({
	        url: getContextPath() + "/extensions/customExtensions/"+name,
	        contentType: "application/json",
	        type:"DELETE",
	        success:function (result) {
	        	customExtension.makeList( JSON.parse( result.data ) );
	        },
	        error:function (e) {
	        	alertError(e);
	        }
	    })
	},

}

//시작
!(function(){
	//fixed
	fixExtension.makeList(fixedExtensionsList);
	//custom
	customExtension.makeList(customExtensionsList);
})();

$(document).ready(function() {
	
	//커스텀 확장자 input 엔터 이벤트
	$("#custom-add-input").on("keyup",function(key){
		if ( $(this).val().trim().length >0 ) {
			$("#custom-add-btn").addClass("custom-add-btn-toggle");
		} else {
			$("#custom-add-btn").removeClass("custom-add-btn-toggle");
		}
		if(key.keyCode==13) {
        	$("#custom-add-btn").click();
        }
    });
	//추가 버튼
	$("#custom-add-btn").click(function(){
		var name = $("#custom-add-input").val().trim();
		var regType1 = /^[A-Za-z0-9+]{0,20}$/; 
		if ( !regType1.test( name ) ) { 
			$("#custom-add-input").focus();
			alert('영와와 숫자만 사용해주세요.'); 
			return;
		}
		customExtension.addName( name );
	});	
});	

