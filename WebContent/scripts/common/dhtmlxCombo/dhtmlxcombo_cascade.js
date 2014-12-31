//v.1.2 build 80512

/*
Copyright DHTMLX LTD. http://www.dhtmlx.com
You allowed to use this component or parts of it under GPL terms
To use it on other terms or get Professional edition of the component please contact us at sales@dhtmlx.com
*/

/*_TOPICS_
@0:Initialization
@1:Selection control
@2:Add/delete
@3:Reserved
@4:Methods of Option object
*/

/**
 * Build combobox from existing select control.
 * 
 * 
 * @param parent
 *            {string} id of existing select control
 * @param size
 *            {int } size of control, optional
 * @return {object} combobox object
 * @type public
 * @topic 0
 * 
 */

function dhtmlXComboFromSelect_cascade(parent,size){
   if (typeof(parent)=="string")
      parent=document.getElementById(parent);
   
   
   size=size||parent.getAttribute("width")||(window.getComputedStyle?window.getComputedStyle(parent,null)["width"]:(parent.currentStyle?parent.currentStyle["width"]:0));
   if ((!size)||(size=="auto"))
   		size=parent.offsetWidth||100;

   var z=document.createElement("SPAN");
   parent.parentNode.insertBefore(z,parent);
   parent.style.display='none';

    var s_type = parent.getAttribute('opt_type');

   var w= new dhtmlXCombo_cascade(z,parent.name,size,s_type,parent.tabIndex);
   
   var x=new Array();
   var sel=0;
   for (var i=0; i<parent.options.length; i++){
      if (parent.options[i].selected) sel=i;
      var label=parent.options[i].innerHTML;
      var val=parent.options[i].getAttribute("value");
      if ((typeof(val)=="undefined")||(val===null)) val=label;
      x[i]={value:val,text:label,img_src:parent.options[i].getAttribute("img_src")};
   }

    w.addOption(x);
   
   
   parent.parentNode.removeChild(parent);
   w.selectOption(sel,null,true); 
 
   if (parent.onchange) 
   		w.attachEvent("onChange",parent.onchange);
   return w;
}

var dhtmlXCombo_cascade_optionTypes = [];
/**
 * @desc: build combobox
 * @param: parent - (string) id of existing object which will be used as
 *         container
 * @param: name - (string) name of combobox - will be used in FORM
 * @param: width - (int) size of combobox
 * @param: tabIndex - (int) tab index, optional
 * @type: public
 * @topic: 0
 */
function dhtmlXCombo_cascade(parent,name,width,optionType,tabIndex){
      if (typeof(parent)=="string")
         parent=document.getElementById(parent);

      this.dhx_Event();
      this.optionType = (optionType != window.undefined && dhtmlXCombo_cascade_optionTypes[optionType]) ? optionType : 'default';
      this._optionObject = dhtmlXCombo_cascade_optionTypes[this.optionType];

      this._disabled = false;

      if (!window.dhx_glbSelectAr){
          window.dhx_glbSelectAr=new Array();
          window.dhx_openedSelect=null;
          window.dhx_SelectId=1;
          dhtmlxEvent(document.body,"click",this.closeAll);
          dhtmlxEvent(document.body,"keydown",function(e){ try { if ((e||event).keyCode==9)  window.dhx_glbSelectAr[0].closeAll(); } catch(e) {} return true; } );
      }

      if (parent.tagName=="SELECT")
         dhtmlXComboFromSelect_cascade(parent);
      else{
         this._createSelf(parent,name,width,tabIndex);
      dhx_glbSelectAr.push(this);
}

}

/**
 * @desc: change control size
 * @param: new_size - (int) new size value
 * @type: public
 * @topic: 0
 */
   dhtmlXCombo_cascade.prototype.setSize = function(new_size){
      this.DOMlist.style.width=new_size+"px";
      if (this.DOMlistF) this.DOMlistF.style.width=new_size+"px";
      this.DOMelem.style.width=new_size+"px";
      this.DOMelem_input.style.width = (new_size)+'px';
   }      
/**
 * @desc: switch between combobox and auto-filter modes
 * @param: mode - (boolean) enable filtering mode
 * @param: url - (string) url for filtering from XML, optional
 * @param: cache - (boolean) XML caching, optional
 * @param: autosubload - (boolean) enable auto load additional suggestions on
 *         selecting last loaded option
 * @type: public
 * @topic: 0
 */
   dhtmlXCombo_cascade.prototype.enableFilteringMode = function(mode,url,cache,autosubload){
      this._filter=convertStringToBoolean(mode);

      if (url){
         this._xml=url;
         this._autoxml=convertStringToBoolean(autosubload);
      }
      if (convertStringToBoolean(cache)) this._xmlCache=[];
      // this.DOMelem_button.style.display=(this._filter?"none":"");
   }
   
   dhtmlXCombo_cascade.prototype.setFilteringParam=function(name,value){
   		if (!this._prs) this._prs=[];
   		this._prs.push([name,value]);
   }
/**
 * @desc: disable combobox
 * @param: mode - (boolean) disable combobox
 * @type: public
 * @topic: 1
 */
   dhtmlXCombo_cascade.prototype.disable = function(mode){
      var z=convertStringToBoolean(mode);
      if (this._disabled==z) return;
      this.DOMelem_input.disabled=z;
      this._disabled=z;
   }
/**
 * @desc: switch to readonly mode
 * @param: mode - (boolean) readonly mode
 * @param: autosearch - (boolean) true by default
 * @type: public
 * @topic: 1
 */
   dhtmlXCombo_cascade.prototype.readonly = function(mode,autosearch){
	   
        this.DOMelem_input.readOnly=mode ? true : false;
		if(autosearch===false){
			this.DOMelem.onkeyup=function(ev){ }
		} else {
			var that = this;
			this.DOMelem.onkeyup=function(ev){ 
				ev=ev||window.event; 
				if (ev.keyCode!=9) ev.cancelBubble=true;
				if((ev.keyCode >= 48 && ev.keyCode <= 57)||(ev.keyCode >= 65 && ev.keyCode <= 90)){
					for(var i=0; i<that.optionsArr.length; i++){
						var text = that.optionsArr[i].text;	
						
						if(text.toString().toUpperCase().indexOf(String.fromCharCode(ev.keyCode)) == 0){
								that.selectOption(i);
								break;
						}
					}
					ev.cancelBubble=true;
				}
			}
		}
	}	
   
/**
 * @desc: get Option by value
 * @param: value - (string) value of option in question
 * @type: public
 * @return: option object
 * @topic: 2
 */
   dhtmlXCombo_cascade.prototype.getOption = function(value)
   {
      for(var i=0; i<this.optionsArr.length; i++)
         if(this.optionsArr[i].value==value)
            return this.optionsArr[i];
      return null;
   }
/**
 * @desc: get Option by label
 * @param: label - (string) label of option in question
 * @type: public
 * @return: option object
 * @topic: 2
 */
   dhtmlXCombo_cascade.prototype.getOptionByLabel = function(value)
   {
	  value=value.replace(/&/g,"&amp;")
      for(var i=0; i<this.optionsArr.length; i++)
         if(this.optionsArr[i].text==value)
            return this.optionsArr[i];
      return null;
   }
/**
 * @desc: get Option by index
 * @param: ind - (int) index of option in question
 * @type: public
 * @return: option object
 * @topic: 2
 */
   dhtmlXCombo_cascade.prototype.getOptionByIndex = function(ind){
      return this.optionsArr[ind];
   }
/**
 * @desc: clear all options from combobox
 * @type: public
 * @param: value - (bool) clear current value as well
 * @topic: 2
 */
   dhtmlXCombo_cascade.prototype.clearAll = function(all)
   {
	  if (all) this.setComboText("");   	  
      this.optionsArr=new Array();
      this.redrawOptions();
      if (all) this._confirmSelection();
   }
/**
 * @desc: delete option by value
 * @param: value - (string) value of option in question
 * @type: public
 * @topic: 2
 */
   dhtmlXCombo_cascade.prototype.deleteOption = function(value)
   {
        var ind=this.getIndexByValue(value);
      if(ind<0) return;                            
      if (this.optionsArr[ind]==this._selOption) this._selOption=null;
      this.optionsArr.splice(ind, 1);
      this.redrawOptions();
   }

/**
 * @desc: enable/disable immideatly rendering after changes in combobox
 * @param: mode - (boolean) enable/disable
 * @type: public
 * @topic: 1
 */
   dhtmlXCombo_cascade.prototype.render=function(mode){
      this._skiprender=(!convertStringToBoolean(mode));
      this.redrawOptions();
   }

/**
 * @desc: update option in combobox
 * @param: oldvalue - (string) index of option in question
 * @param: avalue - (variable)
 * @type: public
 * @topic: 2
 */
   dhtmlXCombo_cascade.prototype.updateOption = function(oldvalue, avalue, atext, acss)
   {
      var dOpt=this.getOption(oldvalue);
      if (typeof(avalue)!="object") avalue={text:atext,value:avalue,css:acss};
      dOpt.setValue(avalue);
        this.redrawOptions();
   }
/**
 * @desc: add new option
 * @param: value - (variable) - different input for different kinds of options -
 *         please refer to examples
 * @type: public
 * @topic: 2
 */
   dhtmlXCombo_cascade.prototype.addOption = function(options)
   {
      if (!arguments[0].length || typeof(arguments[0])!="object")
         args = [arguments];
      else
         args = options;

      this.render(false);
        for (var i=0; i<args.length; i++) {
            var attr = args[i];
         if (attr.length){
               attr.value = attr[0]||"";
               attr.text = attr[1]||"";
               attr.css = attr[2]||"";
         }
            this._addOption(attr);
        }
      this.render(true);
   }

   dhtmlXCombo_cascade.prototype._addOption = function(attr)
   {
         dOpt = new this._optionObject();
         this.optionsArr.push(dOpt);
         dOpt.setValue.apply(dOpt,[attr]);
         this.redrawOptions();
   }


/**
 * @desc: return index of item by value
 * @param: value - (string) value of option in question
 * @type: public
 * @return: option index
 * @topic: 2
 */
   dhtmlXCombo_cascade.prototype.getIndexByValue = function(val){
      for(var i=0; i<this.optionsArr.length; i++)
         if(this.optionsArr[i].value == val) return i;
      return -1;
   }


	/**
	 * code by myself
	 */
	dhtmlXCombo_cascade.prototype.getIndexByText = function(text){
      for(var i=0; i<this.optionsArr.length; i++)
         if(this.optionsArr[i].data()[1] == text) return i;
      return -1;
   }
/**
 * @desc: get value of selected item
 * @type: public
 * @return: option value
 * @topic: 2
 */
   dhtmlXCombo_cascade.prototype.getSelectedValue = function(){
      return (this._selOption ? this._selOption.value : "-1");
   }
/**
 * @desc: get current text in combobox
 * @type: public
 * @return: combobox text
 * @topic: 2
 */
   dhtmlXCombo_cascade.prototype.getComboText = function(){
      return this.DOMelem_input.value;
   }
/**
 * @desc: set text in covmbobox
 * @param: text - (string) new text label
 * @type: public
 * @topic: 2
 */
   dhtmlXCombo_cascade.prototype.setComboText = function(text){
      this.DOMelem_input.value=text;
   }
   
/**
 * @desc: set text in covmbobox
 * @param: text - (string) new text label
 * @type: public
 * @topic: 2
 */
   dhtmlXCombo_cascade.prototype.setComboValue = function(text){
      this.setComboText(text);
	  for(var i=0; i<this.optionsArr.length; i++)
         if (this.optionsArr[i].data()[0]==text)
         return this.selectOption(i,null,true);
      this.DOMelem_hidden_input.value=text;
   }   

/**
 * @desc: get value which will be sent with form
 * @type: public
 * @return: combobox value
 * @topic: 2
 */
   dhtmlXCombo_cascade.prototype.getActualValue = function(){
	 
      return this.DOMelem_hidden_input.value;
   }
/**
 * @desc: get text of selected option
 * @type: public
 * @return: text of option
 * @topic: 2
 */
   dhtmlXCombo_cascade.prototype.getSelectedText = function(){
      return (this._selOption?this._selOption.text:"");
   }
/**
 * @desc: get index of selected option
 * @type: public
 * @return: option index
 * @topic: 2
 */
   dhtmlXCombo_cascade.prototype.getSelectedIndex = function(){
      for(var i=0; i<this.optionsArr.length; i++)
         if(this.optionsArr[i] == this._selOption) return i;
      return -1;
   }
/**
 * @desc: set name used while form submit
 * @param: name - (string) new combobox name
 * @type: public
 * @topic: 2
 */
   dhtmlXCombo_cascade.prototype.setName = function(name){
      this.DOMforSbm.name = name;
      this.name = name;
   }
/**
 * @desc: show combox ( reversion to hide command )
 * @param: mode - (boolean) enable/disable
 * @type: public
 * @topic: 1
 */
   dhtmlXCombo_cascade.prototype.show = function(mode){
      if (convertStringToBoolean(mode))
         this.DOMelem.style.display = "";
      else
         this.DOMelem.style.display = "none";
   }

