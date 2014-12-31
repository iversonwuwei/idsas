function _w_table_rowspan(_w_table_id,_w_table_colnum){   
    _w_table_firsttd = "";   
    _w_table_currenttd = "";   
    _w_table_SpanNum = 0;   
   _w_table_Obj = $(_w_table_id + " tr td:nth-child(" + _w_table_colnum + ")");   
    _w_table_Obj.each(function(i){   
        if(i==0){   
            _w_table_firsttd = $(this);   
            _w_table_SpanNum = 1;   
       }else{   
            _w_table_currenttd = $(this);   
            if(_w_table_firsttd.text()==_w_table_currenttd.text()){   
                _w_table_SpanNum++;   
                _w_table_currenttd.hide(); //remove();
                _w_table_firsttd.attr("rowSpan",_w_table_SpanNum);   
           }else{   
               _w_table_firsttd = $(this);   
                _w_table_SpanNum = 1;   
            }   
       }   
    });    
}

function _w_table_rowspan_colour(_w_table_id,_w_table_colnum){   
	_w_table_firsttd = "";   
    _w_table_currenttd = "";   
    _w_table_SpanNum = 0;   
	_w_table_Obj = $(_w_table_id + " tr td:nth-child(" + _w_table_colnum + ")");
	var _flag = true;
    _w_table_Obj.each(function(i){
    	if(i==0) {   
    		_w_table_firsttd = $(this);   
    		_w_table_SpanNum = 1;   
    	} else {   
    		_w_table_currenttd = $(this);   
    		if(_w_table_firsttd.text()==_w_table_currenttd.text()){   
    			_w_table_SpanNum++;   
                _w_table_currenttd.hide(); //remove();   
                _w_table_firsttd.attr("rowSpan",_w_table_SpanNum);
    		} else {   
    			_w_table_firsttd = $(this);  
                _w_table_SpanNum = 1;
                _flag = !_flag;
            }
    		if(!$(this).hasClass("FixedRow")) {
//				$($(this).parent()).css("background-color",  _flag ? "#FFFFFF" : "#EBF2F9");
    			$($(this).parent()).addClass(_flag ? "" : "oddColour");
			}
    	}
    });    
}  
function _w_table_colspan(_w_table_id,_w_table_rownum,_w_table_maxcolnum){  
    if(_w_table_maxcolnum == void 0){_w_table_maxcolnum=0;}  
    _w_table_firsttd = "";  
    _w_table_currenttd = "";  
   _w_table_SpanNum = 0;  
    $(_w_table_id + " tr:nth-child(" + _w_table_rownum + ")").each(function(i){  
        _w_table_Obj = $(this).children();  
        _w_table_Obj.each(function(i){  
           if(i==0){  
               _w_table_firsttd = $(this);  
                _w_table_SpanNum = 1;  
           }else if((_w_table_maxcolnum>0)&&(i>_w_table_maxcolnum)){  
               return "";  
            }else{  
                _w_table_currenttd = $(this);  
                if(_w_table_firsttd.text()==_w_table_currenttd.text()){  
                    _w_table_SpanNum++;  
                    _w_table_currenttd.hide(); //remove();   
                    _w_table_firsttd.attr("colSpan",_w_table_SpanNum);  
               }else{  
                    _w_table_firsttd = $(this);  
                    _w_table_SpanNum = 1;  
                }  
            }  
        });  
    });  
}     
function _w_table_rowspan_1(_w_table_id,_w_table_colnum, column){   
	_w_table_firsttd = "";   
	_w_table_currenttd = "";   
	_w_table_SpanNum = 0;
	var breakpoints = new Object();
	_w_table_Obj = $(_w_table_id + " tr td:nth-child(" + _w_table_colnum + ")");   
    _w_table_Obj.each(function(i){   
        if(i==0){   
            _w_table_firsttd = $(this);   
            _w_table_SpanNum = 1;   
       }else{   
            _w_table_currenttd = $(this);   
            if(_w_table_firsttd.text()==_w_table_currenttd.text()){   
                _w_table_SpanNum++;   
                _w_table_currenttd.hide(); //remove();  
                _w_table_firsttd.attr("rowSpan",_w_table_SpanNum);
           }else{
               _w_table_firsttd = $(this);   
                _w_table_SpanNum = 1;
                breakpoints[i] = i;
           }   
       }   
    });
    for(var j=0;j<column.length;j++){
    	  _w_table_Obj = $(_w_table_id + " tr td:nth-child(" + column[j] + ")");  
    	  _w_table_Obj.each(function(i){   
    	        if(i==0){   
    	            _w_table_firsttd = $(this);   
    	            _w_table_SpanNum = 1;   
    	       }else{   
    	            _w_table_currenttd = $(this);   
    	            if(_w_table_firsttd.text()==_w_table_currenttd.text() && typeof(breakpoints[i]) == "undefined"){   
    	                _w_table_SpanNum++;   
    	                _w_table_currenttd.hide(); //remove();  
    	                _w_table_firsttd.attr("rowSpan",_w_table_SpanNum);
    	           }else{
    	        	   _w_table_firsttd = $(this);   
    	        	   _w_table_SpanNum = 1;
    	           }   
    	       }   
    	    });
    }
  
  
}