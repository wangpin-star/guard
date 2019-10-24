function isUrL(sUrl) {
    var sRegex = '^((https|http|ftp|rtsp|mms)?://)' + '?(([0-9a-z_!~*\'().&=+$%-]+: )?[0-9a-z_!~*\'().&=+$%-]+@)?' //ftp的user@ 
        + '(([0-9]{1,3}.){3}[0-9]{1,3}' // IP形式的URL- 199.194.52.184 
        + '|' // 允许IP和DOMAIN（域名） 
        + '([0-9a-z_!~*\'()-]+.)*' // 域名- www. 
        + '([0-9a-z][0-9a-z-]{0,61})?[0-9a-z].' // 二级域名 
        + '[a-z]{2,6})' // first level domain- .com or .museum 
        + '(:[0-9]{1,4})?' // 端口- :80 
        + '((/?)|' // a slash isn't required if there is no file name 
        + '(/[0-9a-z_!~*\'().;?:@&=+$,%#-]+)+/?)$';
    var re = new RegExp(sRegex);
    //re.test() 
    if (re.test(sUrl)) {
        return true;
    }
    return false;
}

function isValidIP(ip) {
    var reg = /^(\d{1,2}|1\d\d|2[0-4]\d|25[0-5])\.(\d{1,2}|1\d\d|2[0-4]\d|25[0-5])\.(\d{1,2}|1\d\d|2[0-4]\d|25[0-5])\.(\d{1,2}|1\d\d|2[0-4]\d|25[0-5])$/
    return reg.test(ip);
}

function concat_(arr1,arr2){   
    var arr = arr1.concat();   
    for(var i=0;i<arr2.length;i++){  
        arr.indexOf(arr2[i]) === -1 ? arr.push(arr2[i]) : 0;  
    }  
    return arr;  
}  
function complex(agr1){  
    if(agr1.length=== 1){  
        return agr1[0];  
    }
    if(agr1.length<1){
    	return false;
    }
    var result = concat_(agr1[0],agr1[1]);  
    for(var i=2;i<agr1.length;i++){  
        result = concat_(result,agr1[i]);  
    }  
    return result;  
}
function paramConfig(paramInfo){
	 var results=[];
	 var tb=[];
	 var list=[];
	 var param=[];
	 for (var i = 0; i < paramInfo.length; i++) {
	     var n = tb.indexOf(paramInfo[i].param_sheet);
	     var count=-1;
	     if (n===-1) {
	    	 count++;
	        tb.push(paramInfo[i].param_sheet);
	     }
	 }
	   for(var o=0;o<tb.length;o++){
		  var cor=[];
		  var result={};
		  for(var j=0;j<paramInfo.length;j++){
			  
		    if(paramInfo[j].set!==null&&tb[o]===paramInfo[j].param_sheet){
		    	var sets=[];
		      for(var k=0;k<paramInfo[j].set.length;k++){
				if(paramInfo[j].set[k].control!==''){
				    cor.push(paramInfo[j].set[k].control.split(","));
				    sets.push(paramInfo[j].set[k].control.split(","));
				 }
			    }
			      if(complex(sets)!==false){
			    	  var controlled={};
			    	  controlled.sheet=paramInfo[j].param_sheet;
			    	  controlled.key=paramInfo[j].param_key;
			    	  controlled.display=paramInfo[j].display_controlled;
			    	  controlled.controlled=complex(sets);
			    	  list.push(controlled);
			    	  param.push(paramInfo[j]);
			      }
		       
		     }
	     }
		  result.controll=complex(cor);
		  result.name=tb[o];
		  results.push(result);
	   }
	
	var params=[];
	if(param!=null){
		for(var s=0;s<param.length;s++){
			for(var t=0;t<param[s].set.length;t++){
				var controlleds={};
		    	  controlleds.sheet=param[s].param_sheet;
		    	  controlleds.key=param[s].param_key;
		    	  controlleds.display=param[s].display_controlled;
		    	  controlleds.controlled=param[s].set[t].control.split(",");
		    	  controlleds.value=param[s].set[t].value;
		    	  params.push(controlleds);
			}
		}
	}
	var termInfo={};
	termInfo.results=results;
	termInfo.list=list;
	termInfo.params=params;
	return termInfo;

}
	
	function hideOrShow(sheet,results){
		for(var i=0;i<results.length;i++){
			if(results[i].controll!==false&&results[i].name===sheet){
				for(var m=0;m<results[i].controll.length;m++){
					if(results[i].controll[m]!==""){
						$("#"+results[i].controll[m]).hide();
					}
					
				}
			}
		}
	}
	function hideOrShows(key,list){
		for(var i=0;i<list.length;i++){
			if(list[i].key===key){
				for(var m=0;m<list[i].controlled.length;m++){
					if(list[i].controlled[m]!==""){
						$("#"+list[i].controlled[m]).hide();
					}
					
				}
			}
		}
	}