/**
 * @desc: destroy object and any related HTML elements
 * @type: public
 * @topic: 0
 */
   dhtmlXCombo_cascade.prototype.destructor = function()
   {
      var _sID = this._inID;
      this.DOMParent.removeChild(this.DOMelem);
      this.DOMlist.parentNode.removeChild(this.DOMlist);
      var s=dhx_glbSelectAr;
      this.DOMParent=this.DOMlist=this.DOMelem=0;
      this.DOMlist.combo=this.DOMelem.combo=0;
      for(var i=0; i<s.length; i++)
      {
         if(s[i]._inID == _sID)
         {
            s[i] = null;
            s.splice(i,1);
            return;
         }
      }
   }

/**
 * @desc: create self HTML
 * @type: private
 * @topic: 0
 */
      dhtmlXCombo_cascade.prototype._createSelf = function(selParent, name, width, tab)
      {

		
         if (width.toString().indexOf("%")!=-1){ 
            var self = this;
            var resWidht=parseInt(width)/100;
            window.setInterval(function(){ 
               var ts=selParent.parentNode.offsetWidth*resWidht-2;
               if (ts==self._lastTs) return;
               self.setSize(self._lastTs=ts);},500);
            var width=parseInt(selParent.offsetWidth); // mm
         }

         var width=parseInt(width||100); // mm
         this.ListPosition = "Bottom"; // set optionlist positioning
         this.DOMParent = selParent;
         this._inID = null;
         this.name = name;

         this._selOption = null; // selected option object pointer
         this.optionsArr = Array();

            var opt = new this._optionObject();
            opt.DrawHeader(this,name, width,tab);
         // HTML select part 2 - options list DIV element
         this.DOMlist = document.createElement("DIV");
         this.DOMlist.className = 'dhx_combo_list';
         this.DOMlist.style.width=width-(_isIE?0:0)+"px";
		 if (_isOpera || _isKHTML ) 
		 		this.DOMlist.style.overflow="auto";      
         this.DOMlist.style.display = "none";
         document.body.insertBefore(this.DOMlist,document.body.firstChild);         
         if (_isIE)    {
            this.DOMlistF = document.createElement("IFRAME");
            this.DOMlistF.style.border="0px";
            this.DOMlistF.className = 'dhx_combo_list';
            this.DOMlistF.style.width=width-(_isIE?0:0)+"px";
            this.DOMlistF.style.display = "none";
            this.DOMlistF.src="javascript:false;"; 
            document.body.insertBefore(this.DOMlistF,document.body.firstChild);
            }



         this.DOMlist.combo=this.DOMelem.combo=this;

         this.DOMelem_input.onkeydown = this._onKey;
         this.DOMelem_input.onkeypress = this._onKeyF;
         this.DOMelem_input.onblur = this._onBlur;
         // this.DOMlist.onblur = this._onBlur;
         this.DOMelem.onclick = this._toggleSelect;
         this.DOMlist.onclick = this._selectOption;
         this.DOMlist.onmousedown = function(){
         	this._skipBlur=true;
     	 }
         
         this.DOMlist.onkeydown = function(e){
         	this.combo.DOMelem_input.focus();
         	(e||event).cancelBubble=true;
         	this.combo.DOMelem_input.onkeydown(e)
     	 }
     	 
      }

      dhtmlXCombo_cascade.prototype._listOver = function(e)
      {
         e = e||event;
         e.cancelBubble = true;
         var node = (_isIE?event.srcElement:e.target);
         var that = this.combo;
         if ( node.parentNode == that.DOMlist ) {
            that.unSelectOption();
            var i=0;
            for (i; i<that.DOMlist.childNodes.length; i++) {
               if (that.DOMlist.childNodes[i]==node) break;
            }
            var z=that.optionsArr[i];
            that._selOption=z;
            that._selOption.select();
            
			if ((that._autoxml)&&((i+1)==that._lastLength))
            	that._fetchOptions(i+1,that._lasttext||"");            
         }

      }

/**
 * @desc: place option list in necessary place on screen
 * @type: private
 * @topic: 0
 */
      dhtmlXCombo_cascade.prototype._positList = function()
      {

		 
	  	// if(this.ListAutoPosit) this.enableOptionAutoPositioning(true); //mm
		// auto posit
         var pos=this.getPosition(this.DOMelem);
         if(this.ListPosition == 'Bottom'){
            this.DOMlist.style.top = pos[1]+this.DOMelem.offsetHeight+"px";
            this.DOMlist.style.left = pos[0]+"px";
         }
         else if(this.ListPosition == 'Top'){ // mm
				this.DOMlist.style.top = pos[1] - this.DOMlist.offsetHeight+"px";
            	
				this.DOMlist.style.left = pos[0]+"px"; // mm
          }
		  else{
			this.DOMlist.style.top = pos[1]+"px";
            this.DOMlist.style.left = pos[0]+this.DOMelem.offsetWidth+"px";
         }
     
      }
	  
      dhtmlXCombo_cascade.prototype.getPosition = function(oNode,pNode){

                  if(!pNode)
                        var pNode = document.body

                  var oCurrentNode=oNode;
                  var iLeft=0;
                  var iTop=0;
                  while ((oCurrentNode)&&(oCurrentNode!=pNode)){// .tagName!="BODY"){
               iLeft+=oCurrentNode.offsetLeft-oCurrentNode.scrollLeft;
               iTop+=oCurrentNode.offsetTop-oCurrentNode.scrollTop;
               oCurrentNode=oCurrentNode.offsetParent;// isIE()?:oCurrentNode.parentNode;
                  }
              if (pNode == document.body ){
                 if (_isIE){
                 if (document.documentElement.scrollTop)
                  iTop+=document.documentElement.scrollTop;
                 if (document.documentElement.scrollLeft)
                  iLeft+=document.documentElement.scrollLeft;
                  }
                  else
                       if (!_isFF){
                             iLeft+=document.body.offsetLeft;
                           iTop+=document.body.offsetTop;
                  }
                 }
                     return new Array(iLeft,iTop);
               }
/**
 * @desc: correct current selection ( move it to first visible option )
 * @type: private
 * @topic: 2
 */
      dhtmlXCombo_cascade.prototype._correctSelection = function(){
		 if (this.getComboText()!="")
         for (var i=0; i<this.optionsArr.length; i++)
            if (!this.optionsArr[i].isHidden()){
               return this.selectOption(i,true,false);
           }
         this.unSelectOption();
      }
/**
 * @desc: select next option in combobox
 * @param: step - (int) step size
 * @type: private
 * @topic: 2
 */
      dhtmlXCombo_cascade.prototype.selectNext = function(step){
         var z=this.getSelectedIndex()+step;
         while (this.optionsArr[z]){
            if (!this.optionsArr[z].isHidden())
               return this.selectOption(z,false,false);
            z+=step;
         }
      }
/**
 * @desc: on keypressed handler
 * @type: private
 * @topic: 0
 */
      dhtmlXCombo_cascade.prototype._onKeyF = function(e){
         var that=this.parentNode.combo;
         var ev=e||event;
         ev.cancelBubble=true;
         if (ev.keyCode=="13" || ev.keyCode=="9" ){	
			that.selectNext(0);
         	that._confirmSelection();
         	that.closeAll();
     	} else
        if (ev.keyCode=="27" ){
			
         	that._resetSelection();
         	that.closeAll();
     	} else that._activeMode=true;
         if (ev.keyCode=="13" || ev.keyCode=="27" ){ // enter
            that.callEvent("onKeyPressed",[ev.keyCode])
            return false;
         }
         return true;
      }
/**
 * @desc: on keyup handler
 * @type: private
 * @topic: 0
 */
      dhtmlXCombo_cascade.prototype._onKey = function(e){
         var that=this.parentNode.combo;
         (e||event).cancelBubble=true;
         var ev=(e||event).keyCode;
         if (ev>15 && ev<19) return true; // shift,alt,ctrl
        if (ev==27) return;
        if ((that.DOMlist.style.display!="block")&&(ev!="13")&&(ev!="9")&&((!that._filter)||(that._filterAny)))
            that.DOMelem.onclick(e||event);
       
		if ((ev!="13")&&(ev!="9")){
         	window.setTimeout(function(){ that._onKeyB(ev); },1);
         	if (ev=="40" || ev=="38")
         		return false;
     	}
         else if (ev==9){
         	that.closeAll();
         	(e||event).cancelBubble=false;
         }
      }
      dhtmlXCombo_cascade.prototype._onKeyB = function(ev)
      {
         if (ev=="40"){  // down
            var z=this.selectNext(1);
         } else if (ev=="38"){ // up
            this.selectNext(-1);
         } else{
            this.callEvent("onKeyPressed",[ev])
            if (this._filter) return this.filterSelf((ev==8)||(ev==46));
            for(var i=0; i<this.optionsArr.length; i++)
               if (this.optionsArr[i].data()[1]==this.DOMelem_input.value){
// ev.cancelBubble=true;
                  this.selectOption(i,false,false);
                  return false;
                  }
            this.unSelectOption();
         }
         return true;
      }


/**
 * @desc: on data change handler
 * @type: private
 * @topic: 0
 */
      dhtmlXCombo_cascade.prototype._onBlur = function()
      {
         var self = this.parentNode._self;
         if(self.getIndexByText(self.getComboText()) == -1 && self.getComboText() != "") { 
         	self.DOMelem_input.value = "";
         	return;
         }
          window.setTimeout(function(){
          	if (self.DOMlist._skipBlur) return !(self.DOMlist._skipBlur=false);
          	self._confirmSelection();        
          	self.callEvent("onBlur",[]);
          },100)
          
      }
      
      
/**
 * @desc: redraw combobox options
 * @type: private
 * @topic: 2
 */
      dhtmlXCombo_cascade.prototype.redrawOptions = function(){
         if (this._skiprender) return;
         for(var i=this.DOMlist.childNodes.length-1; i>=0; i--)
            this.DOMlist.removeChild(this.DOMlist.childNodes[i]);
         for(var i=0; i<this.optionsArr.length; i++)
            this.DOMlist.appendChild(this.optionsArr[i].render());
			
		
		
						

      }
/**
 * @desc: load list of options from XML
 * @param: url - (string) xml url
 * @type: public
 * @topic: 0
 */
      dhtmlXCombo_cascade.prototype.loadXML = function(url,afterCall){
         this._load=true;
         if ((this._xmlCache)&&(this._xmlCache[url])){
            this._fillFromXML(this,null,null,null,this._xmlCache[url]);
            if (afterCall) afterCall();
        }
         else{
            var xml=(new dtmlXMLLoaderObject(this._fillFromXML,this,true,true));
            if (afterCall) xml.waitCall=afterCall;
            if (this._prs)
            	for (var i=0; i<this._prs.length; i++)
            		url+=[getUrlSymbol(url),escape(this._prs[i][0]),"=",escape(this._prs[i][1])].join("");
            xml._cPath=url;
            xml.loadXML(url);
         }
      }

/**
 * @desc: load list of options from XML string
 * @param: astring - (string) xml string
 * @type: public
 * @topic: 0
 */
      dhtmlXCombo_cascade.prototype.loadXMLString = function(astring){
         var xml=(new dtmlXMLLoaderObject(this._fillFromXML,this,true,true));
         xml.loadXMLString(astring);
      }

/**
 * @desc: on XML load handler
 * @type: private
 * @topic: 0
 */
      dhtmlXCombo_cascade.prototype._fillFromXML = function(obj,b,c,d,xml){
         if (obj._xmlCache) obj._xmlCache[xml._cPath]=xml;

         // check that XML is correct
         xml.getXMLTopNode("complete");
         
         var top=xml.doXPath("//complete");
         var options=xml.doXPath("//option");
         
         obj.render(false);
         if ((!top[0])||(!top[0].getAttribute("add"))){
            obj.clearAll();
            obj._lastLength=options.length;
	         if (obj._xml){
	         if ((!options) || (!options.length)) 
	         	obj.closeAll();
	         else {
	         		if (obj._activeMode){
	         			obj._positList();
	        			obj.DOMlist.style.display="block";
	         			if (_isIE) obj._IEFix(true);
         			}
	     	 }}            
         } else
            obj._lastLength+=options.length;

         for (var i=0; i<options.length; i++) {
            var attr = new Object();
            attr.text = options[i].firstChild?options[i].firstChild.nodeValue:"";
            for (var j=0; j<options[i].attributes.length; j++) {
               var a = options[i].attributes[j];
               if (a)
                  attr[a.nodeName] = a.nodeValue;
            }
            obj._addOption(attr);
         }
         obj.render(true);
         	
         if ((obj._load)&&(obj._load!==true))
            obj.loadXML(obj._load);
         else{
            obj._load=false;
             if ((!obj._lkmode)&&(!obj._filter))
               obj._correctSelection();
            }

         var selected=xml.doXPath("//option[@selected]");
         if (selected.length)
         	obj.selectOption(obj.getIndexByValue(selected[0].getAttribute("value")),false,true);

      }
/**
 * @desc: deselect option
 * @type: public
 * @topic: 1
 */
      dhtmlXCombo_cascade.prototype.unSelectOption = function(){	
         if (this._selOption)
            this._selOption.deselect();
         this._selOption=null;
      }
      
      dhtmlXCombo_cascade.prototype._confirmSelection = function(data,status){
      	if(arguments.length==0){
      	    var z=this.getOptionByLabel(this.DOMelem_input.value);
          	data = z?z.value:this.DOMelem_input.value;
          	status = (z==null);
          	if (data==this.getActualValue()) return;
      	}
          	
      	this.DOMelem_hidden_input.value=data;
        this.DOMelem_hidden_input2.value = (status?"true":"false");
		
      	this.callEvent("onChange",[]);
      	this._activeMode=false;
  	  }
      dhtmlXCombo_cascade.prototype._resetSelection = function(data,status){
      		var z=this.getOption(this.DOMelem_hidden_input.value);
      		this.setComboValue(z?z.data()[0]:this.DOMelem_hidden_input.value)
      		this.setComboText(z?z.data()[1]:this.DOMelem_hidden_input.value)
  	  }  	  
  	  
/**
 * @desc: select option
 * @param: ind - (int) index of option in question
 * @param: filter - (boolean) enable autocomplit range, optional
 * @param: conf - (boolean) true for real selection, false for pre-selection
 * @type: public
 * @topic: 1
 */
      dhtmlXCombo_cascade.prototype.selectOption = function(ind,filter,conf){	  		 
      	 if (arguments.length<3) conf=true;
         this.unSelectOption();
         var z=this.optionsArr[ind];
         if (!z)  return;
         this._selOption=z;
         this._selOption.select();

         var corr=this._selOption.content.offsetTop+this._selOption.content.offsetHeight-this.DOMlist.scrollTop-this.DOMlist.offsetHeight;
         if (corr>0) this.DOMlist.scrollTop+=corr;
            corr=this.DOMlist.scrollTop-this._selOption.content.offsetTop;
         if (corr>0) this.DOMlist.scrollTop-=corr;

         var data=this._selOption.data();
         
	 	 if (conf){
	 	 	this.setComboText(data[1]);
	 	 	this._confirmSelection(data[0],false);
		 }	 			
         if ((this._autoxml)&&((ind+1)==this._lastLength))
            this._fetchOptions(ind+1,this._lasttext||"");        
         if (filter){
            var text=this.getComboText();			  
            if (text!=data[1]){
              // this.setComboText(data[1]);
               // dhtmlXRange(this.DOMelem_input,text.length+1,data[1].length);//没有写好内容被选中
            }
         }
         else
            this.setComboText(data[1]);
         this._selOption.RedrawHeader(this);         
         /* Event */
         this.callEvent("onSelectionChange",[]);
      }
/**
 * @desc: option on select handler
 * @type: private
 * @topic: 2
 */
      dhtmlXCombo_cascade.prototype._selectOption = function(e)
      {
         (e||event).cancelBubble = true;
         var node=(_isIE?event.srcElement:e.target);
         var that=this.combo;
         while (!node._self) {
            node = node.parentNode;
            if (!node)
               return;
         }

         var i=0;
         for (i; i<that.DOMlist.childNodes.length; i++) {
            if (that.DOMlist.childNodes[i]==node) break;
         }
         that.selectOption(i,false,true);
         that.closeAll();
         that.callEvent("onBlur",[])
         that._activeMode=false;
      }
/**
 * @desc: open list of options
 * @type: public
 * @topic: 2
 */
   dhtmlXCombo_cascade.prototype.openSelect = function(){ 
   	
	
    
	  if (this._disabled) return;
      this.closeAll();
      this._positList();
      this.DOMlist.style.display="block";
      // dhtmlXRange(this.DOMelem_input,1,this.DOMelem_input.value.length);//
		// 把文本况的容全选中
      this.callEvent("onOpen",[]);
	  
      /*
		 * if (this.autoOptionSize){ var x=this.DOMlist.offsetWidth;
		 * 
		 * for ( var i=0; i<this.optionsArr.length; i++){ if(i==0)
		 * alert("this.DOMlist.childNodes[i].scrollWidth ="+
		 * this.DOMlist.childNodes[i].scrollWidth + "> x= "+ x); if
		 * (this.DOMlist.childNodes[i].scrollWidth > x)
		 * x=this.DOMlist.childNodes[i].scrollWidth; }
		 * 
		 * this.DOMlist.style.width=x+"px"; }
		 */
		
		      
      if (_isIE) this._IEFix(true);
      this.DOMelem_input.focus();

         if (this._filter) this.filterSelf();
   }
/**
 * @desc: open(close) list
 * @type: private
 * @topic: 2
 */
   dhtmlXCombo_cascade.prototype._toggleSelect = function(e)
   {
      var that=this.combo;
      if(that.getSelectedText() != "") {	// 点击时，有旧值则清空
    	  that.setComboText("");
      }
      if ( that.DOMlist.style.display == "block" ) {
      that.closeAll();
      } else {
         that.openSelect();
      }
      (e||event).cancelBubble = true;
   }

    dhtmlXCombo_cascade.prototype._fetchOptions=function(ind,text){
		
          if (text=="") { this.closeAll();  return this.clearAll();   }
		 		
         var url=this._xml+((this._xml.indexOf("?")!=-1)?"&":"?")+"pos="+ind+"&mask="+encodeURIComponent(text);
         this._lasttext=text;
         if (this._load) this._load=url;
         else this.loadXML(url);
    }
/**
 * @desc: filter list of options
 * @type: private
 * @topic: 2
 */
    dhtmlXCombo_cascade.prototype.filterSelf = function(mode)
   {
      var text=this.getComboText();
	 
      if (this._xml){
		  
         this._lkmode=mode;
         this._fetchOptions(0,text);
      }
	  
	  /*
		 * try{ var filter=new RegExp("^"+text,"i");
		 * 
		 *  } catch (e){ var filter=new
		 * RegExp("^"+text.replace(/([\[\]\{\}\(\)\+\*\\])/g,"\\$1"));
		 *  }
		 */
      this.filterAny=false;	
      for(var i=0; i<this.optionsArr.length; i++){// 循环所有的选项
         var z=myfilter(text,this.optionsArr[i].text);
      	// var z=filter.test(this.optionsArr[i].text);//!!!过滤
      	 this.filterAny|=z;// 循环或z，只要有一个z为true，this.filterAny就为true
         this.optionsArr[i].hide(!z);// z为false隐藏这项
      }
      if (!this.filterAny) {
		  this.closeAll();
		  this._activeMode=true;
	  }
      else {
      	  if (this.DOMlist.style.display!="block")
      	   		this.openSelect();
	      if (_isIE) this._IEFix(true);
  		}
         
        if (!mode)
         this._correctSelection();   
        else this.unSelectOption();
   }

function myfilter(text,option){
	if(!option)
		return false;	
	
	if(option.indexOf(text)==0){
		return true;
	}
var option_py=makePy(option);
if(!option_py)// 不能转化为拼音
	return false;
option_py=option_py+"";

if(option_py.indexOf(',')==-1){// 非多音字
if(option_py.indexOf(text.toUpperCase())==0)
	{
       return true;
	}
}else{// 多音字
option_py=option_py+"";
var option_py_arr=option_py.split(",");
	　　     			for(var i=0;i<option_py_arr.length;i++) 
					{		             
	     				if(option_py_arr[i].indexOf(text.toUpperCase())==0){
                          return true;
						}
					}
}
return false;
}
/**
 * @desc: set hidden iframe for IE
 * @type: private
 * @topic: 2
 */
   dhtmlXCombo_cascade.prototype._IEFix = function(mode){
      this.DOMlistF.style.display=(mode?"block":"none");
        this.DOMlistF.style.top=this.DOMlist.style.top;
        this.DOMlistF.style.left=this.DOMlist.style.left;
   }
/**
 * @desc: close opened combobox list
 * @type: public
 * @topic: 1
 */
   dhtmlXCombo_cascade.prototype.closeAll = function()
   {
      if(window.dhx_glbSelectAr)
         for (var i=0; i<dhx_glbSelectAr.length; i++){
            if (dhx_glbSelectAr[i].DOMlist.style.display=="block") {
               dhx_glbSelectAr[i].DOMlist.style.display = "none";
               if (_isIE) dhx_glbSelectAr[i]._IEFix(false);
            }
            dhx_glbSelectAr[i]._activeMode=false;
        }
   }
/**
 * @desc: create selection range in input control
 * @param: InputId - (string) id of input ( object can be used as well )
 * @param: Start - (int) start selection position
 * @param: End - (int) end selection position
 * @type: public
 * @topic: 0
 */
function dhtmlXRange(InputId, Start, End)
{
   var Input = typeof(InputId)=='object' ? InputId : document.getElementById(InputId);
   try{    Input.focus();   } catch(e){};
   var Length = Input.value.length;
   Start--;
   if (Start < 0 || Start > End || Start > Length)
      Start = 0;
   if (End > Length)
      End = Length;
   if (Input.setSelectionRange) {
      Input.setSelectionRange(Start, End);
   } else if (Input.createTextRange) {
      var range = Input.createTextRange();
      range.moveStart('character', Start);
      range.moveEnd('character', End-Length);
      range.select();
   }
}
/**
 * @desc: combobox option object constructor
 * @type: public
 * @topic: 0
 */
      dhtmlXCombo_cascade_defaultOption = function(){
         this.init();
      }
/**
 * @desc: option initialization function
 * @type: private
 * @topic: 4
 */
      dhtmlXCombo_cascade_defaultOption.prototype.init = function(){
         this.value = null;
         this.text = "";
         this.selected = false;
         this.css = "";
      }
/**
 * @desc: mark option as selected
 * @type: public
 * @topic: 4
 */
      dhtmlXCombo_cascade_defaultOption.prototype.select = function(){
         if (this.content) ;
            this.content.className="dhx_selected_option";
            this.content.style.backgroundColor='navy';
            this.content.style.color='white';
      }
/**
 * @desc: hide option
 * @param: mode - (boolean)
 * @type: public
 * @topic: 4
 */
      dhtmlXCombo_cascade_defaultOption.prototype.hide = function(mode){
         this.render().style.display=mode?"none":"";
      }
/**
 * @desc: return hide state of option
 * @type: public
 * @return: hide state of option
 * @topic: 4
 */
      dhtmlXCombo_cascade_defaultOption.prototype.isHidden = function(){
         return (this.render().style.display=="none");
      }
/**
 * @desc: mark option as not selected
 * @type: public
 * @topic: 4
 */
      dhtmlXCombo_cascade_defaultOption.prototype.deselect = function(){
         if (this.content) this.render();
            this.content.className="";
            this.content.style.backgroundColor='white';
            this.content.style.color='black';
      }
/**
 * @desc: set value of option
 * @param: value - (string) value
 * @param: text - (string) text
 * @param: css - (string) css style string
 * @type: public
 * @topic: 4
 */
dhtmlXCombo_cascade_defaultOption.prototype.setValue = function(attr){
    this.value = attr.value||"";
    this.text = attr.text||"";
    this.css = attr.css||"";
   this.content=null;
}

				
/**
 * @desc: render option
 * @type: private
 * @topic: 4
 */
// 修改内容：强制下拉列表的高度，防止其换行
      dhtmlXCombo_cascade_defaultOption.prototype.render = function(){
         if (!this.content){
            this.content=document.createElement("DIV");
            this.content.onmouseover=function(){
            	this.style.backgroundColor='navy';
            	this.style.color='white';
            };
            this.content.onmouseout=function(){
            	this.style.backgroundColor='white';
            	this.style.color='black';};
            this.content._self = this;
            this.content.style.cssText='width:100%; overflow:hidden;height:14px; "+this.css+"';
            if (_isOpera || _isKHTML ) this.content.style.padding="2px 0px 2px 0px";
            this.content.innerHTML=this.text;
            this._ctext=_isIE?this.content.innerText:this.content.textContent;
         }
         return this.content;
      }
/**
 * @desc: return option data
 * @type: public
 * @return: array of data related to option
 * @topic: 4
 */
      dhtmlXCombo_cascade_defaultOption.prototype.data = function(){
         if (this.content)
            return [this.value,this._ctext ? this._ctext : this.text];
      }

dhtmlXCombo_cascade_defaultOption.prototype.DrawHeader = function(self, name, width, tab)
{
    var z=document.createElement("DIV");
    z.style.width = width+"px";
	z.style.height="18px";
    z.className = 'dhx_combo_box';
    z._self = self;
	self.DOMelem = z;
    this._DrawHeaderInput(self, name, width,tab);
	this._DrawHeaderButton(self, name, width);
    self.DOMParent.appendChild(self.DOMelem);
}

dhtmlXCombo_cascade_defaultOption.prototype._DrawHeaderInput = function(self, name, width,tab)
{
   var z=document.createElement('input');
   z.className = 'dhx_combo_input';
   z.setAttribute("autocomplete","off"); 
   z.type = 'text';
   z.id="linename";
   z.name="linename";
	z.style.border = '0px';	//设置边框为0，避免与父控件重叠
   if (tab) z.tabIndex=tab;
   z.style.width = (width)+'px';
   z.style.height='15px';
   self.DOMelem.appendChild(z);
   self.DOMelem_input = z;

   z = document.createElement('input');
   z.type = 'hidden';
   z.name = name;
   z.style.height="15px";
   self.DOMelem.appendChild(z);
   self.DOMelem_hidden_input = z;

   z = document.createElement('input');
   z.type = 'hidden';
   z.style.height="15px";
  // z.name = name+"_new_value";
   z.value="true";
   self.DOMelem.appendChild(z);
   self.DOMelem_hidden_input2 = z;
}

dhtmlXCombo_cascade_defaultOption.prototype._DrawHeaderButton = function(self, name, width)
{
 // var z=document.createElement('img');
 // z.className = 'dhx_combo_img';
 // z.style.cursor='pointer';
 // z.src =
	// (window.dhx_globalImgPath?dhx_globalImgPath:"")+'combo_select.gif';
   // self.DOMelem.appendChild(z);
   // self.DOMelem_button=z;
}

dhtmlXCombo_cascade_defaultOption.prototype.RedrawHeader = function(self)
{
}


dhtmlXCombo_cascade_optionTypes['default'] = dhtmlXCombo_cascade_defaultOption;

dhtmlXCombo_cascade.prototype.dhx_Event=function()
{
   this.dhx_SeverCatcherPath="";

   this.attachEvent = function(original, catcher, CallObj)
   {
      CallObj = CallObj||this;
      original = 'ev_'+original;
       if ( ( !this[original] ) || ( !this[original].addEvent ) ) {
           var z = new this.eventCatcher(CallObj);
           z.addEvent( this[original] );
           this[original] = z;
       }
       return ( original + ':' + this[original].addEvent(catcher) );   // return
																		// ID
																		// (event
																		// name
																		// &
																		// event
																		// ID)
   }
   this.callEvent=function(name,arg0){
         if (this["ev_"+name]) return this["ev_"+name].apply(this,arg0);
       return true;
   }
   this.checkEvent=function(name){
         if (this["ev_"+name]) return true;
       return false;
   }

   this.eventCatcher = function(obj)
   {
       var dhx_catch = new Array();
       var m_obj = obj;
       var func_server = function(catcher,rpc)
                         {
                           catcher = catcher.split(":");
                     var postVar="";
                     var postVar2="";
                           var target=catcher[1];
                     if (catcher[1]=="rpc"){
                           postVar='<?xml version="1.0"?><methodCall><methodName>'+catcher[2]+'</methodName><params>';
                        postVar2="</params></methodCall>";
                        target=rpc;
                     }
                           var z = function() {
                                   }
                           return z;
                         }
       var z = function()
             {
                   if (dhx_catch)
                      var res=true;
                   for (var i=0; i<dhx_catch.length; i++) {
                      if (dhx_catch[i] != null) {
                           var zr = dhx_catch[i].apply( m_obj, arguments );
                           res = res && zr;
                      }
                   }
                   return res;
                }
       z.addEvent = function(ev)
                {
                       if ( typeof(ev) != "function" )
						  		
                           if (ev && ev.indexOf && ev.indexOf("server:") == 0)
                               ev = new func_server(ev,m_obj.rpcServer);
                           else
                               ev = eval(ev);
                       if (ev)
                           return dhx_catch.push( ev ) - 1;
                       return false;
                }
       z.removeEvent = function(id)
                   {
                     dhx_catch[id] = null;
                   }
       return z;
   }

   this.detachEvent = function(id)
   {
      if (id != false) {
         var list = id.split(':');            // get EventName and ID
         this[ list[0] ].removeEvent( list[1] );   // remove event
      }
   }
}

var strChineseFirstPY = "YDYQSXMWZSSXJBYMGCCZQPSSQBYCDSCDQLDYLYBSSJGYZZJJFKCCLZDHWDWZJLJPFYYNWJJTMYHZWZHFLZPPQHGSCYYYNJQYXXGJHHSDSJNKKTMOMLCRXYPSNQSECCQZGGLLYJLMYZZSECYKYYHQWJSSGGYXYZYJWWKDJHYCHMYXJTLXJYQBYXZLDWRDJRWYSRLDZJPCBZJJBRCFTLECZSTZFXXZHTRQHYBDLYCZSSYMMRFMYQZPWWJJYFCRWFDFZQPYDDWYXKYJAWJFFXYPSFTZYHHYZYSWCJYXSCLCXXWZZXNBGNNXBXLZSZSBSGPYSYZDHMDZBQBZCWDZZYYTZHBTSYYBZGNTNXQYWQSKBPHHLXGYBFMJEBJHHGQTJCYSXSTKZHLYCKGLYSMZXYALMELDCCXGZYRJXSDLTYZCQKCNNJWHJTZZCQLJSTSTBNXBTYXCEQXGKWJYFLZQLYHYXSPSFXLMPBYSXXXYDJCZYLLLSJXFHJXPJBTFFYABYXBHZZBJYZLWLCZGGBTSSMDTJZXPTHYQTGLJSCQFZKJZJQNLZWLSLHDZBWJNCJZYZSQQYCQYRZCJJWYBRTWPYFTWEXCSKDZCTBZHYZZYYJXZCFFZZMJYXXSDZZOTTBZLQWFCKSZSXFYRLNYJMBDTHJXSQQCCSBXYYTSYFBXDZTGBCNSLCYZZPSAZYZZSCJCSHZQYDXLBPJLLMQXTYDZXSQJTZPXLCGLQTZWJBHCTSYJSFXYEJJTLBGXSXJMYJQQPFZASYJNTYDJXKJCDJSZCBARTDCLYJQMWNQNCLLLKBYBZZSYHQQLTWLCCXTXLLZNTYLNEWYZYXCZXXGRKRMTCNDNJTSYYSSDQDGHSDBJGHRWRQLYBGLXHLGTGXBQJDZPYJSJYJCTMRNYMGRZJCZGJMZMGXMPRYXKJNYMSGMZJYMKMFXMLDTGFBHCJHKYLPFMDXLQJJSMTQGZSJLQDLDGJYCALCMZCSDJLLNXDJFFFFJCZFMZFFPFKHKGDPSXKTACJDHHZDDCRRCFQYJKQCCWJDXHWJLYLLZGCFCQDSMLZPBJJPLSBCJGGDCKKDEZSQCCKJGCGKDJTJDLZYCXKLQSCGJCLTFPCQCZGWPJDQYZJJBYJHSJDZWGFSJGZKQCCZLLPSPKJGQJHZZLJPLGJGJJTHJJYJZCZMLZLYQBGJWMLJKXZDZNJQSYZMLJLLJKYWXMKJLHSKJGBMCLYYMKXJQLBMLLKMDXXKWYXYSLMLPSJQQJQXYXFJTJDXMXXLLCXQBSYJBGWYMBGGBCYXPJYGPEPFGDJGBHBNSQJYZJKJKHXQFGQZKFHYGKHDKLLSDJQXPQYKYBNQSXQNSZSWHBSXWHXWBZZXDMNSJBSBKBBZKLYLXGWXDRWYQZMYWSJQLCJXXJXKJEQXSCYETLZHLYYYSDZPAQYZCMTLSHTZCFYZYXYLJSDCJQAGYSLCQLYYYSHMRQQKLDXZSCSSSYDYCJYSFSJBFRSSZQSBXXPXJYSDRCKGJLGDKZJZBDKTCSYQPYHSTCLDJDHMXMCGXYZHJDDTMHLTXZXYLYMOHYJCLTYFBQQXPFBDFHHTKSQHZYYWCNXXCRWHOWGYJLEGWDQCWGFJYCSNTMYTOLBYGWQWESJPWNMLRYDZSZTXYQPZGCWXHNGPYXSHMYQJXZTDPPBFYHZHTJYFDZWKGKZBLDNTSXHQEEGZZYLZMMZYJZGXZXKHKSTXNXXWYLYAPSTHXDWHZYMPXAGKYDXBHNHXKDPJNMYHYLPMGOCSLNZHKXXLPZZLBMLSFBHHGYGYYGGBHSCYAQTYWLXTZQCEZYDQDQMMHTKLLSZHLSJZWFYHQSWSCWLQAZYNYTLSXTHAZNKZZSZZLAXXZWWCTGQQTDDYZTCCHYQZFLXPSLZYGPZSZNGLNDQTBDLXGTCTAJDKYWNSYZLJHHZZCWNYYZYWMHYCHHYXHJKZWSXHZYXLYSKQYSPSLYZWMYPPKBYGLKZHTYXAXQSYSHXASMCHKDSCRSWJPWXSGZJLWWSCHSJHSQNHCSEGNDAQTBAALZZMSSTDQJCJKTSCJAXPLGGXHHGXXZCXPDMMHLDGTYBYSJMXHMRCPXXJZCKZXSHMLQXXTTHXWZFKHCCZDYTCJYXQHLXDHYPJQXYLSYYDZOZJNYXQEZYSQYAYXWYPDGXDDXSPPYZNDLTWRHXYDXZZJHTCXMCZLHPYYYYMHZLLHNXMYLLLMDCPPXHMXDKYCYRDLTXJCHHZZXZLCCLYLNZSHZJZZLNNRLWHYQSNJHXYNTTTKYJPYCHHYEGKCTTWLGQRLGGTGTYGYHPYHYLQYQGCWYQKPYYYTTTTLHYHLLTYTTSPLKYZXGZWGPYDSSZZDQXSKCQNMJJZZBXYQMJRTFFBTKHZKBXLJJKDXJTLBWFZPPTKQTZTGPDGNTPJYFALQMKGXBDCLZFHZCLLLLADPMXDJHLCCLGYHDZFGYDDGCYYFGYDXKSSEBDHYKDKDKHNAXXYBPBYYHXZQGAFFQYJXDMLJCSQZLLPCHBSXGJYNDYBYQSPZWJLZKSDDTACTBXZDYZYPJZQSJNKKTKNJDJGYYPGTLFYQKASDNTCYHBLWDZHBBYDWJRYGKZYHEYYFJMSDTYFZJJHGCXPLXHLDWXXJKYTCYKSSSMTWCTTQZLPBSZDZWZXGZAGYKTYWXLHLSPBCLLOQMMZSSLCMBJCSZZKYDCZJGQQDSMCYTZQQLWZQZXSSFPTTFQMDDZDSHDTDWFHTDYZJYQJQKYPBDJYYXTLJHDRQXXXHAYDHRJLKLYTWHLLRLLRCXYLBWSRSZZSYMKZZHHKYHXKSMDSYDYCJPBZBSQLFCXXXNXKXWYWSDZYQOGGQMMYHCDZTTFJYYBGSTTTYBYKJDHKYXBELHTYPJQNFXFDYKZHQKZBYJTZBXHFDXKDASWTAWAJLDYJSFHBLDNNTNQJTJNCHXFJSRFWHZFMDRYJYJWZPDJKZYJYMPCYZNYNXFBYTFYFWYGDBNZZZDNYTXZEMMQBSQEHXFZMBMFLZZSRXYMJGSXWZJSPRYDJSJGXHJJGLJJYNZZJXHGXKYMLPYYYCXYTWQZSWHWLYRJLPXSLSXMFSWWKLCTNXNYNPSJSZHDZEPTXMYYWXYYSYWLXJQZQXZDCLEEELMCPJPCLWBXSQHFWWTFFJTNQJHJQDXHWLBYZNFJLALKYYJLDXHHYCSTYYWNRJYXYWTRMDRQHWQCMFJDYZMHMYYXJWMYZQZXTLMRSPWWCHAQBXYGZYPXYYRRCLMPYMGKSJSZYSRMYJSNXTPLNBAPPYPYLXYYZKYNLDZYJZCZNNLMZHHARQMPGWQTZMXXMLLHGDZXYHXKYXYCJMFFYYHJFSBSSQLXXNDYCANNMTCJCYPRRNYTYQNYYMBMSXNDLYLYSLJRLXYSXQMLLYZLZJJJKYZZCSFBZXXMSTBJGNXYZHLXNMCWSCYZYFZLXBRNNNYLBNRTGZQYSATSWRYHYJZMZDHZGZDWYBSSCSKXSYHYTXXGCQGXZZSHYXJSCRHMKKBXCZJYJYMKQHZJFNBHMQHYSNJNZYBKNQMCLGQHWLZNZSWXKHLJHYYBQLBFCDSXDLDSPFZPSKJYZWZXZDDXJSMMEGJSCSSMGCLXXKYYYLNYPWWWGYDKZJGGGZGGSYCKNJWNJPCXBJJTQTJWDSSPJXZXNZXUMELPXFSXTLLXCLJXJJLJZXCTPSWXLYDHLYQRWHSYCSQYYBYAYWJJJQFWQCQQCJQGXALDBZZYJGKGXPLTZYFXJLTPADKYQHPMATLCPDCKBMTXYBHKLENXDLEEGQDYMSAWHZMLJTWYGXLYQZLJEEYYBQQFFNLYXRDSCTGJGXYYNKLLYQKCCTLHJLQMKKZGCYYGLLLJDZGYDHZWXPYSJBZKDZGYZZHYWYFQYTYZSZYEZZLYMHJJHTSMQWYZLKYYWZCSRKQYTLTDXWCTYJKLWSQZWBDCQYNCJSRSZJLKCDCDTLZZZACQQZZDDXYPLXZBQJYLZLLLQDDZQJYJYJZYXNYYYNYJXKXDAZWYRDLJYYYRJLXLLDYXJCYWYWNQCCLDDNYYYNYCKCZHXXCCLGZQJGKWPPCQQJYSBZZXYJSQPXJPZBSBDSFNSFPZXHDWZTDWPPTFLZZBZDMYYPQJRSDZSQZSQXBDGCPZSWDWCSQZGMDHZXMWWFYBPDGPHTMJTHZSMMBGZMBZJCFZWFZBBZMQCFMBDMCJXLGPNJBBXGYHYYJGPTZGZMQBQTCGYXJXLWZKYDPDYMGCFTPFXYZTZXDZXTGKMTYBBCLBJASKYTSSQYYMSZXFJEWLXLLSZBQJJJAKLYLXLYCCTSXMCWFKKKBSXLLLLJYXTYLTJYYTDPJHNHNNKBYQNFQYYZBYYESSESSGDYHFHWTCJBSDZZTFDMXHCNJZYMQWSRYJDZJQPDQBBSTJGGFBKJBXTGQHNGWJXJGDLLTHZHHYYYYYYSXWTYYYCCBDBPYPZYCCZYJPZYWCBDLFWZCWJDXXHYHLHWZZXJTCZLCDPXUJCZZZLYXJJTXPHFXWPYWXZPTDZZBDZCYHJHMLXBQXSBYLRDTGJRRCTTTHYTCZWMXFYTWWZCWJWXJYWCSKYBZSCCTZQNHXNWXXKHKFHTSWOCCJYBCMPZZYKBNNZPBZHHZDLSYDDYTYFJPXYNGFXBYQXCBHXCPSXTYZDMKYSNXSXLHKMZXLYHDHKWHXXSSKQYHHCJYXGLHZXCSNHEKDTGZXQYPKDHEXTYKCNYMYYYPKQYYYKXZLTHJQTBYQHXBMYHSQCKWWYLLHCYYLNNEQXQWMCFBDCCMLJGGXDQKTLXKGNQCDGZJWYJJLYHHQTTTNWCHMXCXWHWSZJYDJCCDBQCDGDNYXZTHCQRXCBHZTQCBXWGQWYYBXHMBYMYQTYEXMQKYAQYRGYZSLFYKKQHYSSQYSHJGJCNXKZYCXSBXYXHYYLSTYCXQTHYSMGSCPMMGCCCCCMTZTASMGQZJHKLOSQYLSWTMXSYQKDZLJQQYPLSYCZTCQQPBBQJZCLPKHQZYYXXDTDDTSJCXFFLLCHQXMJLWCJCXTSPYCXNDTJSHJWXDQQJSKXYAMYLSJHMLALYKXCYYDMNMDQMXMCZNNCYBZKKYFLMCHCMLHXRCJJHSYLNMTJZGZGYWJXSRXCWJGJQHQZDQJDCJJZKJKGDZQGJJYJYLXZXXCDQHHHEYTMHLFSBDJSYYSHFYSTCZQLPBDRFRZTZYKYWHSZYQKWDQZRKMSYNBCRXQBJYFAZPZZEDZCJYWBCJWHYJBQSZYWRYSZPTDKZPFPBNZTKLQYHBBZPNPPTYZZYBQNYDCPJMMCYCQMCYFZZDCMNLFPBPLNGQJTBTTNJZPZBBZNJKLJQYLNBZQHKSJZNGGQSZZKYXSHPZSNBCGZKDDZQANZHJKDRTLZLSWJLJZLYWTJNDJZJHXYAYNCBGTZCSSQMNJPJYTYSWXZFKWJQTKHTZPLBHSNJZSYZBWZZZZLSYLSBJHDWWQPSLMMFBJDWAQYZTCJTBNNWZXQXCDSLQGDSDPDZHJTQQPSWLYYJZLGYXYZLCTCBJTKTYCZJTQKBSJLGMGZDMCSGPYNJZYQYYKNXRPWSZXMTNCSZZYXYBYHYZAXYWQCJTLLCKJJTJHGDXDXYQYZZBYWDLWQCGLZGJGQRQZCZSSBCRPCSKYDZNXJSQGXSSJMYDNSTZTPBDLTKZWXQWQTZEXNQCZGWEZKSSBYBRTSSSLCCGBPSZQSZLCCGLLLZXHZQTHCZMQGYZQZNMCOCSZJMMZSQPJYGQLJYJPPLDXRGZYXCCSXHSHGTZNLZWZKJCXTCFCJXLBMQBCZZWPQDNHXLJCTHYZLGYLNLSZZPCXDSCQQHJQKSXZPBAJYEMSMJTZDXLCJYRYYNWJBNGZZTMJXLTBSLYRZPYLSSCNXPHLLHYLLQQZQLXYMRSYCXZLMMCZLTZSDWTJJLLNZGGQXPFSKYGYGHBFZPDKMWGHCXMSGDXJMCJZDYCABXJDLNBCDQYGSKYDQTXDJJYXMSZQAZDZFSLQXYJSJZYLBTXXWXQQZBJZUFBBLYLWDSLJHXJYZJWTDJCZFQZQZZDZSXZZQLZCDZFJHYSPYMPQZMLPPLFFXJJNZZYLSJEYQZFPFZKSYWJJJHRDJZZXTXXGLGHYDXCSKYSWMMZCWYBAZBJKSHFHJCXMHFQHYXXYZFTSJYZFXYXPZLCHMZMBXHZZSXYFYMNCWDABAZLXKTCSHHXKXJJZJSTHYGXSXYYHHHJWXKZXSSBZZWHHHCWTZZZPJXSNXQQJGZYZYWLLCWXZFXXYXYHXMKYYSWSQMNLNAYCYSPMJKHWCQHYLAJJMZXHMMCNZHBHXCLXTJPLTXYJHDYYLTTXFSZHYXXSJBJYAYRSMXYPLCKDUYHLXRLNLLSTYZYYQYGYHHSCCSMZCTZQXKYQFPYYRPFFLKQUNTSZLLZMWWTCQQYZWTLLMLMPWMBZSSTZRBPDDTLQJJBXZCSRZQQYGWCSXFWZLXCCRSZDZMCYGGDZQSGTJSWLJMYMMZYHFBJDGYXCCPSHXNZCSBSJYJGJMPPWAFFYFNXHYZXZYLREMZGZCYZSSZDLLJCSQFNXZKPTXZGXJJGFMYYYSNBTYLBNLHPFZDCYFBMGQRRSSSZXYSGTZRNYDZZCDGPJAFJFZKNZBLCZSZPSGCYCJSZLMLRSZBZZLDLSLLYSXSQZQLYXZLSKKBRXBRBZCYCXZZZEEYFGKLZLYYHGZSGZLFJHGTGWKRAAJYZKZQTSSHJJXDCYZUYJLZYRZDQQHGJZXSSZBYKJPBFRTJXLLFQWJHYLQTYMBLPZDXTZYGBDHZZRBGXHWNJTJXLKSCFSMWLSDQYSJTXKZSCFWJLBXFTZLLJZLLQBLSQMQQCGCZFPBPHZCZJLPYYGGDTGWDCFCZQYYYQYSSCLXZSKLZZZGFFCQNWGLHQYZJJCZLQZZYJPJZZBPDCCMHJGXDQDGDLZQMFGPSYTSDYFWWDJZJYSXYYCZCYHZWPBYKXRYLYBHKJKSFXTZJMMCKHLLTNYYMSYXYZPYJQYCSYCWMTJJKQYRHLLQXPSGTLYYCLJSCPXJYZFNMLRGJJTYZBXYZMSJYJHHFZQMSYXRSZCWTLRTQZSSTKXGQKGSPTGCZNJSJCQCXHMXGGZTQYDJKZDLBZSXJLHYQGGGTHQSZPYHJHHGYYGKGGCWJZZYLCZLXQSFTGZSLLLMLJSKCTBLLZZSZMMNYTPZSXQHJCJYQXYZXZQZCPSHKZZYSXCDFGMWQRLLQXRFZTLYSTCTMJCXJJXHJNXTNRZTZFQYHQGLLGCXSZSJDJLJCYDSJTLNYXHSZXCGJZYQPYLFHDJSBPCCZHJJJQZJQDYBSSLLCMYTTMQTBHJQNNYGKYRQYQMZGCJKPDCGMYZHQLLSLLCLMHOLZGDYYFZSLJCQZLYLZQJESHNYLLJXGJXLYSYYYXNBZLJSSZCQQCJYLLZLTJYLLZLLBNYLGQCHXYYXOXCXQKYJXXXYKLXSXXYQXCYKQXQCSGYXXYQXYGYTQOHXHXPYXXXULCYEYCHZZCBWQBBWJQZSCSZSSLZYLKDESJZWMYMCYTSDSXXSCJPQQSQYLYYZYCMDJDZYWCBTJSYDJKCYDDJLBDJJSODZYSYXQQYXDHHGQQYQHDYXWGMMMAJDYBBBPPBCMUUPLJZSMTXERXJMHQNUTPJDCBSSMSSSTKJTSSMMTRCPLZSZMLQDSDMJMQPNQDXCFYNBFSDQXYXHYAYKQYDDLQYYYSSZBYDSLNTFQTZQPZMCHDHCZCWFDXTMYQSPHQYYXSRGJCWTJTZZQMGWJJTJHTQJBBHWZPXXHYQFXXQYWYYHYSCDYDHHQMNMTMWCPBSZPPZZGLMZFOLLCFWHMMSJZTTDHZZYFFYTZZGZYSKYJXQYJZQBHMBZZLYGHGFMSHPZFZSNCLPBQSNJXZSLXXFPMTYJYGBXLLDLXPZJYZJYHHZCYWHJYLSJEXFSZZYWXKZJLUYDTMLYMQJPWXYHXSKTQJEZRPXXZHHMHWQPWQLYJJQJJZSZCPHJLCHHNXJLQWZJHBMZYXBDHHYPZLHLHLGFWLCHYYTLHJXCJMSCPXSTKPNHQXSRTYXXTESYJCTLSSLSTDLLLWWYHDHRJZSFGXTSYCZYNYHTDHWJSLHTZDQDJZXXQHGYLTZPHCSQFCLNJTCLZPFSTPDYNYLGMJLLYCQHYSSHCHYLHQYQTMZYPBYWRFQYKQSYSLZDQJMPXYYSSRHZJNYWTQDFZBWWTWWRXCWHGYHXMKMYYYQMSMZHNGCEPMLQQMTCWCTMMPXJPJJHFXYYZSXZHTYBMSTSYJTTQQQYYLHYNPYQZLCYZHZWSMYLKFJXLWGXYPJYTYSYXYMZCKTTWLKSMZSYLMPWLZWXWQZSSAQSYXYRHSSNTSRAPXCPWCMGDXHXZDZYFJHGZTTSBJHGYZSZYSMYCLLLXBTYXHBBZJKSSDMALXHYCFYGMQYPJYCQXJLLLJGSLZGQLYCJCCZOTYXMTMTTLLWTGPXYMZMKLPSZZZXHKQYSXCTYJZYHXSHYXZKXLZWPSQPYHJWPJPWXQQYLXSDHMRSLZZYZWTTCYXYSZZSHBSCCSTPLWSSCJCHNLCGCHSSPHYLHFHHXJSXYLLNYLSZDHZXYLSXLWZYKCLDYAXZCMDDYSPJTQJZLNWQPSSSWCTSTSZLBLNXSMNYYMJQBQHRZWTYYDCHQLXKPZWBGQYBKFCMZWPZLLYYLSZYDWHXPSBCMLJBSCGBHXLQHYRLJXYSWXWXZSLDFHLSLYNJLZYFLYJYCDRJLFSYZFSLLCQYQFGJYHYXZLYLMSTDJCYHBZLLNWLXXYGYYHSMGDHXXHHLZZJZXCZZZCYQZFNGWPYLCPKPYYPMCLQKDGXZGGWQBDXZZKZFBXXLZXJTPJPTTBYTSZZDWSLCHZHSLTYXHQLHYXXXYYZYSWTXZKHLXZXZPYHGCHKCFSYHUTJRLXFJXPTZTWHPLYXFCRHXSHXKYXXYHZQDXQWULHYHMJTBFLKHTXCWHJFWJCFPQRYQXCYYYQYGRPYWSGSUNGWCHKZDXYFLXXHJJBYZWTSXXNCYJJYMSWZJQRMHXZWFQSYLZJZGBHYNSLBGTTCSYBYXXWXYHXYYXNSQYXMQYWRGYQLXBBZLJSYLPSYTJZYHYZAWLRORJMKSCZJXXXYXCHDYXRYXXJDTSQFXLYLTSFFYXLMTYJMJUYYYXLTZCSXQZQHZXLYYXZHDNBRXXXJCTYHLBRLMBRLLAXKYLLLJLYXXLYCRYLCJTGJCMTLZLLCYZZPZPCYAWHJJFYBDYYZSMPCKZDQYQPBPCJPDCYZMDPBCYYDYCNNPLMTMLRMFMMGWYZBSJGYGSMZQQQZTXMKQWGXLLPJGZBQCDJJJFPKJKCXBLJMSWMDTQJXLDLPPBXCWRCQFBFQJCZAHZGMYKPHYYHZYKNDKZMBPJYXPXYHLFPNYYGXJDBKXNXHJMZJXSTRSTLDXSKZYSYBZXJLXYSLBZYSLHXJPFXPQNBYLLJQKYGZMCYZZYMCCSLCLHZFWFWYXZMWSXTYNXJHPYYMCYSPMHYSMYDYSHQYZCHMJJMZCAAGCFJBBHPLYZYLXXSDJGXDHKXXTXXNBHRMLYJSLTXMRHNLXQJXYZLLYSWQGDLBJHDCGJYQYCMHWFMJYBMBYJYJWYMDPWHXQLDYGPDFXXBCGJSPCKRSSYZJMSLBZZJFLJJJLGXZGYXYXLSZQYXBEXYXHGCXBPLDYHWETTWWCJMBTXCHXYQXLLXFLYXLLJLSSFWDPZSMYJCLMWYTCZPCHQEKCQBWLCQYDPLQPPQZQFJQDJHYMMCXTXDRMJWRHXCJZYLQXDYYNHYYHRSLSRSYWWZJYMTLTLLGTQCJZYABTCKZCJYCCQLJZQXALMZYHYWLWDXZXQDLLQSHGPJFJLJHJABCQZDJGTKHSSTCYJLPSWZLXZXRWGLDLZRLZXTGSLLLLZLYXXWGDZYGBDPHZPBRLWSXQBPFDWOFMWHLYPCBJCCLDMBZPBZZLCYQXLDOMZBLZWPDWYYGDSTTHCSQSCCRSSSYSLFYBFNTYJSZDFNDPDHDZZMBBLSLCMYFFGTJJQWFTMTPJWFNLBZCMMJTGBDZLQLPYFHYYMJYLSDCHDZJWJCCTLJCLDTLJJCPDDSQDSSZYBNDBJLGGJZXSXNLYCYBJXQYCBYLZCFZPPGKCXZDZFZTJJFJSJXZBNZYJQTTYJYHTYCZHYMDJXTTMPXSPLZCDWSLSHXYPZGTFMLCJTYCBPMGDKWYCYZCDSZZYHFLYCTYGWHKJYYLSJCXGYWJCBLLCSNDDBTZBSCLYZCZZSSQDLLMQYYHFSLQLLXFTYHABXGWNYWYYPLLSDLDLLBJCYXJZMLHLJDXYYQYTDLLLBUGBFDFBBQJZZMDPJHGCLGMJJPGAEHHBWCQXAXHHHZCHXYPHJAXHLPHJPGPZJQCQZGJJZZUZDMQYYBZZPHYHYBWHAZYJHYKFGDPFQSDLZMLJXKXGALXZDAGLMDGXMWZQYXXDXXPFDMMSSYMPFMDMMKXKSYZYSHDZKXSYSMMZZZMSYDNZZCZXFPLSTMZDNMXCKJMZTYYMZMZZMSXHHDCZJEMXXKLJSTLWLSQLYJZLLZJSSDPPMHNLZJCZYHMXXHGZCJMDHXTKGRMXFWMCGMWKDTKSXQMMMFZZYDKMSCLCMPCGMHSPXQPZDSSLCXKYXTWLWJYAHZJGZQMCSNXYYMMPMLKJXMHLMLQMXCTKZMJQYSZJSYSZHSYJZJCDAJZYBSDQJZGWZQQXFKDMSDJLFWEHKZQKJPEYPZYSZCDWYJFFMZZYLTTDZZEFMZLBNPPLPLPEPSZALLTYLKCKQZKGENQLWAGYXYDPXLHSXQQWQCQXQCLHYXXMLYCCWLYMQYSKGCHLCJNSZKPYZKCQZQLJPDMDZHLASXLBYDWQLWDNBQCRYDDZTJYBKBWSZDXDTNPJDTCTQDFXQQMGNXECLTTBKPWSLCTYQLPWYZZKLPYGZCQQPLLKCCYLPQMZCZQCLJSLQZDJXLDDHPZQDLJJXZQDXYZQKZLJCYQDYJPPYPQYKJYRMPCBYMCXKLLZLLFQPYLLLMBSGLCYSSLRSYSQTMXYXZQZFDZUYSYZTFFMZZSMZQHZSSCCMLYXWTPZGXZJGZGSJSGKDDHTQGGZLLBJDZLCBCHYXYZHZFYWXYZYMSDBZZYJGTSMTFXQYXQSTDGSLNXDLRYZZLRYYLXQHTXSRTZNGZXBNQQZFMYKMZJBZYMKBPNLYZPBLMCNQYZZZSJZHJCTZKHYZZJRDYZHNPXGLFZTLKGJTCTSSYLLGZRZBBQZZKLPKLCZYSSUYXBJFPNJZZXCDWXZYJXZZDJJKGGRSRJKMSMZJLSJYWQSKYHQJSXPJZZZLSNSHRNYPZTWCHKLPSRZLZXYJQXQKYSJYCZTLQZYBBYBWZPQDWWYZCYTJCJXCKCWDKKZXSGKDZXWWYYJQYYTCYTDLLXWKCZKKLCCLZCQQDZLQLCSFQCHQHSFSMQZZLNBJJZBSJHTSZDYSJQJPDLZCDCWJKJZZLPYCGMZWDJJBSJQZSYZYHHXJPBJYDSSXDZNCGLQMBTSFSBPDZDLZNFGFJGFSMPXJQLMBLGQCYYXBQKDJJQYRFKZTJDHCZKLBSDZCFJTPLLJGXHYXZCSSZZXSTJYGKGCKGYOQXJPLZPBPGTGYJZGHZQZZLBJLSQFZGKQQJZGYCZBZQTLDXRJXBSXXPZXHYZYCLWDXJJHXMFDZPFZHQHQMQGKSLYHTYCGFRZGNQXCLPDLBZCSCZQLLJBLHBZCYPZZPPDYMZZSGYHCKCPZJGSLJLNSCDSLDLXBMSTLDDFJMKDJDHZLZXLSZQPQPGJLLYBDSZGQLBZLSLKYYHZTTNTJYQTZZPSZQZTLLJTYYLLQLLQYZQLBDZLSLYYZYMDFSZSNHLXZNCZQZPBWSKRFBSYZMTHBLGJPMCZZLSTLXSHTCSYZLZBLFEQHLXFLCJLYLJQCBZLZJHHSSTBRMHXZHJZCLXFNBGXGTQJCZTMSFZKJMSSNXLJKBHSJXNTNLZDNTLMSJXGZJYJCZXYJYJWRWWQNZTNFJSZPZSHZJFYRDJSFSZJZBJFZQZZHZLXFYSBZQLZSGYFTZDCSZXZJBQMSZKJRHYJZCKMJKHCHGTXKXQGLXPXFXTRTYLXJXHDTSJXHJZJXZWZLCQSBTXWXGXTXXHXFTSDKFJHZYJFJXRZSDLLLTQSQQZQWZXSYQTWGWBZCGZLLYZBCLMQQTZHZXZXLJFRMYZFLXYSQXXJKXRMQDZDMMYYBSQBHGZMWFWXGMXLZPYYTGZYCCDXYZXYWGSYJYZNBHPZJSQSYXSXRTFYZGRHZTXSZZTHCBFCLSYXZLZQMZLMPLMXZJXSFLBYZMYQHXJSXRXSQZZZSSLYFRCZJRCRXHHZXQYDYHXSJJHZCXZBTYNSYSXJBQLPXZQPYMLXZKYXLXCJLCYSXXZZLXDLLLJJYHZXGYJWKJRWYHCPSGNRZLFZWFZZNSXGXFLZSXZZZBFCSYJDBRJKRDHHGXJLJJTGXJXXSTJTJXLYXQFCSGSWMSBCTLQZZWLZZKXJMLTMJYHSDDBXGZHDLBMYJFRZFSGCLYJBPMLYSMSXLSZJQQHJZFXGFQFQBPXZGYYQXGZTCQWYLTLGWSGWHRLFSFGZJMGMGBGTJFSYZZGZYZAFLSSPMLPFLCWBJZCLJJMZLPJJLYMQDMYYYFBGYGYZMLYZDXQYXRQQQHSYYYQXYLJTYXFSFSLLGNQCYHYCWFHCCCFXPYLYPLLZYXXXXXKQHHXSHJZCFZSCZJXCPZWHHHHHAPYLQALPQAFYHXDYLUKMZQGGGDDESRNNZLTZGCHYPPYSQJJHCLLJTOLNJPZLJLHYMHEYDYDSQYCDDHGZUNDZCLZYZLLZNTNYZGSLHSLPJJBDGWXPCDUTJCKLKCLWKLLCASSTKZZDNQNTTLYYZSSYSSZZRYLJQKCQDHHCRXRZYDGRGCWCGZQFFFPPJFZYNAKRGYWYQPQXXFKJTSZZXSWZDDFBBXTBGTZKZNPZZPZXZPJSZBMQHKCYXYLDKLJNYPKYGHGDZJXXEAHPNZKZTZCMXCXMMJXNKSZQNMNLWBWWXJKYHCPSTMCSQTZJYXTPCTPDTNNPGLLLZSJLSPBLPLQHDTNJNLYYRSZFFJFQWDPHZDWMRZCCLODAXNSSNYZRESTYJWJYJDBCFXNMWTTBYLWSTSZGYBLJPXGLBOCLHPCBJLTMXZLJYLZXCLTPNCLCKXTPZJSWCYXSFYSZDKNTLBYJCYJLLSTGQCBXRYZXBXKLYLHZLQZLNZCXWJZLJZJNCJHXMNZZGJZZXTZJXYCYYCXXJYYXJJXSSSJSTSSTTPPGQTCSXWZDCSYFPTFBFHFBBLZJCLZZDBXGCXLQPXKFZFLSYLTUWBMQJHSZBMDDBCYSCCLDXYCDDQLYJJWMQLLCSGLJJSYFPYYCCYLTJANTJJPWYCMMGQYYSXDXQMZHSZXPFTWWZQSWQRFKJLZJQQYFBRXJHHFWJJZYQAZMYFRHCYYBYQWLPEXCCZSTYRLTTDMQLYKMBBGMYYJPRKZNPBSXYXBHYZDJDNGHPMFSGMWFZMFQMMBCMZZCJJLCNUXYQLMLRYGQZCYXZLWJGCJCGGMCJNFYZZJHYCPRRCMTZQZXHFQGTJXCCJEAQCRJYHPLQLSZDJRBCQHQDYRHYLYXJSYMHZYDWLDFRYHBPYDTSSCNWBXGLPZMLZZTQSSCPJMXXYCSJYTYCGHYCJWYRXXLFEMWJNMKLLSWTXHYYYNCMMCWJDQDJZGLLJWJRKHPZGGFLCCSCZMCBLTBHBQJXQDSPDJZZGKGLFQYWBZYZJLTSTDHQHCTCBCHFLQMPWDSHYYTQWCNZZJTLBYMBPDYYYXSQKXWYYFLXXNCWCXYPMAELYKKJMZZZBRXYYQJFLJPFHHHYTZZXSGQQMHSPGDZQWBWPJHZJDYSCQWZKTXXSQLZYYMYSDZGRXCKKUJLWPYSYSCSYZLRMLQSYLJXBCXTLWDQZPCYCYKPPPNSXFYZJJRCEMHSZMSXLXGLRWGCSTLRSXBZGBZGZTCPLUJLSLYLYMTXMTZPALZXPXJTJWTCYYZLBLXBZLQMYLXPGHDSLSSDMXMBDZZSXWHAMLCZCPJMCNHJYSNSYGCHSKQMZZQDLLKABLWJXSFMOCDXJRRLYQZKJMYBYQLYHETFJZFRFKSRYXFJTWDSXXSYSQJYSLYXWJHSNLXYYXHBHAWHHJZXWMYLJCSSLKYDZTXBZSYFDXGXZJKHSXXYBSSXDPYNZWRPTQZCZENYGCXQFJYKJBZMLJCMQQXUOXSLYXXLYLLJDZBTYMHPFSTTQQWLHOKYBLZZALZXQLHZWRRQHLSTMYPYXJJXMQSJFNBXYXYJXXYQYLTHYLQYFMLKLJTMLLHSZWKZHLJMLHLJKLJSTLQXYLMBHHLNLZXQJHXCFXXLHYHJJGBYZZKBXSCQDJQDSUJZYYHZHHMGSXCSYMXFEBCQWWRBPYYJQTYZCYQYQQZYHMWFFHGZFRJFCDPXNTQYZPDYKHJLFRZXPPXZDBBGZQSTLGDGYLCQMLCHHMFYWLZYXKJLYPQHSYWMQQGQZMLZJNSQXJQSYJYCBEHSXFSZPXZWFLLBCYYJDYTDTHWZSFJMQQYJLMQXXLLDTTKHHYBFPWTYYSQQWNQWLGWDEBZWCMYGCULKJXTMXMYJSXHYBRWFYMWFRXYQMXYSZTZZTFYKMLDHQDXWYYNLCRYJBLPSXCXYWLSPRRJWXHQYPHTYDNXHHMMYWYTZCSQMTSSCCDALWZTCPQPYJLLQZYJSWXMZZMMYLMXCLMXCZMXMZSQTZPPQQBLPGXQZHFLJJHYTJSRXWZXSCCDLXTYJDCQJXSLQYCLZXLZZXMXQRJMHRHZJBHMFLJLMLCLQNLDXZLLLPYPSYJYSXCQQDCMQJZZXHNPNXZMEKMXHYKYQLXSXTXJYYHWDCWDZHQYYBGYBCYSCFGPSJNZDYZZJZXRZRQJJYMCANYRJTLDPPYZBSTJKXXZYPFDWFGZZRPYMTNGXZQBYXNBUFNQKRJQZMJEGRZGYCLKXZDSKKNSXKCLJSPJYYZLQQJYBZSSQLLLKJXTBKTYLCCDDBLSPPFYLGYDTZJYQGGKQTTFZXBDKTYYHYBBFYTYYBCLPDYTGDHRYRNJSPTCSNYJQHKLLLZSLYDXXWBCJQSPXBPJZJCJDZFFXXBRMLAZHCSNDLBJDSZBLPRZTSWSBXBCLLXXLZDJZSJPYLYXXYFTFFFBHJJXGBYXJPMMMPSSJZJMTLYZJXSWXTYLEDQPJMYGQZJGDJLQJWJQLLSJGJGYGMSCLJJXDTYGJQJQJCJZCJGDZZSXQGSJGGCXHQXSNQLZZBXHSGZXCXYLJXYXYYDFQQJHJFXDHCTXJYRXYSQTJXYEFYYSSYYJXNCYZXFXMSYSZXYYSCHSHXZZZGZZZGFJDLTYLNPZGYJYZYYQZPBXQBDZTZCZYXXYHHSQXSHDHGQHJHGYWSZTMZMLHYXGEBTYLZKQWYTJZRCLEKYSTDBCYKQQSAYXCJXWWGSBHJYZYDHCSJKQCXSWXFLTYNYZPZCCZJQTZWJQDZZZQZLJJXLSBHPYXXPSXSHHEZTXFPTLQYZZXHYTXNCFZYYHXGNXMYWXTZSJPTHHGYMXMXQZXTSBCZYJYXXTYYZYPCQLMMSZMJZZLLZXGXZAAJZYXJMZXWDXZSXZDZXLEYJJZQBHZWZZZQTZPSXZTDSXJJJZNYAZPHXYYSRNQDTHZHYYKYJHDZXZLSWCLYBZYECWCYCRYLCXNHZYDZYDYJDFRJJHTRSQTXYXJRJHOJYNXELXSFSFJZGHPZSXZSZDZCQZBYYKLSGSJHCZSHDGQGXYZGXCHXZJWYQWGYHKSSEQZZNDZFKWYSSTCLZSTSYMCDHJXXYWEYXCZAYDMPXMDSXYBSQMJMZJMTZQLPJYQZCGQHXJHHLXXHLHDLDJQCLDWBSXFZZYYSCHTYTYYBHECXHYKGJPXHHYZJFXHWHBDZFYZBCAPNPGNYDMSXHMMMMAMYNBYJTMPXYYMCTHJBZYFCGTYHWPHFTWZZEZSBZEGPFMTSKFTYCMHFLLHGPZJXZJGZJYXZSBBQSCZZLZCCSTPGXMJSFTCCZJZDJXCYBZLFCJSYZFGSZLYBCWZZBYZDZYPSWYJZXZBDSYUXLZZBZFYGCZXBZHZFTPBGZGEJBSTGKDMFHYZZJHZLLZZGJQZLSFDJSSCBZGPDLFZFZSZYZYZSYGCXSNXXCHCZXTZZLJFZGQSQYXZJQDCCZTQCDXZJYQJQCHXZTDLGSCXZSYQJQTZWLQDQZTQCHQQJZYEZZZPBWKDJFCJPZTYPQYQTTYNLMBDKTJZPQZQZZFPZSBNJLGYJDXJDZZKZGQKXDLPZJTCJDQBXDJQJSTCKNXBXZMSLYJCQMTJQWWCJQNJNLLLHJCWQTBZQYDZCZPZZDZYDDCYZZZCCJTTJFZDPRRTZTJDCQTQZDTJNPLZBCLLCTZSXKJZQZPZLBZRBTJDCXFCZDBCCJJLTQQPLDCGZDBBZJCQDCJWYNLLZYZCCDWLLXWZLXRXNTQQCZXKQLSGDFQTDDGLRLAJJTKUYMKQLLTZYTDYYCZGJWYXDXFRSKSTQTENQMRKQZHHQKDLDAZFKYPBGGPZREBZZYKZZSPEGJXGYKQZZZSLYSYYYZWFQZYLZZLZHWCHKYPQGNPGBLPLRRJYXCCSYYHSFZFYBZYYTGZXYLXCZWXXZJZBLFFLGSKHYJZEYJHLPLLLLCZGXDRZELRHGKLZZYHZLYQSZZJZQLJZFLNBHGWLCZCFJYSPYXZLZLXGCCPZBLLCYBBBBUBBCBPCRNNZCZYRBFSRLDCGQYYQXYGMQZWTZYTYJXYFWTEHZZJYWLCCNTZYJJZDEDPZDZTSYQJHDYMBJNYJZLXTSSTPHNDJXXBYXQTZQDDTJTDYYTGWSCSZQFLSHLGLBCZPHDLYZJYCKWTYTYLBNYTSDSYCCTYSZYYEBHEXHQDTWNYGYCLXTSZYSTQMYGZAZCCSZZDSLZCLZRQXYYELJSBYMXSXZTEMBBLLYYLLYTDQYSHYMRQWKFKBFXNXSBYCHXBWJYHTQBPBSBWDZYLKGZSKYHXQZJXHXJXGNLJKZLYYCDXLFYFGHLJGJYBXQLYBXQPQGZTZPLNCYPXDJYQYDYMRBESJYYHKXXSTMXRCZZYWXYQYBMCLLYZHQYZWQXDBXBZWZMSLPDMYSKFMZKLZCYQYCZLQXFZZYDQZPZYGYJYZMZXDZFYFYTTQTZHGSPCZMLCCYTZXJCYTJMKSLPZHYSNZLLYTPZCTZZCKTXDHXXTQCYFKSMQCCYYAZHTJPCYLZLYJBJXTPNYLJYYNRXSYLMMNXJSMYBCSYSYLZYLXJJQYLDZLPQBFZZBLFNDXQKCZFYWHGQMRDSXYCYTXNQQJZYYPFZXDYZFPRXEJDGYQBXRCNFYYQPGHYJDYZXGRHTKYLNWDZNTSMPKLBTHBPYSZBZTJZSZZJTYYXZPHSSZZBZCZPTQFZMYFLYPYBBJQXZMXXDJMTSYSKKBJZXHJCKLPSMKYJZCXTMLJYXRZZQSLXXQPYZXMKYXXXJCLJPRMYYGADYSKQLSNDHYZKQXZYZTCGHZTLMLWZYBWSYCTBHJHJFCWZTXWYTKZLXQSHLYJZJXTMPLPYCGLTBZZTLZJCYJGDTCLKLPLLQPJMZPAPXYZLKKTKDZCZZBNZDYDYQZJYJGMCTXLTGXSZLMLHBGLKFWNWZHDXUHLFMKYSLGXDTWWFRJEJZTZHYDXYKSHWFZCQSHKTMQQHTZHYMJDJSKHXZJZBZZXYMPAGQMSTPXLSKLZYNWRTSQLSZBPSPSGZWYHTLKSSSWHZZLYYTNXJGMJSZSUFWNLSOZTXGXLSAMMLBWLDSZYLAKQCQCTMYCFJBSLXCLZZCLXXKSBZQCLHJPSQPLSXXCKSLNHPSFQQYTXYJZLQLDXZQJZDYYDJNZPTUZDSKJFSLJHYLZSQZLBTXYDGTQFDBYAZXDZHZJNHHQBYKNXJJQCZMLLJZKSPLDYCLBBLXKLELXJLBQYCXJXGCNLCQPLZLZYJTZLJGYZDZPLTQCSXFDMNYCXGBTJDCZNBGBQYQJWGKFHTNPYQZQGBKPBBYZMTJDYTBLSQMPSXTBNPDXKLEMYYCJYNZCTLDYKZZXDDXHQSHDGMZSJYCCTAYRZLPYLTLKXSLZCGGEXCLFXLKJRTLQJAQZNCMBYDKKCXGLCZJZXJHPTDJJMZQYKQSECQZDSHHADMLZFMMZBGNTJNNLGBYJBRBTMLBYJDZXLCJLPLDLPCQDHLXZLYCBLCXZZJADJLNZMMSSSMYBHBSQKBHRSXXJMXSDZNZPXLGBRHWGGFCXGMSKLLTSJYYCQLTSKYWYYHYWXBXQYWPYWYKQLSQPTNTKHQCWDQKTWPXXHCPTHTWUMSSYHBWCRWXHJMKMZNGWTMLKFGHKJYLSYYCXWHYECLQHKQHTTQKHFZLDXQWYZYYDESBPKYRZPJFYYZJCEQDZZDLATZBBFJLLCXDLMJSSXEGYGSJQXCWBXSSZPDYZCXDNYXPPZYDLYJCZPLTXLSXYZYRXCYYYDYLWWNZSAHJSYQYHGYWWAXTJZDAXYSRLTDPSSYYFNEJDXYZHLXLLLZQZSJNYQYQQXYJGHZGZCYJCHZLYCDSHWSHJZYJXCLLNXZJJYYXNFXMWFPYLCYLLABWDDHWDXJMCXZTZPMLQZHSFHZYNZTLLDYWLSLXHYMMYLMBWWKYXYADTXYLLDJPYBPWUXJMWMLLSAFDLLYFLBHHHBQQLTZJCQJLDJTFFKMMMBYTHYGDCQRDDWRQJXNBYSNWZDBYYTBJHPYBYTTJXAAHGQDQTMYSTQXKBTZPKJLZRBEQQSSMJJBDJOTGTBXPGBKTLHQXJJJCTHXQDWJLWRFWQGWSHCKRYSWGFTGYGBXSDWDWRFHWYTJJXXXJYZYSLPYYYPAYXHYDQKXSHXYXGSKQHYWFDDDPPLCJLQQEEWXKSYYKDYPLTJTHKJLTCYYHHJTTPLTZZCDLTHQKZXQYSTEEYWYYZYXXYYSTTJKLLPZMCYHQGXYHSRMBXPLLNQYDQHXSXXWGDQBSHYLLPJJJTHYJKYPPTHYYKTYEZYENMDSHLCRPQFDGFXZPSFTLJXXJBSWYYSKSFLXLPPLBBBLBSFXFYZBSJSSYLPBBFFFFSSCJDSTZSXZRYYSYFFSYZYZBJTBCTSBSDHRTJJBYTCXYJEYLXCBNEBJDSYXYKGSJZBXBYTFZWGENYHHTHZHHXFWGCSTBGXKLSXYWMTMBYXJSTZSCDYQRCYTWXZFHMYMCXLZNSDJTTTXRYCFYJSBSDYERXJLJXBBDEYNJGHXGCKGSCYMBLXJMSZNSKGXFBNBPTHFJAAFXYXFPXMYPQDTZCXZZPXRSYWZDLYBBKTYQPQJPZYPZJZNJPZJLZZFYSBTTSLMPTZRTDXQSJEHBZYLZDHLJSQMLHTXTJECXSLZZSPKTLZKQQYFSYGYWPCPQFHQHYTQXZKRSGTTSQCZLPTXCDYYZXSQZSLXLZMYCPCQBZYXHBSXLZDLTCDXTYLZJYYZPZYZLTXJSJXHLPMYTXCQRBLZSSFJZZTNJYTXMYJHLHPPLCYXQJQQKZZSCPZKSWALQSBLCCZJSXGWWWYGYKTJBBZTDKHXHKGTGPBKQYSLPXPJCKBMLLXDZSTBKLGGQKQLSBKKTFXRMDKBFTPZFRTBBRFERQGXYJPZSSTLBZTPSZQZSJDHLJQLZBPMSMMSXLQQNHKNBLRDDNXXDHDDJCYYGYLXGZLXSYGMQQGKHBPMXYXLYTQWLWGCPBMQXCYZYDRJBHTDJYHQSHTMJSBYPLWHLZFFNYPMHXXHPLTBQPFBJWQDBYGPNZTPFZJGSDDTQSHZEAWZZYLLTYYBWJKXXGHLFKXDJTMSZSQYNZGGSWQSPHTLSSKMCLZXYSZQZXNCJDQGZDLFNYKLJCJLLZLMZZNHYDSSHTHZZLZZBBHQZWWYCRZHLYQQJBEYFXXXWHSRXWQHWPSLMSSKZTTYGYQQWRSLALHMJTQJSMXQBJJZJXZYZKXBYQXBJXSHZTSFJLXMXZXFGHKZSZGGYLCLSARJYHSLLLMZXELGLXYDJYTLFBHBPNLYZFBBHPTGJKWETZHKJJXZXXGLLJLSTGSHJJYQLQZFKCGNNDJSSZFDBCTWWSEQFHQJBSAQTGYPQLBXBMMYWXGSLZHGLZGQYFLZBYFZJFRYSFMBYZHQGFWZSYFYJJPHZBYYZFFWODGRLMFTWLBZGYCQXCDJYGZYYYYTYTYDWEGAZYHXJLZYYHLRMGRXXZCLHNELJJTJTPWJYBJJBXJJTJTEEKHWSLJPLPSFYZPQQBDLQJJTYYQLYZKDKSQJYYQZLDQTGJQYZJSUCMRYQTHTEJMFCTYHYPKMHYZWJDQFHYYXWSHCTXRLJHQXHCCYYYJLTKTTYTMXGTCJTZAYYOCZLYLBSZYWJYTSJYHBYSHFJLYGJXXTMZYYLTXXYPZLXYJZYZYYPNHMYMDYYLBLHLSYYQQLLNJJYMSOYQBZGDLYXYLCQYXTSZEGXHZGLHWBLJHEYXTWQMAKBPQCGYSHHEGQCMWYYWLJYJHYYZLLJJYLHZYHMGSLJLJXCJJYCLYCJPCPZJZJMMYLCQLNQLJQJSXYJMLSZLJQLYCMMHCFMMFPQQMFYLQMCFFQMMMMHMZNFHHJGTTHHKHSLNCHHYQDXTMMQDCYZYXYQMYQYLTDCYYYZAZZCYMZYDLZFFFMMYCQZWZZMABTBYZTDMNZZGGDFTYPCGQYTTSSFFWFDTZQSSYSTWXJHXYTSXXYLBYQHWWKXHZXWZNNZZJZJJQJCCCHYYXBZXZCYZTLLCQXYNJYCYYCYNZZQYYYEWYCZDCJYCCHYJLBTZYYCQWMPWPYMLGKDLDLGKQQBGYCHJXY";
// 此处收录了375个多音字,数据来自于<a href="http://www.51window.net/page/pinyin"
// target="_blank" rel="external">http://www.51window.net/page/pinyin</a>
var oMultiDiff = {
	"19969" : "DZ",
	"19975" : "WM",
	"19988" : "QJ",
	"20048" : "YL",
	"20056" : "SC",
	"20060" : "NM",
	"20094" : "QG",
	"20127" : "QJ",
	"20167" : "QC",
	"20193" : "YG",
	"20250" : "KH",
	"20256" : "ZC",
	"20282" : "SC",
	"20285" : "QJG",
	"20291" : "TD",
	"20314" : "YD",
	"20340" : "NE",
	"20375" : "TD",
	"20389" : "YJ",
	"20391" : "CZ",
	"20415" : "PB",
	"20446" : "YS",
	"20447" : "SQ",
	"20504" : "TC",
	"20608" : "KG",
	"20854" : "QJ",
	"20857" : "ZC",
	"20911" : "PF",
	"20504" : "TC",
	"20608" : "KG",
	"20854" : "QJ",
	"20857" : "ZC",
	"20911" : "PF",
	"20985" : "AW",
	"21032" : "PB",
	"21048" : "XQ",
	"21049" : "SC",
	"21089" : "YS",
	"21119" : "JC",
	"21242" : "SB",
	"21273" : "SC",
	"21305" : "YP",
	"21306" : "QO",
	"21330" : "ZC",
	"21333" : "SDC",
	"21345" : "QK",
	"21378" : "CA",
	"21397" : "SC",
	"21414" : "XS",
	"21442" : "SC",
	"21477" : "JG",
	"21480" : "TD",
	"21484" : "ZS",
	"21494" : "YX",
	"21505" : "YX",
	"21512" : "HG",
	"21523" : "XH",
	"21537" : "PB",
	"21542" : "PF",
	"21549" : "KH",
	"21571" : "E",
	"21574" : "DA",
	"21588" : "TD",
	"21589" : "O",
	"21618" : "ZC",
	"21621" : "KHA",
	"21632" : "ZJ",
	"21654" : "KG",
	"21679" : "LKG",
	"21683" : "KH",
	"21710" : "A",
	"21719" : "YH",
	"21734" : "WOE",
	"21769" : "A",
	"21780" : "WN",
	"21804" : "XH",
	"21834" : "A",
	"21899" : "ZD",
	"21903" : "RN",
	"21908" : "WO",
	"21939" : "ZC",
	"21956" : "SA",
	"21964" : "YA",
	"21970" : "TD",
	"22003" : "A",
	"22031" : "JG",
	"22040" : "XS",
	"22060" : "ZC",
	"22066" : "ZC",
	"22079" : "MH",
	"22129" : "XJ",
	"22179" : "XA",
	"22237" : "NJ",
	"22244" : "TD",
	"22280" : "JQ",
	"22300" : "YH",
	"22313" : "XW",
	"22331" : "YQ",
	"22343" : "YJ",
	"22351" : "PH",
	"22395" : "DC",
	"22412" : "TD",
	"22484" : "PB",
	"22500" : "PB",
	"22534" : "ZD",
	"22549" : "DH",
	"22561" : "PB",
	"22612" : "TD",
	"22771" : "KQ",
	"22831" : "HB",
	"22841" : "JG",
	"22855" : "QJ",
	"22865" : "XQ",
	"23013" : "ML",
	"23081" : "WM",
	"23487" : "SX",
	"23558" : "QJ",
	"23561" : "YW",
	"23586" : "YW",
	"23614" : "YW",
	"23615" : "SN",
	"23631" : "PB",
	"23646" : "ZS",
	"23663" : "ZT",
	"23673" : "YG",
	"23762" : "TD",
	"23769" : "ZS",
	"23780" : "QJ",
	"23884" : "QK",
	"24055" : "XH",
	"24113" : "DC",
	"24162" : "ZC",
	"24191" : "GA",
	"24273" : "QJ",
	"24324" : "NL",
	"24377" : "TD",
	"24378" : "QJ",
	"24439" : "PF",
	"24554" : "ZS",
	"24683" : "TD",
	"24694" : "WE",
	"24733" : "LK",
	"24925" : "TN",
	"25094" : "ZG",
	"25100" : "XQ",
	"25103" : "XH",
	"25153" : "PB",
	"25170" : "PB",
	"25179" : "KG",
	"25203" : "PB",
	"25240" : "ZS",
	"25282" : "FB",
	"25303" : "NA",
	"25324" : "KG",
	"25341" : "ZY",
	"25373" : "WZ",
	"25375" : "XJ",
	"25384" : "A",
	"25457" : "A",
	"25528" : "SD",
	"25530" : "SC",
	"25552" : "TD",
	"25774" : "ZC",
	"25874" : "ZC",
	"26044" : "YW",
	"26080" : "WM",
	"26292" : "PB",
	"26333" : "PB",
	"26355" : "ZY",
	"26366" : "CZ",
	"26397" : "ZC",
	"26399" : "QJ",
	"26415" : "ZS",
	"26451" : "SB",
	"26526" : "ZC",
	"26552" : "JG",
	"26561" : "TD",
	"26588" : "JG",
	"26597" : "CZ",
	"26629" : "ZS",
	"26638" : "YL",
	"26646" : "XQ",
	"26653" : "KG",
	"26657" : "XJ",
	"26727" : "HG",
	"26894" : "ZC",
	"26937" : "ZS",
	"26946" : "ZC",
	"26999" : "KJ",
	"27099" : "KJ",
	"27449" : "YQ",
	"27481" : "XS",
	"27542" : "ZS",
	"27663" : "ZS",
	"27748" : "TS",
	"27784" : "SC",
	"27788" : "ZD",
	"27795" : "TD",
	"27812" : "O",
	"27850" : "PB",
	"27852" : "MB",
	"27895" : "SL",
	"27898" : "PL",
	"27973" : "QJ",
	"27981" : "KH",
	"27986" : "HX",
	"27994" : "XJ",
	"28044" : "YC",
	"28065" : "WG",
	"28177" : "SM",
	"28267" : "QJ",
	"28291" : "KH",
	"28337" : "ZQ",
	"28463" : "TL",
	"28548" : "DC",
	"28601" : "TD",
	"28689" : "PB",
	"28805" : "JG",
	"28820" : "QG",
	"28846" : "PB",
	"28952" : "TD",
	"28975" : "ZC",
	"29100" : "A",
	"29325" : "QJ",
	"29575" : "SL",
	"29602" : "FB",
	"30010" : "TD",
	"30044" : "CX",
	"30058" : "PF",
	"30091" : "YSP",
	"30111" : "YN",
	"30229" : "XJ",
	"30427" : "SC",
	"30465" : "SX",
	"30631" : "YQ",
	"30655" : "QJ",
	"30684" : "QJG",
	"30707" : "SD",
	"30729" : "XH",
	"30796" : "LG",
	"30917" : "PB",
	"31074" : "NM",
	"31085" : "JZ",
	"31109" : "SC",
	"31181" : "ZC",
	"31192" : "MLB",
	"31293" : "JQ",
	"31400" : "YX",
	"31584" : "YJ",
	"31896" : "ZN",
	"31909" : "ZY",
	"31995" : "XJ",
	"32321" : "PF",
	"32327" : "ZY",
	"32418" : "HG",
	"32420" : "XQ",
	"32421" : "HG",
	"32438" : "LG",
	"32473" : "GJ",
	"32488" : "TD",
	"32521" : "QJ",
	"32527" : "PB",
	"32562" : "ZSQ",
	"32564" : "JZ",
	"32735" : "ZD",
	"32793" : "PB",
	"33071" : "PF",
	"33098" : "XL",
	"33100" : "YA",
	"33152" : "PB",
	"33261" : "CX",
	"33324" : "BP",
	"33333" : "TD",
	"33406" : "YA",
	"33426" : "WM",
	"33432" : "PB",
	"33445" : "JG",
	"33486" : "ZN",
	"33493" : "TS",
	"33507" : "QJ",
	"33540" : "QJ",
	"33544" : "ZC",
	"33564" : "XQ",
	"33617" : "YT",
	"33632" : "QJ",
	"33636" : "XH",
	"33637" : "YX",
	"33694" : "WG",
	"33705" : "PF",
	"33728" : "YW",
	"33882" : "SR",
	"34067" : "WM",
	"34074" : "YW",
	"34121" : "QJ",
	"34255" : "ZC",
	"34259" : "XL",
	"34425" : "JH",
	"34430" : "XH",
	"34485" : "KH",
	"34503" : "YS",
	"34532" : "HG",
	"34552" : "XS",
	"34558" : "YE",
	"34593" : "ZL",
	"34660" : "YQ",
	"34892" : "XH",
	"34928" : "SC",
	"34999" : "QJ",
	"35048" : "PB",
	"35059" : "SC",
	"35098" : "ZC",
	"35203" : "TQ",
	"35265" : "JX",
	"35299" : "JX",
	"35782" : "SZ",
	"35828" : "YS",
	"35830" : "E",
	"35843" : "TD",
	"35895" : "YG",
	"35977" : "MH",
	"36158" : "JG",
	"36228" : "QJ",
	"36426" : "XQ",
	"36466" : "DC",
	"36710" : "JC",
	"36711" : "ZYG",
	"36767" : "PB",
	"36866" : "SK",
	"36951" : "YW",
	"37034" : "YX",
	"37063" : "XH",
	"37218" : "ZC",
	"37325" : "ZC",
	"38063" : "PB",
	"38079" : "TD",
	"38085" : "QY",
	"38107" : "DC",
	"38116" : "TD",
	"38123" : "YD",
	"38224" : "HG",
	"38241" : "XTC",
	"38271" : "ZC",
	"38415" : "YE",
	"38426" : "KH",
	"38461" : "YD",
	"38463" : "AE",
	"38466" : "PB",
	"38477" : "XJ",
	"38518" : "YT",
	"38551" : "WK",
	"38585" : "ZC",
	"38704" : "XS",
	"38739" : "LJ",
	"38761" : "GJ",
	"38808" : "SQ",
	"39048" : "JG",
	"39049" : "XJ",
	"39052" : "HG",
	"39076" : "CZ",
	"39271" : "XT",
	"39534" : "TD",
	"39552" : "TD",
	"39584" : "PB",
	"39647" : "SB",
	"39730" : "LG",
	"39748" : "TPB",
	"40109" : "ZQ",
	"40479" : "ND",
	"40516" : "HG",
	"40536" : "HG",
	"40583" : "QJ",
	"40765" : "YQ",
	"40784" : "QJ",
	"40840" : "YK",
	"40863" : "QJG"
};

/**
 * @param 中文字符串
 * @return 拼音首字母数组
 */
function makePy(str) {
	if (typeof (str) != "string")
		throw new Error(-1, "函数makePy需要字符串类型参数!");
	var arrResult = new Array(); // 保存中间结果的数组
	for ( var i = 0, len = str.length; i < len; i++) {
		// 获得unicode码
		var ch = str.charAt(i);
		// 检查该unicode码是否在处理范围之内,在则返回该码对映汉字的拼音首字母,不在则调用其它函数处理
		arrResult.push(checkCh(ch));
	}
	// 处理arrResult,返回所有可能的拼音首字母串数组
	return mkRslt(arrResult);
}
function checkCh(ch) {
	var uni = ch.charCodeAt(0);
	// 如果不在汉字处理范围之内,返回原字符,也可以调用自己的处理函数
	if (uni > 40869 || uni < 19968)
		return ch; // dealWithOthers(ch);
		// 检查是否是多音字,是按多音字处理,不是就直接在strChineseFirstPY字符串中找对应的首字母
	return (oMultiDiff[uni] ? oMultiDiff[uni] : (strChineseFirstPY
			.charAt(uni - 19968)));
}
function mkRslt(arr) {
	var arrRslt = [ "" ];
	for ( var i = 0, len = arr.length; i < len; i++) {
		var str = arr[i];
		var strlen = str.length;
		if (strlen == 1) {
			for ( var k = 0; k < arrRslt.length; k++) {
				arrRslt[k] += str;
			}
		} else {
			var tmpArr = arrRslt.slice(0);
			arrRslt = [];
			for (k = 0; k < strlen; k++) {
				// 复制一个相同的arrRslt
				var tmp = tmpArr.slice(0);
				// 把当前字符str[k]添加到每个元素末尾
				for ( var j = 0; j < tmp.length; j++) {
					tmp[j] += str.charAt(k);
				}
				// 把复制并修改后的数组连接到arrRslt上
				arrRslt = arrRslt.concat(tmp);
			}
		}
	}
	return arrRslt;
}

/**
 * @param Trim
 */
String.prototype.trim = function() {
	return this.replace(/(^\s*)|(\s*$)/g, "");
}
// (c)dhtmlx ltd. www.dhtmlx.com
